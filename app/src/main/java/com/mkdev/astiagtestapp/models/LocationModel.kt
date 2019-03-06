package com.mkdev.astiagtestapp.models

import com.google.gson.annotations.SerializedName

data class LocationModel(

        @field:SerializedName("osm_id")
        val osmId: String? = null,

        @field:SerializedName("place_rank")
        val placeRank: String? = null,

        @field:SerializedName("licence")
        val licence: String? = null,

        @field:SerializedName("boundingbox")
        val boundingbox: List<String?>? = null,

        @field:SerializedName("address")
        val address: Address? = null,

        @field:SerializedName("importance")
        val importance: String? = null,

        @field:SerializedName("lon")
        val lon: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("display_name")
        val displayName: String? = null,

        @field:SerializedName("osm_type")
        val osmType: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("addresstype")
        val addresstype: String? = null,

        @field:SerializedName("category")
        val category: String? = null,

        @field:SerializedName("place_id")
        val placeId: String? = null,

        @field:SerializedName("lat")
        val lat: String? = null) {

    fun getMainAddress() = "${address?.neighbourhood} - ${address?.road}"

    fun getSubAddress() = "${address?.city} - ${address?.suburb}"
}