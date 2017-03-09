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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Verbs
 */

public final class VerbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private VerbContract() {}

    public static final String CONTENT_AUTHORITY = "com.xengar.android.englishverbs";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VERBS = "verbs";
    public static final String PATH_REGULAR_VERBS = "irregular_verbs";
    public static final String PATH_IRREGULAR_VERBS = "regular_verbs";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_FAVORITE_VERBS = "favorite_verbs";

    /**
     * Inner class that defines constant values for the verbs database table.
     * Each entry in the table represents a single verb.
     */
    public static final class VerbEntry implements BaseColumns {
        /** The content URI to access the verb data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VERBS);
        public static final Uri CONTENT_REGULARS_URI
                = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REGULAR_VERBS);
        public static final Uri CONTENT_IRREGULARS_URI
                = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_IRREGULAR_VERBS);

        public static final Uri CONTENT_FAVORITES_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_FAVORITES);
        public static final Uri CONTENT_FAVORITE_VERBS_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_FAVORITE_VERBS);

        /** The MIME type of the {@link #CONTENT_URI} for a list of verbs. */
        public static final String CONTENT_LIST_TYPE_VERB =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VERBS;
        public static final String CONTENT_LIST_TYPE_FAVORITE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /** The MIME type of the {@link #CONTENT_URI} for a single verb. */
        public static final String CONTENT_ITEM_TYPE_VERB =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VERBS;
        public static final String CONTENT_ITEM_TYPE_FAVORITE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /** Name of database table for verbs */
        public final static String VERBS_TBL = "VERBS_TBL";
        public final static String FAVORITES_TBL = "FAVORITES_TBL";

        /** Unique ID number for the verb (only for use in the database table). - Type: INTEGER */
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ID ="ID";

        /** Verb in infinitive form. - Type: TEXT  */
        public final static String COLUMN_INFINITIVE ="INFINITIVE";

        /** Verb in simple past form. - Type: TEXT */
        public final static String COLUMN_SIMPLE_PAST ="SIMPLE_PAST";

        /** Verb in past participle form. - Type: TEXT */
        public final static String COLUMN_PAST_PARTICIPLE ="PAST_PARTICIPLE";

        /** Common usage of the verb. - Type: INTEGER */
        public final static String COLUMN_COMMON ="COMMON";

        /** Possible values for the common usage of the verb. */
        public static final int TOP_25 = 0;
        public static final int TOP_50 = 1;
        public static final int TOP_100 = 2;
        public static final int TOP_500 = 3;
        public static final int OTHER = 4;
        public static final String S_TOP_25 = "" + TOP_25;
        public static final String S_TOP_50 = "" + TOP_50;
        public static final String S_TOP_100 = "" + TOP_100;
        public static final String S_TOP_500 = "" + TOP_500;
        public static final String S_OTHER = "" + OTHER;

        public static boolean isValidCommonUsage(int usage) {
            if (usage == TOP_25 || usage == TOP_50 || usage == TOP_100
                    || usage == TOP_500 || usage == OTHER) {
                return true;
            }
            return false;
        }

        /** Regular verbs end in 'ed'. - Type: INTEGER */
        public final static String COLUMN_REGULAR ="REGULAR";

        /** Possible values for regular verbs. */
        public static final int REGULAR = 0;
        public static final int IRREGULAR = 1;
        public static final int REGULAR_AND_IRREGULAR = 2;
        public static final String S_REGULAR = "" + REGULAR;
        public static final String S_IRREGULAR = "" + IRREGULAR;
        public static final String S_REGULAR_AND_IRREGULAR = "" + REGULAR_AND_IRREGULAR;

        public static boolean isValidRegular(int value) {
            if (value == REGULAR || value == IRREGULAR || value == REGULAR_AND_IRREGULAR) {
                return true;
            }
            return false;
        }

        /** Definition of the verb. - Type: TEXT */
        public final static String COLUMN_DEFINITION ="DEFINITION";

        /** Examples of the verb. - Type: TEXT  */
        public final static String COLUMN_SAMPLE_1 ="SAMPLE1";
        public final static String COLUMN_SAMPLE_2 ="SAMPLE2";
        public final static String COLUMN_SAMPLE_3 ="SAMPLE3";

        /** Verb pronunciation in infinitive form. - Type: TEXT  */
        public final static String COLUMN_PHONETIC_INFINITIVE ="PHONETIC_INFINITIVE";
        public final static String COLUMN_PHONETIC_SIMPLE_PAST ="PHONETIC_SIMPLE_PAST";
        public final static String COLUMN_PHONETIC_PAST_PARTICIPLE ="PHONETIC_PAST_PARTICIPLE";

        /** Other information for the verb. - Type: TEXT */
        public final static String COLUMN_NOTES ="NOTES";

        /** Color assigned by the user. - Type: INTEGER */
        public final static String COLUMN_COLOR ="COLOR";

        /** Score assigned by using the exercises or tests. - Type: INTEGER */
        public final static String COLUMN_SCORE ="SCORE";

        /**
         * Translations of the verb into the language.
         * Type: TEXT
         */
        public final static String COLUMN_TRANSLATION_ES ="TRANSLATION_ES";
        public final static String COLUMN_TRANSLATION_FR ="TRANSLATION_FR";

    }

}
