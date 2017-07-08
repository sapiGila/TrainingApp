package com.training.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell on 7/8/2017.
 */

public class PreferenceUtil {

    public static void savePreference(Context ctx, String key, String value) {
        try {
            SharedPreferences credentialDataPref = ctx.getSharedPreferences(VariableUtil.sharePreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorPreference = credentialDataPref.edit();
            editorPreference.putString(key, value);
            editorPreference.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getPreference(Context ctx, String key) {
        String valueReturn = "";
        SharedPreferences credentialDataPref = ctx.getSharedPreferences(VariableUtil.sharePreferences, Context.MODE_PRIVATE);
        valueReturn = credentialDataPref.getString(key, "");
        return valueReturn;
    }

    public static void clearPreference(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(VariableUtil.sharePreferences, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}
