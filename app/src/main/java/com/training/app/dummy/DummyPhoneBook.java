package com.training.app.dummy;

import com.training.app.object.PhoneBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 7/8/2017.
 */

public class DummyPhoneBook {

    public static List<PhoneBook> getPhoneBooks() {
        List<PhoneBook> phoneBooks = new ArrayList<>();
        PhoneBook phoneBook1 = new PhoneBook("Sony Indrajid0", "Kerinci III/1",
                "081381355063", "sony0@nostratech.com", "http://dev.nostratech.com:10003/training-android/sony_indrajid.jpg");
        PhoneBook phoneBook2 = new PhoneBook("Sony Indrajid1", "Kerinci III/1",
                "081381355063", "sony1@nostratech.com", "http://dev.nostratech.com:10003/training-android/sony_indrajid.jpg");
        PhoneBook phoneBook3 = new PhoneBook("Sony Indrajid2", "Kerinci III/1",
                "081381355063", "sony2@nostratech.com", "http://dev.nostratech.com:10003/training-android/sony_indrajid.jpg");
        phoneBooks.add(phoneBook1);
        phoneBooks.add(phoneBook2);
        phoneBooks.add(phoneBook3);
        return phoneBooks;
    }

}
