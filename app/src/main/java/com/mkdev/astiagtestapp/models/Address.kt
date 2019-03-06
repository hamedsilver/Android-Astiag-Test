package com.mkdev.astiagtestapp.models

import com.google.gson.annotations.SerializedName

data class Address(

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("country_code")
        val countryCode: String? = null,

        @field:SerializedName("road")
        val road: String? = null,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("neighbourhood")
        val neighbourhood: String? = null,

        @field:SerializedName("county")
        val county: String? = null,

        @field:SerializedName("postcode")
        val postcode: String? = null,

        @field:SerializedName("suburb")
        val suburb: String? = null,

        @field:SerializedName("state")
        val state: String? = null
)