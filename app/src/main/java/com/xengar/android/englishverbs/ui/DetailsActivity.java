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
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerPalette;
import com.android.colorpicker.ColorPickerSwatch;
import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;
import com.xengar.android.englishverbs.utils.ActivityUtils;

import java.util.Locale;

import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.VERB_ID;
import static com.xengar.android.englishverbs.utils.Constants.VERB_NAME;

/**
 * DetailsActivity
 */
public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    private static final int EXISTING_VERB_LOADER = 0;
    private FloatingActionButton fabAdd, fabDel;
    private long verbID;
    private Verb verb;
    private TextToSpeech tts;
    private TextView infinitive, simplePast, pastParticiple;
    private TextView pInfinitive, pSimplePast, pPastParticiple;
    private TextView definition, translation, sample1, sample2, sample3;

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

        //Text
        infinitive = (TextView) findViewById(R.id.infinitive);
        simplePast = (TextView) findViewById(R.id.simple_past);
        pastParticiple = (TextView) findViewById(R.id.past_participle);
        pInfinitive = (TextView) findViewById(R.id.phonetic_infinitive);
        pSimplePast = (TextView) findViewById(R.id.phonetic_simple_past);
        pPastParticiple = (TextView) findViewById(R.id.phonetic_past_participle);
        definition = (TextView) findViewById(R.id.definition);
        translation = (TextView) findViewById(R.id.translation);
        sample1 = (TextView) findViewById(R.id.sample1);
        sample2 = (TextView) findViewById(R.id.sample2);
        sample3 = (TextView) findViewById(R.id.sample3);

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

        // Initialize a loader to read the verb data from the database and display it
        getLoaderManager().initLoader(EXISTING_VERB_LOADER, null, this);
        showFavoriteButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_change_color:
                changeColorDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Changes the color
     */
    private void changeColorDialog() {
        final int[] colors = {
                ContextCompat.getColor(getApplicationContext(), R.color.colorBlack),
                ContextCompat.getColor(getApplicationContext(), R.color.colorRed),
                ContextCompat.getColor(getApplicationContext(), R.color.colorGreen),
                ContextCompat.getColor(getApplicationContext(), R.color.colorBlue),
                ContextCompat.getColor(getApplicationContext(), R.color.colorPink),
                ContextCompat.getColor(getApplicationContext(), R.color.colorPurple),
                ContextCompat.getColor(getApplicationContext(), R.color.colorDeepPurple),
                ContextCompat.getColor(getApplicationContext(), R.color.colorIndigo),
                ContextCompat.getColor(getApplicationContext(), R.color.colorOrange),
                ContextCompat.getColor(getApplicationContext(), R.color.colorDeepOrange),
                ContextCompat.getColor(getApplicationContext(), R.color.colorBrown),
                ContextCompat.getColor(getApplicationContext(), R.color.colorBlueGray) };

        final int[] selectedColor = {colors[0]};
        if (verb!= null) {
            selectedColor[0] = verb.getColor();
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final ColorPickerPalette colorPickerPalette = (ColorPickerPalette) layoutInflater
                .inflate(R.layout.custom_picker, null);

        ColorPickerSwatch.OnColorSelectedListener listener = new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                selectedColor[0] = color;
                colorPickerPalette.drawPalette(colors, selectedColor[0]);
            }
        };

        colorPickerPalette.init(colors.length, 4, listener);
        colorPickerPalette.drawPalette(colors, selectedColor[0]);

        AlertDialog alert = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setTitle(R.string.select_color)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Save changes
                        saveColor(selectedColor[0]);
                        setVerbColor(selectedColor[0]);
                        verb.setColor(selectedColor[0]);
                    }
                })
                .setView(colorPickerPalette)
                .create();
        alert.show();
    }

    /**
     * Save the color to database.
     * @param color Color
     */
    private void saveColor(int color) {
        ContentValues values = new ContentValues();
        values.put(VerbEntry.COLUMN_COLOR, "" + color);
        int rowsAffected = getContentResolver().update(
                ContentUris.withAppendedId(VerbEntry.CONTENT_URI, verbID), values, null, null);

        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            if (LOG){
                Log.e(TAG, "Failed to change color to verb!");
            }
        }
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

            setVerbColor(verb.getColor());
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

        ActivityUtils.setTranslation(getApplicationContext(), translation, verb);
    }

    /**
     * Set the text color.
     * @param color color
     */
    private void setVerbColor(int color) {
        infinitive.setTextColor(color);
        simplePast.setTextColor(color);
        pastParticiple.setTextColor(color);
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
        switch(view.getId()){
            case R.id.play_infinitive:
                ActivityUtils.speak(getApplicationContext(), tts, verb.getInfinitive());
                Toast.makeText(getApplicationContext(),verb.getInfinitive(),Toast.LENGTH_SHORT).show();
                break;

            case R.id.play_simple_past:
                ActivityUtils.speak(getApplicationContext(), tts, verb.getSimplePast());
                Toast.makeText(getApplicationContext(),verb.getSimplePast(),Toast.LENGTH_SHORT).show();
                break;

            case R.id.play_past_participle:
                ActivityUtils.speak(getApplicationContext(), tts, verb.getPastParticiple());
                Toast.makeText(getApplicationContext(),verb.getPastParticiple(),Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
