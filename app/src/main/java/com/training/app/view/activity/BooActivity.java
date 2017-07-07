package com.training.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.training.app.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/6/2017.
 */

public class BooActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back_button)
    public void onViewClicked() {
        openLifeCycleActivity();
    }

    private void openLifeCycleActivity() {
        Intent openLifeCycleActivity = new Intent(BooActivity.this, LifeCycleActivity.class);
        openLifeCycleActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openLifeCycleActivity, 0);
    }
}
