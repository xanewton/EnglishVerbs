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
package com.xengar.android.englishverbs.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

import static com.xengar.android.englishverbs.R.id.infinitive;

/**
 * {@link VerbCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class VerbCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link VerbCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public VerbCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in verbs_list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.verbs_list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView infinitiveTextView = (TextView) view.findViewById(infinitive);
        TextView simplePastTextView = (TextView) view.findViewById(R.id.simple_past);
        TextView pastParticipleTextView = (TextView) view.findViewById(R.id.past_participle);
        TextView definitionTextView = (TextView) view.findViewById(R.id.definition);
        TextView typeTextView = (TextView) view.findViewById(R.id.type);
        TextView scoreTextView = (TextView) view.findViewById(R.id.score);

        // Read the verb attributes from the Cursor for the current verb
        String infinitive = cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_INFINITIVE));
        String simplePast = cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SIMPLE_PAST));
        String pastParticiple = cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PAST_PARTICIPLE));
        String definition = cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_DEFINITION));
        Integer regular = cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_REGULAR));
        String type = (regular == 1)? "R" : "I";
        Integer score = cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_SCORE));

        // Set values
        infinitiveTextView.setText(infinitive);
        simplePastTextView.setText(simplePast);
        pastParticipleTextView.setText(pastParticiple);
        definitionTextView.setText(definition);
        typeTextView.setText(type);
        scoreTextView.setText(score.toString());
    }
}
