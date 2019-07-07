package com.example.android.testingandroidxpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceDialogFragmentCompat;

/*
created this by adjusting the implementation of numberPickerPreferenceFragmentCompat in the library here:
https://github.com/h6ah4i/android-numberpickerprefcompat
 */

public class TextAutoCompletePreferenceDialogFragment extends PreferenceDialogFragmentCompat {
    // name of key for saving for state change (like rotating the screen
    private static final String SAVE_STATE_VALUE = "TextAutoCompletePreferenceDialogFragment.value";
    // name for sharedPreferences location - saving for next time you open the app
    private static final String SHARED_PREFERENCES = "testandroidxpreferences";
    private AutoCompleteTextView mAutoCompleteTextView;
    private String mValue;

    @NonNull
    public static TextAutoCompletePreferenceDialogFragment newInstance(@NonNull String key) {
        final TextAutoCompletePreferenceDialogFragment fragment = new TextAutoCompletePreferenceDialogFragment();
        final Bundle args = new Bundle(1);
        args.putString(ARG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // if it is first run after installation - get the default value
            mValue = getTextAutoCompletePreference().getDefaultValue();
        } else
            // if not - there is a saved value
            mValue = savedInstanceState.getString(SAVE_STATE_VALUE);
    }

    // get the TextAutoCompletePreference instance
    private TextAutoCompletePreference getTextAutoCompletePreference() {
        return (TextAutoCompletePreference) getPreference();
    }

    // save the value
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_STATE_VALUE, mValue);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        // finding the numberPicker view
        mAutoCompleteTextView = view.findViewById(R.id.auto_complete);
        mAutoCompleteTextView.setSelectAllOnFocus(true);
        // the places that will auto appear when typing similar strings
        String[] PLACES = new String[] {
                "Belgium", "France", "Italy", "Germany", "Spain"
        };
        // setting the adapter for the PLACES
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, PLACES);
        mAutoCompleteTextView.setAdapter(adapter);
        // throw an IllegalStateException if there is no NumberPicker view
        if (mAutoCompleteTextView == null) {
            throw new IllegalStateException("Dialog view must contain an auto_complete with id");
        }
        // set the value for the AutoCompleteTextView - i.e. name of last place saved
        mValue = restorePreferences("country");
        mAutoCompleteTextView.setText(mValue);
    }

    // This method to store the custom preferences changes
    private void savePreferences(String key, String value) {
        Activity activity = getActivity();
        SharedPreferences myPreferences;
        if (activity != null) {
            myPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor myEditor = myPreferences.edit();
            myEditor.putString(key, value);
            myEditor.apply();
        }
    }

    // This method to restore the custom preferences data
    private String restorePreferences(String key) {
        Activity activity = getActivity();
        SharedPreferences myPreferences;
        if (activity != null) {
            myPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            if (myPreferences.contains(key))
                return myPreferences.getString(key, "");
            else return "";
        } else return "";
    }

    // what to do when the dialog is closed
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mAutoCompleteTextView.clearFocus();
            final String value = mAutoCompleteTextView.getText().toString();
            savePreferences("country",value);
            getTextAutoCompletePreference().setValue(value);
            getTextAutoCompletePreference().setSummary(value);
            if (getTextAutoCompletePreference().callChangeListener(value)) {
                getTextAutoCompletePreference().setValue(value);
            }
        }
    }
}
