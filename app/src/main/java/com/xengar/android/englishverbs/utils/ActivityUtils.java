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

import com.xengar.android.englishverbs.data.VerbContract;
import com.xengar.android.englishverbs.ui.EditorActivity;

import static com.xengar.android.englishverbs.utils.Constants.SHARED_PREF_NAME;


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
}