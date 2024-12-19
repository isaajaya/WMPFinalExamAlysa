package com.example.finalexamalysa;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Preferences {
    private static final String PREF_NAME = "EnrollmentApp";
    private static final String SUBJECTS_KEY = "Subjects";
    private static final String TOTAL_CREDITS_KEY = "TotalCredits";
    private static final String KEY_USERNAME = "user";
    private static final String KEY_PASSWORD = "pass";
    private static final String KEY_USER_LOGIN = "name";
    private static final String KEY_STATUS_LOGIN = "status";

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // User Credentials Management
    public static void setUsername(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public static String getUsername(Context context) {
        return getSharedPreference(context).getString(KEY_USERNAME, "");
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public static String getPassword(Context context) {
        return getSharedPreference(context).getString(KEY_PASSWORD, "");
    }

    public static void setUserLogin(Context context, String userLogin) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USER_LOGIN, userLogin);
        editor.apply();
    }

    public static String getUserLogin(Context context) {
        return getSharedPreference(context).getString(KEY_USER_LOGIN, "");
    }

    public static void setStatusLogin(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_LOGIN, status);
        editor.apply();
    }

    public static boolean getStatusLogin(Context context) {
        return getSharedPreference(context).getBoolean(KEY_STATUS_LOGIN, false);
    }

    public static void clearLoggedUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USER_LOGIN);
        editor.remove(KEY_STATUS_LOGIN);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    // Subject Selection Management
    public static void setSubjects(Context context, ArrayList<String> subjects) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        Set<String> subjectSet = new HashSet<>(subjects);
        editor.putStringSet(SUBJECTS_KEY, subjectSet);
        editor.apply();
    }

    public static ArrayList<String> getSubjects(Context context) {
        Set<String> subjectSet = getSharedPreference(context).getStringSet(SUBJECTS_KEY, new HashSet<>());
        return new ArrayList<>(subjectSet);
    }

    public static void setTotalCredits(Context context, int totalCredits) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(TOTAL_CREDITS_KEY, totalCredits);
        editor.apply();
    }

    public static int getTotalCredits(Context context) {
        return getSharedPreference(context).getInt(TOTAL_CREDITS_KEY, 0);
    }
}
