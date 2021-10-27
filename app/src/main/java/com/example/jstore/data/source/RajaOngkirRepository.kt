package com.example.jstore.data.source

import com.example.jstore.data.source.remote.RemoteRepository
import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.data.source.remote.response.GetCostResponse
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.example.jstore.utils.Constants.DEFAULT_ERROR_MESSAGE
import com.example.jstore.utils.ErrorResponseMapper
import com.example.jstore.vo.Resource
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class RajaOngkirRepository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val ioDispatcher: CoroutineDispatcher
) : RajaOngkirDataSource {

    override fun getProvinces(): Flow<Resource<GetProvinceResponse>> = flow {
        remoteRepository.getProvinces().let {
            it.suspendOnSuccess {
                emit(Resource.success(data))
            }.suspendOnError {
                emit(Resource.error(map(ErrorResponseMapper)?.rajaongkir?.status?.description ?: DEFAULT_ERROR_MESSAGE, null))
            }.suspendOnException {
                emit(Resource.error(message(), null))
            }
        }
    }.onStart { emit(Resource.loading(null)) }.flowOn(ioDispatcher)

    override fun getCities(provinceId: String): Flow<Resource<GetCityResponse>> = flow {
        remoteRepository.getCities(provinceId).let {
            it.suspendOnSuccess {
                emit(Resource.success(data))
            }.suspendOnError {
                emit(Resource.error(map(ErrorResponseMapper)?.rajaongkir?.status?.description ?: DEFAULT_ERROR_MESSAGE, null))
            }.suspendOnException {
                emit(Resource.error(message(), null))
            }
        }
    }.onStart { emit(Resource.loading(null)) }.flowOn(ioDispatcher)

    override fun getCost(
        origin: String,
        destination: String,
        weight: Int,
        courier: String
    ): Flow<Resource<GetCostResponse>> = flow {
        remoteRepository.getCost(origin, destination, weight, courier).let {
            it.suspendOnSuccess {
                emit(Resource.success(data))
            }.suspendOnError {
                emit(Resource.error(map(ErrorResponseMapper)?.rajaongkir?.status?.description ?: DEFAULT_ERROR_MESSAGE, null))
            }.suspendOnException {
                emit(Resource.error(message(), null))
            }
        }
    }.onStart { emit(Resource.loading(null)) }.flowOn(ioDispatcher)

}
