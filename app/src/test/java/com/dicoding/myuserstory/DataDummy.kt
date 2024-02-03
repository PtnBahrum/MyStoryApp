package com.dicoding.myuserstory

import com.dicoding.myuserstory.model.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "name $i",
                "deskripsi $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                i.toFloat(),
                i.toFloat()
            )
            items.add(story)
        }
        return items
    }
}