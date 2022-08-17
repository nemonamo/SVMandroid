package com.Team.sehun.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.Team.sehun.data.remote.HomeService

object HomeCreator {
    private const val BASE_URL = "http://203.253.128.177:7579"
    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    val homeService: HomeService = retrofit.create(HomeService::class.java)


}