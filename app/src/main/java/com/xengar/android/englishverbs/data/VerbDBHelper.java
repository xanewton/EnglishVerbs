package com.xengar.android.englishverbs.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

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


    /**
     * Constructs a new instance of {@link VerbDBHelper}.
     *
     * @param context of the app
     */
    public VerbDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_VERBS_TABLE =  "CREATE TABLE " + VerbEntry.TABLE_NAME + " ("
                + VerbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VerbEntry.COLUMN_INFINITIVE + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_SIMPLE_PAST + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_PAST_PARTICIPLE + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_DEFINITION + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_SAMPLES + " TEXT NOT NULL, "
                + VerbEntry.COLUMN_COMMON + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_REGULAR + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_COLOR + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_SCORE + " INTEGER NOT NULL DEFAULT 0, "
                + VerbEntry.COLUMN_DATA + " TEXT, "
                + VerbEntry.COLUMN_TRANSLATION_ES + " TEXT, "
                + VerbEntry.COLUMN_TRANSLATION_FR + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_VERBS_TABLE);
    }


    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
