package com.Team.sehun.data.remote

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("m2m:cin")
    val data: Data
) {
    data class Data(
        val pi: String,
        val ri: String,
        val ty: Int,
        val ct: String,
        val st: Int,
        val rn: String,
        val lt: String,
        val et: String,
        val cs: Int,
        val cr: String,
        val con: String
    )
}

/*
{
    "m2m:cin": {
    "pi": "3-20220525054056084255",
    "ri": "4-20220525062304413130",
    "ty": 4,
    "ct": "20220525T062304",
    "st": 245,
    "rn": "4-20220525062304412",
    "lt": "20220525T062304",
    "et": "20240525T062304",
    "cs": 33,
    "cr": "SY7-GBHxEQhd",
    "con": "pm1_0 :10 , pm2_5 : 13 , pm10 :14"
    }
}
*/