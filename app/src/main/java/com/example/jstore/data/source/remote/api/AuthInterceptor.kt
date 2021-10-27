package com.example.jstore.data.source.remote.api

import com.example.jstore.BuildConfig
import com.example.jstore.utils.Constants.KEY
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header(KEY, BuildConfig.API_KEY)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}