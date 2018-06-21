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

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.android.pets.data.PetContract.Gender
import com.example.android.pets.model.PetModel
import com.example.android.pets.petedit.PetEditProtocol
import com.example.android.pets.petedit.PetEditRouter
import kotlinx.android.synthetic.main.activity_editor.*

/**
 * Allows user to create a new pet or edit an existing one.
 */
class EditorActivity : BaseActivity(), PetEditProtocol.View {
    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private var mGender = Gender.Unknown
    private var uri: Uri? = null
    override var presenter: PetEditProtocol.Presenter? = null

    companion object {
        const val INPUT_URL = "URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        uri = intent?.extras?.get(INPUT_URL) as Uri?
        presenter = PetEditRouter.presenterFor(this, baseContext.contentResolver, uri)
        setupSpinner()
        presenter?.start()
    }

    override fun onDestroy() {
        presenter?.stop()
        presenter = null
        super.onDestroy()
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private fun setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        spinner_gender.adapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        }

        // Set the integer mSelected to the constant values
        spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selection = parent.getItemAtPosition(position) as String
                mGender = Gender.valueOf(selection)
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
        if (edit_pet_name!!.text.isEmpty()) {
            edit_pet_name!!.requestFocus()
            showToastMessage("Name is empty")
            return false
        }
        if (edit_pet_weight!!.text.isEmpty()) {
            edit_pet_weight!!.requestFocus()
            showToastMessage("Weight is empty")
            return false
        }

        return true
    }

    private fun savePet() {
        try {
            val pet = PetModel(id = 0, name = edit_pet_name.text.toString(), breed = edit_pet_name.text.toString(), gender = mGender.ordinal, weight = Integer.parseInt(edit_pet_weight.text.toString()))
            val inserted = presenter?.insertPet(pet)

            showToastMessage("Pet saved with id: $inserted")
            NavUtils.navigateUpFromSameTask(this)
        } catch (e: Exception) {
            showToastMessage("Error with saving pet ($e)")
        }
    }
}