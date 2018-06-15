package com.example.android.pets.data

import android.net.Uri
import android.provider.BaseColumns

object PetContract {
    internal val CONTENT_AUTHORITY = "com.example.android.pets"
    internal val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")
    internal val PATH_PETS = "pets"

    // Table contents are grouped together in an anonymous object.
    object PetEntry : BaseColumns {
        fun isValidGender(gender: Int?): Boolean {
            return arrayOf(Gender.Unknown.ordinal, Gender.Male.ordinal, Gender.Female.ordinal).contains(gender)
        }

        val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        internal const val TABLE_NAME = "pets"
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