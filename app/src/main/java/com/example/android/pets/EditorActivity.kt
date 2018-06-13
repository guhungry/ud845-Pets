/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets

import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.android.pets.data.PetContract.Gender
import com.example.android.pets.data.PetContract.PetEntry
import com.example.android.pets.data.PetDbHelper

/**
 * Allows user to create a new pet or edit an existing one.
 */
class EditorActivity : AppCompatActivity() {

    /** EditText field to enter the pet's name  */
    private var mNameEditText: EditText? = null

    /** EditText field to enter the pet's breed  */
    private var mBreedEditText: EditText? = null

    /** EditText field to enter the pet's weight  */
    private var mWeightEditText: EditText? = null

    /** EditText field to enter the pet's gender  */
    private var mGenderSpinner: Spinner? = null

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private var mGender = Gender.Unknown

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById<View>(R.id.edit_pet_name) as EditText
        mBreedEditText = findViewById<View>(R.id.edit_pet_breed) as EditText
        mWeightEditText = findViewById<View>(R.id.edit_pet_weight) as EditText
        mGenderSpinner = findViewById<View>(R.id.spinner_gender) as Spinner

        setupSpinner()
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private fun setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        val genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item)

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        // Apply the adapter to the spinner
        mGenderSpinner!!.adapter = genderSpinnerAdapter

        // Set the integer mSelected to the constant values
        mGenderSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selection = parent.getItemAtPosition(position) as String
                if (!TextUtils.isEmpty(selection)) {
                    if (selection == getString(R.string.gender_male)) {
                        mGender = Gender.Male
                    } else if (selection == getString(R.string.gender_female)) {
                        mGender = Gender.Female
                    } else {
                        mGender = Gender.Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            override fun onNothingSelected(parent: AdapterView<*>) {
                mGender = Gender.Unknown
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
        // Respond to a click on the "Save" menu option
            R.id.action_save -> {
                // Do nothing for now

                if (!validateInput()) return false
                savePet()
                return true
            }
        // Respond to a click on the "Delete" menu option
            R.id.action_delete ->
                // Do nothing for now
                return true
        // Respond to a click on the "Up" arrow button in the app bar
            android.R.id.home -> {
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validateInput(): Boolean {
        if (mNameEditText!!.text.toString().isEmpty()) {
            mNameEditText!!.requestFocus()
            showToastMessage("Name is empty")
            return false
        }
        if (mWeightEditText!!.text.toString().isEmpty()) {
            mWeightEditText!!.requestFocus()
            showToastMessage("Weight is empty")
            return false
        }

        return true
    }

    private fun savePet() {
        val values = ContentValues()

        try {
            values.put(PetEntry.NAME, mNameEditText!!.text.toString())
            values.put(PetEntry.BREED, mBreedEditText!!.text.toString())
            values.put(PetEntry.GENDER, mGender.ordinal)
            values.put(PetEntry.WEIGHT, Integer.parseInt(mWeightEditText!!.text.toString()))
            values.put(PetEntry.AGE, 0)
            val db = PetDbHelper(this).writableDatabase
            val inerted = db.insert(PetEntry.TABLE_NAME, null, values)

            showToastMessage("Pet saved with id: $inerted")
            NavUtils.navigateUpFromSameTask(this)
        } catch (e: Exception) {
            showToastMessage("Error with saving pet")
        }

    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}