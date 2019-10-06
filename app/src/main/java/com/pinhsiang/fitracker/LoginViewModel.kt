package com.pinhsiang.fitracker

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.data.User
import com.pinhsiang.fitracker.user.UserManager

class LoginViewModel : ViewModel() {

    var firebaseAuth = FirebaseAuth.getInstance()
    private val userCollection = FirebaseFirestore.getInstance().collection(USER)

    private val _logInCompletely = MutableLiveData<Boolean>().apply {
        value = false
    }
    val logInCompletely: LiveData<Boolean>
        get() = _logInCompletely

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {

            val account = completedTask.getResult(ApiException::class.java)

            UserManager.userName = account?.displayName!!
            UserManager.userEmail = account.email
            UserManager.userAvatarUrl = account.photoUrl.toString()

            firebaseAuthWithGoogle(account)

        } catch (e: ApiException) {

            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        firebaseAuth
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->

                when (task.isSuccessful) {

                    true -> {
                        UserManager.userUid = firebaseAuth.currentUser?.uid
                        checkUserDocumentId()
                    }

                    else -> {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(FitrackerApplication.appContext, "Login failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun checkUserDocumentId() {

        userCollection
            .whereEqualTo(UID, UserManager.userUid)
            .get()
            .addOnCompleteListener { query ->

                when (query.result?.documents?.size) {

                    1 -> query.result?.documents?.forEach { userDocument ->
                        UserManager.userDocId = userDocument.id
                        _logInCompletely.value = true
                    }

                    // Create an account for new user.
                    0 -> createNewAccount()

                    else -> {
                        Toast.makeText(
                            FitrackerApplication.appContext,
                            "Error: UID is not unique, contact developer, please.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "Error: UID is not unique.")
                    }

                }
            }
            .addOnFailureListener { exception ->

                Log.i(TAG, "Error getting documents.", exception)
            }
    }

    private fun createNewAccount() {

        val newUser = User(
            name = UserManager.userName!!,
            uid = UserManager.userUid!!
        )

        userCollection.add(newUser)
            .addOnCompleteListener {

                Log.i(TAG, "Create new account successfully.")
                getNewAccountDocId()
            }
            .addOnFailureListener { exception ->

                Toast.makeText(
                    FitrackerApplication.appContext,
                    "Fail to create new account.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i(TAG, "Fail to create new account, exception : $exception")
            }
    }

    private fun getNewAccountDocId() {

        userCollection
            .whereEqualTo(UID, UserManager.userUid)
            .get()
            .addOnCompleteListener { query ->

                when (query.result?.documents?.size) {

                    1 -> query.result?.documents?.forEach { userDocument ->
                        UserManager.userDocId = userDocument.id
                        _logInCompletely.value = true
                    }

                    else -> Toast.makeText(
                        FitrackerApplication.appContext,
                        "Error : UID is not unique, contact developer, please.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "Fail to get new user document.", exception)
            }
    }

    fun navigateToMainActivityDone() {
        _logInCompletely.value = false
    }

}