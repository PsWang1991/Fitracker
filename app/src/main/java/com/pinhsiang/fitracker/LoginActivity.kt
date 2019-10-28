package com.pinhsiang.fitracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.pinhsiang.fitracker.databinding.ActivityLoginBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.user.UserManager


class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    /**
     * Lazily initialize our [LoginViewModel].
     */
    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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

        binding.layoutBtnLogin.setOnClickListener {
            signIn()
        }

        viewModel.logInCompletely.observe(this, Observer { logInCompletely ->

            if (logInCompletely) {

                toMainActivity()
                viewModel.navigateToMainActivityDone()
            }
        })
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
            viewModel.handleSignInResult(
                GoogleSignIn.getSignedInAccountFromIntent(data)
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (viewModel.firebaseAuth.currentUser != null && UserManager.hasLoggedIn()) {
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