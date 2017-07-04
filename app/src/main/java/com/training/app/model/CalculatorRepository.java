package com.training.app.model;

/**
 * Created by Dell on 7/4/2017.
 */

public class CalculatorRepository implements CalculatorModel {

    @Override
    public double add(double op1, double op2) {
        return op1 + op2;
    }

    @Override
    public double mult(double op1, double op2) {
        return op1 * op2;
    }

    @Override
    public double div(double op1, double op2) {
        try {
            return op1 / op2;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public double sub(double op1, double op2) {
        return op1 - op2;
    }
}
