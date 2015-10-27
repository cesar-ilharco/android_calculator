package com.myapp.calculator;

import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
        put("×", 2);
        put("/", 3);
    }};

    private static int scale = 10;

    public static void setScale(int scale) {
        Kernel.scale = scale;
    }
    public static int getScale() {
        return scale;
    }

    // TODO: Implement expression evaluation from Syntax Tree. Handle exceptions properly.
    public static String evaluate (LinkedList<ExpressionUnit> expressionUnits) {

        List<ExpressionUnit>  convertedList= digitsToNumber(expressionUnits);

        if (! isValid(convertedList)){
            return "formatting error";
        }

        BigDecimal result = null;
        try {
            ExpressionNode root = parse(convertedList);
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
            case "×": return evaluateRecursive(root.getLeft()).multiply(evaluateRecursive(root.getRight()));
            case "/": return evaluateRecursive(root.getLeft()).divide(evaluateRecursive(root.getRight()), scale, BigDecimal.ROUND_HALF_EVEN);
        }
        return new BigDecimal(root.getExpressionUnit().getText());
    }

    // TODO: Implement expression verifier.
    static boolean isValid (final List<ExpressionUnit> expressionUnits){
        return true;
    }

    // Create Syntax Tree from list of expression units.
    public static ExpressionNode parse(final List<ExpressionUnit> expressionUnits) throws  IOException {
        if (expressionUnits.isEmpty()){
            return null;
        }
        return parseAddSub(expressionUnits, new MyInt(-1));
    }

    private static ExpressionNode parseAddSub(final List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = parseMulDiv(expressionUnits, index);
        if (index.getValue() < expressionUnits.size() - 1) {

            ExpressionUnit expressionUnit = expressionUnits.get(index.increaseAndGet());

            while (expressionUnit.getText().equals("+") || expressionUnit.getText().equals("-")) {
                root = new ExpressionNode(expressionUnit, root, parseMulDiv(expressionUnits, index));
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increaseAndGet());
                } else {
                    index.increaseAndGet();
                    break;
                }
            }
        }
        index.decreaseAndGet();
        // System.out.println("DEBUG: Exiting ParseAddSub, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }


    private static ExpressionNode parseMulDiv(final List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = parseSimpleTerm(expressionUnits, index);

        if (index.getValue() < expressionUnits.size() - 1) {
            ExpressionUnit expressionUnit = expressionUnits.get(index.increaseAndGet());

            while (expressionUnit.getText().equals("×") || expressionUnit.getText().equals("/")) {
                root = new ExpressionNode(expressionUnit, root, parseSimpleTerm(expressionUnits, index));
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increaseAndGet());
                } else {
                    index.increaseAndGet();
                    break;
                }
            }
        }
        index.decreaseAndGet();
        // System.out.println("DEBUG: Exiting ParseMultiDiv, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }

    private static ExpressionNode parseSimpleTerm(final List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = null;

        if (index.getValue() < expressionUnits.size() - 1) {
            ExpressionUnit expressionUnit = expressionUnits.get(index.increaseAndGet());
            if (expressionUnit instanceof NumberUnit) {
                root = new ExpressionNode(expressionUnit);
            } else if (expressionUnit.getText().equals("(")) {
                root = parseAddSub(expressionUnits, index);
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increaseAndGet());
                    if (!expressionUnit.getText().equals(")")) {
                        throw new IOException("Unbalanced parenthesis count");
                    }
                } else {
                    index.increaseAndGet();
                }
            }
        }
        // System.out.println("DEBUG: Exiting ParseSimpleTerm, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }

    // Convert DigitUnits into NumberInit and return it as an ArrayList.
    public static List<ExpressionUnit> digitsToNumber (LinkedList<ExpressionUnit> expressionUnits){
        List<ExpressionUnit> result = new ArrayList<>();

        StringBuilder currentNumber = new StringBuilder();
        for (ExpressionUnit expressionUnit : expressionUnits){
            if (expressionUnit instanceof OperatorUnit){
                // Operator "-" can sometimes be the prefix of a negative number.
                if (expressionUnit.getText().equals("-") && currentNumber.length() == 0){
                    currentNumber.append("-");
                }
                // Check if a number was being built. If so, add it to the result.
                else {
                    if (currentNumber.length() > 0){
                        result.add(new NumberUnit(currentNumber.toString()));
                        currentNumber = new StringBuilder();
                    }
                    result.add(expressionUnit);
                }
            }
            // If it's a digit, append it to the currentNumber.
            else {
                currentNumber.append(expressionUnit.getText());
            }
        }
        // Add last number if existent.
        if (currentNumber.length() > 0){
            result.add(new NumberUnit(currentNumber.toString()));
        }
        return result;
    }


}
