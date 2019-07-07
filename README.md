# TestingAndroidxPreferences
example for creating custom preferences using AndroidX libraries, 
created to custom preferences based on androidx.preference.DialogPreference:
1. after looking at the implementation here: https://github.com/h6ah4i/android-numberpickerprefcompat
   made my adjustments and created my NumberPickerPreference (in mine there is no unit textVies, and
   you can set the wheel fanctionality), added saving preference to sharedPreference file, for next run
2. used what I learned to create a custom preference that used autoCompleteTextView

for both preferences you create to classes: one that extends DialogPreference, and another that extends PreferenceDialogFragmentCompat

doesn't work when you rotate the screen... canceled that option

![ezgif com-resize (3)](https://user-images.githubusercontent.com/33417968/60767257-eb646880-a0bd-11e9-93df-ff0fc6b5258f.gif)
