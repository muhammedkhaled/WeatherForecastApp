package com.mukhaled.weatherforecastapp.core.data.api.interceptors


import com.mukhaled.weatherforecastapp.core.data.api.ApiParameters
import okhttp3.*
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val interceptedRequest: Request = chain.createAuthenticatedRequest()
        return chain.proceed(interceptedRequest)
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(): Request {
        return request()
            .newBuilder()
            .addHeader(ApiParameters.ACCEPT, "application/json")
            .build()
    }

}
