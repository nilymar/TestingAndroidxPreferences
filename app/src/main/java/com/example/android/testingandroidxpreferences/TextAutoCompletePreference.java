package com.example.android.testingandroidxpreferences;

/*
created this by adjusting the implementation of numberPickerPreference in the library here:
https://github.com/h6ah4i/android-numberpickerprefcompat
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import androidx.preference.DialogPreference;

public class TextAutoCompletePreference extends DialogPreference {
    private String mValue = "";
    private String mDefaultValue = "Albania";

    public TextAutoCompletePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, null, 0, 0);
    }

    public TextAutoCompletePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null, 0, 0);
    }

    public TextAutoCompletePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null, 0, 0);
    }

    public TextAutoCompletePreference(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray ta = context.obtainStyledAttributes(
                attrs, R.styleable.Auto_Complete_attrs, defStyleAttr, defStyleRes);
        ta.recycle();
        setDialogLayoutResource(R.layout.auto_complete_layout);
    }

    public void setValue(String value) {
        final boolean wasBlocking = shouldDisableDependents();
        mValue = value;
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
           notifyDependencyChange(isBlocking);
       }
    }

    public String getValue() {
        return mValue;
    }

    public String getDefaultValue() {
        return mDefaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        mDefaultValue = defaultValue;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.value);
    }

    private static class SavedState extends BaseSavedState {
        String value;

        private SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        private SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<TextAutoCompletePreference.SavedState>() {
            @Override
            public TextAutoCompletePreference.SavedState createFromParcel(Parcel in) {
                return new TextAutoCompletePreference.SavedState(in);
            }

            @Override
            public TextAutoCompletePreference.SavedState[] newArray(int size) {
                return new TextAutoCompletePreference.SavedState[size];
            }
        };
    }

}
