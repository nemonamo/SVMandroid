package com.Team.sehun.feature.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.Team.sehun.data.remote.HomeCreator
import com.Team.sehun.data.remote.HomeResponse
import com.Team.sehun.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vibrationflag
import vmstate
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: error("binding no")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        clickEvent()
        localDate()
        initNetwork("ultrasonic")
        initNetwork("vibration")
        waitGuest()

        return binding.root
    }


    private val mDelayHandler: Handler by lazy {
        Handler()
    }

    private fun waitGuest() {
        mDelayHandler.postDelayed(::showGuest, 5000) // 10초 후에 showGuest 함수를 실행한다.
    }

    private fun showGuest() {
        // 실제 반복하는 코드를 여기에 적는다
        initNetwork("ultrasonic")
        initNetwork("vibration")
        waitGuest() // 코드 실행뒤에 계속해서 반복하도록 작업한다.
    }

    private fun clickEvent() {
        with(binding) {
            clHomeBtnmanage.setOnClickListener {

                startActivity(
                    Intent(
                        context,
                        NowvmActivity::class.java
                    )
                )


            }
            clHomeBtnsecurity.setOnClickListener {
                startActivity(
                    Intent(
                        context,
                        SecurityActivity::class.java
                    )
                )
            }
            clHomeBtnclean.setOnClickListener {
                startActivity(
                    Intent(
                        context,
                        CleanActivity::class.java
                    )
                )
            }

        }
    }

    private fun localDate() {
        val currentTime: Long = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일", Locale("ko", "KR"))
        binding.tvMainLocaldate.text = dateFormat.format(currentTime)

    }

    private fun initNetwork(container: String) {

        val call: Call<HomeResponse> = HomeCreator.homeService.getContainer(container)
        call.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    when (container) {
                        "ultrasonic" -> {
                            data?.let { GetData(it.con) }
                        }
                        else -> {
                            data?.let { GetDatatwo(it.con) }
                        }
                    }
                } else Log.d("else", "asd")
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("debugTest", "error:(${t.message})")
            }
        })
    }


    private fun GetData(ultrasonic: String) {
        binding.tvHomeUltrasonic.text = ultrasonic
        SellingData(ultrasonic.toInt())
        InventoryData(ultrasonic.toInt())
    }

    private fun GetDatatwo(container: String) {

        vibrationflag = container.toInt();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun InventoryData(can: Int) {

        if (can == 7 || can == 6 || can == 5) {
            binding.tvHomeCanstate.text = "재고 충분"
            binding.ivHomeLocation1.setImageDrawable(
                resources.getDrawable(com.Team.sehun.R.drawable.vending_blu, null)
            )
            vmstate = 0 // color b
        } else if (can == 4) {
            binding.tvHomeCanstate.text = "재고 절반"
            binding.ivHomeLocation1.setImageDrawable(
                resources.getDrawable(com.Team.sehun.R.drawable.vending_pup, null)
            )
            vmstate = 1 // color p
        } else if (can == 3 || can == 2 || can == 1) {
            binding.tvHomeCanstate.text = "재고 부족"
            binding.ivHomeLocation1.setImageDrawable(
                resources.getDrawable(com.Team.sehun.R.drawable.vending_red, null)
            )
            vmstate = 2 // color r
        } else if(can == 0){
            binding.tvHomeCanstate.text = "재고 없음"
            binding.ivHomeLocation1.setImageDrawable(
                resources.getDrawable(com.Team.sehun.R.drawable.vending_b, null)
            )
            vmstate = 3 // emtpy 0
        }
        else
        {
            binding.tvHomeCanstate.text = "error"
            binding.tvHomeUltrasonic.text = "error"
            binding.tvHomeSellingcan.text = "error"
        }
    }

    private fun SellingData(sellingcan: Int) {
        var can_default: Int = 7
        binding.tvHomeSellingcan.text = (can_default - sellingcan).toString()
    }


}