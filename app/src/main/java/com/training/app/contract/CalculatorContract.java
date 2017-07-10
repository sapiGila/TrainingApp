package com.training.app.contract;

import com.training.app.object.Calculator;

import java.math.BigDecimal;

/**
 * Created by Dell on 7/4/2017.
 */

public interface CalculatorContract {

    interface Presenter {
        void doCalculate(Calculator calculator);
    }

    interface View {
        void updateDisplay(BigDecimal value);
    }

}
