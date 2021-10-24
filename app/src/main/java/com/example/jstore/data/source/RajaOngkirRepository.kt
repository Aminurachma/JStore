package com.example.jstore.data.source

import com.example.jstore.data.source.remote.RemoteDataSource
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class RajaOngkirRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : RajaOngkirDataSource {

//    override fun login(username: String, password: String): Flow<Resource<LoginResponse>> = flow {
//        remoteDataSource.login(username, password).let {
//            it.suspendOnSuccess {
//                emit(Resource.success(data))
//            }.suspendOnError {
//                emit(Resource.error(map(ErrorResponseMapper)?.message ?: DEFAULT_ERROR_MESSAGE, null))
//            }.suspendOnException {
//                emit(Resource.error(message(), null))
//            }
//        }
//    }.onStart { emit(Resource.loading(null)) }.flowOn(ioDispatcher)

}
