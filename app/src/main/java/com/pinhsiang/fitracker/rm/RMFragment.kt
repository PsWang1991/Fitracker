package com.pinhsiang.fitracker.rm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.Float2StringConverter
import com.pinhsiang.fitracker.Int2StringConverter
import com.pinhsiang.fitracker.MainActivity
import com.pinhsiang.fitracker.databinding.FragmentRmBinding
import com.pinhsiang.fitracker.databinding.FragmentTimerBinding
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "Fitracker"

class RMFragment : Fragment() {

    private lateinit var binding: FragmentRmBinding

    /**
     * Lazily initialize [TimerViewModel].
     */
    private lateinit var viewModelFactory: RMViewModelFactory
    private val viewModel: RMViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(RMViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Bind ViewModel
        viewModelFactory = RMViewModelFactory()
        binding.viewModel = viewModel

        return binding.root
    }
}