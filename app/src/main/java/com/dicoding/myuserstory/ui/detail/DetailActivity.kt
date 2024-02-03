package com.dicoding.myuserstory.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.myuserstory.databinding.ActivityDetailBinding
import com.dicoding.myuserstory.model.Story

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        setPost()
    }
    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            val story = intent.getParcelableExtra<Story>("STORY") as Story
            title = story.name.toUpperCase()
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setPost() {
        with(binding) {
            val story = intent.getParcelableExtra<Story>("STORY") as Story
            Glide.with(applicationContext)
                .load(story.photoUrl)
                .into(ivStory)
            tvDescription.text = story.description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}