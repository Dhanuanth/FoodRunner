package com.dhanuanth.foodapp.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dhanuanth.foodapp.R

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 2000
    lateinit var anim1: Animation

    lateinit var slog: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        slog = findViewById(R.id.slogan)
        anim1 = AnimationUtils.loadAnimation(this, R.anim.top_animation)

        slog.animation = anim1
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({

            startActivity(
                Intent(
                    this,
                    LoginActivity()::class.java
                )
            )

            finish()
        }, SPLASH_TIME_OUT)
    }


}


