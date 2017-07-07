package com.training.app.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.training.app.R;
import com.training.app.contract.LifeCycleContract;
import com.training.app.presenter.SmsVerificationReceiver;
import com.training.app.util.SnappyDB;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/6/2017.
 */

public class LifeCycleActivity extends AppCompatActivity implements LifeCycleContract.View {


    @BindView(R.id.dateTimeTextView)
    TextView dateTimeTextView;
    @BindView(R.id.resultTextView)
    TextView resultTextView;
    String TAG = "Android : ";
    private final int ADD_TASK_REQUEST = 1;
    private SnappyDB snappy;
    private SmsVerificationReceiver smsVerificationReceiver;
    private String sms = "";
    private static final int REQUEST_PERMISSIONS = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);
        ButterKnife.bind(this);
        snappy = new SnappyDB(this);
    }

    @OnClick(R.id.addTaskBtn)
    public void onViewClicked() {
        Intent intent = new Intent(LifeCycleActivity.this, LifeCycleAddTaskActivity.class);
        startActivityForResult(intent, ADD_TASK_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "The onStart() event");
        showDialogue();
        sms = snappy.getSms();
        resultTextView.setText(resultTextView.getText().toString() + "\n" + snappy.getSms());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "The onResume() event");
        dateTimeTextView.setText(getCurrentTimeStamp());
        checkPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "The onPause() event");
        if (smsVerificationReceiver != null) {
            unregisterReceiver(smsVerificationReceiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "The onStop() event");
        snappy.setSms(sms);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "The onRestart() event");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "The onDestroy() event");
        snappy.closeDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK_REQUEST) {
            if (resultCode == RESULT_OK) {
                String task = data.getStringExtra(LifeCycleAddTaskActivity.EXTRA_TASK_DESCRIPTION);
                resultTextView.setText(resultTextView.getText().toString() + "\n" + task);
            }
        }
    }

    private void showDialogue() {
        AlertDialog alertDialog = new AlertDialog.Builder(LifeCycleActivity.this).create();
        alertDialog.setTitle("Welcome");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }

    @Override
    public void setText(String text) {
        sms = sms + "\n" + text;
        resultTextView.setText(resultTextView.getText().toString() + "\n"
                + text + " : " + getCurrentTimeStamp());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    smsVerificationReceiver = new SmsVerificationReceiver(this);
                    registerReceiver(smsVerificationReceiver, new IntentFilter(smsVerificationReceiver.SMS_RECEIVED_INTENT));
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enable Permissions from settings",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                return;
            }
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(LifeCycleActivity.this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (LifeCycleActivity.this, Manifest.permission.RECEIVE_SMS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(LifeCycleActivity.this,
                                        new String[]{Manifest.permission
                                                .RECEIVE_SMS},
                                        REQUEST_PERMISSIONS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(LifeCycleActivity.this,
                        new String[]{Manifest.permission
                                .RECEIVE_SMS},
                        REQUEST_PERMISSIONS);
            }
        } else {
            smsVerificationReceiver = new SmsVerificationReceiver(this);
            registerReceiver(smsVerificationReceiver, new IntentFilter(smsVerificationReceiver.SMS_RECEIVED_INTENT));
        }
    }
}
