package com.belajar.uas_crud_202101251002

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.belajar.uas_crud_202101251002.adapter.MahasiswaAdapter
import com.belajar.uas_crud_202101251002.databinding.ActivityMainBinding
import com.belajar.uas_crud_202101251002.entity.Mahasiswa

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            intent.putExtra("HAS_INSTANCE_STATE", true)
            finish()
        }, 6000)
    }

}