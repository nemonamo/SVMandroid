package com.Team.sehun

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.viewpager2.widget.ViewPager2
import com.Team.sehun.data.remote.HomeCreator
import com.Team.sehun.data.remote.HomeResponse
import com.Team.sehun.databinding.ActivityMainBinding
import com.Team.sehun.feature.ViewPagerAdapter
import com.Team.sehun.feature.home.HomeFragment
import com.Team.sehun.feature.more.MoreFragment
import com.Team.sehun.feature.setting.SettingFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val channelID = "com.anushka.notificationdemo.channel1"
    private val channelID2 = "com.anushka.notificationdemo.channel2"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY = "key_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initBottomNavi()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //노티피케이션 채널 생성
        createNotificationChannel(channelID, "DemoChannel", "this is a demo")
        waitGuest()
    }
    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //중요도
            val importance = NotificationManager.IMPORTANCE_HIGH
            //채널 생성
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        } else {

        }
    }
    private val mDelayHandler: Handler by lazy {
        Handler()
    }

    private fun waitGuest() {
        mDelayHandler.postDelayed(::showGuest, 5500) // 10초 후에 showGuest 함수를 실행한다.
    }

    private fun showGuest() {
        // 실제 반복하는 코드를 여기에 적는다
        initNetwork("vibration")
        initNetwork("loadcell")
        waitGuest() // 코드 실행뒤에 계속해서 반복하도록 작업한다.
    }
    private fun displayNotification(flag : Int) {
        /* 1. 알림콘텐츠 설정*/
        //채널 ID
        if( flag == 1) {
            val notificationId = 45
            //알림의 탭 작업 설정 -----------------------------------------------------------------------
            //노티피케이션 생성 -------------------------------------------------------------------------
            val notification: Notification =
                NotificationCompat.Builder(this@MainActivity, channelID)
                    .setContentTitle("보안 카메라 이상 발생") // 노티 제목
                    .setContentText("보안카메라 내역을 확인해주세요") // 노티 내용
                    .setSmallIcon(android.R.drawable.ic_dialog_info) //아이콘이미
                    .setAutoCancel(true) // 사용자가 알림을 탭하면 자동으로 알림을 삭제합니다.
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()
            /* 3. 알림 표시*///---------------------------------------------------------------------------
            //NotificationManagerCompat.notify()에 전달하는 알림 ID를 저장해야 합니다.
            // 알림을 업데이트하거나 삭제하려면 나중에 필요하기 때문입니다.
            notificationManager?.notify(notificationId, notification) //노티실행
        }else if( flag == 2) {
            val notificationId = 45
            //알림의 탭 작업 설정 -----------------------------------------------------------------------
            //노티피케이션 생성 -------------------------------------------------------------------------
            val notification: Notification =
                NotificationCompat.Builder(this@MainActivity, channelID)
                    .setContentTitle("코인함 가득참") // 노티 제목
                    .setContentText("코인함을 확인해주세요") // 노티 내용
                    .setSmallIcon(android.R.drawable.ic_dialog_info) //아이콘이미
                    .setAutoCancel(true) // 사용자가 알림을 탭하면 자동으로 알림을 삭제합니다.
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()
            /* 3. 알림 표시*///---------------------------------------------------------------------------
            //NotificationManagerCompat.notify()에 전달하는 알림 ID를 저장해야 합니다.
            // 알림을 업데이트하거나 삭제하려면 나중에 필요하기 때문입니다.
            notificationManager?.notify(notificationId, notification) //노티실행
        }
    }

    private fun initAdapter() {
        val fragmentList = listOf(HomeFragment(), MoreFragment(), SettingFragment())
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.fragments.addAll(fragmentList)

        binding.vpMain.adapter = viewPagerAdapter
    }


    private fun initBottomNavi() {
        with(binding) {
            vpMain.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    bnvMain.menu.getItem(position).isChecked = true
                }
            })

            bnvMain.setOnItemSelectedListener {
                when (it.itemId) {
                    com.Team.sehun.R.id.menu_home -> {
                        vpMain.currentItem = com.Team.sehun.MainActivity.Companion.FIRST_FRAGMENT

                        return@setOnItemSelectedListener true
                    }
                    com.Team.sehun.R.id.menu_more -> {
                        vpMain.currentItem = com.Team.sehun.MainActivity.Companion.SECOND_FRAGMENT

                        return@setOnItemSelectedListener true
                    }
                    else -> {
                        vpMain.currentItem = com.Team.sehun.MainActivity.Companion.THIRD_FRAGMENT

                        return@setOnItemSelectedListener true
                    }
                }
            }
        }
    }
    private fun initNetwork(container: String) {

        val call: Call<HomeResponse> = HomeCreator.homeService.getContainer(container)
        call.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    when (container) {
                        "vibration" -> {
                            var dataint = data?.con?.toInt()
                            if(dataint  == 1)
                                displayNotification(1)
                        }
                        "loadcell"-> {
                            var loadcelldata : String? = data?.con
                            if(loadcelldata == "full")
                                displayNotification(2)
                            else
                                loadcelldata?.let { Log.d("locellerror", it) }
                        }
                    }


                } else Log.d("else", "asd")
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("debugTest", "error:(${t.message})")
            }
        })
    }
    companion object {
        const val FIRST_FRAGMENT = 0
        const val SECOND_FRAGMENT = 1
        const val THIRD_FRAGMENT = 2
    }

}