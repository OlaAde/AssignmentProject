package com.example.adeogo.scratch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.adeogo.scratch.data.PetContract.PetEntry;
import com.example.adeogo.scratch.data.PetDbHelper;

public class CatalogActivity extends AppCompatActivity {

    /**
     * Database helper that will provide us access to the database
     */
    private PetDbHelper mDbHelper;

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

        // To access the database, we instantiate the subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new PetDbHelper(this);

        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };
//My Tests
//        // The columns for the WHERE clause
//        String selection = PetEntry.COLUMN_PET_WEIGHT + ">?";
//
//        // The values for the WHERE clause
//        String[] selectionArgs = {"4"};

        // to get a Cursor that contains all pets with weight greater than 4kg from the pets table.
        Cursor cursor = db.query(PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).

            displayView.setText("The pets table contains " + cursor.getCount() + " pets. \n\n");
            //The append adds text to the textView

            displayView.append(PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - " + PetEntry.COLUMN_PET_BREED + " - " +
                    PetEntry.COLUMN_PET_GENDER + " - " + PetEntry.COLUMN_PET_WEIGHT + "\n");

            // Now we find the index of each column

            int idColumn = cursor.getColumnIndex(PetEntry._ID);
            int nameColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            while (cursor.moveToNext()){
                int currentPetId = cursor.getInt(idColumn);
                String currentPetName = cursor.getString(nameColumn);
                String currentPetBreed = cursor.getString(breedColumn);
                int currentPetGender = cursor.getInt(genderColumn);
                int currentPetWeight = cursor.getInt(weightColumn);

                displayView.append(("\n" + currentPetId + " - " + currentPetName + " - " + currentPetBreed + " - " + currentPetGender + " - " + currentPetWeight));
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertDummyPet() {

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues dummyValues = new ContentValues();
        dummyValues.put(PetEntry.COLUMN_PET_NAME, "Toto");
        dummyValues.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        dummyValues.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        dummyValues.put(PetEntry.COLUMN_PET_WEIGHT, 3);

        long newRowId = db.insert(PetEntry.TABLE_NAME, null, dummyValues);
        Log.v("NewRow", "" + newRowId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
