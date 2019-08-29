package com.pinhsiang.fitracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinhsiang.fitracker.util.CurrentFragmentType

class MainViewModel : ViewModel() {

    // Record current fragment to do condition navigation.
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()
}