package com.training.app.datasource;

import com.training.app.object.PhoneBook;
import com.training.app.object.Result;
import com.training.app.object.ResultList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Dell on 7/8/2017.
 */

public interface PhoneBookRestApi {

    @GET("api/v1/person")
    Call<ResultList> getPerson();

    @Headers("Content-Type: application/json")
    @POST("api/v1/person")
    Call<Result> addPerson(@Body PhoneBook phoneBook);

    @Multipart
    @POST("api/v1/person/upload")
    Call<Result> uploadPhoto(@Part MultipartBody.Part image);

    @Headers("Content-Type: application/json")
    @PUT("api/v1/person/{secure_id}")
    Call<Result> editPerson(@Path("secure_id") String secureId,
                            @Body PhoneBook phoneBook);

    @Headers("Content-Type: application/json")
    @DELETE("api/v1/person/{secure_id}")
    Call<Result> deletePerson(@Path("secure_id") String secureId);
}
