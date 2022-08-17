package com.Team.sehun.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.net.HttpURLConnection
import java.net.URL

interface HomeService {
    @Headers(
        "X-M2M-RI: 12345",
        "X-M2M-Origin: 123",
        "nmtype: short"
    )
    @GET("/Mobius/svm/{container}/latest") // dust, ultrasonic


    fun getContainer(
        @Path("container") container: String
    ): Call<HomeResponse>
}