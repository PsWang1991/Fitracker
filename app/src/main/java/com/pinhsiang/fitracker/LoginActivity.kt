package com.pinhsiang.fitracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.crashlytics.android.Crashlytics
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
import com.pinhsiang.fitracker.user.UserManager

const val RC_SIGN_IN = 1

class LoginActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        supportActionBar?.hide()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.layoutBtnLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Log.i(TAG, "account id = ${account?.id}")
            Log.i(TAG, "account id token = ${account?.idToken}")
            Log.i(TAG, "account display name = ${account?.displayName}")
            UserManager.userName = account?.displayName!!
            Log.i(TAG, "save userName")
            UserManager.userEmail = account?.email
            Log.i(TAG, "photoUrl = ${account?.photoUrl.toString()}")
            UserManager.userAvatarUrl = account?.photoUrl.toString()
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }

    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.i(TAG, "account UID (firebaseAuthWithGoogle) = ${user?.uid}")
                    UserManager.userUid = user?.uid
                    checkUserDocumentId()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(FitrackerApplication.appContext, "Login failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserDocumentId() {
        db.collection(getString(R.string.user_collection_path)).whereEqualTo("uid", UserManager.userUid)
            .get()
            .addOnCompleteListener {
                Log.i(TAG, "******** checkUserDocumentId ***********")
                Log.i(TAG, "UserManager.userUid = ${UserManager.userUid}")
                when {
                    it.result?.documents?.size == 1 -> it.result?.documents?.forEach {
                        UserManager.userDocId = it.id
                        Log.i(TAG, "userDocId = ${it.id}")
                        toMainActivity()
                    }
                    it.result?.documents?.size!! > 1 -> Toast.makeText(
                        FitrackerApplication.appContext,
                        "Error : UID is not unique, contact developer, please.",
                        Toast.LENGTH_SHORT
                    ).show()
                    it.result?.documents?.size == 0 -> {
                        Log.i(TAG, "This user is a new user.")
                        createNewAccount()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "******** checkUserDocumentId ***********")
                Log.i(TAG, "Error getting documents.", exception)
                Log.i(TAG, "******** checkUserDocumentId ***********")
            }
    }

    private fun createNewAccount() {
        Log.i(TAG, "Creating new account starts.")
        val user = User(
            name = GoogleSignIn.getLastSignedInAccount(this)?.displayName!!,
            uid = UserManager.userUid!!
        )
        db.collection(getString(R.string.user_collection_path)).add(user)
            .addOnCompleteListener {
                Log.i(TAG, "Create new account successfully.")
                getNewAccountDocId()
            }
            .addOnFailureListener {exception ->
                Toast.makeText(
                    FitrackerApplication.appContext,
                    "Fail to create new account.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i(TAG, "******** createNewAccount ***********")
                Log.i(TAG, "Error : create new account, exception : $exception")
                Log.i(TAG, "******** createNewAccount ***********")
            }
    }

    private fun getNewAccountDocId() {
        db.collection(getString(R.string.user_collection_path)).whereEqualTo("uid", UserManager.userUid)
            .get()
            .addOnCompleteListener {
                Log.i(TAG, "******** getNewAccountDocId ***********")
                Log.i(TAG, "UserManager.userUid = ${UserManager.userUid}")
                when {
                    it.result?.documents?.size == 1 -> it.result?.documents?.forEach {
                        UserManager.userDocId = it.id
                        Log.i(TAG, "userDocId = ${it.id}")
                        toMainActivity()
                    }
                    it.result?.documents?.size!! > 1 -> Toast.makeText(
                        FitrackerApplication.appContext,
                        "Error : UID is not unique, contact developer, please.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "******** checkUserDocumentId ***********")
                Log.i(TAG, "Error getting documents.", exception)
                Log.i(TAG, "******** checkUserDocumentId ***********")
            }
    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        Log.i(TAG, "account id = ${account?.id}")
        Log.i(TAG, "account id token = ${account?.idToken}")
        Log.i(TAG, "account display name = ${account?.displayName}")

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.i(TAG, "account UID (onStart) = ${currentUser?.uid}")
        if (currentUser != null && UserManager.userUid != "" && UserManager.userDocId != "") {
            toMainActivity()
        }

    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}