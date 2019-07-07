package com.example.android.testingandroidxpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


public class MySettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    // name for sharedPreferences location
    private static final String SHARED_PREFERENCES = "testandroidxpreferences";
    private static final String DIALOG_FRAGMENT_TAG =
            "androidx.preference.PreferenceFragment.DIALOG";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference autoCompletePreference = findPreference("country");
        if (autoCompletePreference != null) {
            bindPreferenceSummaryToValue(autoCompletePreference);
            autoCompletePreference.setSelectable(true);
        }
        Preference numberPickerPreference = findPreference("number_picker_preference");
        if (numberPickerPreference != null) {
            bindPreferenceSummaryToValue(numberPickerPreference);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference instanceof NumberPickerPreference) {
            String chosenNumber = newValue.toString();
            savePreferences(preference.getKey(), chosenNumber);
            preference.setSummary(chosenNumber);
            if (newValue.toString().isEmpty()) {
                int defaultValue = ((NumberPickerPreference) preference).getDefaultValue();
                preference.setSummary(defaultValue);
                ((NumberPickerPreference) preference).setValue(defaultValue);
            }
            else {
                preference.setSummary(chosenNumber);
                ((NumberPickerPreference) preference).setValue(Integer.parseInt(chosenNumber));
            }

        } else if (preference instanceof TextAutoCompletePreference) {
            String chosenCountry = newValue.toString();
            savePreferences(preference.getKey(), chosenCountry);
            if (newValue.toString().isEmpty()) {
                String defaultValue = ((TextAutoCompletePreference) preference).getDefaultValue();
                preference.setSummary(defaultValue);
                ((TextAutoCompletePreference) preference).setValue(defaultValue);
            }
            else {
                preference.setSummary(chosenCountry);
                ((TextAutoCompletePreference) preference).setValue(chosenCountry);
            }
        }
        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        if (preference instanceof TextAutoCompletePreference) { // for the name
            String preferenceString = restorePreferences(preference.getKey());
            if ((preferenceString == null || preferenceString.isEmpty())) {
                // when there is no saved data - put the default value
                onPreferenceChange(preference, ((TextAutoCompletePreference) preference).getDefaultValue());
            } else {
                onPreferenceChange(preference, preferenceString);
            }
        }
        else if (preference instanceof NumberPickerPreference) {
            String preferenceString = restorePreferences(preference.getKey());
            if ((preferenceString == null ||preferenceString.isEmpty())) {
                // when there is no saved data - put the default value
                onPreferenceChange(preference, ((NumberPickerPreference) preference).getDefaultValue());
            } else {
                onPreferenceChange(preference, preferenceString);
            }
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // check if dialog is already showing
        if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }

        final DialogFragment f;

        if (preference instanceof NumberPickerPreference) {
            f = NumberPickerPreferenceDialogFragment.newInstance(preference.getKey());
        } else if (preference instanceof TextAutoCompletePreference) {
            f = TextAutoCompletePreferenceDialogFragment.newInstance(preference.getKey());
        } else
            f = null;

        if (f != null) {
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
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
}
