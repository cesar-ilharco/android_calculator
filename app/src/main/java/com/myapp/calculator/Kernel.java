package com.myapp.calculator;

import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Android calculator app
 */

// Process user's input expression and computes the output result.
public class Kernel {

    // TODO: Create a global operator precedence and generalize the Syntax Tree construction (parse method).
    private static final Map<String, Integer> operatorPrecedence = new HashMap<String, Integer>(){{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 3);
    }};

    private static int scale = 100;

    public static void setScale(int scale) {
        Kernel.scale = scale;
    }

    // TODO: Implement expression evaluation from Syntax Tree. Handle exceptions properly.
    public static String evaluate (Stack<ExpressionUnit> expressionUnits) {
        if (! isValid(expressionUnits)){
            return "formatting error";
        }
        BigDecimal result = null;
        try {
            ExpressionNode root = parse(expressionUnits);
            result = evaluateRecursive(root);
        } catch (IOException e){

        }

        return result == null ? "" : result.toString();
    }

    private static BigDecimal evaluateRecursive (ExpressionNode root){
        if (root == null){
            return null;
        }
        switch(root.getExpressionUnit().getText()){
            case "+": return evaluateRecursive(root.getLeft()).add(evaluateRecursive(root.getRight()));
            case "-": return evaluateRecursive(root.getLeft()).subtract(evaluateRecursive(root.getRight()));
            case "*": return evaluateRecursive(root.getLeft()).multiply(evaluateRecursive(root.getRight()));
            case "/": return evaluateRecursive(root.getLeft()).divide(evaluateRecursive(root.getRight()), scale, BigDecimal.ROUND_HALF_EVEN);
        }
        return new BigDecimal(root.getExpressionUnit().getText());
    }

    // TODO: Implement expression verifier.
    @VisibleForTesting
    static boolean isValid (Stack<ExpressionUnit> expressionUnits){
        return true;
    }

    // Create Syntax Tree from list of expression units.
    @VisibleForTesting
    public static ExpressionNode parse(final Stack<ExpressionUnit> expressionUnits) throws  IOException {
        if (expressionUnits.isEmpty()){
            return null;
        }
        List<ExpressionUnit> expressionUnitList = new ArrayList<>(expressionUnits);
        return parseAddSub(expressionUnitList, new MyInt(-1));
    }

    private static ExpressionNode parseAddSub(List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = parseMulDiv(expressionUnits, index);
        if (index.getValue() < expressionUnits.size() - 1) {

            ExpressionUnit expressionUnit = expressionUnits.get(index.increase());

            while (expressionUnit.getText().equals("+") || expressionUnit.getText().equals("-")) {
                root = new ExpressionNode(expressionUnit, root, parseMulDiv(expressionUnits, index));
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increase());
                } else {
                    index.increase();
                    break;
                }
            }
        }
        index.decrease();
        // System.out.println("DEBUG: Exiting ParseAddSub, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }


    private static ExpressionNode parseMulDiv(List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = parseSimpleTerm(expressionUnits, index);

        if (index.getValue() < expressionUnits.size() - 1) {
            ExpressionUnit expressionUnit = expressionUnits.get(index.increase());

            while (expressionUnit.getText().equals("*") || expressionUnit.getText().equals("/")) {
                root = new ExpressionNode(expressionUnit, root, parseSimpleTerm(expressionUnits, index));
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increase());
                } else {
                    index.increase();
                    break;
                }
            }
        }
        index.decrease();
        // System.out.println("DEBUG: Exiting ParseMultiDiv, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }

    private static ExpressionNode parseSimpleTerm(List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = null;

        if (index.getValue() < expressionUnits.size() - 1) {
            ExpressionUnit expressionUnit = expressionUnits.get(index.increase());
            if (expressionUnit instanceof NumberUnit) {
                root = new ExpressionNode(expressionUnit);
            } else if (expressionUnit.getText().equals("(")) {
                root = parseAddSub(expressionUnits, index);
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increase());
                    if (!expressionUnit.getText().equals(")")) {
                        throw new IOException("Unbalanced parenthesis count");
                    }
                } else {
                    index.increase();
                }
            }
        }
        // System.out.println("DEBUG: Exiting ParseSimpleTerm, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }

    private static class MyInt {
        private int value;
        public MyInt (int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
        public int increase(){
            return ++value;
        }
        public int decrease(){
            return --value;
        }
    }

}
