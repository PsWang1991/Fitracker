package com.pinhsiang.fitracker.tdee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.Int2StringConverter
import com.pinhsiang.fitracker.MainActivity
import com.pinhsiang.fitracker.databinding.FragmentTdeeBinding
import com.pinhsiang.fitracker.databinding.FragmentTimerBinding
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "Fitracker"

class TDEEFragment : Fragment() {

    private lateinit var binding: FragmentTdeeBinding

    /**
     * Lazily initialize [TimerViewModel].
     */
    private lateinit var viewModelFactory: TDEEViewModelFactory
    private val viewModel: TDEEViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TDEEViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentTdeeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // Bind ViewModel
        val application = requireNotNull(activity).application
        viewModelFactory = TDEEViewModelFactory(application)
        binding.viewModel = viewModel

        return binding.root
    }
}