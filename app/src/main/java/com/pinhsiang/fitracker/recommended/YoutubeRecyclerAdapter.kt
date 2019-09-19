package com.pinhsiang.fitracker.recommended

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinhsiang.fitracker.data.YoutubeVideo
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pinhsiang.fitracker.databinding.ItemYoutubeListBinding

class YoutubeRecyclerAdapter : ListAdapter<YoutubeVideo, YoutubeRecyclerAdapter.YoutubeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        return YoutubeViewHolder(
            ItemYoutubeListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [YoutubeVideo]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<YoutubeVideo>() {
        override fun areItemsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
            return (oldItem.title == newItem.title && oldItem.videoId == newItem.videoId)
        }
    }

    class YoutubeViewHolder(var binding: ItemYoutubeListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(youtubeVideo: YoutubeVideo) {
            binding.youtube = youtubeVideo
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(item.videoId, 0f)
            }

        })

        holder.bind(item)
    }
}