package com.example.jstore.data.source

import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.data.source.remote.response.GetCostResponse
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.example.jstore.vo.Resource
import kotlinx.coroutines.flow.Flow

interface RajaOngkirDataSource {

    fun getProvinces(): Flow<Resource<GetProvinceResponse>>

    fun getCities(provinceId: String): Flow<Resource<GetCityResponse>>

    fun getCost(origin: String, destination: String, weight: Int, courier: String): Flow<Resource<GetCostResponse>>

}