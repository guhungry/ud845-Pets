package com.example.android.pets.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.android.pets.data.PetContract.PetEntry

class PetProvider() : ContentProvider() {
    lateinit private var db: PetDbHelper

    private companion object {
        const val PETS = 100
        const val PET_ID = 101
    }
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS)
        addURI(PetContract.CONTENT_AUTHORITY, "${PetContract.PATH_PETS}/#", PET_ID)
    }

    override fun onCreate(): Boolean {
        db = PetDbHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val database = db.readableDatabase
        return when (uriMatcher.match(uri)) {
            PETS -> database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            PET_ID -> database.query(PetEntry.TABLE_NAME, projection, "${PetEntry._ID}=?", arrayOf(ContentUris.parseId(uri).toString()), null, null, sortOrder)
            else -> throw IllegalArgumentException("Cannot query unknown URI ($uri)")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        return when (uriMatcher.match(uri)) {
            PETS -> insertPet(uri, values)
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val database = db.writableDatabase
        return when (uriMatcher.match(uri)) {
            PET_ID -> database.update(PetEntry.TABLE_NAME, values, "${PetEntry._ID}=?", arrayOf(ContentUris.parseId(uri).toString()))
            else -> throw IllegalArgumentException("Update is not supported for ($uri)")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val database = db.writableDatabase
        return when (uriMatcher.match(uri)) {
            PETS -> database.delete(PetEntry.TABLE_NAME, "", arrayOf())
            PET_ID -> database.delete(PetEntry.TABLE_NAME, "${PetEntry._ID}=?", arrayOf(ContentUris.parseId(uri).toString()))
            else -> throw IllegalArgumentException("Deletion is not support for ($uri)")
        }
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun insertPet(uri: Uri, values: ContentValues?) : Uri {
        val database = db.writableDatabase
        val id = database.insert(PetEntry.TABLE_NAME, "", values)

        return Uri.withAppendedPath(uri, id.toString())
    }
}