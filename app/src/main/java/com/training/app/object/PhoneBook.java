package com.training.app.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Dell on 7/8/2017.
 */

@RealmClass
@Parcel(Parcel.Serialization.BEAN)
public class PhoneBook extends RealmObject {

    @PrimaryKey
    @SerializedName("uid")
    @Expose
    private int uid;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("version")
    @Expose
    private Integer version = 1;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("picture")
    @Expose
    private String picture;

    public PhoneBook() {
    }


    public PhoneBook(String name, String address, String phone, String email, String picture) {
        this(name, address, phone, email);
        this.picture = picture;
    }

    public PhoneBook(String name, String address, String phone, String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Number generateId(Number number) {
        long id = (number != null) ? number.intValue() + 1 : 0;
        return id;
    }

    public int generateVersion() {
        int id = 0;
        id = getVersion() + 1;
        return id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
