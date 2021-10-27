package com.example.jstore.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class GetProvinceResponse(
    @Json(name = "rajaongkir")
    val rajaongkir: RajaOngkir? = null
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class RajaOngkir(
        @Json(name = "query")
        val query: List<Query> = emptyList(),
        @Json(name = "status")
        val status: Status? = null,
        @Json(name = "results")
        val results: List<Result> = emptyList()
    ) : Parcelable {

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
        data class Query(
            @Json(name = "id")
            val id: String? = null
        ) : Parcelable

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Result(
            @Json(name = "province_id")
            val provinceId: String? = null,
            @Json(name = "province")
            val province: String? = null
        ) : Parcelable
    }
}