package com.example.android.pets

import android.annotation.SuppressLint
import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import com.example.android.pets.data.PetContract

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected fun insertData(values: ContentValues) {
        baseContext.contentResolver.insert(PetContract.PetEntry.CONTENT_URI, values)
    }
}