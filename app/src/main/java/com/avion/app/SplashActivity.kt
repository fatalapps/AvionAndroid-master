package com.avion.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Logger.addLogAdapter(AndroidLogAdapter())
        val currentUser = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed({
            if (currentUser == null)
                startActivity(Intent(this, SigninActivity::class.java))
            else
                startActivity(Intent(this, MainActivity::class.java))

            finish()
        }, 1000)

    }
}
