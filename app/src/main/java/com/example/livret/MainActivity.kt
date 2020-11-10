package com.example.livret

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.livret.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val userId = MutableLiveData<String?>()

    var themeMode = 0

    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_THEMEMODE = "ThemeMode"
    lateinit var mSettings : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        if (mSettings.contains(APP_PREFERENCES_THEMEMODE)) {
            themeMode = mSettings.getInt(APP_PREFERENCES_THEMEMODE, AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            val editor = mSettings.edit()
            themeMode = AppCompatDelegate.MODE_NIGHT_NO
            editor.putInt(APP_PREFERENCES_THEMEMODE, themeMode)
            editor.apply()
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getResources()
            .getColor(R.color.primaryColor)))
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        // val currentUser = auth.currentUser
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            createSignInIntent()
        } else {
            getSupportActionBar()?.setTitle("Livret: ${user?.displayName}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out_item) {
            signOut()
        } else if (item.itemId == R.id.change_theme_item) {
            swapTheme()
        }
        return true
    }

    fun swapTheme() {
        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES) {
            themeMode = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            themeMode = AppCompatDelegate.MODE_NIGHT_YES
        }
        val editor = mSettings.edit()
        editor.putInt(APP_PREFERENCES_THEMEMODE, themeMode)
        editor.apply()
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
        // [END auth_fui_create_intent]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                userId.value = user?.uid
                getSupportActionBar()?.setTitle("Livret: ${user?.displayName}")
            }
        }
    }

    fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                createSignInIntent()
            }
        // [END auth_fui_signout]
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}