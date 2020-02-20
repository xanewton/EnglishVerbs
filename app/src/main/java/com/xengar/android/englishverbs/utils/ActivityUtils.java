/*
 * Copyright (C) 2017 Angel Newton
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
package com.xengar.android.englishverbs.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;
import com.xengar.android.englishverbs.ui.DetailsActivity;
import com.xengar.android.englishverbs.ui.EditorActivity;
import com.xengar.android.englishverbs.ui.HelpActivity;
import com.xengar.android.englishverbs.ui.SearchActivity;
import com.xengar.android.englishverbs.ui.SettingsActivity;

import static com.xengar.android.englishverbs.utils.Constants.DEMO_MODE;
import static com.xengar.android.englishverbs.utils.Constants.FRENCH;
import static com.xengar.android.englishverbs.utils.Constants.LIST;
import static com.xengar.android.englishverbs.utils.Constants.NONE;
import static com.xengar.android.englishverbs.utils.Constants.SHARED_PREF_NAME;
import static com.xengar.android.englishverbs.utils.Constants.SPANISH;
import static com.xengar.android.englishverbs.utils.Constants.USE_TEST_ADS;
import static com.xengar.android.englishverbs.utils.Constants.VERB_ID;
import static com.xengar.android.englishverbs.utils.Constants.VERB_NAME;


/**
 * ActivityUtils. To handle common tasks.
 */
public class ActivityUtils {

    private static final String TAG = ActivityUtils.class.getSimpleName();

    /**
     * Saves the variable into Preferences.
     * @param context context
     * @param name name of preference
     * @param value value
     */
    public static void saveIntToPreferences(final Context context, final String name,
                                            final int value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor e = prefs.edit();
        e.putInt(name, value);
        e.commit();
    }

    /**
     * Saves the variable into Preferences.
     * @param context context
     * @param name name of preference
     * @param value value
     */
    public static void saveLongToPreferences(final Context context, final String name,
                                            final long value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor e = prefs.edit();
        e.putLong(name, value);
        e.commit();
    }

    /**
     * Saves the variable into Preferences.
     * @param context context
     * @param name name of preference
     * @param value value
     */
    public static void saveBooleanToPreferences(final Context context, final String name,
                                                final boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean(name, value);
        e.commit();
    }

    /**
     * Saves the variable into Preferences.
     * @param context context
     * @param name name of preference
     * @param value value
     */
    public static void saveStringToPreferences(final Context context, final String name,
                                                final String value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor e = prefs.edit();
        e.putString(name, value);
        e.commit();
    }

    /**
     * Launches the Edit verb activity.
     * @param context Context
     * @param id long
     */
    public static void launchEditorActivity(final Context context, final long id){
        // Create new intent to go to {@link EditorActivity}
        Intent intent = new Intent(context, EditorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putLong(VERB_ID, id);
        intent.putExtras(bundle);

        // Launch the {@link EditorActivity} to display the data for the current pet.
        context.startActivity(intent);
    }

    public static void launchDetailsActivity(final Context context, final long id,
                                             final String verb, final boolean demoMode) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putLong(VERB_ID, id);
        bundle.putString(VERB_NAME, verb);
        bundle.putBoolean(DEMO_MODE, demoMode);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    /**
     * Launches Help Activity.
     * @param context context
     */
    public static void launchHelpActivity(final Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Launches Settings Activity.
     * @param context context
     */
    public static void launchSettingsActivity(final Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                SettingsActivity.GeneralPreferenceFragment.class.getName() );
        intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
        intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT_TITLE, R.string.action_settings);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Launches Search Activity.
     * @param context context
     */
    public static void launchSearchActivity(final Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Helper class to handle deprecated method.
     * Source: http://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
     * @param html html string
     * @return Spanned
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /**
     * Returns the value of show definitions from preferences.
     * @param context context
     * @return boolean or default(true)
     */
    public static boolean getPreferenceShowDefinitions(final Context context) {
        String key = context.getString(R.string.pref_show_definitions_switch);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, true);
    }

    /**
     * Returns the translation language from preferences.
     * @param context Context
     * @return code of launguage (default NONE)
     */
    public static String getPreferenceTranslationLanguage(final Context context) {
        String key = context.getString(R.string.pref_translation_language);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String lang =  prefs.getString(key, "None");
        switch (lang){
            case "":
            case "None":
            default:
                return NONE;
            case "fr_FR":
                return FRENCH;
            case "es_ES":
                return SPANISH;
        }
    }

    /**
     * Returns the favorites mode from preferences.
     * @param context context
     * @return CARD or LIST
     */
    public static String getPreferenceFavoritesMode(final Context context) {
        String key = context.getString(R.string.pref_favorite_mode_list);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, LIST);
    }

