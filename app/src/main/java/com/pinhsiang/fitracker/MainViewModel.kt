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

//    private val _userName = MutableLiveData<String>().apply {
//
//    }
//    val userName: LiveData<String>
//        get() = _userName

    val userName = UserManager.userName
    val userEmail = UserManager.userEmail
    val userAvatarUrl = UserManager.userAvatarUrl

    init {
        Log.i(TAG, "(MainViewModel) userName = ${UserManager.userName}")
//        _userName.value = UserManager.userName
        Log.i(TAG, "(MainViewModel) userEmail = $userEmail")
        Log.i(TAG, "(MainViewModel) userAvatarUrl = $userAvatarUrl")
    }

    fun setUserName(name: String) {
//        _userName.value = name
        Log.i(TAG, "(MainViewModel) userName = ${userName}")
    }

    fun loglog() {
        Log.i(TAG, "(MainViewModel) userName = ${userName}")
    }
}