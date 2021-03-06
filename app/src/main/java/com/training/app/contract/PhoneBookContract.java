package com.training.app.contract;

import com.training.app.object.PhoneBook;

import java.io.File;
import java.util.List;

/**
 * Created by Dell on 7/9/2017.
 */

public interface PhoneBookContract {

    interface Presenter {
        void getPersons();
        void uploadPhotoPerson(File file);
        void addPerson(PhoneBook phoneBook);
        void editPerson(PhoneBook phoneBook);
        void deletePerson(String personId);
        void setPerson(PhoneBook phoneBook, boolean isEdit);
        void onDestroy();
    }

    interface PhoneBookView {
        void doBeforeProcessing();
        void doShowPerson(List<PhoneBook> phoneBooks);
        void doOnError(String message);
        void doAfterProcessing();
    }

    interface PersonView {
        void doBeforeProcessing();
        void doAfterUploadPhotoPerson(String imageUrl);
        void doAfterProcessing();
        void doOnError(String message);
    }
}
