package com.training.app.model;

import com.training.app.object.PhoneBook;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Dell on 7/12/2017.
 */

public interface PhoneBookModelLocalSource {
    RealmResults<PhoneBook> getPhoneBooks(Realm realm);
    void setPerson(Realm realm, PhoneBook phoneBook, boolean isEdit);
    void deletePerson(Realm realm, String personId);
}
