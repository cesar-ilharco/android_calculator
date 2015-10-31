package com.myapp.calculator;


import com.myapp.calculator.ast.DigitUnit;
import com.myapp.calculator.ast.ExpressionUnit;
import com.myapp.calculator.ast.OperatorUnit;
import com.myapp.calculator.utils.MyInt;
import com.myapp.calculator.utils.Pair;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Android calculator app
 */

public class DisplayHelperTests {

    @Test
    public void testDisplayHelperInputDecimalPoint(){

        // True if decimal point insertion is possible, false otherwise.
        Map<LinkedList<ExpressionUnit>, Boolean> expressions = new HashMap<>();

        List<LinkedList<ExpressionUnit>> e = new ArrayList<>();
        for (int i=0; i<15; ++i){
            e.add(new LinkedList<ExpressionUnit>());
        }

        int i = 0;
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new DigitUnit("1"));
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new DigitUnit("2"));
        e.get(i).addLast(new DigitUnit("3"));
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new OperatorUnit("log"));
        e.get(i).addLast(new DigitUnit("2"));
        e.get(i).addLast(new DigitUnit("8"));
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new OperatorUnit("+"));
        e.get(i).addLast(new DigitUnit("2"));
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new OperatorUnit("×"));
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new DigitUnit("."));
        e.get(i).addLast(new DigitUnit("5"));
        e.get(i).addLast(new OperatorUnit("/"));
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new DigitUnit("."));
        e.get(i).addLast(new DigitUnit("2"));
        e.get(i).addLast(new DigitUnit("3"));
        e.get(i).addLast(new OperatorUnit("+"));
        expressions.put(e.get(i++), true);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new DigitUnit("0"));
        e.get(i).addLast(new DigitUnit("."));
        expressions.put(e.get(i++), false);

        e.get(i).addLast(new DigitUnit(".1"));
        expressions.put(e.get(i++), false);

        e.get(i).addLast(new DigitUnit("."));
        expressions.put(e.get(i++), false);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new DigitUnit("."));
        e.get(i).addLast(new DigitUnit("2"));
        e.get(i).addLast(new DigitUnit("3"));
        e.get(i).addLast(new OperatorUnit("+"));
        e.get(i).addLast(new DigitUnit("."));
        e.get(i).addLast(new DigitUnit("6"));
        expressions.put(e.get(i++), false);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new DigitUnit("."));
        e.get(i).addLast(new DigitUnit("2"));
        e.get(i).addLast(new DigitUnit("3"));
        e.get(i).addLast(new OperatorUnit("-"));
        e.get(i).addLast(new DigitUnit("4"));
        e.get(i).addLast(new DigitUnit("."));
        e.get(i).addLast(new DigitUnit("5"));
        e.get(i).addLast(new DigitUnit("7"));
        expressions.put(e.get(i++), false);

        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new OperatorUnit("/"));
        e.get(i).addLast(new DigitUnit("0"));
        e.get(i).addLast(new DigitUnit("."));
        e.get(i).addLast(new DigitUnit("5"));
        expressions.put(e.get(i), false);

        for (Map.Entry<LinkedList<ExpressionUnit>, Boolean> entry : expressions.entrySet()) {
            String append = entry.getValue() ? "." : "";
            String expected = DisplayHelper.convertToString(entry.getKey()) + append;
            DisplayHelper.getExpressionDisplay(entry.getKey(), new MyInt(entry.getKey().size()), ".");
            String actual = DisplayHelper.convertToString(entry.getKey());
            Assert.assertEquals(expected, actual);
        }
    }

    @Test // "+", "-", "×", "/".
    public void testDisplayHelperInputBasicOperator(){

        // Maps Pair<Expression, ButtonOperator> to the expected resulting Expression.
        Map<Pair<LinkedList<ExpressionUnit>, String>, String> expressions = new HashMap<>();

        expressions.put(Pair.create(new LinkedList<ExpressionUnit>(), "+"), "");
        expressions.put(Pair.create(new LinkedList<ExpressionUnit>(), "×"), "");
        expressions.put(Pair.create(new LinkedList<ExpressionUnit>(), "/"), "");
        expressions.put(Pair.create(new LinkedList<ExpressionUnit>(), "-"), "-");

        List<LinkedList<ExpressionUnit>> e = new ArrayList<>();

        int i = 0;
        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit("1"));
        expressions.put(Pair.create(e.get(i++), "+"), "1+");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new DigitUnit("0"));
        e.get(i).addLast(new DigitUnit("0"));
        expressions.put(Pair.create(e.get(i++), "/"), "100/");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit("1"));
        e.get(i).addLast(new OperatorUnit("+"));
        e.get(i).addLast(new DigitUnit("2"));
        e.get(i).addLast(new OperatorUnit("×"));
        e.get(i).addLast(new DigitUnit("3"));
        expressions.put(Pair.create(e.get(i++), "-"), "1+2×3-");

        for (String op1 : DisplayHelper.basicOperators) {
            for (String op2 : DisplayHelper.basicOperators) {
                if (! op2.equals("-")) {
                    e.add(new LinkedList<ExpressionUnit>());
                    e.get(i).addLast(new DigitUnit(Integer.toString(i)));
                    e.get(i).addLast(new OperatorUnit(op1));
                    expressions.put(Pair.create(e.get(i), op2), Integer.toString(i++) + op2);
                }
            }
        }

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("+"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "-");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "+");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("×"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "×-");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("/"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "/-");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("×"));
        e.get(i).addLast(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "×");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("/"));
        e.get(i).addLast(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "/");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("×"));
        e.get(i).addLast(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "+"), Integer.toString(i++) + "+");

        e.add(new LinkedList<ExpressionUnit>());
        e.get(i).addLast(new DigitUnit(Integer.toString(i)));
        e.get(i).addLast(new OperatorUnit("/"));
        e.get(i).addLast(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "×"), Integer.toString(i) + "×");

        for ( Map.Entry<Pair<LinkedList<ExpressionUnit>, String>, String>  entry : expressions.entrySet()) {
            DisplayHelper.getExpressionDisplay(entry.getKey().getFirst(),
                          new MyInt(entry.getKey().getFirst().size()), entry.getKey().getSecond());
            String result = DisplayHelper.convertToString(entry.getKey().getFirst());
            Assert.assertEquals(entry.getValue(), result);
        }
    }

    // TODO: Add tests for insertion in positions other than the end of the expression.

}
