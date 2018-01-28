package com.training.app.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.training.app.contract.PhoneBookContract;
import com.training.app.datasource.PhoneBookRestApi;
import com.training.app.datasource.PhoneBookRestApiAdapter;
import com.training.app.model.PhoneBookModelLocalSource;
import com.training.app.model.PhoneBookModelRemoteSource;
import com.training.app.model.PhoneBookRealmRepository;
import com.training.app.model.PhoneBookRepository;
import com.training.app.object.PhoneBook;
import com.training.app.object.Result;
import com.training.app.object.ResultList;
import com.training.app.util.RealmDB;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell on 7/9/2017.
 */

public class PhoneBookPresenter implements PhoneBookContract.Presenter {

    private PhoneBookContract.PersonView personView;
    private PhoneBookContract.PhoneBookView phoneBookView;
    private PhoneBookModelRemoteSource modelRemote;
    private Scheduler io;
    private Scheduler mainThread;
    private Gson gson;
    private PhoneBookModelLocalSource modelLocal;
    private RealmDB realmDB;

    public PhoneBookPresenter(PhoneBookContract.PhoneBookView phoneBookView, Scheduler io, Scheduler mainThread,
                              RealmDB realmDB) {
        this(phoneBookView, io, mainThread);
        this.realmDB = realmDB;
        this.modelLocal = new PhoneBookRealmRepository();
    }

    public PhoneBookPresenter(PhoneBookContract.PhoneBookView phoneBookView, Scheduler io, Scheduler mainThread) {
        this(io, mainThread);
        this.phoneBookView = phoneBookView;
    }

    public PhoneBookPresenter(PhoneBookContract.PersonView personView, Scheduler io, Scheduler mainThread,
                              RealmDB realmDB) {
        this(personView, io, mainThread);
        this.realmDB = realmDB;
        this.modelLocal = new PhoneBookRealmRepository();
    }

    public PhoneBookPresenter(PhoneBookContract.PersonView personView, Scheduler io, Scheduler mainThread) {
        this(io, mainThread);
        this.personView = personView;
    }

    public PhoneBookPresenter(Scheduler io, Scheduler mainThread) {
        this.io = io;
        this.mainThread = mainThread;
        this.modelRemote = new PhoneBookRepository(getPhoneBookRestApi());
        this.gson = new Gson();
    }

    private PhoneBookRestApi getPhoneBookRestApi() {
        return PhoneBookRestApiAdapter.getPhoneBookRestApi();
    }

    @Override
    public void getPersons() {
        getRemotePerson();
//        getLocalPerson();
    }

    private void getRemotePerson() {
        modelRemote.getPhoneBooks()
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

    private void getLocalPerson() {
        final List<PhoneBook> phoneBooks = new ArrayList<>();
        realmDB.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<PhoneBook> phoneBooks1 = modelLocal.getPhoneBooks(realm);
                if (phoneBooks1.isLoaded()) {
                    phoneBooks.addAll(phoneBooks1);
                    phoneBookView.doShowPerson(phoneBooks);
                }
                phoneBookView.doAfterProcessing();
            }
        });
    }

    @Override
    public void uploadPhotoPerson(File file) {
        personView.doBeforeProcessing();
        modelRemote.uploadPhotoPerson(file)
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
        addPersonRemote(phoneBook);
    }

    private void addPersonRemote(final PhoneBook phoneBook) {
        personView.doBeforeProcessing();
        modelRemote.addPerson(phoneBook)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {
                                phoneBook.setId(response.body().getResult().toString());

                                //TODO set local (not yet)

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
        editPersonRemote(phoneBook);
    }

    private void editPersonRemote(PhoneBook phoneBook) {
        personView.doBeforeProcessing();
        modelRemote.editPerson(phoneBook)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {
                                JsonObject jsonObject = gson.toJsonTree(response.body().getResult()).getAsJsonObject();
                                PhoneBook result = gson.fromJson(jsonObject.toString(), PhoneBook.class);

                                //TODO set local with result (not yet)

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
    public void setPerson(PhoneBook phoneBook, boolean isEdit) {
        setPersonLocal(phoneBook, isEdit);
    }

    private void setPersonLocal(final PhoneBook phoneBook, final boolean isEdit) {
        personView.doBeforeProcessing();
        realmDB.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                modelLocal.setPerson(realm, phoneBook, isEdit);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                personView.doAfterProcessing();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                personView.doOnError(error.getMessage());
            }
        });
    }

    @Override
    public void deletePerson(String personId) {
        deletePersonRemote(personId);
//        deletePersonLocal(personId);
    }

    private void deletePersonRemote(String personId) {
        personView.doBeforeProcessing();
        modelRemote.deletePerson(personId)
                .enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("OK")) {

                                //TODO delete local (not yet)

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

    private void deletePersonLocal(final String personId) {
        personView.doBeforeProcessing();
        realmDB.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                modelLocal.deletePerson(realm, personId);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                personView.doAfterProcessing();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                personView.doOnError(error.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        realmDB.closeDB();
    }
}
