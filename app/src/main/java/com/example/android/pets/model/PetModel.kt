package com.example.android.pets.model

import android.database.Cursor
import android.net.Uri
import com.example.android.pets.data.PetContract.PetEntry

class PetModel(var id: Long, var name: String, var gender: Int, var age: Int, var weight: Int, var breed: String = "") {
    val url = Uri.withAppendedPath(PetEntry.CONTENT_URI, id.toString())

    companion object {
        fun fromCursor(cursor: Cursor) : PetModel {
            val columnID = cursor.getColumnIndex(PetEntry._ID)
            val columnNAME = cursor.getColumnIndex(PetEntry.NAME)
            val columnBREED = cursor.getColumnIndex(PetEntry.BREED)
            val columnGENDER = cursor.getColumnIndex(PetEntry.GENDER)
            val columnAGE = cursor.getColumnIndex(PetEntry.AGE)
            val columnWEIGHT = cursor.getColumnIndex(PetEntry.WEIGHT)

            return PetModel(id = cursor.getLong(columnID),
                    name = cursor.getString(columnNAME),
                    breed = cursor.getString(columnBREED),
                    gender = cursor.getInt(columnGENDER),
                    age = cursor.getInt(columnAGE),
                    weight = cursor.getInt(columnWEIGHT))
        }
    }
}