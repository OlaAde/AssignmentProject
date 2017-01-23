package com.example.adeogo.scratch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

     //Not needed again
        //   SQLiteDatabase db = mDbHelper.getReadableDatabase();


        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };



        // This makes a query that uses the constructed Content Resolver and Provider
        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI, projection, null, null, null);

        ListView petListView = (ListView) findViewById(R.id.list_view_pet);

        View petEmptyView = findViewById(R.id.empty_view);

        PetCursorAdapter petCursorAdapter = new PetCursorAdapter(this,cursor, false);

        petListView.setAdapter(petCursorAdapter);

        petListView.setEmptyView(petEmptyView);

    }

    private void insertDummyPet() {


        ContentValues dummyValues = new ContentValues();
        dummyValues.put(PetEntry.COLUMN_PET_NAME, "Toto");
        dummyValues.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        dummyValues.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        dummyValues.put(PetEntry.COLUMN_PET_WEIGHT, 3);

        //Calling the ContentResolver
        Toast toast = Toast.makeText(this, getString(R.string.insert_dummy_pet_successful), Toast.LENGTH_SHORT);
        toast.show();

     Uri newUri =getContentResolver().insert(PetEntry.CONTENT_URI,dummyValues);

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
