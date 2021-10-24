package com.example.jstore.data.source.remote.api

import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface ApiService {

    @GET("province")
    suspend fun getProvince(): ApiResponse<GetProvinceResponse>

//    @FormUrlEncoded
//    @POST("users/login")
//    suspend fun login(
//        @Field("username") username: String,
//        @Field("password") password: String
//    ): ApiResponse<LoginResponse>
//

}