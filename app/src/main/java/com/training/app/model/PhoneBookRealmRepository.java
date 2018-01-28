package com.training.app.model;

import com.training.app.object.PhoneBook;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Dell on 7/12/2017.
 */

public class PhoneBookRealmRepository implements PhoneBookModelLocalSource {

    @Override
    public RealmResults<PhoneBook> getPhoneBooks(Realm realm) {
        return realm.where(PhoneBook.class).findAll();
    }

    @Override
    public void setPerson(Realm realm, PhoneBook phoneBook, boolean isEdit) {
//        setManual(realm, phoneBook, isEdit);
        setOtomatic(realm, phoneBook);
    }

    private void setManual(Realm realm, PhoneBook phoneBook, boolean isEdit) {
        PhoneBook person;
        if (isEdit) {
            RealmQuery<PhoneBook> phoneBookRealmQuery = realm.where(PhoneBook.class);
            person = phoneBookRealmQuery.equalTo("uid", phoneBook.getUid()).findFirst();
        } else {

            //jika tidak ada PK
//            person = realm.createObject(PhoneBook.class);

            //jika ada PK
            Number id = realm.where(PhoneBook.class).max("uid");
            person = realm.createObject(PhoneBook.class, phoneBook.generateId(id));
        }

        //jika tidak ada PK
//        person.setId(phoneBook.getId());

        person.setName(phoneBook.getName());
        person.setPicture(phoneBook.getPicture());
        person.setPhone(phoneBook.getPhone());
        person.setEmail(phoneBook.getEmail());
        person.setAddress(phoneBook.getAddress());
        person.setVersion(phoneBook.generateVersion());
    }

    private void setOtomatic(Realm realm, PhoneBook phoneBook) {
        realm.copyToRealmOrUpdate(phoneBook);
    }

    @Override
    public void deletePerson(Realm realm, String personId) {
        realm.where(PhoneBook.class)
                .equalTo("uid", Integer.valueOf(personId))
                .findAll()
                .deleteAllFromRealm();
    }
}
