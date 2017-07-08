package com.training.app.util;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.SnappydbException;

/**
 * Created by hesam on 9/7/16.
 */
public class SnappyDB {

    private DB snappyDB;

    public SnappyDB(Context context) {
        try {
            snappyDB = new com.snappydb.SnappyDB.Builder(context)
                    .directory(context.getFilesDir().getAbsolutePath()) //optional
                    .name("TrainingApp")//optional
                    .build();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void closeDB() {
        try {
            snappyDB.close();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String task) {
        try {
            snappyDB.put(VariableUtil.keyTask, task);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        String task = "";
        try {
            task = snappyDB.get(VariableUtil.keyTask);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return task;
    }
}
