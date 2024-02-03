package com.dicoding.myuserstory.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.myuserstory.databinding.ActivitySpalshBinding
import com.dicoding.myuserstory.ui.auth.AuthActivity
import com.dicoding.myuserstory.ui.main.MainActivity
import com.dicoding.myuserstory.utils.DataPref

class SpalshActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySpalshBinding
    private lateinit var dataPref : DataPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpalshBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataPref = DataPref(this)

        Handler(mainLooper).postDelayed({
            if (dataPref.isLogin()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }, 2000)
        setupView()
        playAnimation()
    }

    private fun playAnimation() {
        val tagline = ObjectAnimator.ofFloat(binding.tagline, View.ALPHA, 1f).setDuration(500)
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(logo,tagline)
            start()
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

}