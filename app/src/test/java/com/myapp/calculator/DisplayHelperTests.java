package com.myapp.calculator;


import com.myapp.calculator.ast.DigitUnit;
import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.ast.OperatorUnit;
import com.myapp.calculator.utils.MyInt;
import com.myapp.calculator.utils.Pair;

import junit.framework.Assert;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Android calculator app
 */

public class DisplayHelperTests {

    @Test
    public void testDisplayHelperInputDecimalPoint(){

        // True if decimal point insertion is possible, false otherwise.
        Map<Expression, Boolean> mapExpressions = new HashMap<>();

        Expression expression = new Expression();
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new DigitUnit("3"));
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new OperatorUnit("log"));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new DigitUnit("8"));
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        expression.getUnits().addLast(new DigitUnit("2"));
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("."));
        expression.getUnits().addLast(new DigitUnit("5"));
        expression.getUnits().addLast(new OperatorUnit("/"));
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new DigitUnit("."));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new DigitUnit("3"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        mapExpressions.put(expression, true);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new DigitUnit("0"));
        expression.getUnits().addLast(new DigitUnit("."));
        mapExpressions.put(expression, false);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(".1"));
        mapExpressions.put(expression, false);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("."));
        mapExpressions.put(expression, false);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new DigitUnit("."));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new DigitUnit("3"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        expression.getUnits().addLast(new DigitUnit("."));
        expression.getUnits().addLast(new DigitUnit("6"));
        mapExpressions.put(expression, false);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new DigitUnit("."));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new DigitUnit("3"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("4"));
        expression.getUnits().addLast(new DigitUnit("."));
        expression.getUnits().addLast(new DigitUnit("5"));
        expression.getUnits().addLast(new DigitUnit("7"));
        mapExpressions.put(expression, false);

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("/"));
        expression.getUnits().addLast(new DigitUnit("0"));
        expression.getUnits().addLast(new DigitUnit("."));
        expression.getUnits().addLast(new DigitUnit("5"));
        mapExpressions.put(expression, false);

        for (Map.Entry<Expression, Boolean> entry : mapExpressions.entrySet()) {
            String append = entry.getValue() ? "." : "";
            String expected = DisplayHelper.convertToString(entry.getKey().getUnits()) + append;
            Expression updated = DisplayHelper.updateExpression(entry.getKey(), new MyInt(entry.getKey().getUnits().size()), ".");
            String actual = DisplayHelper.convertToString(updated.getUnits());
            Assert.assertEquals(expected, actual);
        }
    }

    @Test // "+", "-", "×", "/".
    public void testDisplayHelperInputBasicOperator(){

        // Maps Pair<Expression, ButtonOperator> to the expected resulting Expression.
        Map<Pair<Expression, String>, String> mapExpressions = new HashMap<>();

        mapExpressions.put(Pair.create(new Expression(), "+"), "");
        mapExpressions.put(Pair.create(new Expression(), "×"), "");
        mapExpressions.put(Pair.create(new Expression(), "/"), "");
        mapExpressions.put(Pair.create(new Expression(), "-"), "-");

        Expression expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        mapExpressions.put(Pair.create(expression, "+"), "1+");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new DigitUnit("0"));
        expression.getUnits().addLast(new DigitUnit("0"));
        mapExpressions.put(Pair.create(expression, "/"), "100/");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("3"));
        mapExpressions.put(Pair.create(expression, "-"), "1+2×3-");

        int i = 0;
        for (String op1 : DisplayHelper.basicOperators) {
            for (String op2 : DisplayHelper.basicOperators) {
                if (! op2.equals("-")) {
                    expression = new Expression();
                    expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
                    expression.getUnits().addLast(new OperatorUnit(op1));
                    mapExpressions.put(Pair.create(expression, op2), Integer.toString(i++) + op2);
                }
            }
        }

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("+"));
        mapExpressions.put(Pair.create(expression, "-"), Integer.toString(i++) + "-");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("-"));
        mapExpressions.put(Pair.create(expression, "-"), Integer.toString(i++) + "+");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("×"));
        mapExpressions.put(Pair.create(expression, "-"), Integer.toString(i++) + "×-");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("/"));
        mapExpressions.put(Pair.create(expression, "-"), Integer.toString(i++) + "/-");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        mapExpressions.put(Pair.create(expression, "-"), Integer.toString(i++) + "×");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("/"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        mapExpressions.put(Pair.create(expression, "-"), Integer.toString(i++) + "/");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        mapExpressions.put(Pair.create(expression, "+"), Integer.toString(i++) + "+");

        expression = new Expression();
        expression.getUnits().addLast(new DigitUnit(Integer.toString(i)));
        expression.getUnits().addLast(new OperatorUnit("/"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        mapExpressions.put(Pair.create(expression, "×"), Integer.toString(i) + "×");

        for (Map.Entry<Pair<Expression, String>, String>  entry : mapExpressions.entrySet()) {
            Expression updated = DisplayHelper.updateExpression(entry.getKey().getFirst(),
                    new MyInt(entry.getKey().getFirst().getUnits().size()), entry.getKey().getSecond());
            String result = DisplayHelper.convertToString(updated.getUnits());
            Assert.assertEquals(entry.getValue(), result);
        }
    }

    // TODO: Add tests for insertion in positions other than the end of the expression.

}
