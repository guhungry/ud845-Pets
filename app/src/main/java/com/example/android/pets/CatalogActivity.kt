package com.example.android.pets

import android.app.LoaderManager
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.android.pets.data.PetContract
import com.example.android.pets.data.PetContract.PetEntry
import com.example.android.pets.data.adapters.PetAdapter
import com.example.android.pets.model.PetModel
import com.example.android.pets.petcatalog.PetCatalogProtocol
import com.example.android.pets.petcatalog.PetCatalogRouter
import kotlinx.android.synthetic.main.activity_catalog.*

/**
 * Displays list of pets that were entered and stored in the app.
 */
class CatalogActivity : BaseActivity(), PetCatalogProtocol.View, LoaderManager.LoaderCallbacks<Cursor> {
    private val PET_LOADER_ID = 1
    private val adapter = PetAdapter(context = this, cursor = null)
    override var presenter: PetCatalogProtocol.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        presenter = PetCatalogRouter.presenterFor(this)
        loaderManager.initLoader(PET_LOADER_ID, savedInstanceState, this)
        list_pets.adapter = adapter
        list_pets.emptyView = empty_pet
        list_pets.setOnItemClickListener { adapterView, view, position, id ->
            val pet: PetModel = view.tag as PetModel
            presenter?.showPetEditScreenFor(this@CatalogActivity, pet.url)
        }
        // Setup FAB to open EditorActivity
        fab.setOnClickListener {
            presenter?.showPetAddScreenFor(this@CatalogActivity)
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
        return when (item.itemId) {
        // Respond to a click on the "Insert dummy data" menu option
            R.id.action_insert_dummy_data -> {
                insertDummyPet()
                true
            }
        // Respond to a click on the "Delete all entries" menu option
            R.id.action_delete_all_entries -> {
                deletePets()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    private fun insertDummyPet() {
        val pet = PetModel.dummy()
        insertPet(pet.toContentValues())
    }

    // Data Loader
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this, PetEntry.CONTENT_URI, arrayOf(PetEntry._ID, PetEntry.NAME, PetEntry.BREED, PetEntry.GENDER, PetEntry.WEIGHT, PetEntry.AGE), null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        adapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        adapter.swapCursor(null)
    }
}