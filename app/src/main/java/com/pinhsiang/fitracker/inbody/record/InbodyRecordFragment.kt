package com.pinhsiang.fitracker.inbody.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinhsiang.fitracker.databinding.FragmentInbodyBinding
import com.pinhsiang.fitracker.databinding.FragmentInbodyRecordBinding
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding

class InbodyRecordFragment : Fragment() {

    private lateinit var binding: FragmentInbodyRecordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentInbodyRecordBinding.inflate(inflater, container, false)
        return binding.root
    }
}