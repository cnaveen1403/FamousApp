package com.andro.naveen.famousapp.myapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

/**
 * Created by Lenovo on 4/4/2016.
 */
public class MyApplication extends Application {

    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        initFont();
        mSharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public static class Fonts{

        public static Typeface FontIcon;
    }

    private void initFont(){

        Fonts.FontIcon = Typeface.create(Typeface.createFromAsset(getAssets(),"fonts.ttf"),Typeface.NORMAL);
    }

    public void  saveToken(String key, String value){
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();

    }

    public String getSavedValue(String key){

        return  mSharedPreferences.getString(key,null);
    }
}

