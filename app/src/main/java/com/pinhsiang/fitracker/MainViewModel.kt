package com.pinhsiang.fitracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.CurrentFragmentType

class MainViewModel : ViewModel() {

    // Record current fragment to handle navigation or view properties.
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    val userName = UserManager.userName
    val userEmail = UserManager.userEmail
    val userAvatarUrl = UserManager.userAvatarUrl

    init {
        Log.i(TAG, "(MainViewModel) userName = ${UserManager.userName}")
        Log.i(TAG, "(MainViewModel) userEmail = $userEmail")
        Log.i(TAG, "(MainViewModel) userAvatarUrl = $userAvatarUrl")
    }
}