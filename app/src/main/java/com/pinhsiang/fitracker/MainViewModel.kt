package com.pinhsiang.fitracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinhsiang.fitracker.util.CurrentFragmentType

class MainViewModel : ViewModel() {

    // Record current fragment to handle navigation or view properties.
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()
}