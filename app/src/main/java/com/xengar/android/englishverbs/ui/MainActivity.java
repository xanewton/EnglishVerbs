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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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

import static com.xengar.android.englishverbs.utils.Constants.ALPHABET;
import static com.xengar.android.englishverbs.utils.Constants.BOTH;
import static com.xengar.android.englishverbs.utils.Constants.CARD;
import static com.xengar.android.englishverbs.utils.Constants.COLOR;
import static com.xengar.android.englishverbs.utils.Constants.CURRENT_PAGE;
import static com.xengar.android.englishverbs.utils.Constants.FAVORITES;
import static com.xengar.android.englishverbs.utils.Constants.IRREGULAR;
import static com.xengar.android.englishverbs.utils.Constants.ITEM_TYPE;
import static com.xengar.android.englishverbs.utils.Constants.LAST_ACTIVITY;
import static com.xengar.android.englishverbs.utils.Constants.LIST;
import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.MAIN_ACTIVITY;
import static com.xengar.android.englishverbs.utils.Constants.MOST_COMMON_100;
import static com.xengar.android.englishverbs.utils.Constants.MOST_COMMON_25;
import static com.xengar.android.englishverbs.utils.Constants.MOST_COMMON_50;
import static com.xengar.android.englishverbs.utils.Constants.MOST_COMMON_500;
import static com.xengar.android.englishverbs.utils.Constants.MOST_COMMON_ALL;
import static com.xengar.android.englishverbs.utils.Constants.PAGE_CARDS;
import static com.xengar.android.englishverbs.utils.Constants.PAGE_FAVORITES;
import static com.xengar.android.englishverbs.utils.Constants.PAGE_VERBS;
import static com.xengar.android.englishverbs.utils.Constants.REGULAR;
import static com.xengar.android.englishverbs.utils.Constants.SHARED_PREF_NAME;
import static com.xengar.android.englishverbs.utils.Constants.SORT_TYPE;
import static com.xengar.android.englishverbs.utils.Constants.VERBS_ED;
import static com.xengar.android.englishverbs.utils.Constants.VERB_TYPE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private UniversalFragment verbsFragment;
    private UniversalFragment cardsFragment;
    private UniversalFragment favoritesFragment;
    private FrameLayout fragmentLayout;

    final String VERB_TYPES[] = {REGULAR, IRREGULAR, BOTH};
    private final int[] verbSelection = {2};
    private final String[] verbType = {VERB_TYPES[verbSelection[0]]}; // current verb type list in screen

    final String SORT_TYPES[] = {ALPHABET, COLOR, VERBS_ED};
    private final int[] sortSelection = {0};
    private final String[] sortType = {SORT_TYPES[sortSelection[0]]}; // current sort type list in screen

    final String COMMON_TYPES[] = {MOST_COMMON_25, MOST_COMMON_50, MOST_COMMON_100,
            MOST_COMMON_500, MOST_COMMON_ALL};
    private final int[] commonSelection = {0};
    private final String[] commonType = {COMMON_TYPES[commonSelection[0]]}; // current most common type list in screen

    final String ITEM_TYPES[] = {LIST, CARD};
    private final String[] itemType = {ITEM_TYPES[0]};
    private String page = PAGE_VERBS; // Current page

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
        page = prefs.getString(CURRENT_PAGE, PAGE_VERBS);

        fragmentLayout = (FrameLayout) findViewById(R.id.fragment_container);
        verbsFragment = createUniversalFragment(BOTH, LIST, ALPHABET);
        cardsFragment = createUniversalFragment(BOTH, CARD, ALPHABET);
        favoritesFragment = createUniversalFragment(FAVORITES, LIST, ALPHABET);
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
            case R.id.action_change_type:
                changeVerbType();
                return true;

            case R.id.action_sort:
                sortVerbs();
                return true;

            case R.id.action_most_common:
                showMostCommon();
                return true;

            case R.id.action_search:
                ActivityUtils.launchSearchActivity(getApplicationContext());
                return true;

            /*
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertSampleVerbs();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllVerbs();
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a Fragment.
     * @param verbsType Type of verbs to display REGULAR, IRREGULAR, BOTH
     * @param itemType Display mode LIST, CARD
     * @return fragment
     */
    private UniversalFragment createUniversalFragment(String verbsType, String itemType,
                                                      String sortType) {
        UniversalFragment fragment = new UniversalFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VERB_TYPE, verbsType);
        bundle.putString(ITEM_TYPE, itemType);
        bundle.putString(SORT_TYPE, sortType);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Changes the type of verb (Regular, Irregular, both).
     */
    private void changeVerbType(){
        final CharSequence options[] = new CharSequence[] {
                getString(R.string.regular), getString(R.string.irregular),
                getString(R.string.both) };

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(getString(R.string.select_show_verbs));
        builder.setSingleChoiceItems(options, verbSelection[0],
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // save the selected verb type
                        verbSelection[0] = item;
                        verbType[0] = VERB_TYPES[item];
                    }
                });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Change the selection.
                switch (verbType[0]){
                    case REGULAR:
                    case IRREGULAR:
                    case BOTH:
                        changeFragmentsDisplay();
                        break;

                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Changes the sort order.
     */
    private void sortVerbs() {
        final CharSequence options[] = new CharSequence[] {
                getString(R.string.alphabet), getString(R.string.color),
                getString(R.string.verbs_ed) };

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(getString(R.string.select_type_of_sort));
        builder.setSingleChoiceItems(options, sortSelection[0],
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // save the selected verb type
                        sortSelection[0] = item;
                        sortType[0] = SORT_TYPES[item];
                    }
                });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Change the selection.
                switch (sortType[0]){
                    case ALPHABET:
                    case COLOR:
                    case VERBS_ED:
                        changeFragmentsDisplay();
                        break;

                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Shos the most common verbs according to selection.
     */
    private void showMostCommon() {
        final CharSequence options[] = new CharSequence[] {
                getString(R.string.most_common_25), getString(R.string.most_common_50),
                getString(R.string.most_common_100), getString(R.string.most_common_500),
                getString(R.string.most_common_all),};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(getString(R.string.select_show_verbs));
        builder.setSingleChoiceItems(options, commonSelection[0],
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // save the selected verb type
                        commonSelection[0] = item;
                        commonType[0] = COMMON_TYPES[item];
                    }
                });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Change the selection.
                switch (commonType[0]){
                    case MOST_COMMON_25:
                    case MOST_COMMON_50:
                    case MOST_COMMON_100:
                    case MOST_COMMON_500:
                    case MOST_COMMON_ALL:
                        // TODO: implement once we have enought verbs
                        //changeFragmentsDisplay();
                        break;

                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Creates if needed a new fragment with the new display configurations.
     */
    private void changeFragmentsDisplay(){
        if (!verbsFragment.getVerbsType().contentEquals(verbType[0]) ||
                !verbsFragment.getSortType().contentEquals(sortType[0])) {
            verbsFragment = createUniversalFragment(verbType[0], LIST, sortType[0]);
            if (page.contentEquals(PAGE_VERBS)) {
                launchFragment(PAGE_VERBS);
            }
        }
        if (!cardsFragment.getVerbsType().contentEquals(verbType[0]) ||
                !cardsFragment.getSortType().contentEquals(sortType[0])) {
            cardsFragment = createUniversalFragment(verbType[0], CARD, sortType[0]);
            if (page.contentEquals(PAGE_CARDS)) {
                launchFragment(PAGE_CARDS);
            }
        }
        if (!favoritesFragment.getVerbsType().contentEquals(verbType[0]) ||
                !favoritesFragment.getSortType().contentEquals(sortType[0])) {
            favoritesFragment = createUniversalFragment(verbType[0], LIST, sortType[0]);
            if (page.contentEquals(PAGE_FAVORITES)) {
                launchFragment(PAGE_FAVORITES);
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_verbs:
                page = PAGE_VERBS;
                getSupportActionBar().setTitle(R.string.menu_option_verbs);
                ActivityUtils.saveStringToPreferences(this, CURRENT_PAGE, PAGE_VERBS);
                launchFragment(PAGE_VERBS);
                break;
            case R.id.nav_cards:
                page = PAGE_CARDS;
                getSupportActionBar().setTitle(R.string.menu_option_cards);
                ActivityUtils.saveStringToPreferences(this, CURRENT_PAGE, PAGE_CARDS);
                launchFragment(PAGE_CARDS);
                break;
            case R.id.nav_favorites:
                page = PAGE_FAVORITES;
                getSupportActionBar().setTitle(R.string.menu_option_favorites);
                ActivityUtils.saveStringToPreferences(this, CURRENT_PAGE, PAGE_FAVORITES);
                launchFragment(PAGE_FAVORITES);
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
            case PAGE_VERBS:
                fragmentTransaction.replace(R.id.fragment_container, verbsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case PAGE_CARDS:
                fragmentTransaction.replace(R.id.fragment_container, cardsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case PAGE_FAVORITES:
                fragmentTransaction.replace(R.id.fragment_container, favoritesFragment);
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
            case PAGE_VERBS:
                getSupportActionBar().setTitle(R.string.menu_option_verbs);
                ActivityUtils.saveStringToPreferences(this, CURRENT_PAGE, PAGE_VERBS);
                launchFragment(PAGE_VERBS);
                break;
            case PAGE_CARDS:
                getSupportActionBar().setTitle(R.string.menu_option_verbs);
                ActivityUtils.saveStringToPreferences(this, CURRENT_PAGE, PAGE_CARDS);
                launchFragment(PAGE_CARDS);
                break;
            case PAGE_FAVORITES:
                getSupportActionBar().setTitle(R.string.menu_option_favorites);
                ActivityUtils.saveStringToPreferences(this, CURRENT_PAGE, PAGE_FAVORITES);
                launchFragment(PAGE_FAVORITES);
                break;
        }
    }

    public void assignCheckedItem(String page){
        // set selected
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        switch (page){
            case PAGE_VERBS:
                navigationView.setCheckedItem(R.id.nav_verbs);
                break;
            case PAGE_CARDS:
                navigationView.setCheckedItem(R.id.nav_cards);
                break;
            case PAGE_FAVORITES:
                navigationView.setCheckedItem(R.id.nav_favorites);
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
