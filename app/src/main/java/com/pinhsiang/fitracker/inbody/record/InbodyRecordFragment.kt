package com.pinhsiang.fitracker.inbody.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.Float2StringConverter
import com.pinhsiang.fitracker.databinding.FragmentInbodyBinding
import com.pinhsiang.fitracker.databinding.FragmentInbodyRecordBinding
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding

class InbodyRecordFragment : Fragment() {

    private lateinit var binding: FragmentInbodyRecordBinding

    /**
     * Lazily initialize [InbodyRecordViewModel].
     */
    private lateinit var viewModelFactory: InbodyRecordViewModelFactory
    private val viewModel: InbodyRecordViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(InbodyRecordViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInbodyRecordBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        val inbody = InbodyRecordFragmentArgs.fromBundle(arguments!!).inbody
        viewModelFactory = InbodyRecordViewModelFactory(inbody, application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.converter = Float2StringConverter

        return binding.root
    }
}