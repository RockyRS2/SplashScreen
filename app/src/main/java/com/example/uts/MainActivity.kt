package com.example.uts

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginActivity"
    }

    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private lateinit var mAuth : FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var googleSignInButton : Button
    lateinit var register: TextView
    lateinit var loginButton: Button
    lateinit var emailInput : EditText
    lateinit var passwordInput : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        register = findViewById(R.id.tvRegister)
        loginButton=findViewById(R.id.btnLogin)
        emailInput = findViewById(R.id.email)
        passwordInput = findViewById(R.id.password)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                emailInput.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                passwordInput.error = "Password is required"
                return@setOnClickListener
            }

            emailPasswordLogin(email, password)
        }


        googleSignInButton =findViewById(R.id.googleSignInButton)
        progressBar = findViewById(R.id.progressBar)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
        mAuth = FirebaseAuth.getInstance()

        googleSignInButton.setOnClickListener {
            signIn()
        }

        register.setOnClickListener {
            val intent = Intent(this@MainActivity,RegisActivity::class.java)
            startActivity(intent)
        }
    }

    private fun emailPasswordLogin(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    Toast.makeText(this, "Authentication Successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fireBaseAuthWithGoogle(acct : GoogleSignInAccount){
        Log.d(TAG,"fireBaseAuthWithGoogle:"+ acct.id)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener (this){task->
                if(task.isSuccessful){
                    Log.d(TAG,"signInWithCredential successful.")
                    val user : FirebaseUser? = mAuth.currentUser
                    Toast.makeText(this,"Authentication Successful.",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,Dashboard::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Log.w(TAG,"signInWithCredential failed.")
                    Toast.makeText(this,"Authentication Failed",Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = View.GONE
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                fireBaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google Sign In failed", e)
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Google sign in failed : ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun signIn(){
        progressBar.visibility = View.VISIBLE
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }



}