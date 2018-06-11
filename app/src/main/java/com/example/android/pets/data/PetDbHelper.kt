package com.example.android.pets.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.android.pets.data.PetContract.PetEntry

class PetDbHelper(context: Context): SQLiteOpenHelper(context, PetDbHelper.DATABASE_NAME, null, PetDbHelper.DATABASE_VERSION) {
    val SQL_CREATE_PETS = "CREATE TABLE ${PetEntry.TABLE_NAME} (" +
            "${PetEntry._ID}_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${PetEntry.NAME} TEXT NOT NULL," +
            "${PetEntry.BREED} TEXT," +
            "${PetEntry.GENDER} INTEGER NOT NULL," +
            "${PetEntry.AGE} INTEGER NOT NULL DEFAULT 0);"
    val SQL_DEOP_PETS = "DROP TABLE ${PetEntry.TABLE_NAME};"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_PETS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DEOP_PETS)
        db?.execSQL(SQL_CREATE_PETS)
    }

    companion object {
        const val DATABASE_NAME = "Pets.db"
        const val DATABASE_VERSION = 1
    }
}