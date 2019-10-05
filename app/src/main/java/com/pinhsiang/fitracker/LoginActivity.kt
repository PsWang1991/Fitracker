package com.pinhsiang.fitracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.pinhsiang.fitracker.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pinhsiang.fitracker.data.User
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.user.UserManager


class LoginActivity : AppCompatActivity() {

    private val userCollection = FirebaseFirestore.getInstance().collection(USER)

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * Lazily initialize our [LoginViewModel].
     */
    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        /**
         *  Hiding action bar makes login fragment beautiful.
         */
        supportActionBar?.hide()

        /**
         *  Configure sign-in to request the user's ID, email address, and basic profile.
         *  ID and basic profile are included in DEFAULT_SIGN_IN.
         */
        val signInOptions =
            GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        /**
         *  Build a GoogleSignInClient with the options specified by signInOptions.
         */
        googleSignInClient = GoogleSignIn.getClient(this, signInOptions)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        binding.layoutBtnLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            // The Task returned from this call is always completed, no need to attach a listener.
            handleSignInResult(
                GoogleSignIn.getSignedInAccountFromIntent(data)
            )
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

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
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    UserManager.userUid = firebaseAuth.currentUser?.uid
                    checkUserDocumentId()

                } else {

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(FitrackerApplication.appContext, "Login failed.", Toast.LENGTH_SHORT).show()
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
                        toMainActivity()
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
                        toMainActivity()
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

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null && UserManager.hasLoggedIn()) {
            toMainActivity()
        }
    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val RC_SIGN_IN: Int = 1
    }
}