package com.dicoding.myuserstory.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myuserstory.databinding.ItemStoryBinding
import com.dicoding.myuserstory.model.Story
import com.dicoding.myuserstory.ui.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (story : Story) {
            binding.apply {
                with(story) {
                    Glide.with(itemView.context)
                        .load(photoUrl)
                        .into(ivStory)
                    tvName.text = "$name"
                    tvDescription.text = description

                    itemView.setOnClickListener{
                        val intent = Intent(itemView.context, DetailActivity::class.java)
                        intent.putExtra("STORY", story)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(ivStory, "photo")
                            )
                        itemView.context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }
}