package com.example.android.pets.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues

class PetStore(val contentResolver: ContentResolver) {
    fun insertPet(values: ContentValues): Long {
        val uri = contentResolver.insert(PetContract.PetEntry.CONTENT_URI, values)
        return ContentUris.parseId(uri)
    }

    fun deletePets() : Int {
        return contentResolver.delete(PetContract.PetEntry.CONTENT_URI, "", arrayOf())
    }
}