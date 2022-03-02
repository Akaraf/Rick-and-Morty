package com.raaf.rickandmorty.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raaf.rickandmorty.R

/* This activity is intended only for display splash screen and should not contain other data or functionality.
   Splash screen can be implemented without SplashActivity - just use setTheme(R.style.Theme_MovieReviewsClient)
   in MainActivity before super.onCreate(...). */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RickAndMorty)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() { }
}