package com.training.app.presenter;

import com.training.app.contract.CalculatorContract;
import com.training.app.enumeration.Operator;
import com.training.app.object.Calculator;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;


/**
 * Created by Dell on 7/4/2017.
 */
public class CalculatorPresenterTest extends TestCase {

    private CalculatorContract.View view;
    private CalculatorContract.Presenter presenter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        view = mock(CalculatorContract.View.class);
        presenter = new CalculatorPresenter(view);
    }

    @Test
    public void testDoCalculateAdd() throws Exception {
        Calculator request = new Calculator(Operator.ADD, "5", "2");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(BigDecimal.valueOf(Double.valueOf("7")));
    }

    @Test
    public void testDoCalculateMultiply() throws Exception {
        Calculator request = new Calculator(Operator.MUL, "5", "2");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(new BigDecimal("10.00"));
    }

    @Test
    public void testDoCalculateSubtract() throws Exception {
        Calculator request = new Calculator(Operator.SUB, "5", "2");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(BigDecimal.valueOf(Double.valueOf("3")));
    }

    @Test
    public void testDoCalculateSubtractMinusResult() throws Exception {
        Calculator request = new Calculator(Operator.SUB, "2", "5");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(BigDecimal.valueOf(Double.valueOf("-3")));
    }

    @Test
    public void testDoCalculateDivideDecimalResult() throws Exception {
        Calculator request = new Calculator(Operator.DIV, "2", "5");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(new BigDecimal("0.40"));
    }

    @Test
    public void testDoCalculateDivide() throws Exception {
        Calculator request = new Calculator(Operator.DIV, "10", "5");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(new BigDecimal("2.00"));
    }

    @Test
    public void testDoCalculateDivideZeroResult() throws Exception {
        Calculator request = new Calculator(Operator.DIV, "0", "5");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(new BigDecimal("0.00"));
    }

    @Test
    public void testDoCalculateDivideInfinityResult() throws Exception {
        Calculator request = new Calculator(Operator.DIV, "5", "0");
        presenter.doCalculate(request);
        Mockito.verify(view).updateDisplay(new BigDecimal("0"));
    }
}