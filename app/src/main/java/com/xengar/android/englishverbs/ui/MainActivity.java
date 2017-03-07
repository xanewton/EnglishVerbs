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

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;
import com.xengar.android.englishverbs.utils.ActivityUtils;

import static com.xengar.android.englishverbs.utils.Constants.ITEM_CATEGORY;
import static com.xengar.android.englishverbs.utils.Constants.LAST_ACTIVITY;
import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.MAIN_ACTIVITY;
import static com.xengar.android.englishverbs.utils.Constants.PAGE_HOME;
import static com.xengar.android.englishverbs.utils.Constants.PAGE_IRREGULAR;
import static com.xengar.android.englishverbs.utils.Constants.PAGE_REGULAR;
import static com.xengar.android.englishverbs.utils.Constants.SHARED_PREF_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private HomeFragment homeFragment;
    private RegularFragment regularFragment;
    private IrregularFragment irregularFragment;
    private FrameLayout fragmentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Save name of activity, in case of calling SettingsActivity
        ActivityUtils.saveStringToPreferences(getApplicationContext(), LAST_ACTIVITY,
                MAIN_ACTIVITY);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, 0);
        String page = prefs.getString(ITEM_CATEGORY, PAGE_HOME);

        fragmentLayout = (FrameLayout) findViewById(R.id.fragment_container);
        homeFragment = new HomeFragment();
        regularFragment = new RegularFragment();
        irregularFragment = new IrregularFragment();

        showPage(page);
        assignCheckedItem(page);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                getSupportActionBar().setTitle(R.string.menu_option_home);
                ActivityUtils.saveStringToPreferences(this, ITEM_CATEGORY, PAGE_HOME);
                launchFragment(PAGE_HOME);
                break;
            case R.id.nav_regular:
                getSupportActionBar().setTitle(R.string.menu_option_regular);
                ActivityUtils.saveStringToPreferences(this, ITEM_CATEGORY, PAGE_REGULAR);
                launchFragment(PAGE_REGULAR);
                break;
            case R.id.nav_irregular:
                getSupportActionBar().setTitle(R.string.menu_option_irregular);
                ActivityUtils.saveStringToPreferences(this, ITEM_CATEGORY, PAGE_IRREGULAR);
                launchFragment(PAGE_IRREGULAR);
                break;
            case R.id.nav_settings:
                ActivityUtils.launchSettingsActivity(getApplicationContext());
                break;
            case R.id.nav_help:
                ActivityUtils.launchHelpActivity(getApplicationContext());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Launches the selected fragment.
     * @param category The type of search
     */
    private void launchFragment(String category) {
        android.support.v4.app.FragmentTransaction fragmentTransaction
                = getSupportFragmentManager().beginTransaction();
        switch (category) {
            case PAGE_HOME:
                fragmentTransaction.replace(R.id.fragment_container, homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case PAGE_REGULAR:
                fragmentTransaction.replace(R.id.fragment_container, regularFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case PAGE_IRREGULAR:
                fragmentTransaction.replace(R.id.fragment_container, irregularFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }

    /***
     * Shows the correct page on screen.
     * @param page name of page
     */
    public void showPage(String page) {
        switch (page){
            case PAGE_HOME:
                getSupportActionBar().setTitle(R.string.menu_option_home);
                ActivityUtils.saveStringToPreferences(this, ITEM_CATEGORY, PAGE_HOME);
                launchFragment(PAGE_HOME);
                break;

            case PAGE_REGULAR:
                getSupportActionBar().setTitle(R.string.menu_option_regular);
                ActivityUtils.saveStringToPreferences(this, ITEM_CATEGORY, PAGE_REGULAR);
                launchFragment(PAGE_REGULAR);
                break;

            case PAGE_IRREGULAR:
                getSupportActionBar().setTitle(R.string.menu_option_irregular);
                ActivityUtils.saveStringToPreferences(this, ITEM_CATEGORY, PAGE_IRREGULAR);
                launchFragment(PAGE_IRREGULAR);
                break;
        }
    }

    public void assignCheckedItem(String page){
        // set selected
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        switch (page){
            case PAGE_HOME:
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case PAGE_REGULAR:
                navigationView.setCheckedItem(R.id.nav_regular);
                break;
            case PAGE_IRREGULAR:
                navigationView.setCheckedItem(R.id.nav_irregular);
                break;
        }
    }


    /**
     * Helper method to insert hardcoded verb data into the database.
     * For debugging purposes only.
     */
    private void insertSampleVerbs(){
        insertVerb(new Verb(0L, "ask", "asked", "asked", "To question or demand",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_50, VerbEntry.REGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "become", "became", "become", "To come into existance",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "begin", "began", "begun", "To start something",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_50, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "call", "called", "called", "To comunicate with someone by phone",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_25, VerbEntry.REGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "come", "came", "come", "To arrive into a place",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "do", "did", "done", "To come into existance",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "feel", "felt", "felt", "To sense something phusically or emotionally",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_25, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "go", "went", "gone", "To come into existance",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.TOP_100, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "keep", "kept", "kept", "To retain something in our possession",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
                VerbEntry.OTHER, VerbEntry.IRREGULAR, 0, 0, "", "convertir", "devenir"));
        insertVerb(new Verb(0L, "missunderstand", "missunderstood", "missunderstood", "To not get the sense of something",
                "He becomes president today.",
                        "Alice became teacher last year.",
                        "He has become a new person since he left her.", "", "", "",
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
        values.put(VerbEntry.COLUMN_PHONETIC_INFINITIVE, v.getPhoneticInfinitive());
        values.put(VerbEntry.COLUMN_PHONETIC_SIMPLE_PAST, v.getPhoneticSimplePast());
        values.put(VerbEntry.COLUMN_PHONETIC_PAST_PARTICIPLE, v.getPhoneticPastParticiple());
        values.put(VerbEntry.COLUMN_SAMPLE_1, v.getSample1());
        values.put(VerbEntry.COLUMN_SAMPLE_2, v.getSample2());
        values.put(VerbEntry.COLUMN_SAMPLE_3, v.getSample3());
        values.put(VerbEntry.COLUMN_COMMON, v.getCommon());
        values.put(VerbEntry.COLUMN_REGULAR, v.getRegular());
        values.put(VerbEntry.COLUMN_COLOR, v.getColor());
        values.put(VerbEntry.COLUMN_SCORE, v.getScore());
        values.put(VerbEntry.COLUMN_NOTES, v.getNotes());
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
        if (LOG) {
            Log.v(TAG, rowsDeleted + " rows deleted from pet database");
        }
    }
}
