package com.training.app.util;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

/**
 * Created by hesam on 9/7/16.
 */
public class SnappyDB {

    private Context mContext;
    private DB snappyDB;
    private String keyTask = "KEY_TASK";

    public SnappyDB(Context context) {
        mContext = context;

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

    public void setSms(String task) {
        try {
            snappyDB.put(keyTask, task);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public String getSms() {
        String task = "";
        try {
            task = snappyDB.get(keyTask);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        return task;
    }
}
