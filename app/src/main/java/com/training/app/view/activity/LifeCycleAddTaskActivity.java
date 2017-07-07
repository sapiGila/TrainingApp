package com.training.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.training.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/6/2017.
 */

public class LifeCycleAddTaskActivity extends AppCompatActivity {

    @BindView(R.id.addTaskEditText)
    EditText addTaskEditText;

    public static final String EXTRA_TASK_DESCRIPTION = "task";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle_add_task);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addTaskBtn)
    public void onViewClicked() {
        String taskDescription = addTaskEditText.getText().toString();
        if (!taskDescription.isEmpty()) {
            Intent result = new Intent();
            result.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription);
            setResult(RESULT_OK, result);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
