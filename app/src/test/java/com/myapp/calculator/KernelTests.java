package com.myapp.calculator;

import com.myapp.calculator.ast.DigitUnit;
import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.ast.ExpressionNode;
import com.myapp.calculator.ast.ExpressionUnit;
import com.myapp.calculator.ast.NumberUnit;
import com.myapp.calculator.ast.OperatorUnit;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Android calculator app
 */
public class KernelTests {

    @Test
    public void testKernelEvaluator() throws IOException{
        Expression expression = new Expression();
        // 1 + 2 = 3
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        expression.getUnits().addLast(new DigitUnit("2"));
        Assert.assertEquals("3", DisplayHelper.getResultDisplay(expression, 1));
        Assert.assertEquals("3", DisplayHelper.getResultDisplay(expression, 1000));

        // 1 + 2 × 4 - 3 = 6
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("4"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("3"));
        Assert.assertEquals("6", DisplayHelper.getResultDisplay(expression, 1));
        Assert.assertEquals("6", DisplayHelper.getResultDisplay(expression, 1000));

        // 1 × 2 × 3 × 4 × 5 = 120
        expression.getUnits().clear();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("3"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("4"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("5"));
//        Assert.assertEquals("1E2", DisplayHelper.getResultDisplay(expression, 1));
//        Assert.assertEquals("1.2E2", DisplayHelper.getResultDisplay(expression, 2));
        Assert.assertEquals("120", DisplayHelper.getResultDisplay(expression, 3));
        Assert.assertEquals("120", DisplayHelper.getResultDisplay(expression, 1000));

        // 1 - 2 × 3 + 4 × 5 = 15
        expression.getUnits().clear();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("3"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        expression.getUnits().addLast(new DigitUnit("4"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("5"));
//        Assert.assertEquals("1E1", DisplayHelper.getResultDisplay(expression, 1));
        Assert.assertEquals("15", DisplayHelper.getResultDisplay(expression, 2));
        Assert.assertEquals("15", DisplayHelper.getResultDisplay(expression, 1000));

        // 1 / 3 = 0.333...
        expression.getUnits().clear();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("/"));
        expression.getUnits().addLast(new DigitUnit("3"));
        Assert.assertEquals("0.3", DisplayHelper.getResultDisplay(expression, 1));
        Assert.assertEquals("0.33", DisplayHelper.getResultDisplay(expression, 2));
        Assert.assertEquals("0.3333333333", DisplayHelper.getResultDisplay(expression, 10));

        StringBuilder oneThird = new StringBuilder("0.");
        int scale = 2000;
        for (int i=0; i<scale; i++){
            oneThird.append("3");
        }

        Assert.assertEquals(oneThird.toString(), DisplayHelper.getResultDisplay(expression, scale));

        // -1 × -2 = 2
        expression.getUnits().clear();
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("2"));
        Assert.assertEquals("2", DisplayHelper.getResultDisplay(expression, 1));
        Assert.assertEquals("2", DisplayHelper.getResultDisplay(expression, 1000));

        // TODO: Add more complex expressions:

    }

    @Test
    public void testDigitsToNumber(){
        LinkedList<ExpressionUnit> expressionUnits = new LinkedList<>();

        // 123
        expressionUnits.addLast(new DigitUnit("1"));
        expressionUnits.addLast(new DigitUnit("2"));
        expressionUnits.addLast(new DigitUnit("3"));
        List<ExpressionUnit> convertedExpression = Kernel.digitsToNumber(expressionUnits);
        Assert.assertEquals(Arrays.asList(new NumberUnit("123")), convertedExpression);

        // -50-1×-23+4
        expressionUnits.clear();
        expressionUnits.addLast(new OperatorUnit("-"));
        expressionUnits.addLast(new DigitUnit("5"));
        expressionUnits.addLast(new DigitUnit("0"));
        expressionUnits.addLast(new OperatorUnit("-"));
        expressionUnits.addLast(new DigitUnit("1"));
        expressionUnits.addLast(new OperatorUnit("×"));
        expressionUnits.addLast(new OperatorUnit("-"));
        expressionUnits.addLast(new DigitUnit("2"));
        expressionUnits.addLast(new DigitUnit("3"));
        expressionUnits.addLast(new OperatorUnit("+"));
        expressionUnits.addLast(new DigitUnit("4"));
        convertedExpression = Kernel.digitsToNumber(expressionUnits);
        List<ExpressionUnit> expectedResult = Arrays.asList(
                new NumberUnit("-50"),
                new OperatorUnit("-"),
                new NumberUnit("1"),
                new OperatorUnit("×"),
                new NumberUnit("-23"),
                new OperatorUnit("+"),
                new NumberUnit("4")
        );

        Assert.assertEquals(expectedResult, convertedExpression);
    }

}
