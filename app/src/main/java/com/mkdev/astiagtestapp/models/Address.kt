package com.mkdev.astiagtestapp.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Address(

        @field:SerializedName("country")
        val country: String? = "",

        @field:SerializedName("country_code")
        val countryCode: String? = "",

        @field:SerializedName("road")
        val road: String? = "",

        @field:SerializedName("city")
        val city: String? = "",

        @field:SerializedName("neighbourhood")
        val neighbourhood: String? = "",

        @field:SerializedName("county")
        val county: String? = "",

        @field:SerializedName("postcode")
        val postcode: String? = "",

        @field:SerializedName("suburb")
        val suburb: String? = "",

        @field:SerializedName("state")
        val state: String? = "") : Parcelable {

    constructor(source: Parcel) : this(source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString())

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