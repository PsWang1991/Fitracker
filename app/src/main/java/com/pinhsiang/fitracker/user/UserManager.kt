package com.pinhsiang.fitracker.user

import android.content.Context
import com.pinhsiang.fitracker.FitrackerApplication

object UserManager {

    private const val userSharedPreference = "user_info"
    private const val uidKey = "firebase_authentication_uid"
    private const val user_doc_id_key = "user_doc_id"



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
        )?.getString(user_doc_id_key, "")
        set(inputString) {
            field = inputString
            FitrackerApplication.appContext?.getSharedPreferences(
                userSharedPreference,
                Context.MODE_PRIVATE
            )?.edit()?.putString(user_doc_id_key, inputString)?.apply()
        }
}