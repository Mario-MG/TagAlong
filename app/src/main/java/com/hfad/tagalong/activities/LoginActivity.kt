package com.hfad.tagalong.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.api.TokenManager
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    private var code: String? = null
    private var error: String? = null

    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (isIntentFromBrowser()) {
            handleLogin()
        }

        initializeLoginButton()
    }

    private fun isIntentFromBrowser() = intent?.action == Intent.ACTION_VIEW

    private fun handleLogin() {
        extractCodeOrErrorFromIntentData()
        if (code != null) {
            generateTokenAndGoBackToMain(code!!)
        } else {
            handleError()
        }
    }

    private fun extractCodeOrErrorFromIntentData() {
        val uri = intent.data
        code = uri?.getQueryParameter("code")
        error = uri?.getQueryParameter("error")
    }

    private fun generateTokenAndGoBackToMain(code: String) {
        thread {
            TokenManager.generateTokenFromCode(code)
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    private fun handleError() {
        Log.e("LOGIN_ERROR", error ?: "unknown")
    }

    private fun initializeLoginButton() {
        loginButton = findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            onClickLoginButton()
        }
    }

    private fun onClickLoginButton() {
        val authUri = TokenManager.getAuthSpotifyURL()
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TOP
        customTabsIntent.launchUrl(this, authUri.toUri())
    }
}