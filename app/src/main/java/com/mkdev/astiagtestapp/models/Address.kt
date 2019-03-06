package com.mkdev.astiagtestapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Address(

        @field:SerializedName("country") val country: String? = null,

        @field:SerializedName("country_code") val countryCode: String? = null,

        @field:SerializedName("road") val road: String? = null,

        @field:SerializedName("city") val city: String? = null,

        @field:SerializedName("neighbourhood") val neighbourhood: String? = null,

        @field:SerializedName("county") val county: String? = null,

        @field:SerializedName("postcode") val postcode: String? = null,

        @field:SerializedName("suburb") val suburb: String? = null,

        @field:SerializedName("state") val state: String? = null) : Parcelable {
    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(country)
        writeString(countryCode)
        writeString(road)
        writeString(city)
        writeString(neighbourhood)
        writeString(county)
        writeString(postcode)
        writeString(suburb)
        writeString(state)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Address> = object : Parcelable.Creator<Address> {
            override fun createFromParcel(source: Parcel): Address = Address(source)
            override fun newArray(size: Int): Array<Address?> = arrayOfNulls(size)
        }
    }
}