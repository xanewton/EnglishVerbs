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
package com.xengar.android.englishverbs.adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.utils.ActivityUtils;

import java.util.Locale;

import static com.xengar.android.englishverbs.utils.Constants.CARD;
import static com.xengar.android.englishverbs.utils.Constants.LIST;

/**
 * VerbHolder. Represents an item view in screen
 */
public class VerbHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final Context context;
    private String layoutType = LIST;
    private TextToSpeech tts;
    private Verb verb;
    private final TextView infinitive, simplePast, pastParticiple;
    private final TextView definition;
    private final TextView type;
    private final TextView score;
    private final TextView translation;
    private TextView pInfinitive, pSimplePast, pPastParticiple;
    private TextView sample1, sample2, sample3;


    public VerbHolder(View view) {
        super(view);
        infinitive = (TextView) view.findViewById(R.id.infinitive);
        simplePast = (TextView) view.findViewById(R.id.simple_past);
        pastParticiple = (TextView) view.findViewById(R.id.past_participle);
        definition = (TextView) view.findViewById(R.id.definition);
        type = (TextView) view.findViewById(R.id.type);
        score = (TextView) view.findViewById(R.id.score);
        translation = (TextView) view.findViewById(R.id.translation);

        // Card items
        pInfinitive = (TextView) view.findViewById(R.id.phonetic_infinitive);
        pSimplePast = (TextView) view.findViewById(R.id.phonetic_simple_past);
        pPastParticiple = (TextView) view.findViewById(R.id.phonetic_past_participle);
        sample1 = (TextView) view.findViewById(R.id.sample1);
        sample2 = (TextView) view.findViewById(R.id.sample2);
        sample3 = (TextView) view.findViewById(R.id.sample3);
        // define click listeners
        LinearLayout header = (LinearLayout) view.findViewById(R.id.play_infinitive);
        if (header != null) {
            header.setOnClickListener(this);
        }
        header = (LinearLayout) view.findViewById(R.id.play_simple_past);
        if (header != null) {
            header.setOnClickListener(this);
        }
        header = (LinearLayout) view.findViewById(R.id.play_past_participle);
        if (header != null) {
            header.setOnClickListener(this);
        }

        context = view.getContext();
        view.setOnClickListener(this);
    }

    /**
     * Fills the view with the verb object.
     * @param verb Verb
     * @param layoutType LIST, CARD
     * @param tts TextToSpeech
     */
    public void bindVerb(Verb verb, String layoutType, TextToSpeech tts) {
        this.verb = verb;
        this.layoutType = layoutType;
        this.tts = tts;

        // Set values
        infinitive.setText(verb.getInfinitive());
        simplePast.setText(verb.getSimplePast());
        pastParticiple.setText(verb.getPastParticiple());
        infinitive.setTextColor(verb.getColor());
        simplePast.setTextColor(verb.getColor());
        pastParticiple.setTextColor(verb.getColor());
        definition.setText(verb.getDefinition());
        if(!ActivityUtils.getPreferenceShowDefinitions(context)) {
            definition.setVisibility(View.GONE);
        }

        if (this.layoutType.contentEquals(LIST)) {
            type.setText((verb.getRegular() == 0) ? "R" : "I");
            score.setText(String.format(Locale.ENGLISH, "%d", verb.getScore()));
        } else if(this.layoutType.contentEquals(CARD)) {
            pInfinitive.setText(verb.getPhoneticInfinitive());
            pSimplePast.setText(verb.getPhoneticSimplePast());
            pPastParticiple.setText(verb.getPhoneticPastParticiple());
            sample1.setText(verb.getSample1());
            sample2.setText(verb.getSample2());
            sample3.setText(verb.getSample3());
        }
        ActivityUtils.setTranslation(context, translation, verb);

        // TODO: Logic for card
    }

    // Handles the item click.
    @Override
    public void onClick(View view) {
        int position = getAdapterPosition(); // gets item position
        // Check if an item was deleted, but the user clicked it before the UI removed it
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        // Play the sounds
        switch(view.getId()){
            case R.id.play_infinitive:
                ActivityUtils.speak(context, tts, verb.getInfinitive());
                Toast.makeText(context, verb.getInfinitive(),Toast.LENGTH_SHORT).show();
                break;

            case R.id.play_simple_past:
                ActivityUtils.speak(context, tts, verb.getSimplePast());
                Toast.makeText(context, verb.getSimplePast(),Toast.LENGTH_SHORT).show();
                break;

            case R.id.play_past_participle:
                ActivityUtils.speak(context, tts, verb.getPastParticiple());
                Toast.makeText(context, verb.getPastParticiple(),Toast.LENGTH_SHORT).show();
                break;

            default:
                ActivityUtils.launchDetailsActivity(
                        context, verb.getId(), verb.getInfinitive(), false);
                break;
        }
    }

}
