package com.example.jstore.data.source.remote

import com.skydoves.sandwich.ApiResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.jstore.data.source.remote.api.ApiService
import java.io.File
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

//    suspend fun login(username: String, password: String): ApiResponse<LoginResponse> =
//        apiService.login(username, password)

}
