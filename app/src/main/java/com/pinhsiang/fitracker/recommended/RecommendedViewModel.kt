package com.pinhsiang.fitracker.recommended

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinhsiang.fitracker.data.YoutubeVideo

class RecommendedViewModel : ViewModel() {

    private val _youtubeVideoList = MutableLiveData<List<YoutubeVideo>>()
    val youtubeVideoList: LiveData<List<YoutubeVideo>>
        get() = _youtubeVideoList

    init {
        createMockVideoList()
    }

    private fun createMockVideoList() {

        val video1 = YoutubeVideo(
            title = "How to Lose Weight WITHOUT Counting Calories!!",
            videoId = "wJ0QXCTqjUs"
        )

        val video2 = YoutubeVideo(
                title = "The Back Workout “Master Tip” (EVERY EXERCISE!)",
            videoId = "TQUaU1jibAE"
        )

        val video3 = YoutubeVideo(
            title = "The Triceps Workout “Master Tip” (EVERY EXERCISE!)",
            videoId = "5PsCMjseTZA"
        )

        val video4 = YoutubeVideo(
            title = "館長深蹲教學 2017",
            videoId = "TaM2sgxOduU"
        )

        val video5 = YoutubeVideo(
            title = "【館長教學】重訓新手必看！機械式器材訓練技巧",
            videoId = "h_uBuJOYI4E"
        )

        val dataList = mutableListOf(video1, video2, video3, video4, video5)
        _youtubeVideoList.value = dataList
    }

}