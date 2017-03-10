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

import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.Verb;

import java.util.List;

import static com.xengar.android.englishverbs.utils.Constants.LIST;

/**
 * VerbAdapter
 */
public class VerbAdapter extends RecyclerView.Adapter<VerbHolder> {

    private final List<Verb> verbs;
    private String layoutType = LIST;
    private TextToSpeech tts;

    public VerbAdapter(final List<Verb> verbs, final String layoutType, final TextToSpeech tts) {
        this.verbs = verbs;
        this.layoutType = layoutType;
        this.tts = tts;
    }

    @Override
    public VerbHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(
                (layoutType.contentEquals(LIST))? R.layout.verbs_list_item
                        : R.layout.verbs_card_item, parent, false);
        return new VerbHolder(v);
    }

    @Override
    public void onBindViewHolder(VerbHolder holder, int position) {
        holder.bindVerb(verbs.get(position), layoutType, tts);
    }

    @Override
    public int getItemCount() {
        return (verbs != null) ? verbs.size() : 0;
    }

}
