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


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.adapters.VerbAdapter;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.sync.FetchVerbs;
import com.xengar.android.englishverbs.utils.CustomErrorView;
import com.xengar.android.englishverbs.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.xengar.android.englishverbs.utils.Constants.ALPHABET;
import static com.xengar.android.englishverbs.utils.Constants.BOTH;
import static com.xengar.android.englishverbs.utils.Constants.ITEM_TYPE;
import static com.xengar.android.englishverbs.utils.Constants.LIST;
import static com.xengar.android.englishverbs.utils.Constants.LOG;
import static com.xengar.android.englishverbs.utils.Constants.SORT_TYPE;
import static com.xengar.android.englishverbs.utils.Constants.VERB_TYPE;

/**
 * UniversalFragment
 */
public class UniversalFragment extends Fragment {

    private static final String TAG = UniversalFragment.class.getSimpleName();

    private CustomErrorView mCustomErrorView;
    private RecyclerView mRecyclerView;
    private CircularProgressBar progressBar;
    private VerbAdapter mAdapter;
    private List<Verb> mVerbs;
    private String verbsType = BOTH; // regular, irregular, both
    private String sortTYpe = ALPHABET; // alphabet, color, verbs_ed
    private String itemType = LIST; // card, list
    private TextToSpeech tts;

    public UniversalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            verbsType = getArguments().getString(VERB_TYPE, BOTH);
            itemType = getArguments().getString(ITEM_TYPE, LIST);
            sortTYpe = getArguments().getString(SORT_TYPE, ALPHABET);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_universal, container, false);

        mCustomErrorView = (CustomErrorView) view.findViewById(R.id.error);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);
        mVerbs = new ArrayList<>();

        // initialize Speaker
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        if (LOG) {
                            Log.e("TTS", "This Language is not supported");
                        }
                    }
                } else {
                    if (LOG) {
                        Log.e("TTS", "Initilization Failed!");
                    }
                }
            }
        });
        mAdapter = new VerbAdapter(mVerbs, itemType, tts);

        return view;
    }

    public String getVerbsType(){
        return verbsType;
    }

    public String getSortType(){
        return sortTYpe;
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        if (!FragmentUtils.checkInternetConnection(getActivity())) {
            if (LOG) {
                Log.e(TAG, "Network is not available");
            }
            onLoadFailed(new Throwable(getString(R.string.network_not_available_message)));
            return;
        }*/

        mVerbs.clear();
        fillVerbs();
    }

    private void onLoadFailed(Throwable t) {
        mCustomErrorView.setError(t);
        mCustomErrorView.setVisibility(View.VISIBLE);
        FragmentUtils.updateProgressBar(progressBar, false);
    }

    private void fillVerbs(){
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        FragmentUtils.updateProgressBar(progressBar, true);

        FetchVerbs fetch =
                new FetchVerbs(verbsType, sortTYpe, mAdapter, getActivity().getContentResolver(),
                        mVerbs, progressBar);
        fetch.execute();
    }

}
