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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import static com.xengar.android.englishverbs.utils.Constants.IRREGULAR;

/**
 * IrregularFragment for Irregular verbs
 */
public class IrregularFragment extends Fragment {

    private CustomErrorView mCustomErrorView;
    private RecyclerView mRecyclerView;
    private CircularProgressBar progressBar;
    private VerbAdapter mAdapter;
    private List<Verb> mVerbs;


    public IrregularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_universal, container, false);

        mCustomErrorView = (CustomErrorView) view.findViewById(R.id.error);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);
        mVerbs = new ArrayList<>();
        mAdapter = new VerbAdapter(mVerbs);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVerbs.clear();
        fillVerbs();
    }

    private void fillVerbs(){
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        FragmentUtils.updateProgressBar(progressBar, true);

        FetchVerbs fetch =
                new FetchVerbs(IRREGULAR, mAdapter, getActivity().getContentResolver(), mVerbs,
                        progressBar);
        fetch.execute();
    }

}
