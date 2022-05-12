package com.sliidepracticaltask.network

import android.util.Log
import com.sliidepracticaltask.BuildConfig
import com.sliidepracticaltask.utils.AppConst
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val BASE_URL: String = AppConst.BASE_URL

    val retrofit: Retrofit by lazy {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        val client = OkHttpClient.Builder()
        client.connectTimeout(1, TimeUnit.MINUTES)
        client.readTimeout(1, TimeUnit.MINUTES)
        client.writeTimeout(1, TimeUnit.MINUTES)


        //Add Interceptor
        val interceptor = Interceptor {
            val request = it.request()

            try {
                val newBuilder = request.newBuilder()
                newBuilder.addHeader("Content-Type", "application/json")
                newBuilder.addHeader("Authorization", "Bearer ${AppConst.API_TOKEN}")
                val response = it.proceed(newBuilder.build())
                return@Interceptor response

            } catch (e: Exception) {
                e.printStackTrace()
            }
            val response = it.proceed(request)
            response

        }
        client.addInterceptor(interceptor)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(loggingInterceptor)
        }

        builder.client(client.build())
        builder.build()
    }

    val service: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}