package com.dicoding.myuserstory.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.myuserstory.R
import com.dicoding.myuserstory.databinding.ActivityAuthBinding
import com.dicoding.myuserstory.ui.main.MainActivity
import com.dicoding.myuserstory.utils.DataPref

class AuthActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthBinding
    private lateinit var dataPref: DataPref
    private val viewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        binding.btnGuest.setOnClickListener {
            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
            finish()
        }
        binding.btnRegister.setOnClickListener {
            RegisterFragment().show(supportFragmentManager, "RegisterFragment")
        }
        binding.btnLogin.setOnClickListener {
            binding.apply {
                val email = edEmailLogin.text.toString()
                val password = edPasswordLogin.text.toString()
                if(edEmailLogin.text?.isEmpty()!! || edPasswordLogin.text?.isEmpty()!!){
                    Toast.makeText(this@AuthActivity,R.string.must_fill,Toast.LENGTH_SHORT).show()
                }else if(password.length < 8){
                    Toast.makeText(this@AuthActivity,R.string.too_short,Toast.LENGTH_SHORT).show()
                }else{
                    dataPref = DataPref(this@AuthActivity)
                    if(edEmailLogin.text?.isNotEmpty()!! && edPasswordLogin.text?.isNotEmpty()!!){
                        showProgress()
                        viewModel.apply {
                            login(this@AuthActivity,email,password)

                            state.observe(this@AuthActivity){ it ->
                                if (it == true){
                                    user.observe(this@AuthActivity){
                                        if(!it.error){
                                            with(it){
                                                dataPref.setUser(
                                                    loginResult.userId,
                                                    loginResult.name,
                                                    loginResult.token
                                                )
                                            }
                                        }
                                    }
                                    Toast.makeText(this@AuthActivity,resources.getString(R.string.login_success_message),Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                    finish()
                                }else{
                                    Toast.makeText(this@AuthActivity,resources.getString(R.string.login_failed_message),Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
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

    private fun showProgress() {
        ProgressFragment().show(supportFragmentManager, "ProgressFragment")
    }
}