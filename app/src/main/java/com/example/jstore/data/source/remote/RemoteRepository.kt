package com.example.jstore.data.source.remote

import com.example.jstore.data.source.remote.api.ApiService
import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.data.source.remote.response.GetCostResponse
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiService: ApiService): RemoteDataSource {

    override suspend fun getProvinces(): ApiResponse<GetProvinceResponse> {
        return apiService.getProvinces()
    }

    override suspend fun getCities(provinceId: String): ApiResponse<GetCityResponse> {
        return apiService.getCities(provinceId)
    }

    override suspend fun getCost(
        origin: String,
        destination: String,
        weight: Int,
        courier: String
    ): ApiResponse<GetCostResponse> {
        return apiService.getCost(origin, destination, weight, courier)
    }


}
