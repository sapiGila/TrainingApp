package com.training.app.model;

import com.training.app.object.PhoneBook;
import com.training.app.object.Result;
import com.training.app.object.ResultList;

import java.io.File;

import retrofit2.Call;

/**
 * Created by Dell on 7/9/2017.
 */

public interface PhoneBookModelRemoteSource {

    Call<ResultList> getPhoneBooks();
    Call<Result> addPerson(PhoneBook phoneBook);
    Call<Result> editPerson(PhoneBook phoneBook);
    Call<Result> deletePerson(String personId);
    Call<Result> uploadPhotoPerson(File file);
}
