package com.mkdev.astiagtestapp.views.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.utils.NavigationEvent
import com.mkdev.astiagtestapp.views.ui.base.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private lateinit var navController: NavController

    @Inject
    lateinit var navEvents: PublishProcessor<NavigationEvent>

    override fun initBeforeView() {
        with(application as App) {
            di.inject(this@MainActivity)
        }
    }

    override fun getContentViewId(): Int = R.layout.activity_main

    @SuppressLint("CheckResult")
    override fun initViews() {
        navController = findNavController(R.id.mainNav)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            // LOCK NAVIGATION DRAWER IN ALL FRAGMENT ELSE MAIN_FRAGMENT
            if (controller.currentDestination?.id in arrayListOf(R.id.mainFragment))
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            else
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

        initNavDrawer()

        navEvents
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Navigation Event: ${it.navEvent}")
                when (it.navEvent) {
                    NavigationEvent.NavEvent.OPEN_DRAWER -> {
                        if (drawerLayout.isDrawerOpen(GravityCompat.START))
                            drawerLayout.closeDrawer(GravityCompat.START)
                        else
                            drawerLayout.openDrawer(GravityCompat.START)
                    }
                }
            }, {
                Timber.d(it)
            })
    }

    private fun initNavDrawer() {
        navView.setNavigationItemSelectedListener(this)
        navView.itemIconTintList = null

        val headerView = navView.getHeaderView(0)
        val consNavHeader = headerView.findViewById<ConstraintLayout>(R.id.consNavHeader)
        consNavHeader.setOnClickListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        when (item.itemId) {
            R.id.nav_credit -> {
                navController.navigate(R.id.action_mainFragment_to_blankFragment)
            }
            R.id.nav_transactions -> {
                /*intent.setClass(this, RecyclerViewActivity::class.java!!)
                startActivity(intent)*/
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View) {
        when (v) {
            consNavHeader -> {
                Timber.d("")
            }
        }
    }
}
