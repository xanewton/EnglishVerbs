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
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

import java.util.Locale;

import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.VERB_ID;
import static com.xengar.android.englishverbs.utils.Constants.VERB_NAME;

/**
 * DetailsActivity
 */
public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final int EXISTING_VERB_LOADER = 0;
    private FloatingActionButton fabAdd, fabDel;
    private long verbID;
    private Verb verb;
    private TextToSpeech tts;

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

        // define click listeners
        LinearLayout header = (LinearLayout) findViewById(R.id.play_infinitive);
        header.setOnClickListener(this);
        header = (LinearLayout) findViewById(R.id.play_simple_past);
        header.setOnClickListener(this);
        header = (LinearLayout) findViewById(R.id.play_past_participle);
        header.setOnClickListener(this);

        // initialize Speaker
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        if (LOG) {
                            Log.e("TTS", "This Language is not supported");
                        }
                    }
                } else {
                    if (LOG) {
                        Log.e("TTS", "Initilization Failed!");
                    }
                }
            }
        });
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
                VerbEntry.COLUMN_DEFINITION,
                VerbEntry.COLUMN_PHONETIC_INFINITIVE,
                VerbEntry.COLUMN_PHONETIC_SIMPLE_PAST,
                VerbEntry.COLUMN_PHONETIC_PAST_PARTICIPLE,
                VerbEntry.COLUMN_SAMPLE_1,
                VerbEntry.COLUMN_SAMPLE_2,
                VerbEntry.COLUMN_SAMPLE_3,
                VerbEntry.COLUMN_COMMON,
                VerbEntry.COLUMN_REGULAR,
                VerbEntry.COLUMN_COLOR,
                VerbEntry.COLUMN_SCORE,
                VerbEntry.COLUMN_NOTES,
                VerbEntry.COLUMN_TRANSLATION_ES,
                VerbEntry.COLUMN_TRANSLATION_FR };

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
            verb = new Verb(verbID,
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_INFINITIVE)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SIMPLE_PAST)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PAST_PARTICIPLE)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_DEFINITION)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SAMPLE_1)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SAMPLE_2)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SAMPLE_3)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PHONETIC_INFINITIVE)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PHONETIC_SIMPLE_PAST)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PHONETIC_PAST_PARTICIPLE)),
                    cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_COMMON)),
                    cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_REGULAR)),
                    cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_COLOR)),
                    cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_SCORE)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_NOTES)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_TRANSLATION_ES)),
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_TRANSLATION_FR))  );

            fillVerbDetails(verb);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Fill verb details.
     * @param verb
     */
    private void fillVerbDetails(Verb verb) {
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
        infinitive.setText(verb.getInfinitive());
        simplePast.setText(verb.getSimplePast());
        pastParticiple.setText(verb.getPastParticiple());
        pInfinitive.setText(verb.getPhoneticInfinitive());
        pSimplePast.setText(verb.getPhoneticSimplePast());
        pPastParticiple.setText(verb.getPhoneticPastParticiple());

        definition.setText(verb.getDefinition());
        sample1.setText(verb.getSample1());
        sample2.setText(verb.getSample2());
        sample3.setText(verb.getSample3());
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        // Play the sounds
        final int DURATION = 1000;
        switch(view.getId()){
            case R.id.play_infinitive:
                speak(verb.getInfinitive());
                //Snackbar.make(view, "infinitive", DURATION).setAction("Action", null).show();
                Toast.makeText(getApplicationContext(),verb.getInfinitive(),Toast.LENGTH_SHORT).show();
                break;

            case R.id.play_simple_past:
                speak(verb.getSimplePast());
                //Snackbar.make(view, "past", DURATION).setAction("Action", null).show();
                Toast.makeText(getApplicationContext(),verb.getSimplePast(),Toast.LENGTH_SHORT).show();
                break;

            case R.id.play_past_participle:
                speak(verb.getPastParticiple());
                //Snackbar.make(view, "past part", DURATION).setAction("Action", null).show();
                Toast.makeText(getApplicationContext(),verb.getPastParticiple(),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Text we want to speak
     * @param text String
     */
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
