package com.training.app.util;

import android.content.Context;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Dell on 7/12/2017.
 */

public class RealmDB {

    private static Realm realm;

    public RealmDB(Context context) {
        Realm.init(context);
    }

    public RealmDB() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
    }

    public void configRealm() {
        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("TrainingApp.realm")
                .build();

        // Make this config the default
//        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
    }

    public void closeDB() {
//        if (realm != null) {
//            realm.close();
//            realm = null;
//        }
    }

    public Realm getRealm() {
        return realm;
    }
}
