package com.example.android.pets

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.android.pets.data.PetContract
import com.example.android.pets.data.PetContract.PetEntry
import com.example.android.pets.data.PetDbHelper
import com.example.android.pets.model.PetModel
import com.example.android.pets.utils.StringUtils

/**
 * Displays list of pets that were entered and stored in the app.
 */
class CatalogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        // Setup FAB to open EditorActivity
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
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
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        val mDbHelper = PetDbHelper(this)

        // Create and/or open a database to read from it
        val db = mDbHelper.readableDatabase

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        val cursor = db.query(
                PetEntry.TABLE_NAME,
                arrayOf(PetEntry._ID, PetEntry.NAME, PetEntry.BREED, PetEntry.GENDER, PetEntry.WEIGHT, PetEntry.AGE),
                "", null, null, null, null)
        cursor.use { cursor ->
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            val displayView = findViewById<TextView>(R.id.text_view_pet)
            displayView.text = "The pers table contains ${cursor.count} pets${StringUtils.NEW_LINE}${StringUtils.NEW_LINE}"
            displayView.append("${PetEntry._ID}, ${PetEntry.NAME}, ${PetEntry.BREED}, ${PetEntry.GENDER}, ${PetEntry.AGE}, ${PetEntry.WEIGHT}${StringUtils.NEW_LINE}")

            while (cursor.moveToNext()) {
                val pet = PetModel.fromCursor(cursor)
                displayView.append("${StringUtils.NEW_LINE}${pet.id}, ${pet.name}, ${pet.breed}, ${pet.gender}, ${pet.age}, ${pet.weight}")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_catalog, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {
        // Respond to a click on the "Insert dummy data" menu option
            R.id.action_insert_dummy_data -> {
                // Do nothing for now
                insertPet()
                displayDatabaseInfo()
                return true
            }
        // Respond to a click on the "Delete all entries" menu option
            R.id.action_delete_all_entries ->
                // Do nothing for now
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertPet() {
        val values = ContentValues()

        values.put(PetEntry.NAME, "Tommy")
        values.put(PetEntry.BREED, "Pitbull")
        values.put(PetEntry.GENDER, PetContract.Gender.Male.ordinal)
        values.put(PetEntry.AGE, 5)
        values.put(PetEntry.WEIGHT, 9)
        insertData(values)
    }

    private fun insertData(values: ContentValues) {
        val db = PetDbHelper(this).writableDatabase
        db.insert(PetEntry.TABLE_NAME, null, values)
    }
}