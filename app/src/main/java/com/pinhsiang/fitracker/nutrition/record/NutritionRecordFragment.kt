package com.pinhsiang.fitracker.nutrition.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.pinhsiang.fitracker.Int2StringConverter
import com.pinhsiang.fitracker.databinding.FragmentNutritionBinding
import com.pinhsiang.fitracker.databinding.FragmentNutritionRecordBinding
import com.pinhsiang.fitracker.databinding.FragmentWorkoutBinding

class NutritionRecordFragment : Fragment() {

    private lateinit var binding: FragmentNutritionRecordBinding

    /**
     * Lazily initialize [NutritionRecordViewModel].
     */
    private lateinit var viewModelFactory: NutritionRecordViewModelFactory
    private val viewModel: NutritionRecordViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NutritionRecordViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentNutritionRecordBinding.inflate(inflater, container, false)

        // Pass dataTime from workout fragment to detail fragment
        val application = requireNotNull(activity).application
        val nutrition = NutritionRecordFragmentArgs.fromBundle(arguments!!).nutrition
        viewModelFactory = NutritionRecordViewModelFactory(nutrition, application)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.converter = Int2StringConverter

        return binding.root
    }
}