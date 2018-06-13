package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.utils.StringUtils;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        PetDbHelper mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.query(
                PetEntry.TABLE_NAME,
                new String[] {PetEntry._ID, PetEntry.NAME, PetEntry.BREED, PetEntry.GENDER, PetEntry.WEIGHT, PetEntry.AGE},
                "",
                null,
                null,
                null,
                null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = findViewById(R.id.text_view_pet);
            displayView.setText("The pers table contains " + cursor.getCount() + " pets" + StringUtils.Companion.getNEW_LINE() + StringUtils.Companion.getNEW_LINE());
            displayView.append(PetEntry._ID + ", " + PetEntry.NAME + ", " + PetEntry.BREED + ", " + PetEntry.GENDER + ", " + PetEntry.AGE + ", " + PetEntry.WEIGHT + StringUtils.Companion.getNEW_LINE());

            int columnID = cursor.getColumnIndex(PetEntry._ID);
            int columnNAME = cursor.getColumnIndex(PetEntry.NAME);
            int columnBREED = cursor.getColumnIndex(PetEntry.BREED);
            int columnGENDER = cursor.getColumnIndex(PetEntry.GENDER);
            int columnAGE = cursor.getColumnIndex(PetEntry.AGE);
            int columnWEIGHT = cursor.getColumnIndex(PetEntry.WEIGHT);
            while (cursor.moveToNext()) {
                displayView.append(
                        StringUtils.Companion.getNEW_LINE()
                                + cursor.getLong(columnID) + ", "
                                + cursor.getString(columnNAME) + ", "
                                + cursor.getString(columnBREED) + ", "
                                + cursor.getInt(columnGENDER) + ", "
                                + cursor.getInt(columnAGE) + ", "
                                + cursor.getInt(columnWEIGHT));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {
        ContentValues values = new ContentValues();

        values.put(PetEntry.NAME, "Tommy");
        values.put(PetEntry.BREED, "Pitbull");
        values.put(PetEntry.GENDER, PetContract.Gender.Male.ordinal());
        values.put(PetEntry.AGE, 5);
        values.put(PetEntry.WEIGHT, 9);
        insertData(values);
    }

    private void insertData(ContentValues values) {
        SQLiteDatabase db = new PetDbHelper(this).getWritableDatabase();
        db.insert(PetEntry.TABLE_NAME, null, values);
    }
}