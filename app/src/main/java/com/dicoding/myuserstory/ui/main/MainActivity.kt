package com.dicoding.myuserstory.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myuserstory.R
import com.dicoding.myuserstory.adapter.LoadingStateAdapter
import com.dicoding.myuserstory.adapter.StoryAdapter
import com.dicoding.myuserstory.databinding.ActivityMainBinding
import com.dicoding.myuserstory.ui.auth.AuthActivity
import com.dicoding.myuserstory.ui.maps.MapsActivity
import com.dicoding.myuserstory.ui.story.StoryActivity
import com.dicoding.myuserstory.utils.DataPref

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataPref: DataPref

    private val viewModel: MainViewModel by viewModels{
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataPref = DataPref(this)
        setToolbar()

        binding.tvWelcome.text = "${resources.getString(R.string.welcome)}${dataPref.getName()}!"
        binding.btnAdd.setOnClickListener{
            startActivity(Intent(this, StoryActivity::class.java))
        }
        binding.btnLocation.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
        }
        setInformationForUser()
        setInformationforGuest()
        if(dataPref.isLogin() == true){
            binding.btnAdd.isVisible = true
            binding.btnLocation.isVisible = true
        }
        showRecyclerView()
    }

    private fun setToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when(it .itemId) {
                R.id.exit -> {
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle(resources.getString(R.string.logout))
                    dialog.setMessage(resources.getString(R.string.alert_message))
                    dialog.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                        dataPref.removeUser()
                        startActivity(Intent(this, AuthActivity::class.java))
                        finish()
                    }
                    dialog.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                    dialog.show()
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                else -> false
            }
        }
    }
    private fun setInformationForUser() {
        binding.forUser.isVisible = dataPref.isLogin()
    }

    private fun setInformationforGuest() {
        binding.forGuest.isVisible = !dataPref.isLogin()

        binding.linkLogin.setOnClickListener {
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
            finish()
        }
    }

    private fun showRecyclerView() {
        val adapter = StoryAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
    override fun onBackPressed() {
        finish()
    }
}