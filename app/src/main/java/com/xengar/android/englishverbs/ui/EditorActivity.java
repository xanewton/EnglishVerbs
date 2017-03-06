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

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.xengar.android.englishverbs.R;
import com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    /** Identifier for the verb data loader */
    private static final int EXISTING_VERB_LOADER = 0;

    /** Content URI for the existing verb (null if it's a new verb) */
    private Uri mCurrentVerbUri;

    /** EditText field to enter the verb's gender */
    private Spinner mRegularSpinner;

    /**
     * Type of verb. The possible valid values are in the VerbContract.java file:
     * {@link VerbEntry#REGULAR}, or
     * {@link VerbEntry#IRREGULAR}.
     */
    private int mRegular = VerbEntry.REGULAR;

    /** Boolean flag that keeps track of whether the verb has been edited (true) or not (false) */
    private boolean mVerbHasChanged = false;

    /** EditText field to enter the values*/
    private EditText mInfinitiveEditText;
    private EditText mSimplePastEditText;
    private EditText mPastParticipleEditText;
    private EditText mDefinitionEditText;
    private EditText mSamplesEditText;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mVerbHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVerbHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new verb or editing an existing one.
        Intent intent = getIntent();
        mCurrentVerbUri = intent.getData();

        // If the intent DOES NOT contain a verb content URI, then we know that we are
        // creating a new verb.
        if (mCurrentVerbUri == null) {
            // This is a new verb, so change the app bar to say "Add a Verb"
            setTitle(getString(R.string.editor_activity_title_new_verb));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing verb, so change app bar to say "Edit Verb"
            setTitle(getString(R.string.editor_activity_title_edit_verb));

            // Initialize a loader to read the verb data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_VERB_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mInfinitiveEditText = (EditText) findViewById(R.id.edit_infinitive);
        mSimplePastEditText = (EditText) findViewById(R.id.edit_simple_past);
        mPastParticipleEditText = (EditText) findViewById(R.id.edit_past_participle);
        mRegularSpinner = (Spinner) findViewById(R.id.spinner_regular);
        mDefinitionEditText = (EditText) findViewById(R.id.edit_definition);
        mSamplesEditText = (EditText) findViewById(R.id.edit_samples);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mInfinitiveEditText.setOnTouchListener(mTouchListener);
        mInfinitiveEditText.setOnTouchListener(mTouchListener);
        mPastParticipleEditText.setOnTouchListener(mTouchListener);
        mRegularSpinner.setOnTouchListener(mTouchListener);
        mDefinitionEditText.setOnTouchListener(mTouchListener);
        mSamplesEditText.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the verb.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mRegularSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mRegularSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.type_regular))) {
                        mRegular = VerbEntry.REGULAR;
                    } else {
                        mRegular = VerbEntry.IRREGULAR;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mRegular = VerbEntry.REGULAR;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all verb attributes, define a projection that contains
        // all columns from the verb table
        String[] projection = {
                VerbEntry._ID,
                VerbEntry.COLUMN_INFINITIVE,
                VerbEntry.COLUMN_SIMPLE_PAST,
                VerbEntry.COLUMN_PAST_PARTICIPLE,
                VerbEntry.COLUMN_REGULAR,
                VerbEntry.COLUMN_DEFINITION,
                VerbEntry.COLUMN_PRONUNCIATION_INFINITIVE,
                VerbEntry.COLUMN_PRONUNCIATION_SIMPLE_PAST,
                VerbEntry.COLUMN_PRONUNCIATION_PAST_PARTICIPLE,
                VerbEntry.COLUMN_SAMPLE_1,
                VerbEntry.COLUMN_SAMPLE_2,
                VerbEntry.COLUMN_SAMPLE_3 };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentVerbUri,         // Query the content URI for the current verb
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Update the views on the screen with the values from the database
            mInfinitiveEditText.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_INFINITIVE)));
            mSimplePastEditText.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_SIMPLE_PAST)));
            mPastParticipleEditText.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_PAST_PARTICIPLE)));
            mDefinitionEditText.setText(
                    cursor.getString(cursor.getColumnIndex(VerbEntry.COLUMN_DEFINITION)));

            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Regular, 1 is Irregular, 2 Both).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (cursor.getInt(cursor.getColumnIndex(VerbEntry.COLUMN_REGULAR))) {
                case VerbEntry.REGULAR:
                    mRegularSpinner.setSelection(0);
                    break;
                default:
                    mRegularSpinner.setSelection(1);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mInfinitiveEditText.setText("");
        mSimplePastEditText.setText("");
        mPastParticipleEditText.setText("");
        mRegularSpinner.setSelection(0);
        mDefinitionEditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the verb.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this verb.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the verb.
                deleteVerb();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the verb.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the verb in the database.
     */
    private void deleteVerb() {
        // Only perform the delete if this is an existing verb.
        if (mCurrentVerbUri != null) {
            // Call the ContentResolver to delete the verb at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentverbUri
            // content URI already identifies the verb that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentVerbUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_verb_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_verb_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/editorfile.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new verb, hide the "Delete" menu item.
        if (mCurrentVerbUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the verb hasn't changed, continue with handling back button press
        if (!mVerbHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save verb to database
                saveVerb();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the verb hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mVerbHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get user input from editor and save verb into database.
     */
    private void saveVerb() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String infinitive = mInfinitiveEditText.getText().toString().trim();
        String simplePast = mSimplePastEditText.getText().toString().trim();
        String pastParticiple = mPastParticipleEditText.getText().toString().trim();
        String definition = mDefinitionEditText.getText().toString().trim();

        // Check if this is supposed to be a new verb
        // and check if all the fields in the editor are blank
        if (mCurrentVerbUri == null &&
                TextUtils.isEmpty(infinitive) && TextUtils.isEmpty(simplePast) &&
                TextUtils.isEmpty(pastParticiple)) {
            // Since no fields were modified, we can return early without creating a new verb.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and verb attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(VerbEntry.COLUMN_INFINITIVE, infinitive);
        values.put(VerbEntry.COLUMN_SIMPLE_PAST, simplePast);
        values.put(VerbEntry.COLUMN_PAST_PARTICIPLE, pastParticiple);
        values.put(VerbEntry.COLUMN_DEFINITION, definition);
        values.put(VerbEntry.COLUMN_PRONUNCIATION_INFINITIVE, "");
        values.put(VerbEntry.COLUMN_PRONUNCIATION_SIMPLE_PAST, "");
        values.put(VerbEntry.COLUMN_PRONUNCIATION_PAST_PARTICIPLE, "");
        values.put(VerbEntry.COLUMN_SAMPLE_1, "");
        values.put(VerbEntry.COLUMN_SAMPLE_2, "");
        values.put(VerbEntry.COLUMN_SAMPLE_3, "");
        values.put(VerbEntry.COLUMN_COMMON, VerbEntry.OTHER);
        values.put(VerbEntry.COLUMN_REGULAR, mRegular);
        values.put(VerbEntry.COLUMN_COLOR, 0);
        values.put(VerbEntry.COLUMN_SCORE, 0);
        values.put(VerbEntry.COLUMN_TRANSLATION_ES, "");
        values.put(VerbEntry.COLUMN_TRANSLATION_FR, "");
        values.put(VerbEntry.COLUMN_NOTES, "");

        // Determine if this is a new or existing verb by checking if mCurrenVerbUri is null or not
        if (mCurrentVerbUri == null) {
            // This is a NEW verb, so insert a new verb into the provider,
            // returning the content URI for the new verb.
            Uri newUri = getContentResolver().insert(VerbEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_verb_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_verb_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING verb, so update the verb with content URI: mCurrentVerbUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentVerbUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentVerbUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_verb_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_verb_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
