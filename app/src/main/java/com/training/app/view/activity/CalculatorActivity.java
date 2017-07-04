package com.training.app.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.training.app.R;
import com.training.app.contract.CalculatorContract;
import com.training.app.enumeration.Operator;
import com.training.app.object.Calculator;
import com.training.app.presenter.CalculatorPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 7/4/2017.
 */

public class CalculatorActivity extends AppCompatActivity implements CalculatorContract.View {

    @BindView(R.id.input_value_1)
    EditText inputValue1;
    @BindView(R.id.operation_selector)
    Spinner operationSelector;
    @BindView(R.id.input_value_2)
    EditText inputValue2;
    @BindView(R.id.result_value)
    TextView resultValue;

    private CalculatorContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        ButterKnife.bind(this);
        initSpinner();
        presenter = new CalculatorPresenter(this);
    }

    private void initSpinner() {
        //initialize spinner
        ArrayList<Operator> operators = new ArrayList<>();
        operators.add(Operator.ADD);
        operators.add(Operator.MUL);
        operators.add(Operator.SUB);
        operators.add(Operator.DIV);

        ArrayAdapter<Operator> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, operators);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operationSelector.setAdapter(dataAdapter);
    }

    @Override
    public void updateDisplay(double value) {
        resultValue.setText(Double.toString(value));
    }

    @OnClick(R.id.calculate_button)
    public void onViewClicked() {
        Calculator request = new Calculator((Operator) operationSelector.getAdapter()
                .getItem(operationSelector.getSelectedItemPosition()),
                inputValue1.getText().toString(),
                inputValue2.getText().toString());
        presenter.doCalculate(request);
    }
}
