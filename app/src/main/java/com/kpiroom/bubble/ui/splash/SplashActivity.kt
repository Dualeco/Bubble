package com.kpiroom.bubble.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kpiroom.bubble.R
import com.kpiroom.bubble.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startActivity(LoginActivity.getIntent(this@SplashActivity))
        finish()
    }
}