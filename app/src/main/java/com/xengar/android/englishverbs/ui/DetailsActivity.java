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
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;
import com.xengar.android.englishverbs.utils.ActivityUtils;
import com.xengar.android.englishverbs.utils.LogAdListener;

import java.util.Locale;

import static com.xengar.android.englishverbs.data.VerbContract.VerbEntry.COLUMN_ID;
import static com.xengar.android.englishverbs.utils.Constants.DEMO_MODE;
import static com.xengar.android.englishverbs.utils.Constants.DETAILS_ACTIVITY;
import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.PAGE_VERB_DETAILS;
import static com.xengar.android.englishverbs.utils.Constants.TYPE_ADD_FAV;
import static com.xengar.android.englishverbs.utils.Constants.TYPE_DEL_FAV;
import static com.xengar.android.englishverbs.utils.Constants.TYPE_PAGE;
import static com.xengar.android.englishverbs.utils.Constants.VERBS;
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

    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;
    private LogAdListener listener;

    // Demo
    private ShowcaseView showcaseView;
    private boolean demo;
    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        demo = bundle.getBoolean(DEMO_MODE, false);
        verbID = bundle.getLong(VERB_ID, -1);
        String title = bundle.getString(VERB_NAME);
        getSupportActionBar().setTitle(title);

        // Invalidate the options menu, so the "Edit" menu option can be hidden.
        invalidateOptionsMenu();

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

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ActivityUtils.firebaseAnalyticsLogEventSelectContent(
                mFirebaseAnalytics, PAGE_VERB_DETAILS, PAGE_VERB_DETAILS, TYPE_PAGE);

        // create AdMob banner
        listener = new LogAdListener(mFirebaseAnalytics, DETAILS_ACTIVITY);
        mAdView = ActivityUtils.createAdMobBanner(this, listener);

        if (demo){
            defineDemoMode();
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new verb, hide the "Edit" menu item for application verbs.
        if (verbID >= 0) {
            MenuItem menuItem = menu.findItem(R.id.action_edit);
            menuItem.setVisible(false);
        }
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

            case R.id.action_search:
                ActivityUtils.launchSearchActivity(getApplicationContext());
                return true;

            case R.id.action_edit:
                ActivityUtils.launchEditorActivity(getApplicationContext(), verbID);
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
                VerbEntry.CONTENT_URI, values,
                COLUMN_ID + " = ?", new String[]{Long.toString(verbID)});

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

        Cursor cursor = getContentResolver().query(VerbEntry.CONTENT_FAVORITES_URI,
                new String[]{ COLUMN_ID}, //select
                COLUMN_ID + " = ?" , // where
                new String[]{Long.toString(verbID)}, //whereArgs
                null);
        if (cursor != null && cursor.getCount() != 0) {
            fabDel.setVisibility(View.VISIBLE);
        } else {
            fabAdd.setVisibility(View.VISIBLE);
        }
        if (cursor != null)
            cursor.close();
    }

    /**
     * Defines what to do when click on add/remove from Favorites buttons.
     */
    private void defineClickFavoriteButtons() {
        final int DURATION = 1000;

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.favorites_add_message), DURATION)
                        .setAction("Action", null).show();
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, verbID);
                getContentResolver().insert(VerbContract.VerbEntry.CONTENT_FAVORITES_URI, values);

                fabAdd.setVisibility(View.INVISIBLE);
                fabDel.setVisibility(View.VISIBLE);
                ActivityUtils.firebaseAnalyticsLogEventSelectContent(mFirebaseAnalytics,
                        VERB_ID + " " + verbID, verb.getInfinitive(), TYPE_ADD_FAV);
            }
        });

        fabDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.favorites_del_message), DURATION)
                        .setAction("Action", null).show();
                getContentResolver().delete(VerbContract.VerbEntry.CONTENT_FAVORITES_URI,
                        COLUMN_ID + " = ?",
                        new String[]{Long.toString(verbID)} );

                fabAdd.setVisibility(View.VISIBLE);
                fabDel.setVisibility(View.INVISIBLE);
                ActivityUtils.firebaseAnalyticsLogEventSelectContent(mFirebaseAnalytics,
                        VERB_ID + " " + verbID, verb.getInfinitive(), TYPE_DEL_FAV);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = ActivityUtils.allVerbColumns();

        return new CursorLoader(this,   // Parent activity context
                VerbEntry.CONTENT_URI,
                projection,             // Columns to include in the resulting Cursor
                COLUMN_ID + " = ?",     // selection clause
                new String[]{Long.toString(verbID)}, // selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            finish(); // the verb doesn't exist, this should not happen.
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            verb = ActivityUtils.verbFromCursor(cursor);
            getSupportActionBar().setTitle(verb.getInfinitive());
            setVerbColor(verb.getColor());
            fillVerbDetails(verb);
            defineClickFavoriteButtons();
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

        ActivityUtils.firebaseAnalyticsLogEventViewItem(
                mFirebaseAnalytics, "" + verbID, verb.getInfinitive(), VERBS);
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

            default:
                onClickDemo();
                break;
        }
    }

    /**
     * Start a show case view for demo mode.
     */
    private void defineDemoMode() {
        showcaseView = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(new ViewTarget(findViewById(R.id.infinitive)))
                .setContentTitle(getString(R.string.details))
                .setContentText(getString(R.string.infinitive))
                .setStyle(R.style.CustomShowcaseTheme2)
                .replaceEndButton(R.layout.view_custom_button)
                .setOnClickListener(this)
                .build();
        showcaseView.setButtonText(getString(R.string.next));
    }

    /**
     * Defines what item to show case view for demo mode.
     */
    public void onClickDemo() {
        if (!demo) return;
        switch (counter) {
            case 0:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.simple_past)), true);
                showcaseView.setContentText(getString(R.string.simple_past));
                break;

            case 1:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.past_participle)), true);
                showcaseView.setContentText(getString(R.string.past_participle));
                break;

            case 2:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.phonetic_infinitive)), true);
                showcaseView.setContentText(getString(R.string.phonetics));
                break;

            case 3:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.play_simple_past)), true);
                showcaseView.setContentText(getString(R.string.pronunciation));
                break;

            case 4:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.definition)), true);
                showcaseView.setContentText(getString(R.string.definition));
                break;

            case 5:
                showcaseView.setShowcase(new ViewTarget(findViewById(R.id.sample2)), true);
                showcaseView.setContentText(getString(R.string.examples));
                break;

            case 6:
                showcaseView.setShowcase(new ViewTarget(fabAdd), true);
                showcaseView.setContentTitle(getString(R.string.menu_option_favorites));
                showcaseView.setContentText(getString(R.string.add_remove_from_favorites));
                showcaseView.setButtonText(getString(R.string.got_it));
                break;

            case 7:
                showcaseView.hide();
                demo = false;
                break;
        }
        counter++;
    }
}
