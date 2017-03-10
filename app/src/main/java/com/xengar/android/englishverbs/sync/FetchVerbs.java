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
import com.xengar.android.englishverbs.utils.ActivityUtils;
import com.xengar.android.englishverbs.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.xengar.android.englishverbs.utils.Constants.ALPHABET;
import static com.xengar.android.englishverbs.utils.Constants.BOTH;
import static com.xengar.android.englishverbs.utils.Constants.COLOR;
import static com.xengar.android.englishverbs.utils.Constants.FAVORITES;
import static com.xengar.android.englishverbs.utils.Constants.IRREGULAR;
import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.REGULAR;
import static com.xengar.android.englishverbs.utils.Constants.VERBS_ED;

/**
 * FetchVerbs from the database.
 */
public class FetchVerbs extends AsyncTask<Void, Void, ArrayList<Verb>> {

    private final String TAG = FetchVerbs.class.getSimpleName();
    private final String type; // Verb type (regular, irregular, both)
    private final String sort; // Sort order (alpabet, color, verbs_ed)
    private final ContentResolver contentResolver;
    private final VerbAdapter adapter;
    private final List<Verb> verbs;
    private final CircularProgressBar progressBar;

    // Constructor
    public FetchVerbs(final String type, final String sort, final VerbAdapter adapter,
                      final ContentResolver contentResolver, final List<Verb> verbs,
                      final CircularProgressBar progressBar) {
        this.type = type;
        this.sort = sort;
        this.adapter = adapter;
        this.contentResolver = contentResolver;
        this.verbs = verbs;
        this.progressBar = progressBar;
    }

    @Override
    protected ArrayList<Verb> doInBackground(Void... voids) {
        // Define a projection that specifies the columns from the table we care about.
        String[] columns = ActivityUtils.allVerbColumns();

        String sortOrder = null;
        switch (sort){
            case ALPHABET:
            default:
                sortOrder = VerbEntry.COLUMN_INFINITIVE + " ASC";
                break;

            case COLOR:
                sortOrder = VerbEntry.COLUMN_COLOR + " DESC, " + VerbEntry.COLUMN_INFINITIVE + " ASC";
                break;

            case VERBS_ED:
                sortOrder = VerbEntry.COLUMN_REGULAR + " ASC, " + VerbEntry.COLUMN_INFINITIVE + " ASC";
                break;
        }

        Cursor cursor;
        switch (type){
            case BOTH:
            default:
                cursor = contentResolver.query(VerbEntry.CONTENT_URI, columns, null, null, sortOrder);
                break;

            case REGULAR:
                cursor = contentResolver.query(VerbEntry.CONTENT_REGULARS_URI, columns, null, null, sortOrder);
                break;

            case IRREGULAR:
                cursor = contentResolver.query(VerbEntry.CONTENT_IRREGULARS_URI, columns, null, null, sortOrder);
                break;

            case FAVORITES:
                cursor = contentResolver.query(VerbEntry.CONTENT_FAVORITE_VERBS_URI, columns, null, null, sortOrder);
                break;
        }

        ArrayList<Verb> verbs = new ArrayList<>();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                verbs.add(ActivityUtils.verbFromCursor(cursor));
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
