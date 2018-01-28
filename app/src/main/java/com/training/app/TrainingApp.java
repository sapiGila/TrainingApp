package com.training.app;

import android.app.Application;

import com.training.app.util.RealmDB;

/**
 * Created by Dell on 7/13/2017.
 */

public class TrainingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmDB realmDB = new RealmDB(this);
        realmDB.configRealm();
    }
}
