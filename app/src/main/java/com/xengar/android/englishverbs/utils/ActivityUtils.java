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
package com.xengar.android.englishverbs.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract;
import com.xengar.android.englishverbs.ui.DetailsActivity;
import com.xengar.android.englishverbs.ui.EditorActivity;
import com.xengar.android.englishverbs.ui.HelpActivity;
import com.xengar.android.englishverbs.ui.SettingsActivity;

import static com.xengar.android.englishverbs.utils.Constants.FRENCH;
import static com.xengar.android.englishverbs.utils.Constants.NONE;
import static com.xengar.android.englishverbs.utils.Constants.SHARED_PREF_NAME;
import static com.xengar.android.englishverbs.utils.Constants.SPANISH;
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
     * @param context
     * @param id
     */
    public static void launchEditorActivity(final Context context, final long id){
        // Create new intent to go to {@link EditorActivity}
        Intent intent = new Intent(context, EditorActivity.class);

        // Form the content URI that represents the specific verb that was clicked on,
        // by appending the "id" (passed as input to this method) onto the
        // {@link VerbEntry#CONTENT_URI}.
        // For example, the URI would be "content://com.xengar.android.englishverbs/verbs/2"
        // if the verb with ID 2 was clicked on.
        Uri currentVerbUri = ContentUris.withAppendedId(VerbContract.VerbEntry.CONTENT_URI, id);

        // Set the URI on the data field of the intent
        intent.setData(currentVerbUri);

        // Launch the {@link EditorActivity} to display the data for the current pet.
        context.startActivity(intent);
    }

    public static void launchDetailsActivity(final Context context, final long id, final String verb) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putLong(VERB_ID, id);
        bundle.putString(VERB_NAME, verb);
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
    public static void speak(TextToSpeech tts, String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
