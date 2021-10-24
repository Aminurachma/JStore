package com.example.jstore.data.source.remote

import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.data.source.remote.response.GetCostResponse
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.skydoves.sandwich.ApiResponse

interface RemoteDataSource {

    suspend fun getProvinces(): ApiResponse<GetProvinceResponse>

    suspend fun getCities(provinceId: String): ApiResponse<GetCityResponse>

    suspend fun getCost(origin: String, destination: String, weight: Int, courier: String): ApiResponse<GetCostResponse>

}
