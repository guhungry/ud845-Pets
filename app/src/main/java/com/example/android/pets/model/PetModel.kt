package com.example.android.pets.model

import android.database.Cursor
import com.example.android.pets.data.PetContract

class PetModel(var id: Long, var name: String, var gender: Int, var age: Int, var weight: Int, var breed: String = "") {
    companion object {
        fun fromCursor(cursor: Cursor) : PetModel {
            val columnID = cursor.getColumnIndex(PetContract.PetEntry._ID)
            val columnNAME = cursor.getColumnIndex(PetContract.PetEntry.NAME)
            val columnBREED = cursor.getColumnIndex(PetContract.PetEntry.BREED)
            val columnGENDER = cursor.getColumnIndex(PetContract.PetEntry.GENDER)
            val columnAGE = cursor.getColumnIndex(PetContract.PetEntry.AGE)
            val columnWEIGHT = cursor.getColumnIndex(PetContract.PetEntry.WEIGHT)

            return PetModel(id = cursor.getLong(columnID),
                    name = cursor.getString(columnNAME),
                    breed = cursor.getString(columnBREED),
                    gender = cursor.getInt(columnGENDER),
                    age = cursor.getInt(columnAGE),
                    weight = cursor.getInt(columnWEIGHT))
        }
    }
}