    /**
     * Set the correct translation or hide the view.
     * @param context Context
     * @param textView view
     * @param verb Verb
     */
    public static void setTranslation(final Context context, TextView textView, Verb verb) {
        switch (getPreferenceTranslationLanguage(context)) {
            case NONE:
                textView.setVisibility(View.GONE);
                break;
            case FRENCH:
                textView.setText(verb.getTranslationFR());
                break;
            case SPANISH:
                textView.setText(verb.getTranslationES());
                break;
        }
    }

    /**
     * Text we want to speak.
     * @param text String
     */
    public static void speak(final Context context, TextToSpeech tts, String text){
        // Use the current media player volume
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int volume = am.getStreamVolume(am.STREAM_MUSIC);
        am.setStreamVolume(am.STREAM_MUSIC, volume, 0);

        // Speak
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    /**
     * Generate all table verb columns.
     * @return String[]
     */
    public static String[] allVerbColumns(){
        String[] columns = {
                VerbEntry.COLUMN_INFINITIVE,
                VerbEntry.COLUMN_SIMPLE_PAST,
                VerbEntry.COLUMN_PAST_PARTICIPLE,
                VerbEntry.COLUMN_ID,
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
                VerbEntry.COLUMN_SOURCE,
                VerbEntry.COLUMN_NOTES,
                VerbEntry.COLUMN_TRANSLATION_ES,
                VerbEntry.COLUMN_TRANSLATION_FR };
        return columns;
    }

    /**
     * Create a Verb from the current cursor position.
     * Note: columns must exist.
     * @param cursor Cursor
     * @return Verb
     */
    public static Verb verbFromCursor(final Cursor cursor){
        return new Verb(cursor.getLong(cursor.getColumnIndex(VerbEntry.COLUMN_ID)),
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
                cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_SOURCE)),
                cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_NOTES)),
                cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_TRANSLATION_ES)),
                cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_TRANSLATION_FR))  );
    }


    /**
     * Initializes and show the AdMob banner.
     * Needs to be called in onCreate of the activity.
     * https://firebase.google.com/docs/admob/android/quick-start
     * @param activity activity
     * @param listener LogAdListener
     */
    public static AdView createAdMobBanner(final AppCompatActivity activity,
                                           final LogAdListener listener) {
        final String adMobAppId = activity.getString(R.string.admob_app_id);
        // Initialize AdMob
        MobileAds.initialize(activity.getApplicationContext(), adMobAppId);

        AdView adView = (AdView) activity.findViewById(R.id.adView);
        // Set listener
        // https://firebase.google.com/docs/admob/android/ad-events
        adView.setAdListener(listener);

        // Load an ad into the AdMob banner view.
        AdRequest adRequest;
        if (USE_TEST_ADS) {
            adRequest = new AdRequest.Builder()
                    // Use AdRequest.Builder.addTestDevice() to get test ads on this device.
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    // SLONE SLONE Pilot_S5004 (Android 6.0, API 23)
                    .addTestDevice("4DF5D2AB04EBFA06FB2656A06D2C0EE3")
                    .build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }
        adView.loadAd(adRequest);

        return adView;
    }

    /**
     * Logs a Firebase Analytics select content event.
     * https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event#SELECT_CONTENT
     * @param analytics FirebaseAnalytics
     * @param id id
     * @param name name
     * @param type type
     */
    public static void firebaseAnalyticsLogEventSelectContent(final FirebaseAnalytics analytics,
                                                              final String id, final String name,
                                                              final String type) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Logs a Firebase Analytics search event.
     * https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event#SEARCH
     * @param analytics FirebaseAnalytics
     * @param search string to search
     */
    public static void firebaseAnalyticsLogEventSearch(final FirebaseAnalytics analytics,
                                                       final String search) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, search);
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    /**
     * Logs a Firebase Analytics view search results event.
     * https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event#VIEW_SEARCH_RESULTS
     * @param analytics FirebaseAnalytics
     * @param search string to search
     */
    public static void firebaseAnalyticsLogEventViewSearchResults(final FirebaseAnalytics analytics,
                                                                  final String search) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, search);
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, bundle);
    }

    /**
     * Logs a Firebase Analytics view item event.
     * https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event#VIEW_ITEM
     * @param analytics FirebaseAnalytics
     * @param id id
     * @param name name
     * @param category category
     */
    public static void firebaseAnalyticsLogEventViewItem(final FirebaseAnalytics analytics,
                                                         final String id, final String name,
                                                         final String category) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
}
