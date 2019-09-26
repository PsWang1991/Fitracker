package com.pinhsiang.fitracker.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pinhsiang.fitracker.FitrackerApplication

object UserManager {

    private const val userSharedPreference = "user_info"
    private const val uidKey = "firebase_authentication_uid"
    private const val userDocIdKey = "user_doc_id"
    private const val userNameKey = "user_name"
    private const val userEmailKey = "user_email"
    private const val userAvatarUrlKey = "user_avatar_url"


    var userUid: String? = ""
        get() = FitrackerApplication.appContext?.getSharedPreferences(
            userSharedPreference,
            Context.MODE_PRIVATE
        )?.getString(uidKey, "")
        set(inputString) {
            field = inputString
            FitrackerApplication.appContext?.getSharedPreferences(
                userSharedPreference,
                Context.MODE_PRIVATE
            )?.edit()?.putString(uidKey, inputString)?.apply()
        }

    var userDocId: String? = ""
        get() = FitrackerApplication.appContext?.getSharedPreferences(
            userSharedPreference,
            Context.MODE_PRIVATE
        )?.getString(userDocIdKey, "")
        set(inputString) {
            field = inputString
            FitrackerApplication.appContext?.getSharedPreferences(
                userSharedPreference,
                Context.MODE_PRIVATE
            )?.edit()?.putString(userDocIdKey, inputString)?.apply()
        }

    var userName: String? = ""
        get() = FitrackerApplication.appContext?.getSharedPreferences(
            userSharedPreference,
            Context.MODE_PRIVATE
        )?.getString(userNameKey, "")
        set(inputString) {
            field = inputString
            FitrackerApplication.appContext?.getSharedPreferences(
                userSharedPreference,
                Context.MODE_PRIVATE
            )?.edit()?.putString(userNameKey, inputString)?.apply()
        }

//    private val _userName = MutableLiveData<String>().apply {
//        value = "Loading Name..."
//    }
//    val userName: LiveData<String>
//        get() = _userName
//
//    fun setName(name: String?) {
//        if (name == null) {
//            _userName.value= "Loading Name..."
//        } else {
//            _userName.value = name
//        }
//    }
//
//    fun clearName() {
//        _userName.value = "Loading Name..."
//    }

    var userEmail: String? = ""
        get() = FitrackerApplication.appContext?.getSharedPreferences(
            userSharedPreference,
            Context.MODE_PRIVATE
        )?.getString(userEmailKey, "")
        set(inputString) {
            field = inputString
            FitrackerApplication.appContext?.getSharedPreferences(
                userSharedPreference,
                Context.MODE_PRIVATE
            )?.edit()?.putString(userEmailKey, inputString)?.apply()
        }

    var userAvatarUrl: String? = ""
        get() = FitrackerApplication.appContext?.getSharedPreferences(
            userSharedPreference,
            Context.MODE_PRIVATE
        )?.getString(userAvatarUrlKey, "")
        set(inputString) {
            field = inputString
            FitrackerApplication.appContext?.getSharedPreferences(
                userSharedPreference,
                Context.MODE_PRIVATE
            )?.edit()?.putString(userAvatarUrlKey, inputString)?.apply()
        }
}