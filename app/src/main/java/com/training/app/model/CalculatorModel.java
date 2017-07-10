package com.training.app.model;

import java.math.BigDecimal;

/**
 * Created by Dell on 7/4/2017.
 */

public interface CalculatorModel {

    BigDecimal add(BigDecimal op1, BigDecimal op2);
    BigDecimal mult(BigDecimal op1, BigDecimal op2);
    BigDecimal div(BigDecimal op1, BigDecimal op2);
    BigDecimal sub(BigDecimal op1, BigDecimal op2);

}
