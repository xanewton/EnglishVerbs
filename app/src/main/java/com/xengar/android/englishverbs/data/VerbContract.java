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


    /**
     * Inner class that defines constant values for the verbs database table.
     * Each entry in the table represents a single verb.
     */
    public static final class VerbEntry implements BaseColumns {
        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VERBS);

        /** The MIME type of the {@link #CONTENT_URI} for a list of verbs. */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VERBS;

        /** The MIME type of the {@link #CONTENT_URI} for a single verb. */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VERBS;

        /** Name of database table for verbs */
        public final static String TABLE_NAME = "VERBS_TBL";

        /** Unique ID number for the pet (only for use in the database table). - Type: INTEGER */
        public final static String _ID = BaseColumns._ID;

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

        public static boolean isValidRegular(int value) {
            if (value == REGULAR || value == IRREGULAR) {
                return true;
            }
            return false;
        }

        /** Definition of the verb. - Type: TEXT */
        public final static String COLUMN_DEFINITION ="DEFINITION";

        /** Examples of the verb. - Type: TEXT  */
        public final static String COLUMN_SAMPLES ="SAMPLES";

        /** Other information for the verb. - Type: TEXT */
        public final static String COLUMN_DATA ="DATA";

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
