package com.training.app.presenter;

import android.text.TextUtils;

import com.training.app.contract.CalculatorContract;
import com.training.app.model.CalculatorModel;
import com.training.app.model.CalculatorRepository;
import com.training.app.object.Calculator;

import java.math.BigDecimal;

/**
 * Created by Dell on 7/4/2017.
 */

public class CalculatorPresenter implements CalculatorContract.Presenter {

    private CalculatorContract.View view;
    private CalculatorModel model;

    public CalculatorPresenter(CalculatorContract.View view) {
        this.view = view;
        model = new CalculatorRepository();
    }

    @Override
    public void doCalculate(Calculator calculator) {
        BigDecimal result = BigDecimal.valueOf(0);
        switch (calculator.getOperator()) {
            case ADD:
                result = model.add(getValue(calculator.getInputValue1()), getValue(calculator.getInputValue2()));
                break;
            case MUL:
                result = model.mult(getValue(calculator.getInputValue1()), getValue(calculator.getInputValue2()));
                break;
            case DIV:
                result = model.div(getValue(calculator.getInputValue1()), getValue(calculator.getInputValue2()));
                break;
            case SUB:
                result = model.sub(getValue(calculator.getInputValue1()), getValue(calculator.getInputValue2()));
                break;
        }
        view.updateDisplay(result);
    }

    private BigDecimal getValue(String text) {
        String value = "0";
        if (!TextUtils.isEmpty(text)) {
            value = text;
        }
        return BigDecimal.valueOf(Double.valueOf(value));
    }
}
