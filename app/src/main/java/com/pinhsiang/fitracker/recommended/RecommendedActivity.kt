package com.pinhsiang.fitracker.recommended

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.pinhsiang.fitracker.R
import com.pinhsiang.fitracker.data.Nutrition
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

    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener
    lateinit var youTubePlayerFragment: YouTubePlayerSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recommended)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


    }

    fun initYoutubePlayer() {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
                if(player == null) return
                if(wasRestored) {
                    player.play()
                } else {
                    player.cueVideo("oHg5SJYRHA0")
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                }
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Log.i(TAG, "Failed to initialize")
            }
        }
        youTubePlayerFragment =
            supportFragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerSupportFragment
        youTubePlayerFragment.initialize(getString(R.string.youtube_api_key), youtubePlayerInit)
    }
}