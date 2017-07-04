package com.training.app.object;

import com.training.app.enumeration.Operator;

/**
 * Created by Dell on 7/4/2017.
 */

public class Calculator {

    private Operator operator;
    private String inputValue1;
    private String inputValue2;

    public Calculator(Operator operator,
                      String inputValue1,
                      String inputValue2) {
        this.operator = operator;
        this.inputValue1 = inputValue1;
        this.inputValue2 = inputValue2;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getInputValue1() {
        return inputValue1;
    }

    public String getInputValue2() {
        return inputValue2;
    }
}
