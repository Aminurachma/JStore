package com.example.jstore.data.source.remote.api

import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.data.source.remote.response.GetCostResponse
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.*

interface ApiService {

    @GET("province")
    suspend fun getProvinces(): ApiResponse<GetProvinceResponse>

    @GET("city")
    suspend fun getCities(@Query("province") provinceId: String): ApiResponse<GetCityResponse>

    @FormUrlEncoded
    @POST("cost")
    suspend fun getCost(
        @Field("origin") origin: String,
        @Field("destination") destination: String,
        @Field("weight") weight: Int,
        @Field("courier") courier: String
    ): ApiResponse<GetCostResponse>


}