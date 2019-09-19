package com.pinhsiang.fitracker.recommended

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.databinding.ActivityRecommendedBinding

const val TAG = "Fitracker"

class RecommendedActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecommendedBinding

    /**
     * Lazily initialize our [RecommendedViewModel].
     */
    private val viewModel: RecommendedViewModel by lazy {
        ViewModelProviders.of(this).get(RecommendedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Recommended Workout Video"

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recommended)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.rvYoutube.adapter = YoutubeRecyclerAdapter()

    }
}