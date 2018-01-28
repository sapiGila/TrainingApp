package com.training.app.model;

import com.training.app.datasource.PhoneBookRestApi;
import com.training.app.object.PhoneBook;
import com.training.app.object.Result;
import com.training.app.object.ResultList;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Dell on 7/9/2017.
 */

public class PhoneBookRepository implements PhoneBookModelRemoteSource {

    private PhoneBookRestApi restApi;

    public PhoneBookRepository(PhoneBookRestApi restApi) {
        this.restApi = restApi;
    }

    @Override
    public Call<ResultList> getPhoneBooks() {
        return restApi.getPerson();
    }

    @Override
    public Call<Result> addPerson(PhoneBook phoneBook) {
        return restApi.addPerson(phoneBook);
    }

    @Override
    public Call<Result> editPerson(PhoneBook phoneBook) {
        return restApi.editPerson(phoneBook.getId(), phoneBook);
    }

    @Override
    public Call<Result> deletePerson(String personId) {
        return restApi.deletePerson(personId);
    }

    @Override
    public Call<Result> uploadPhotoPerson(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return restApi.uploadPhoto(body);
    }
}
