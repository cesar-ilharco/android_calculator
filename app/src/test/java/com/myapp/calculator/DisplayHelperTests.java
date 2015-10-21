package com.myapp.calculator;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


/**
 * Android calculator app
 */
public class DisplayHelperTests {

    @Test
    public void testDisplayHelperInputDecimalPoint(){
        // True if decimal point insertion is possible, false otherwise.
        Map<Stack<ExpressionUnit>, Boolean> expressions = new HashMap<>();

        List<Stack<ExpressionUnit>> e = new ArrayList<>();
        for (int i=0; i<15; ++i){
            e.add(new Stack<ExpressionUnit>());
        }

        int i = 0;
        expressions.put(e.get(i++), true);

        e.get(i).push(new NumberUnit("1"));
        expressions.put(e.get(i++), true);

        e.get(i).push(new NumberUnit("1234567890"));
        expressions.put(e.get(i++), true);

        e.get(i).push(new OperatorUnit("log"));
        e.get(i).push(new NumberUnit("28"));
        expressions.put(e.get(i++), true);

        e.get(i).push(new NumberUnit("1"));
        e.get(i).push(new OperatorUnit("+"));
        e.get(i).push(new NumberUnit("2"));
        expressions.put(e.get(i++), true);

        e.get(i).push(new NumberUnit("1"));
        e.get(i).push(new OperatorUnit("*"));
        expressions.put(e.get(i++), true);

        e.get(i).push(new NumberUnit(".5"));
        e.get(i).push(new OperatorUnit("/"));
        expressions.put(e.get(i++), true);

        e.get(i).push(new NumberUnit("1.23"));
        e.get(i).push(new OperatorUnit("+"));
        expressions.put(e.get(i++), true);

        e.get(i).push(new NumberUnit("10."));
        expressions.put(e.get(i++), false);

        e.get(i).push(new NumberUnit("3.1415926535"));
        expressions.put(e.get(i++), false);

        e.get(i).push(new NumberUnit(".1"));
        expressions.put(e.get(i++), false);

        e.get(i).push(new NumberUnit("."));
        expressions.put(e.get(i++), false);

        e.get(i).push(new NumberUnit("1.23"));
        e.get(i).push(new OperatorUnit("+"));
        e.get(i).push(new NumberUnit(".6"));
        expressions.put(e.get(i++), false);

        e.get(i).push(new NumberUnit("1.23"));
        e.get(i).push(new OperatorUnit("-"));
        e.get(i).push(new NumberUnit("4.57"));
        expressions.put(e.get(i++), false);

        e.get(i).push(new NumberUnit("1"));
        e.get(i).push(new OperatorUnit("/"));
        e.get(i).push(new NumberUnit("0.5"));
        expressions.put(e.get(i), false);

        for (Map.Entry<Stack<ExpressionUnit>, Boolean> entry : expressions.entrySet()) {
            String append = entry.getValue() ? "." : "";
            Assert.assertEquals(DisplayHelper.toString(entry.getKey()) + append, DisplayHelper.getExpressionDisplay(entry.getKey(), "."));
        }
    }

    @Test // "+", "-", "*", "/".
    public void testDisplayHelperInputBasicOperator(){

        // Maps Pair<Expression, ButtonOperator> to the expected resulting Expression.
        Map<Pair<Stack<ExpressionUnit>, String>, String> expressions = new HashMap<>();

        expressions.put(Pair.create(new Stack<ExpressionUnit>(), "+"), "");
        expressions.put(Pair.create(new Stack<ExpressionUnit>(), "*"), "");
        expressions.put(Pair.create(new Stack<ExpressionUnit>(), "/"), "");
        expressions.put(Pair.create(new Stack<ExpressionUnit>(), "-"), "-");

        List<Stack<ExpressionUnit>> e = new ArrayList<>();

        int i = 0;
        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit("1"));
        expressions.put(Pair.create(e.get(i++), "+"), "1+");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit("100"));
        expressions.put(Pair.create(e.get(i++), "/"), "100/");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit("1"));
        e.get(i).push(new OperatorUnit("+"));
        e.get(i).push(new NumberUnit("2"));
        e.get(i).push(new OperatorUnit("*"));
        e.get(i).push(new NumberUnit("3"));
        expressions.put(Pair.create(e.get(i++), "-"), "1+2*3-");

        for (String op1 : DisplayHelper.basicOperators) {
            for (String op2 : DisplayHelper.basicOperators) {
                if (! op2.equals("-")) {
                    e.add(new Stack<ExpressionUnit>());
                    e.get(i).push(new NumberUnit(Integer.toString(i)));
                    e.get(i).push(new OperatorUnit(op1));
                    expressions.put(Pair.create(e.get(i), op2), Integer.toString(i++) + op2);
                }
            }
        }

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("+"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "-");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "+");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("*"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "*-");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("/"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "/-");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("*"));
        e.get(i).push(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "*");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("/"));
        e.get(i).push(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "-"), Integer.toString(i++) + "/");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("*"));
        e.get(i).push(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "+"), Integer.toString(i++) + "+");

        e.add(new Stack<ExpressionUnit>());
        e.get(i).push(new NumberUnit(Integer.toString(i)));
        e.get(i).push(new OperatorUnit("/"));
        e.get(i).push(new OperatorUnit("-"));
        expressions.put(Pair.create(e.get(i), "*"), Integer.toString(i) + "*");

        for ( Map.Entry<Pair<Stack<ExpressionUnit>, String>, String>  entry : expressions.entrySet()) {
            String result = DisplayHelper.getExpressionDisplay(entry.getKey().getFirst(), entry.getKey().getSecond());
            Assert.assertEquals(entry.getValue(), result);
        }
    }
}
