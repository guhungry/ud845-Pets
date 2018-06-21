package com.example.android.pets

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.android.pets.data.PetContract

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