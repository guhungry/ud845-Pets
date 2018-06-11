package com.example.android.pets.data

import android.provider.BaseColumns

object PetContract {
    // Table contents are grouped together in an anonymous object.
    object PetEntry : BaseColumns {
        const val TABLE_NAME = "pets"
        const val _ID = BaseColumns._ID
        const val NAME = "name"
        const val BREED = "breed"
        const val AGE = "age"
        const val GENDER = "gender"
        const val WEIGHT = "weight"
    }

    enum class Gender {
        Unknown,
        Male,
        Female
    }
}