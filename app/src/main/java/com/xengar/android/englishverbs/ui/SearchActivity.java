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

import android.app.SearchManager;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.adapters.VerbHolder;
import com.xengar.android.englishverbs.data.Verb;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;
import com.xengar.android.englishverbs.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.xengar.android.englishverbs.utils.Constants.LIST;
import static com.xengar.android.englishverbs.utils.Constants.LOG;

/**
 * SearchActivity
 */
public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private List<Verb> mVerbs; // all verbs in the database
    private SearchView mSearchView;
    private SearchAdapter mAdapter;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        setupActionBar();
        setupSearchView();

        mVerbs = new ArrayList<>();
        mAdapter = new SearchAdapter();
        // Get all verbs
        FetchVerbs fetch = new FetchVerbs(getContentResolver(), mVerbs, mAdapter.getVerbs());
        fetch.execute();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        // initialize Speaker
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
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
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        mSearchView.setQueryHint(getString(R.string.action_search));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 1) {
                    mAdapter.getFilter().filter(newText);
                    return true;
                }

                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return true;
            }
        });
    }

    /**
     * SearchAdapter
     */
    class SearchAdapter extends RecyclerView.Adapter<VerbHolder> implements Filterable {

        private final List<Verb> mMultiSearchItems = new ArrayList<>();

        public SearchAdapter() {
        }

        public List<Verb> getVerbs(){ return mMultiSearchItems; }

        @Override
        public VerbHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.verbs_list_item, parent, false);
            return new VerbHolder(v);
        }

        @Override
        public void onBindViewHolder(VerbHolder holder, int position) {
            Verb item = mMultiSearchItems.get(position);
            holder.bindVerb(item, LIST, tts);
        }

        @Override
        public int getItemCount() {
            return mMultiSearchItems.size();
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(final CharSequence charSequence) {
                    final FilterResults results = new FilterResults();

                    // Iterate though the list and get the verbs that contain the string
                    mMultiSearchItems.clear();
                    for(Verb verb : mVerbs){
                        if (verb.getInfinitive().contains(charSequence)
                                || verb.getSimplePast().contains(charSequence)
                                || verb.getPastParticiple().contains(charSequence)){
                            mMultiSearchItems.add(verb);
                        }
                    }
                    results.values = mMultiSearchItems;
                    results.count = mMultiSearchItems.size();

                    //ActivityUtils.firebaseAnalyticsLogEventSearch(
                    //        mFirebaseAnalytics, charSequence.toString());
                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence,
                                              FilterResults filterResults) {
                    notifyDataSetChanged();
                }
            };
        }
    }

    /**
     * FetchVerbs from the database.
     */
    public class FetchVerbs extends AsyncTask<Void, Void, ArrayList<Verb>> {

        private final String TAG = FetchVerbs.class.getSimpleName();
        private final ContentResolver contentResolver;
        private final List<Verb> verbs; // list of all verbs
        private final List<Verb> search; // list of search result verbs

        // Constructor
        public FetchVerbs(final ContentResolver contentResolver, final List<Verb> verbs,
                          final List<Verb> search) {
            this.contentResolver = contentResolver;
            this.verbs = verbs;
            this.search = search;
        }

        @Override
        protected ArrayList<Verb> doInBackground(Void... voids) {
            // Define a projection that specifies the columns from the table we care about.
            String[] columns = ActivityUtils.allVerbColumns();
            String sortOrder = VerbEntry.COLUMN_INFINITIVE + " ASC";;
            Cursor cursor = contentResolver.query(
                    VerbEntry.CONTENT_URI, columns, null, null, sortOrder);

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
                search.addAll(list); // begin search with all verbs in screen.
            }
        }
    }
}
