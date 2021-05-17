package utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.udolgc.mustknowscriptures.MustKnow;

public class UtilityManager {

    public static final String myPreferences = "MUST_KNOW_PREFERENCES";
    public static final String SETUP_DONE = "SETUP_DONE";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String ENGLISH = "English";
    public static final String FRENCH = "French";

    public void setPreferences(String preferenceName, String preferenceValue) {

        SharedPreferences sharedPreferences = MustKnow.getContext().getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();

    }

    public void setBooleanPreferences(String preferenceName, boolean preferenceValue) {

        SharedPreferences sharedPreferences = MustKnow.getContext().getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();

    }

    public String getSharedPreference(String preferenceName) {

        SharedPreferences sharedPreferences = MustKnow.getContext().getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, "");

    }

    public boolean getBooleanSharedPreference(String preferenceName) {

        SharedPreferences sharedPreferences = MustKnow.getContext().getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(preferenceName, false);

    }

}
