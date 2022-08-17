package com.Team.sehun.feature.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Team.sehun.R
import com.Team.sehun.data.remote.HomeCreator
import com.Team.sehun.data.remote.HomeResponse
import com.Team.sehun.databinding.ActivitySecurityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SecurityActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecurityBinding

    var dataname = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNetworkdate()
        initNetwork()
        clickEvent()
    }

    private fun clickEvent() {
        binding.ivSecurityBtnback.setOnClickListener { finish() }
    }

    private fun initNetworkdate() {
        val container = "outsideimg"

        val call: Call<HomeResponse> = HomeCreator.homeService.getContainer(container)
        call.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    data?.let { Log.d("cttest", it.ct) }
                    data?.let { GetDatatwo(it.ct) }
                } else Log.d("ctelse", "asd")
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("debugTest", "error:(${t.message})")
            }
        })
    }

    private fun initNetwork() {
        val container = "outsideimg"

        val call: Call<HomeResponse> = HomeCreator.homeService.getContainer(container)
        call.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    data?.let { Log.d("contest", (it.con)) }
                    dataname = data?.con.toString()
                    data?.let { GetData(it.con) }
                } else Log.d("conelse", "asd")
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("debugTest", "error:(${t.message})")
            }
        })
    }

    private fun GetData(base64: String) {

        val encodeByte = Base64.decode(dataname, Base64.NO_WRAP)
        val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        //서버에서 다 못받음
        binding.ivSecurityImg.setImageBitmap(bitmap)

    }


    private fun GetDatatwo(container: String) {

        binding.tvSecurityBtn1.text = container
    }

}