package com.example.android.pets

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import com.example.android.pets.data.PetContract

@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    protected fun insertPet(values: ContentValues) : Long {
        val uri = baseContext.contentResolver.insert(PetContract.PetEntry.CONTENT_URI, values)
        return ContentUris.parseId(uri)
    }

    protected fun queryPets(projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?) : Cursor {
        return baseContext.contentResolver.query(PetContract.PetEntry.CONTENT_URI, projection, null, null, null)
    }
}