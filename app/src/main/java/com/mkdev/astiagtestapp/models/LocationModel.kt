package com.mkdev.astiagtestapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LocationModel(

        @field:SerializedName("osm_id") val osmId: String? = null,

        @field:SerializedName("place_rank") val placeRank: String? = null,

        @field:SerializedName("licence") val licence: String? = null,

        @field:SerializedName("boundingbox") val boundingbox: List<String?>? = null,

        @field:SerializedName("address") val address: Address? = null,

        @field:SerializedName("importance") val importance: String? = null,

        @field:SerializedName("lon") val lon: String? = null,

        @field:SerializedName("type") val type: String? = null,

        @field:SerializedName("display_name") val displayName: String? = null,

        @field:SerializedName("osm_type") val osmType: String? = null,

        @field:SerializedName("name") val name: String? = null,

        @field:SerializedName("addresstype") val addresstype: String? = null,

        @field:SerializedName("category") val category: String? = null,

        @field:SerializedName("place_id") val placeId: String? = null,

        @field:SerializedName("lat") val lat: String? = null) : Parcelable {
    fun getMainAddress() = "${address?.neighbourhood} - ${address?.road}"

    fun getSubAddress() = "${address?.city} - ${address?.suburb}"

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), ArrayList<String?>().apply { source.readList(this, String::class.java.classLoader) }, source.readParcelable<Address>(Address::class.java.classLoader), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(osmId)
        writeString(placeRank)
        writeString(licence)
        writeList(boundingbox)
        writeParcelable(address, 0)
        writeString(importance)
        writeString(lon)
        writeString(type)
        writeString(displayName)
        writeString(osmType)
        writeString(name)
        writeString(addresstype)
        writeString(category)
        writeString(placeId)
        writeString(lat)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocationModel> = object : Parcelable.Creator<LocationModel> {
            override fun createFromParcel(source: Parcel): LocationModel = LocationModel(source)
            override fun newArray(size: Int): Array<LocationModel?> = arrayOfNulls(size)
        }
    }
}