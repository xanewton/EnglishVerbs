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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.utils.ActivityUtils;

import java.util.List;
import java.util.Locale;

/**
 * VerbAdapter
 */
public class VerbAdapter extends RecyclerView.Adapter<VerbAdapter.VerbHolder> {

    private final List<Verb> mVerbs;

    public VerbAdapter(List<Verb> verbs) {
        mVerbs = verbs;
    }

    @Override
    public VerbHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.verbs_list_item, parent, false);
        return new VerbHolder(v);
    }

    @Override
    public void onBindViewHolder(VerbHolder holder, int position) {
        Verb movie = mVerbs.get(position);
        holder.bindMovie(movie);
    }

    @Override
    public int getItemCount() {
        return (mVerbs != null) ? mVerbs.size() : 0;
    }


    /**
     * VerbHolder
     */
    public class VerbHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final Context mContext;
        private Verb mVerb;
        final TextView infinitiveTextView;
        final TextView simplePastTextView;
        final TextView pastParticipleTextView;
        final TextView definitionTextView;
        final TextView typeTextView;
        final TextView scoreTextView;
        final TextView translationView;


        public VerbHolder(View view) {
            super(view);
            infinitiveTextView = (TextView) view.findViewById(R.id.infinitive);
            simplePastTextView = (TextView) view.findViewById(R.id.simple_past);
            pastParticipleTextView = (TextView) view.findViewById(R.id.past_participle);
            definitionTextView = (TextView) view.findViewById(R.id.definition);
            typeTextView = (TextView) view.findViewById(R.id.type);
            scoreTextView = (TextView) view.findViewById(R.id.score);
            translationView = (TextView) view.findViewById(R.id.translation);
            mContext = view.getContext();
            view.setOnClickListener(this);
        }

        void bindMovie(Verb verb) {
            mVerb = verb;
            // Set values
            infinitiveTextView.setText(verb.getInfinitive());
            simplePastTextView.setText(verb.getSimplePast());
            pastParticipleTextView.setText(verb.getPastParticiple());
            infinitiveTextView.setTextColor(verb.getColor());
            simplePastTextView.setTextColor(verb.getColor());
            pastParticipleTextView.setTextColor(verb.getColor());
            definitionTextView.setText(verb.getDefinition());
            if(!ActivityUtils.getPreferenceShowDefinitions(mContext)) {
                definitionTextView.setVisibility(View.GONE);
            }
            typeTextView.setText((verb.getRegular()  == 0)? "R" : "I");
            scoreTextView.setText(String.format(Locale.ENGLISH, "%d", verb.getScore()));
            ActivityUtils.setTranslation(mContext, translationView, verb);
        }

        // Handles the item click.
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            // Check if an item was deleted, but the user clicked it before the UI removed it
            if (position != RecyclerView.NO_POSITION) {
                // TODO: Change id into something bigger, like string
                //ActivityUtils.launchEditorActivity(mContext, mVerb.getId());
                ActivityUtils.lauchDetailsActivity(mContext, mVerb.getId(), mVerb.getInfinitive());
            }
        }

    }
}
