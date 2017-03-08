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
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.VerbContract;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

import static com.xengar.android.englishverbs.utils.Constants.VERB_ID;
import static com.xengar.android.englishverbs.utils.Constants.VERB_NAME;

/**
 * DetailsActivity
 */
public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_VERB_LOADER = 0;
    private FloatingActionButton fabAdd, fabDel;
    private long verbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        verbID = bundle.getLong(VERB_ID, -1);
        String title = bundle.getString(VERB_NAME);
        getSupportActionBar().setTitle(title);
        showFavoriteButtons();

        // Initialize a loader to read the verb data from the database and display it
        getLoaderManager().initLoader(EXISTING_VERB_LOADER, null, this);
    }

    /**
     * Defines if add or remove from Favorites should be initially visible for this movieId.
     */
    private void showFavoriteButtons() {
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabDel = (FloatingActionButton) findViewById(R.id.fab_minus);

        /*Cursor cursor = getContentResolver().query(ContentUris.withAppendedId(URI, movieID),
                new String[]{COLUMN_MOVIE_ID}, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {*/
        if (false){
            fabDel.setVisibility(View.VISIBLE);
        } else {
            fabAdd.setVisibility(View.VISIBLE);
        }
        /*
        if (cursor != null)
            cursor.close();*/
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                VerbEntry._ID,
                VerbEntry.COLUMN_INFINITIVE,
                VerbEntry.COLUMN_SIMPLE_PAST,
                VerbEntry.COLUMN_PAST_PARTICIPLE,
                VerbEntry.COLUMN_REGULAR,
                VerbEntry.COLUMN_DEFINITION,
                VerbEntry.COLUMN_PHONETIC_INFINITIVE,
                VerbEntry.COLUMN_PHONETIC_SIMPLE_PAST,
                VerbEntry.COLUMN_PHONETIC_PAST_PARTICIPLE,
                VerbEntry.COLUMN_SAMPLE_1,
                VerbEntry.COLUMN_SAMPLE_2,
                VerbEntry.COLUMN_SAMPLE_3 };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                // Query the content URI for the verb id
                ContentUris.withAppendedId(VerbContract.VerbEntry.CONTENT_URI, verbID),
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            TextView infinitive = (TextView) findViewById(R.id.infinitive);
            TextView simplePast = (TextView) findViewById(R.id.simple_past);
            TextView pastParticiple = (TextView) findViewById(R.id.past_participle);
            TextView pInfinitive = (TextView) findViewById(R.id.phonetic_infinitive);
            TextView pSimplePast = (TextView) findViewById(R.id.phonetic_simple_past);
            TextView pPastParticiple = (TextView) findViewById(R.id.phonetic_past_participle);
            TextView definition = (TextView) findViewById(R.id.definition);
            TextView sample1 = (TextView) findViewById(R.id.sample1);
            TextView sample2 = (TextView) findViewById(R.id.sample2);
            TextView sample3 = (TextView) findViewById(R.id.sample3);

            // Update the views on the screen with the values from the database
            infinitive.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_INFINITIVE)));
            simplePast.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SIMPLE_PAST)));
            pastParticiple.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PAST_PARTICIPLE)));
            pInfinitive.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PHONETIC_INFINITIVE)));
            pSimplePast.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PHONETIC_SIMPLE_PAST)));
            pPastParticiple.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PHONETIC_PAST_PARTICIPLE)));

            definition.setText(cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_DEFINITION)));
            sample1.setText(cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SAMPLE_1)));
            sample2.setText(cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SAMPLE_2)));
            sample3.setText(cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SAMPLE_3)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
