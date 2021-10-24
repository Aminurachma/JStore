package com.example.jstore.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class GetCityResponse(
    @Json(name = "rajaongkir")
    val rajaOngkir: RajaOngkir? = null
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class RajaOngkir(
        @Json(name = "query")
        val query: Query? = null,
        @Json(name = "status")
        val status: Status? = null,
        @Json(name = "results")
        val results: List<Result> = emptyList()
    ) : Parcelable {

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Query(
            @Json(name = "province")
            val province: String? = null
        ) : Parcelable

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Status(
            @Json(name = "code")
            val code: Int? = null,
            @Json(name = "description")
            val description: String? = null
        ) : Parcelable

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Result(
            @Json(name = "city_id")
            val cityId: String? = null,
            @Json(name = "province_id")
            val provinceId: String? = null,
            @Json(name = "province")
            val province: String? = null,
            @Json(name = "type")
            val type: String? = null,
            @Json(name = "city_name")
            val cityName: String? = null,
            @Json(name = "postal_code")
            val postalCode: String? = null
        ) : Parcelable
    }
}