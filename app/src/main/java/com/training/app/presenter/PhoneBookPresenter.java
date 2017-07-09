package com.training.app.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.training.app.contract.PhoneBookContract;
import com.training.app.datasource.PhoneBookRestApi;
import com.training.app.datasource.PhoneBookRestApiAdapter;
import com.training.app.model.PhoneBookModel;
import com.training.app.model.PhoneBookRepository;
import com.training.app.object.PhoneBook;
import com.training.app.object.Result;
import com.training.app.object.ResultList;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 7/9/2017.
 */

public class PhoneBookPresenter implements PhoneBookContract.Presenter {

    private PhoneBookContract.PersonView personView;
    private PhoneBookContract.PhoneBookView phoneBookView;
    private PhoneBookModel model;
    private Scheduler io;
    private Scheduler mainThread;
    private Gson gson;

    public PhoneBookPresenter(PhoneBookContract.PhoneBookView phoneBookView, Scheduler io, Scheduler mainThread) {
        this(io, mainThread);
        this.phoneBookView = phoneBookView;
    }

    public PhoneBookPresenter(PhoneBookContract.PersonView personView, Scheduler io, Scheduler mainThread) {
        this(io, mainThread);
        this.personView = personView;
    }

    public PhoneBookPresenter(Scheduler io, Scheduler mainThread) {
        this.io = io;
        this.mainThread = mainThread;
        this.model = new PhoneBookRepository(getPhoneBookRestApi());
        this.gson = new Gson();
    }

    private PhoneBookRestApi getPhoneBookRestApi() {
        return PhoneBookRestApiAdapter.getPhoneBookRestApi();
    }

    @Override
    public void getPersons() {
        model.getPhoneBooks()
                .enqueue(new Callback<ResultList>() {
                    @Override
                    public void onResponse(Call<ResultList> call, Response<ResultList> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {
                                JsonArray jsonArray = gson.toJsonTree(response.body().getResult()).getAsJsonArray();
                                Type phoneBookType = new TypeToken<ArrayList<PhoneBook>>() {
                                }.getType();
                                List<PhoneBook> phoneBooks = gson.fromJson(jsonArray, phoneBookType);
                                phoneBookView.doShowPerson(phoneBooks);
                            } else {
                                phoneBookView.doOnError(response.body().getResult().toString());
                            }
                        } else {
                            try {
                                phoneBookView.doOnError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultList> call, Throwable t) {
                        phoneBookView.doOnError(t.getMessage());
                    }
                });
    }

    @Override
    public void uploadPhotoPerson(File file) {
        personView.doBeforeProcessing();
        model.uploadPhotoPerson(file)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {
                                personView.doAfterUploadPhotoPerson(response.body().getResult().toString());
                            } else {
                                personView.doOnError(response.body().getResult().toString());
                            }
                        } else {
                            try {
                                personView.doOnError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        personView.doOnError(t.getMessage());
                    }
                });
    }

    @Override
    public void addPerson(final PhoneBook phoneBook) {
        personView.doBeforeProcessing();
        model.addPerson(phoneBook)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {
                                phoneBook.setId(response.body().getResult().toString());
                                personView.doSetPhoneBook(phoneBook);
                                personView.doAfterProcessing();
                            }
                        } else {
                            try {
                                personView.doOnError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        personView.doOnError(t.getMessage());
                    }
                });

    }

    @Override
    public void editPerson(PhoneBook phoneBook) {
        personView.doBeforeProcessing();
        model.editPerson(phoneBook)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {
                                JsonObject jsonObject = gson.toJsonTree(response.body().getResult()).getAsJsonObject();
                                PhoneBook result = gson.fromJson(jsonObject.toString(), PhoneBook.class);
                                personView.doSetPhoneBook(result);
                                personView.doAfterProcessing();
                            } else {
                                personView.doOnError(response.body().getResult().toString());
                            }
                        } else {
                            try {
                                phoneBookView.doOnError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        personView.doOnError(t.getMessage());
                    }
                });
    }

    @Override
    public void deletePerson(String personId) {
        personView.doBeforeProcessing();
        model.deletePerson(personId)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {
                                personView.doAfterProcessing();
                            } else {
                                personView.doOnError(response.body().getResult().toString());
                            }
                        } else {
                            try {
                                personView.doOnError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        personView.doOnError(t.getMessage());
                    }
                });
    }
}
