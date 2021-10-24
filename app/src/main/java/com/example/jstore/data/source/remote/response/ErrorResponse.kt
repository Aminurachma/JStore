package com.example.jstore.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "rajaongkir")
    val rajaongkir: Rajaongkir? = null
) : Parcelable {
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Rajaongkir(
        @Json(name = "status")
        val status: Status? = null
    ) : Parcelable {
        @Parcelize
        @JsonClass(generateAdapter = true)
        data class Status(
            @Json(name = "code")
            val code: Int? = null,
            @Json(name = "description")
            val description: String? = null
        ) : Parcelable
    }
}