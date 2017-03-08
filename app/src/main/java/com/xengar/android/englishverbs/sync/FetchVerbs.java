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
package com.xengar.android.englishverbs.sync;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.xengar.android.englishverbs.adapters.VerbAdapter;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;
import com.xengar.android.englishverbs.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.xengar.android.englishverbs.utils.Constants.BOTH;
import static com.xengar.android.englishverbs.utils.Constants.IRREGULAR;
import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.REGULAR;

/**
 * FetchVerbs from the database.
 */
public class FetchVerbs extends AsyncTask<Void, Void, ArrayList<Verb>> {

    private final String TAG = FetchVerbs.class.getSimpleName();
    private final String type; // Verb type (regular, irregular, both)
    private final ContentResolver contentResolver;
    private final VerbAdapter adapter;
    private final List<Verb> verbs;
    private final CircularProgressBar progressBar;

    // Constructor
    public FetchVerbs(final String type, final VerbAdapter adapter,
                      final ContentResolver contentResolver, final List<Verb> verbs,
                      final CircularProgressBar progressBar) {
        this.type = type;
        this.adapter = adapter;
        this.contentResolver = contentResolver;
        this.verbs = verbs;
        this.progressBar = progressBar;
    }

    @Override
    protected ArrayList<Verb> doInBackground(Void... voids) {
        ArrayList<Verb> verbs = new ArrayList<>();
        // Define a projection that specifies the columns from the table we care about.
        String[] columns = {
                VerbEntry._ID,
                VerbEntry.COLUMN_INFINITIVE,
                VerbEntry.COLUMN_SIMPLE_PAST,
                VerbEntry.COLUMN_PAST_PARTICIPLE,
                VerbEntry.COLUMN_DEFINITION,
                VerbEntry.COLUMN_REGULAR,
                VerbEntry.COLUMN_SCORE };

        Cursor cursor;
        switch (type){
            case BOTH:
            default:
                cursor = contentResolver.query(VerbEntry.CONTENT_URI, columns, null, null, null);
                break;

            case REGULAR:
                cursor = contentResolver.query(VerbEntry.CONTENT_REGULARS_URI, columns, null, null, null);
                break;

            case IRREGULAR:
                cursor = contentResolver.query(VerbEntry.CONTENT_IRREGULARS_URI, columns, null, null, null);
                break;
        }

        if (cursor != null && cursor.getCount() != 0) {
            Verb verb;
            while (cursor.moveToNext()) {
                verb = new Verb(cursor.getLong(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4),
                        "", "", "", "", "", "",
                        0, cursor.getInt(5), 0, cursor.getInt(6), "", "", "");
                verbs.add(verb);
            }
        } else {
            if (LOG) {
                Log.d(TAG, "Cursor is empty");
            }
        }
        if (cursor != null)
            cursor.close();
        return verbs;
    }

    @Override
    protected void onPostExecute(ArrayList<Verb> list) {
        super.onPostExecute(list);
        if (list != null) {
            verbs.addAll(list);
            adapter.notifyDataSetChanged();
        }
        FragmentUtils.updateProgressBar(progressBar, false);
    }
}
