package com.training.app.object;

import com.google.gson.annotations.Expose;

/**
 * Created by user on 6/6/2017.
 */

public class Result {

    @Expose
    private String message;

    @Expose
    private Object result;

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
