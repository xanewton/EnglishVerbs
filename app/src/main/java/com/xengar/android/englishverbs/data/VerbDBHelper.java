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
package com.xengar.android.englishverbs.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;
import static com.xengar.android.englishverbs.data.VerbContract.VerbEntry.S_IRREGULAR;
import static com.xengar.android.englishverbs.data.VerbContract.VerbEntry.S_REGULAR;
import static com.xengar.android.englishverbs.data.VerbContract.VerbEntry.S_TOP_25;
import static com.xengar.android.englishverbs.data.VerbContract.VerbEntry.S_TOP_50;

/**
 * Database helper for Verbs app. Manages database creation and version management.
 */

public class VerbDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = VerbDBHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "verbs.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    private Context context;


    /**
     * Constructs a new instance of {@link VerbDBHelper}.
     *
     * @param context of the app
     */
    public VerbDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createSchemaVersion1(db);
        insertVerbs(db);
        insertFavorites(db);
    }

    /**
     * Creates the schema for version 1.
     * NOTE: If the version changes, add code for the upgrade also.
     * @param db SQLiteDatabase
     */
    private void createSchemaVersion1(SQLiteDatabase db){
        // Create a String that contains the SQL statement to create the verbs table
        String query =  "CREATE TABLE " + VerbEntry.VERBS_TBL + " ("
                + VerbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VerbEntry.COLUMN_ID + " INTEGER NOT NULL, "
                + VerbEntry.COLUMN_INFINITIVE + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_SIMPLE_PAST + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_PAST_PARTICIPLE + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_PHONETIC_INFINITIVE + " TEXT, "
                + VerbEntry.COLUMN_PHONETIC_SIMPLE_PAST + " TEXT, "
                + VerbEntry.COLUMN_PHONETIC_PAST_PARTICIPLE + " TEXT, "
                + VerbEntry.COLUMN_SAMPLE_1 + " TEXT, "
                + VerbEntry.COLUMN_SAMPLE_2 + " TEXT, "
                + VerbEntry.COLUMN_SAMPLE_3 + " TEXT, "
                + VerbEntry.COLUMN_COMMON + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_REGULAR + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_COLOR + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_SCORE + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_DEFINITION + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_TRANSLATION_ES + " TEXT, "
                + VerbEntry.COLUMN_TRANSLATION_FR + " TEXT, "
                + VerbEntry.COLUMN_NOTES + " TEXT);";

        // Execute the SQL statement
        db.execSQL(query);

        query = "CREATE TABLE " + VerbEntry.FAVORITES_TBL + " ("
                + VerbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VerbEntry.COLUMN_ID + " INTEGER NOT NULL); ";
        db.execSQL(query);
    }


    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        if (oldVersion > newVersion) {
            // This should not happen, version numbers should increment. Start clean.
            query = "DROP TABLE IF EXISTS " +  VerbEntry.VERBS_TBL;
            db.execSQL(query);
            query = "DROP TABLE IF EXISTS " +  VerbEntry.FAVORITES_TBL;
            db.execSQL(query);
        }

        // Update version by version using a method for the update. See sample below.
        switch(oldVersion) {
            /* Sample
            case 3:
                switch (oldVersion){
                    case 1:
                        updateSchemaToVersion2(db);
                    case 2:
                        updateSchemaToVersion3(db);
                        break;
                }
                break*/
            default:
                break;
        }
    }

    /**
     * Insert the 5 most common verbs.
     * @param db
     */
    private void insertFavorites(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < favorites.length; i++) {
            values.put("_id", i);
            values.put(VerbEntry.COLUMN_ID, favorites[i][0]);
            db.insertWithOnConflict(VerbEntry.FAVORITES_TBL, null, values, CONFLICT_REPLACE );
        }
    }

    // List of pre-loaded favorite verbs.
    public static final String[][] favorites = {
            {"0"}, // be
            {"1"}, // have
            {"2"}, // do
            {"3"}, // say
            {"4"}  // get
    };

    /**
     * Insert default verbs.
     * @param db
     */
    private void insertVerbs(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        final String DEFAULT_COLOR = "" + ContextCompat.getColor(context, R.color.colorBlack);
        final String DEFAULT_SCORE = "0";
        for (int i = 0; i < verbs.length; i++) {
            values.put("_id", i);
            values.put(VerbEntry.COLUMN_ID, verbs[i][0]);
            values.put(VerbEntry.COLUMN_INFINITIVE, verbs[i][1]);
            values.put(VerbEntry.COLUMN_SIMPLE_PAST, verbs[i][2]);
            values.put(VerbEntry.COLUMN_PAST_PARTICIPLE, verbs[i][3]);
            values.put(VerbEntry.COLUMN_PHONETIC_INFINITIVE, verbs[i][4]);
            values.put(VerbEntry.COLUMN_PHONETIC_SIMPLE_PAST, verbs[i][5]);
            values.put(VerbEntry.COLUMN_PHONETIC_PAST_PARTICIPLE, verbs[i][6]);
            values.put(VerbEntry.COLUMN_SAMPLE_1, verbs[i][7]);
            values.put(VerbEntry.COLUMN_SAMPLE_2, verbs[i][8]);
            values.put(VerbEntry.COLUMN_SAMPLE_3, verbs[i][9]);
            values.put(VerbEntry.COLUMN_COMMON, verbs[i][10]);
            values.put(VerbEntry.COLUMN_REGULAR, verbs[i][11]);
            values.put(VerbEntry.COLUMN_COLOR, DEFAULT_COLOR);
            values.put(VerbEntry.COLUMN_SCORE, DEFAULT_SCORE);
            values.put(VerbEntry.COLUMN_DEFINITION, verbs[i][12]);
            values.put(VerbEntry.COLUMN_TRANSLATION_ES, verbs[i][13]);
            values.put(VerbEntry.COLUMN_TRANSLATION_FR, verbs[i][14]);
            values.put(VerbEntry.COLUMN_NOTES, verbs[i][15]);
            db.insertWithOnConflict(VerbEntry.VERBS_TBL, null, values, CONFLICT_REPLACE );
        }
    }

    // List of pre-loaded verbs.
    public static final String[][] verbs = {
            // TOP25
            {"0", "be", "was, were", "been", "/bi/", "/wəz/ /wə(r)/", "/biːn/",
                "John is a man.", "My birthday was last Thursday.", "I haven't been there for several years.",
                S_TOP_25, S_IRREGULAR,
                "to exist; to take place, occur; an auxiliary verb",
                "ser, estar", "être", ""},
            {"1", "have", "had", "had", "/hæv/", "/həd/", "/həd/",
                "The Simpsons have three children.", "The boy saw what a mistake he had made.", "You have had a remarkable life.",
                S_TOP_25, S_IRREGULAR,
                "to posses; to do; an auxiliary verb",
                "tener, padecer", "avoir", ""},
            {"2", "do", "did", "done", "/du:/", "/dɪd/", "/dʌn/",
                "I was trying to do some work.", "We did not know Jamie had a knife.", "You have done nothing all morning!",
                S_TOP_25, S_IRREGULAR,
                "to perform or carry out; an auxiliary verb",
                "hacer", "faire", ""},
            {"3", "say", "said", "said", "/seɪ/", "/sed/", "/sed/",
                "I like the way you say thank you.", "\"Let's do it,\" she finally said unceremoniously.", "Martha, will you say the Pledge of Allegiance?",
                S_TOP_25, S_IRREGULAR,
                "utter words so as to convey information, an opinion, a feeling or intention, or an instruction.",
                "decir", "dire", ""},
            {"4", "get", "got", "got, gotten", "/get/", "/gɒt/", "/gɒt/, /'gɒtn/",
                "He gets very cross when you ask him personal questions.", "He's got two sisters and a brother.", "He's just gotten a new job.",
                S_TOP_25, S_IRREGULAR,
                "come to have or hold (something); receive. succeed in attaining, achieving, or experiencing; obtain.", "obtener", "obtenir", ""},
            {"5", "make", "made", "made", "/meɪk/", "/meɪd/", "/meɪd/",
                "They make a cute couple.", "I made a poem for her wedding.", "I don’t know what to make of it.",
                S_TOP_25, S_REGULAR,
                "form (something) by putting parts together or combining substances; construct; create.",
                "hacer", "faire", ""},
            {"6", "go", "went", "gone", "/gou/", "/went/", "/gɒn/",
                "Peter goes to church on Sundays.", "She went out to the store.", "They had already gone to the show so we didn't go.",
                S_TOP_25, S_IRREGULAR,
                "move from one place or point to another; travel.",
                "ir", "aller", ""},
            {"7", "know", "knew", "known", "/nou/", "/nu:/", "/noun/",
                "I know your mother, but I’ve never met your father.", "She knew of our plan.", "Bivalved crustaceans little known to non professionals.",
                S_TOP_25, S_IRREGULAR,
                "be aware of through observation, inquiry, or information; be familiar or friendly with", "saber", "connaître", ""},
            {"8", "take", "took", "taken", "/teɪk/", "/tʊk/", "/'teɪkən/",
                "She leaned forward to take her hand.", "He took ten gold pieces from his table and wrapped them in the little letter.", "It was a picture taken at the party and the focus was on Carmen and Alex.",
                S_TOP_25, S_IRREGULAR,
                "lay hold of (something) with one's hands; reach for and hold; carry or bring with one; convey", "tomar", "prendre", ""},
            {"9", "see", "saw", "seen", "/si:/", "/sɔ:/", "/si:n/",
                "I can't see any other way to treat it.", "The wolf saw him.", "You should have seen Dad's face.",
                S_TOP_25, S_IRREGULAR,
                "perceive with the eyes; discern visually; discern or deduce mentally after reflection or from information; understand", "ver", "voir", ""},
            {"10", "come", "came", "come", "/kʌm/", "/keɪm/", "/kʌm/",
                "May we come in?", "Jessica came into the kitchen.", "\"The end of the world has come!\" cried some; and they ran about in the darkness.",
                S_TOP_25, S_IRREGULAR, "0",
                "move or travel toward or into a place thought of as near or familiar to the speaker", "venir", "venir", ""},
            {"11", "think", "thought", "thought", "/θɪŋk/", "/θɔ:t/", "/θɔ:t/",
                "He could not think of anything else.", "She thought that nothing would be the same again.", "I never thought I could do it.",
                S_TOP_25, S_IRREGULAR,
                "have a particular opinion, belief, or idea about someone or something; direct one's mind toward someone or something; use one's mind actively to form connected ideas.",
                "pensar", "pense", ""},
            {"12", "look", "looked", "looked", "/lʊk/", "/'lʊkt/", "/'lʊkt/",
                "Do I look that bad?", "The boy looked around him with wondering eyes.", "Oh, I wish I had looked after my teeth.",
                S_TOP_25, S_REGULAR,
                "direct one's gaze toward someone or something or in a specified direction",
                "mirar", "regarder", ""},
            {"13", "want", "wanted", "wanted", "/'wɒnt/", "/'wɒntɪd/", "/'wɒntɪd/",
                "I want an apple.", "He wanted the wisdom of the gods.", "You shall want for nothing while you are with me.",
                S_TOP_25, S_REGULAR,
                "have a desire to possess or do (something); wish for; lack or be short of something desirable or essential",
                "querer", "vouloir", ""},
            {"14", "give", "gave", "given", "/gɪv/", "/geɪv/", "/'gɪvən/",
                "I'm too close to my goal to give up now.", "They gave her water to drink.", "She hadn't been given that much consideration.",
                S_TOP_25, S_IRREGULAR,
                "freely transfer the possession of (something) to (someone); hand over to",
                "dar", "donner", ""},
            {"15", "use", "used", "used", "/'ju:z/", "/'ju:zd/", "/'ju:zd/",
                "I figured you could use the rest.", "She used the remote to shut the television off.", "You'll get used to it.",
                S_TOP_25, S_REGULAR,
                "take, hold, or deploy (something) as a means of accomplishing a purpose or achieving a result; employ",
                "utilizar", "utiliser", ""},
            {"16", "find", "found", "found", "/faɪnd/", "/faʊnd/", "/faʊnd/",
                "Water finds its own level.", "Vitamin B12 is found in dairy products.", "Uncle Henry says 'Eureka' means 'I have found it.'",
                S_TOP_25, S_IRREGULAR,
                "discover or perceive by chance or unexpectedly; recognize or discover (something) to be present",
                "encontrar", "trouver, découvrir", ""},
            {"17", "tell", "told", "told", "/tel/", "/tould/", "/tould/",
                "Boys, what did I tell you?", "The doctor hasn't told us yet.", "Men have told me that there is no riddle so cunning that you can not solve it.",
                S_TOP_25, S_IRREGULAR,
                "communicate information, facts, or news to someone in spoken or written words",
                "decir", "dire", ""},
            {"18", "ask", "asked", "asked", "/'ɑ:sk/", "/'ɑ:skt/", "/'ɑ:skt/",
                "Oh, I was going to ask you.", "\"Where's my milk?\" asked the kitten, looking up into Dorothy's face.", "Two months is all Kris asks, and I'm free.",
                S_TOP_25, S_REGULAR,
                "say something in order to obtain an answer or some information; request (someone) to do or give something",
                "pedir", "demander", ""},
            {"19", "work", "worked", "worked", "/'wɜ:k/", "/'wɜ:kt/", "/'wɜ:kt/",
                "He said it would work for a girl or a boy.", "A coloring book and crayons kept her busy while they worked and talked.", "We had worked hard on a presentation to a potential customer.",
                S_TOP_25, S_REGULAR,
                "be engaged in physical or mental activity in order to achieve a purpose or result, especially in one's job; do work",
                "trabajar", "travailler", ""},
            {"20", "seem", "seemed", "seemed", "/'si:m/", "/'si:md/", "/'si:md/",
                "It didn't seem like Christmas.", "Dawn seemed annoyed.", "For a long time it had seemed to me that life was about to begin.",
                S_TOP_25, S_REGULAR,
                "give the impression or sensation of being something or having a particular quality",
                "parecer", "sembler", ""},
            {"21", "feel", "felt", "felt", "/fi:l/", "/felt/", "/felt/",
                "I feel much better now.", "We felt a sense of excitement.", "It had felt very poorly for ages before he decided to try that new medicine.",
                S_TOP_25, S_IRREGULAR,
                "be aware of (a person or object) through touching or being touched; experience (an emotion or sensation)",
                "sentir", "ressentir", ""},
            {"22", "try", "tried", "tried", "/'traɪ/", "/'traɪd/", "/'traɪd/",
                "Try to stay out of the woods.", "He tried to regain his breath.", "She said you tried to call her.",
                S_TOP_25, S_REGULAR,
                "make an attempt or effort to do something",
                "intentar", "essayer", ""},
            {"23", "leave", "left", "left", "/li:v/", "/left/", "/left/",
                "So the prisoners resolved to leave their prison at once.", "She left New York on June 6.", "All the sun's warmth left the air.",
                S_TOP_25, S_IRREGULAR,
                "go away from",
                "salir, dejar", "laisser", ""},
            {"24", "call", "called", "called", "/'kɔ:l/", "/'kɔ:ld/", "/'kɔ:ld/",
                "I suppose you could call it that.", "The doctor called today.", "I wish you had called me.",
                S_TOP_25, S_REGULAR,
                "cry out to (someone) in order to summon them or attract their attention",
                "llamar", "appeler", ""},
            // TOP50
            {"25", "become", "became", "become", "/bɪ 'kʌm/", "/bɪ 'keɪm/", "/bɪ 'kʌm/",
                "It seems like you've become the resident veterinarian.", "Henry became king of England.", "Read in order to become wise.",
                S_TOP_50, S_IRREGULAR,
                "begin to be; (of clothing) look good on or suit (someone)", "volverse", "devenir", ""},
            {"26", "begin", "began", "begun", "/bɪ 'gɪn/", "/bɪ 'gæn/", "/bɪ 'gʌn/",
                "The future begins now.", "They say life began when simple chemicals built up into more complex chemicals.", "Theorists have just begun to address these complex questions.",
                S_TOP_50, S_IRREGULAR,
                "start; perform or undergo the first part of (an action or activity)", "empezar", "commencer", ""},
    };
}
