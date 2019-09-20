//package com.pinhsiang.fitracker.recommended
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.ViewModelProviders
//import com.pinhsiang.fitracker.R
//import com.pinhsiang.fitracker.databinding.ActivityRecommendedBinding
//
//const val TAG = "Fitracker"
//
//class RecommendedActivity : AppCompatActivity() {
//
//    lateinit var binding: ActivityRecommendedBinding
//
//    /**
//     * Lazily initialize our [RecommendedViewModel].
//     */
//    private val viewModel: RecommendedViewModel by lazy {
//        ViewModelProviders.of(this).get(RecommendedViewModel::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        supportActionBar?.title = "Recommended Workout Video"
//
//        binding = DataBindingUtil.setContentView(this, R.layout.fragment_recommended)
//        binding.viewModel = viewModel
//        binding.lifecycleOwner = this
//
//        binding.rvYoutube.adapter = YoutubeRecyclerAdapter()
//
//    }
//}