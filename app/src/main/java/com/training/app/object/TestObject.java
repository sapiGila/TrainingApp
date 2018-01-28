package com.training.app.object;

import org.parceler.Parcel;

/**
 * Created by Dell on 8/3/2017.
 */

@Parcel(Parcel.Serialization.BEAN)
public class TestObject {

    private String a;
    private String b;

    public TestObject() {
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
