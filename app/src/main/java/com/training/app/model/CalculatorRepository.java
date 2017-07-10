package com.training.app.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Dell on 7/4/2017.
 */

public class CalculatorRepository implements CalculatorModel {

    @Override
    public BigDecimal add(BigDecimal op1, BigDecimal op2) {
        return op1.add(op2);
    }

    @Override
    public BigDecimal mult(BigDecimal op1, BigDecimal op2) {
        return op1.multiply(op2);
    }

    @Override
    public BigDecimal div(BigDecimal op1, BigDecimal op2) {
        try {
            return op1.divide(op2, 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.valueOf(0);
        }
    }

    @Override
    public BigDecimal sub(BigDecimal op1, BigDecimal op2) {
        return op1.subtract(op2);
    }
}
