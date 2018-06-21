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

import android.app.AlertDialog
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.DialogInterface
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.android.pets.data.PetContract
import com.example.android.pets.data.PetContract.Gender
import com.example.android.pets.model.PetModel
import com.example.android.pets.petedit.PetEditProtocol
import com.example.android.pets.petedit.PetEditRouter
import kotlinx.android.synthetic.main.activity_editor.*

/**
 * Allows user to create a new pet or edit an existing one.
 */
class EditorActivity : BaseActivity(), PetEditProtocol.View, LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private var mGender = Gender.Unknown
    private val PET_LOADER_ID = 2
    override var presenter: PetEditProtocol.Presenter? = null

    companion object {
        const val INPUT_URL = "URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        setupPresenter(savedInstanceState)
        setupSpinner()
        setupInputs()
        setupMenu()
    }

    private fun setupMenu() {
        if (!presenter!!.isEdit()) invalidateOptionsMenu()
    }

    private fun setupInputs() {
        val onTouch = View.OnTouchListener { view, event ->
            view?.performClick()
            presenter?.edited = true
            false
        }
        edit_pet_name.setOnTouchListener(onTouch)
        edit_pet_breed.setOnTouchListener(onTouch)
        spinner_gender.setOnTouchListener(onTouch)
        edit_pet_weight.setOnTouchListener(onTouch)
    }

    private fun setupPresenter(savedInstanceState: Bundle?) {
        val uri = intent?.extras?.get(INPUT_URL) as Uri?
        presenter = PetEditRouter.presenterFor(this, baseContext.contentResolver, uri)
        presenter!!.start()

        if (presenter!!.isEdit()) loaderManager.initLoader(PET_LOADER_ID, savedInstanceState, this)
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
        spinner_gender.adapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        }

        spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selection = parent.getItemAtPosition(position) as String
                mGender = Gender.valueOf(selection)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                mGender = Gender.Unknown
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
        // Respond to a click on the "Save" menu option
            R.id.action_save -> {
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
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)

        if (!presenter!!.isEdit()) {
            menu?.findItem(R.id.action_delete)?.isVisible = false;
        }
        return true
    }

    override fun onBackPressed() {
        if (!presenter!!.edited) {
            super.onBackPressed()
            return
        }

        showConfirmCloseDialog(DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
            finish()
        })
    }

    private fun showConfirmCloseDialog(listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this).apply {
            setMessage(R.string.unsaved_changes_dialog_msg);
            setPositiveButton(R.string.discard, listener);
            setNegativeButton(R.string.keep_editing) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }
            create().show()
        }
    }

    private fun validateInput(): Boolean {
        if (isInputAllBlank()) {
            navigateBack()
            return false
        }

        if (isInputNameEmpty()) {
            edit_pet_name.requestFocus()
            showToastMessage("Name is empty")
            return false
        }
        if (inputWeightEmpty()) {
            edit_pet_weight.requestFocus()
            showToastMessage("Weight is empty")
            return false
        }

        return true
    }

    private fun isInputAllBlank(): Boolean {
        if (presenter!!.isEdit()) return false

        return isInputNameEmpty() && edit_pet_breed.text.isEmpty() && inputWeightEmpty() && mGender == Gender.Unknown
    }

    private fun inputWeightEmpty() = edit_pet_weight.text.isEmpty()

    private fun isInputNameEmpty() = edit_pet_name.text.isEmpty()

    private fun savePet() {
        if (!validateInput()) return

        try {
            val pet = PetModel(id = 0, name = inputName(), breed = inputBreed(), gender = inputGender(), weight = inputWeight())

            if (presenter!!.isEdit()) presenter?.updatePet(pet)
            else presenter?.insertPet(pet)
        } catch (e: Exception) {
            showToastMessage("Error with saving pet ($e)")
        }
    }

    // ///////////////
    // Input Functions
    // ///////////////
    private fun inputName() = edit_pet_name.text.toString()
    private fun inputGender() = mGender.ordinal
    private fun inputBreed() = edit_pet_breed.text.toString()
    private fun inputWeight() = edit_pet_weight.text.toString().toIntOrNull() ?: 0

    override fun onSaveSuccess(message: String) {
        showToastMessage(message)
        navigateBack()
    }

    override fun onSaveFail(message: String) {
        showToastMessage(message)
    }

    // ///////////////////////
    // Loader Manager Delegate
    // ///////////////////////
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this, presenter?.uri, arrayOf(PetContract.PetEntry._ID, PetContract.PetEntry.NAME, PetContract.PetEntry.BREED, PetContract.PetEntry.GENDER, PetContract.PetEntry.WEIGHT, PetContract.PetEntry.AGE), null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        if (data != null && data.moveToFirst()) updatePetData(PetModel.fromCursor(data))
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        clearPetData()
    }

    // //////////////////
    // Pet Data Functions
    // //////////////////
    private fun updatePetData(pet: PetModel) {
        edit_pet_name.setText(pet.name)
        edit_pet_breed.setText(pet.breed)
        edit_pet_weight.setText(pet.weight.toString())
        mGender = Gender.values()[pet.gender]
        spinner_gender.setSelection(pet.gender)
    }

    private fun clearPetData() {
        edit_pet_name.setText("")
        edit_pet_breed.setText("")
        edit_pet_weight.setText("")
        mGender = Gender.Unknown
        spinner_gender.setSelection(0)
    }
}