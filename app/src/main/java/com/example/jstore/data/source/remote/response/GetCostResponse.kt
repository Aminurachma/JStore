package com.example.jstore.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class GetCostResponse(
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
        @Json(name = "origin_details")
        val originDetails: OriginDetails? = null,
        @Json(name = "destination_details")
        val destinationDetails: DestinationDetails? = null,
        @Json(name = "results")
        val results: List<Result> = emptyList()
    ) : Parcelable {
        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Query(
            @Json(name = "origin")
            val origin: String? = null,
            @Json(name = "destination")
            val destination: String? = null,
            @Json(name = "weight")
            val weight: Int? = null,
            @Json(name = "courier")
            val courier: String? = null
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
        data class OriginDetails(
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

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class DestinationDetails(
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

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Result(
            @Json(name = "code")
            val code: String? = null,
            @Json(name = "name")
            val name: String? = null,
            @Json(name = "costs")
            val costs: List<Cost> = emptyList()
        ) : Parcelable {
            @JsonClass(generateAdapter = true)
            @Parcelize
            data class Cost(
                @Json(name = "service")
                val service: String? = null,
                @Json(name = "description")
                val description: String? = null,
                @Json(name = "cost")
                val cost: List<Cost> = emptyList()
            ) : Parcelable {
                @JsonClass(generateAdapter = true)
                @Parcelize
                data class Cost(
                    @Json(name = "value")
                    val value: Int? = null,
                    @Json(name = "etd")
                    val etd: String? = null,
                    @Json(name = "note")
                    val note: String? = null
                ) : Parcelable
            }
        }
    }
}