package com.example.android.pets.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri

class PetStore(val contentResolver: ContentResolver) {
    fun insertPet(values: ContentValues): Long {
        val uri = contentResolver.insert(PetContract.PetEntry.CONTENT_URI, values)
        return ContentUris.parseId(uri)
    }

    fun updatePet(uri: Uri?, values: ContentValues): Int? {
        return contentResolver.update(uri, values, "", arrayOf())
    }

    fun deleteAllPets(): Int {
        return delete(PetContract.PetEntry.CONTENT_URI)
    }

    fun deletePet(uri: Uri?): Int {
        return delete(uri)
    }

    // //////////////////
    // Database Functions
    // //////////////////
    private fun delete(uri: Uri?) = contentResolver.delete(uri, "", arrayOf())
}