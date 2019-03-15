package com.kpiroom.bubble.ui.splash

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.accountSetup.AccountSetupActivity
import com.kpiroom.bubble.ui.login.LoginActivity
import com.kpiroom.bubble.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        boot(this@SplashActivity)
    }

    private fun boot(context: Context) {
        val intent = Source.userPrefs.let { pref ->
            pref.uuid?.let {
                pref.username?.let {
                    MainActivity.getIntent(context)
                } ?: AccountSetupActivity.getIntent(context)

            } ?: run {
                LoginActivity.getIntent(context)
            }
        }
        startActivity(intent)
        finish()
    }
}