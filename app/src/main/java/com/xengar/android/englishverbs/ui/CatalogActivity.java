/*
 * Copyright (C) 2017 Angel Garcia
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
package com.xengar.android.englishverbs.ui;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.adapters.VerbCursorAdapter;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;


public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the verb data loader */
    private static final int VERB_LOADER = 0;

    /** Adapter for the ListView */
    VerbCursorAdapter mCursorAdapter;


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

        // Find the ListView which will be populated with the verb data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.loading_indicator);
        petListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no verb data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new VerbCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link VerbEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.xengar.android.englishverbs/verbs/2"
                // if the verb with ID 2 was clicked on.
                Uri currentVerbUri = ContentUris.withAppendedId(VerbEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVerbUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(VERB_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                VerbEntry._ID,
                VerbEntry.COLUMN_INFINITIVE,
                VerbEntry.COLUMN_SIMPLE_PAST,
                VerbEntry.COLUMN_PAST_PARTICIPLE,
                VerbEntry.COLUMN_DEFINITION,
                VerbEntry.COLUMN_REGULAR,
                VerbEntry.COLUMN_SCORE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                VerbEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link VerbCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    /**
     * Helper method to insert hardcoded verb data into the database.
     * For debugging purposes only.
     */
    private void insertSampleVerbs(){
        insertVerb(new Verb("ask", "asked", "asked", "To question or demand",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.TOP_50, VerbEntry.REGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("become", "became", "become", "To come into existance",
                "He becomes president today." +
                "Alice became teacher last year." +
                "He has become a new person since he left her.",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("begin", "began", "begun", "To start something",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.TOP_50, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("call", "called", "called", "To comunicate with someone by phone",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.TOP_25, VerbEntry.REGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("come", "came", "come", "To arrive into a place",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("do", "did", "done", "To come into existance",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("feel", "felt", "felt", "To sense something phusically or emotionally",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("go", "went", "gone", "To come into existance",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.TOP_100, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("keep", "kept", "kept", "To retain something in our possession",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.OTHER, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb("missunderstand", "missunderstood", "missunderstood", "To not get the sense of something",
                "He becomes president today." +
                        "Alice became teacher last year." +
                        "He has become a new person since he left her.",
                VerbEntry.OTHER, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
    }

    /**
     * Helper method to insert hardcoded verb data into the database.
     * For debugging purposes only.
     */
    private void insertVerb(Verb v) {
        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(VerbEntry.COLUMN_INFINITIVE, v.getInfinitive());
        values.put(VerbEntry.COLUMN_SIMPLE_PAST, v.getSimplePast());
        values.put(VerbEntry.COLUMN_PAST_PARTICIPLE, v.getPastParticiple());
        values.put(VerbEntry.COLUMN_DEFINITION, v.getDefinition());
        values.put(VerbEntry.COLUMN_SAMPLES, v.getSamples());
        values.put(VerbEntry.COLUMN_COMMON, v.getCommon());
        values.put(VerbEntry.COLUMN_REGULAR, v.getRegular());
        values.put(VerbEntry.COLUMN_COLOR, v.getColor());
        values.put(VerbEntry.COLUMN_SCORE, v.getScore());
        values.put(VerbEntry.COLUMN_DATA, v.getData());
        values.put(VerbEntry.COLUMN_TRANSLATION_ES, v.getTranslationES());
        values.put(VerbEntry.COLUMN_TRANSLATION_FR, v.getTranslationFR());

        // Use the {@link VerbEntry#CONTENT_URI} to indicate that we want to insert
        // into the verbs database table.
        // Receive the new content URI that will allow us to access it in the future.
        Uri newUri = getContentResolver().insert(VerbEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all verbs in the database.
     */
    private void deleteAllVerbs() {
        int rowsDeleted = getContentResolver().delete(VerbEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
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
                insertSampleVerbs();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllVerbs();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
