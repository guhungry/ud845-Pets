package com.example.android.pets

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.android.pets.data.PetContract
import com.example.android.pets.data.PetContract.PetEntry
import com.example.android.pets.data.adapters.PetAdapter
import kotlinx.android.synthetic.main.activity_catalog.*

/**
 * Displays list of pets that were entered and stored in the app.
 */
class CatalogActivity : BaseActivity() {
    private val adapter = PetAdapter(context = this, cursor = null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        list_pets.adapter = adapter
        // Setup FAB to open EditorActivity
        fab.setOnClickListener {
            val intent = Intent(this@CatalogActivity, EditorActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        displayDatabaseInfo()
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private fun displayDatabaseInfo() {
        adapter.changeCursor(queryPets(arrayOf(PetEntry._ID, PetEntry.NAME, PetEntry.BREED, PetEntry.GENDER, PetEntry.WEIGHT, PetEntry.AGE), null, null, null))
        empty_pet.visibility = adapter.showEmptyPet()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_catalog, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        return when (item.itemId) {
        // Respond to a click on the "Insert dummy data" menu option
            R.id.action_insert_dummy_data -> {
                insertDummyPet()
                displayDatabaseInfo()
                true
            }
        // Respond to a click on the "Delete all entries" menu option
            R.id.action_delete_all_entries -> {
                deletePets()
                displayDatabaseInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun insertDummyPet() {
        val values = ContentValues()

        values.put(PetEntry.NAME, "Tommy")
        values.put(PetEntry.BREED, "Pitbull")
        values.put(PetEntry.GENDER, PetContract.Gender.Male.ordinal)
        values.put(PetEntry.AGE, 5)
        values.put(PetEntry.WEIGHT, 9)
        insertPet(values)
    }
}