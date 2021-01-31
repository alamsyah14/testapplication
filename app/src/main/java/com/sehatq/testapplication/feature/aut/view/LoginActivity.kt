package com.sehatq.testapplication.feature.aut.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookAuthorizationException
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.platform.BaseActivity
import com.sehatq.testapplication.core.widget.LoadingDialog
import com.sehatq.testapplication.databinding.ActivityLoginBinding
import com.sehatq.testapplication.feature.aut.viewmodel.LoginViewModel
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.Observer
import com.sehatq.testapplication.core.util.ErrorPassword
import com.sehatq.testapplication.feature.home.view.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity :BaseActivity<ActivityLoginBinding>() {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val requestCodeGoogle = 11
    private val requestCodeSocialMedia = 13

    private val vm: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.google_client_id))
            .requestEmail()
            .build()
    }

    private val callbackManager by lazy { CallbackManager.Factory.create() }
    private val mGoogleSignInClient by lazy { GoogleSignIn.getClient(this, gso) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initRegisterFacebookCallback()
        subscribeUI()
    }

    fun initBinding() {
        bindView(R.layout.activity_login)
        binding.apply {
            usernameTextField.hideError()
            passwordTextField.hideError()

            loginButton.setOnClickListener {
                vm.validateLogin(
                    username = usernameTextField.text,
                    password = passwordTextField.text
                )
            }
            facebookButton.setOnClickListener {
                LoginManager.getInstance().logInWithReadPermissions(
                    this@LoginActivity,
                    listOf("public_profile","email")
                )
            }
            googleButton.setOnClickListener {
                openGoogleSignIn()
            }
        }

    }

    fun subscribeUI() {

        vm.getLoading().observe(this, Observer {
            if (it) LoadingDialog.show(this) else LoadingDialog.dismiss()
        })

        vm.isUserNameEmpty.observe(this, Observer {
            if (it) usernameTextField.errorText = getString(R.string.error_empty_username)
            else usernameTextField.hideError()
        })

        vm.isPasswordError.observe(this, Observer {
            val errorMessage = when (it) {
                ErrorPassword.Empty -> getString(R.string.error_empty_password)
                ErrorPassword.MinCharacter -> getString(R.string.error_password_min_char)
                ErrorPassword.InvalidFormat -> getString(R.string.error_password_format)
            }
            passwordTextField.errorText = errorMessage
        })

        vm.successLogin.observe(this, Observer {
            goToHomePages()
        })

    }

    private fun openGoogleSignIn(){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, requestCodeGoogle)
    }

    private fun initRegisterFacebookCallback() {
        LoginManager.getInstance().let {
            it.logOut()
        }

        LoginManager.getInstance().registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) { result ?: return
                goToHomePages()
            }

            override fun onCancel() {
                LoginManager.getInstance().logOut()
            }

            override fun onError(error: FacebookException?) {
                if (error is FacebookAuthorizationException) {
                    LoginManager.getInstance().logOut()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed){
            compositeDisposable.clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            requestCodeGoogle       -> goToHomePages()
            requestCodeSocialMedia  -> goToHomePages()
        }
    }

    private fun goToHomePages() {
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
        finish()
    }


}