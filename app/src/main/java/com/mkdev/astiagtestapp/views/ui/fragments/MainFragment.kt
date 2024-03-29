package com.mkdev.astiagtestapp.views.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.models.LocationModel
import com.mkdev.astiagtestapp.utils.*
import com.mkdev.astiagtestapp.viewModels.MainFragmentViewModel
import com.mkdev.astiagtestapp.viewModels.ViewModelFactory
import com.mkdev.astiagtestapp.views.ui.activities.MainActivity
import com.mkdev.astiagtestapp.views.ui.base.BaseFragment
import com.mkdev.astiagtestapp.views.ui.dialogs.ConfirmDialog
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val LOCATION_REQ_CODE = 4000

class MainFragment : BaseFragment(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationServiceResult, OnMapReadyCallback {

    private val TAG_FRAGMENT = "TAG_MAIN_FRAGMENT"

    private lateinit var viewModel: MainFragmentViewModel

    private val viewDestroyCompositeDisposable = CompositeDisposable()
    private val destroyCompositeDisposable = CompositeDisposable()

    private var updateListener: LocationListener? = null
    private var locationManager: LocationManager? = null

    private lateinit var mMap: GoogleMap
    private var centerOfMap: LatLng? = null
    private var sourceMarker: Marker? = null
    private var destMarker: Marker? = null

    private lateinit var sourceFragment: Fragment
    private lateinit var destFragment: Fragment

    companion object {
        var mainActionsPublisher: PublishProcessor<Pair<ActionType, Any?>> = PublishProcessor.create()
    }

    override fun initBeforeView() {
        with(context!!.applicationContext as App) {
            viewModel = ViewModelProviders.of(this@MainFragment,
                    ViewModelFactory(this)).get(MainFragmentViewModel::class.java)
        }
    }

    override fun getContentViewId(): Int = R.layout.fragment_main

    override fun initViews(rootView: View) {
        Completable.timer(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe({
                    requestLocationPermission()
                    setupUI()
                }, {}).addTo(viewDestroyCompositeDisposable)
    }

    override fun onClick(view: View) {
        when (view) {
            fabNav -> {
                viewModel.openNavigation()
            }
            fabBack -> {
                backLocation()
            }
            fabCurrentLocation -> {
                requestLocationPermission()
            }
        }
    }

    private fun setupUI() {
        fabNav.setOnClickListener(this)
        fabBack.setOnClickListener(this)
        fabCurrentLocation.setOnClickListener(this)

        sourceFragment = TripDataSourceFragment.newInstance()
        addFragment(sourceFragment)

        GlideApp.with(context!!).load(R.drawable.img_user).rounded(dpToPx(25)).into(imgAvatar)

        mainActionsPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it.first) {
                        ActionType.ACCEPT_SOURCE -> {
                            destFragment = TripDataDestFragment.newInstance(it.second as LocationModel)
                            removeFragment(sourceFragment)
                            addFragment(destFragment)

                            sourceMarker = addMapMarker(centerOfMap!!, ContextCompat.getDrawable(context!!, R.drawable.img_origin)!!)
                            imgMarker.setImageResource(R.drawable.img_dest)

                            fabBack.visible()
                        }

                        ActionType.ACCEPT_DESTINATION -> {
                            destMarker = addMapMarker(centerOfMap!!, ContextCompat.getDrawable(context!!, R.drawable.img_dest)!!)
                            imgMarker.gone()
                        }
                    }
                }.addTo(viewDestroyCompositeDisposable)
    }

    private fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        drawable.setBounds(0, 0, widthPixels, heightPixels)
        drawable.draw(canvas)
        return mutableBitmap
    }

    private fun requestLocationPermission() {
        (activity as MainActivity).requestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), object : PermissionCallback {

            override fun onGranted(permissions: Array<String>) {
                if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    enableGPSAutomatically()
                }
            }

            override fun onDenied(permissions: Array<String>) {

            }

            override fun onShowRationale(permissions: Array<String>) {
                if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    ConfirmDialog(context!!, getString(R.string.permission_request), getString(R.string.location_permission_rationale), {
                        requestLocationPermission()
                    }).show()
                }
            }
        })
    }

    private fun enableGPSAutomatically() {
        var googleApiClient: GoogleApiClient? = null
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(context!!).addApi(LocationServices.API)
                    .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build()
            googleApiClient!!.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000
            locationRequest.fastestInterval = 5 * 1000
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            builder.setAlwaysShow(true)

            val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback { result ->
                val status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        setupMap()
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            status.startResolutionForResult(activity, LOCATION_REQ_CODE)
                        } catch (e: Exception) {
                            // Ignore the error.
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        CustomToast.makeText(context!!, "Setting change not allowed", CustomToast.WARNING)
                }
            }
        }
    }

    override fun onResult(code: Int, resultCode: Int, data: Intent?) {
        if (code == LOCATION_REQ_CODE) {
            setupMap()
        }
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this@MainFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapAction()
    }

    @SuppressLint("MissingPermission")
    private fun mapAction() {
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.isMyLocationEnabled = true //blue circle

        mMap.isTrafficEnabled = false
        mMap.isIndoorEnabled = false
        mMap.isBuildingsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = false

        locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager
        val location = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (location != null) {
            updateLocation(location)
        } else {
            updateListener = object : LocationListener {
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                }

                override fun onProviderEnabled(provider: String?) {
                }

                override fun onProviderDisabled(provider: String?) {
                }

                override fun onLocationChanged(location: Location) {
                    updateLocation(location)
                }
            }
            locationManager?.requestSingleUpdate(Criteria(), updateListener, Looper.getMainLooper())
        }
    }

    private fun updateLocation(loc: Location) {
        centerOfMap = LatLng(loc.latitude, loc.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerOfMap, 16f))

        fabCurrentLocation.isClickable = true

        Flowable.create<LatLng>({
            mMap.setOnCameraIdleListener {
                it.onNext(mMap.cameraPosition.target)
            }
        }, BackpressureStrategy.DROP)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    centerOfMap = it
                    if (sourceMarker == null) TripDataSourceFragment.tripDataActionsPublisher.onNext(Pair(ActionType.SHOW_LOCATION_DATA, it))
                    else if (destMarker == null) TripDataDestFragment.tripDataActionsPublisher.onNext(Pair(ActionType.SHOW_LOCATION_DATA, it))
                }.addTo(viewDestroyCompositeDisposable)
    }

    private fun backLocation() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourceMarker!!.position, 16f))

        destMarker?.remove()
        sourceMarker?.remove()
        destMarker = null
        sourceMarker = null

        imgMarker.setImageResource(R.drawable.img_origin)
        imgMarker.visible()

        removeFragment(destFragment)
        addFragment(sourceFragment)

        fabBack.gone()
    }

    private fun addFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.frameTripData, fragment, TAG_FRAGMENT).commit()
    }

    private fun removeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().remove(fragment).commit()
    }

    private fun addMapMarker(loc: LatLng, origin: Drawable): Marker {
        val marker = mMap.addMarker(MarkerOptions().position(loc).icon(BitmapDescriptorFactory
                .fromBitmap(convertToBitmap(origin, dpToPx(45), dpToPx(70)))))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(loc.latitude - 0.0005, loc.longitude - 0.0005), 16f))
        return marker
    }

    override fun onDestroy() {
        destroyCompositeDisposable.clear()
        if (locationManager != null && updateListener != null) {
            locationManager!!.removeUpdates(updateListener)
            locationManager = null
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        viewDestroyCompositeDisposable.clear()
        super.onDestroyView()
    }

    enum class ActionType {
        SHOW_LOCATION_DATA, ACCEPT_SOURCE, ACCEPT_DESTINATION
    }

    override fun onConnected(@Nullable bundle: Bundle?) {
        Timber.d("Connected")
    }

    override fun onConnectionSuspended(i: Int) {
        Timber.d("Suspended")
    }

    override fun onConnectionFailed(@NonNull connectionResult: ConnectionResult) {
        Timber.d("Failed")
    }
}

interface LocationServiceResult {
    fun onResult(requestCode: Int, resultCode: Int, data: Intent?)
}

