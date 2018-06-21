package com.example.android.pets

import android.annotation.SuppressLint
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // ///////////////////////////
    // Navigation Helper Functions
    // ///////////////////////////
    protected fun navigateBack() {
        NavUtils.navigateUpFromSameTask(this)
    }
}