package com.myapp.calculator;


import com.myapp.calculator.ast.DigitUnit;
import com.myapp.calculator.ast.ExpressionUnit;
import com.myapp.calculator.ast.OperatorUnit;
import com.myapp.calculator.utils.MyInt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Android calculator app
 */

// Helper for displaying input and output correctly.
public class DisplayHelper {

    static final Set<String> numberButtons = new HashSet<>(Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."));

    static final Set<String> basicOperators = new HashSet<>(Arrays.asList("+", "-", "/", "×"));

    static final Set<String> function1Operators = new HashSet<>(Arrays.asList(
            "ln", "log", "sin", "cos", "tan", "Fib", "isPrime", "sinh", "cosh", "tanh",
            "arcsin", "arccos", "arctan", "arcsinh", "arccosh", "arctanh" ));

    static final Set<String> expOperators = new HashSet<>(Arrays.asList("e^x", "10^x", "x²"));

    // TODO: Handle edge cases such as '.' + '.' = '.' or '-' + '-' = '+'.
    public static String getExpressionDisplay (LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition, String buttonPressed){
        if (buttonPressed.equals("del")){
            if (cursorPosition.getValue() > 0){
                expressionUnits.remove(cursorPosition.decreaseAndGet());
            }
        } else if (buttonPressed.equals("clear")){
            expressionUnits.clear();
            cursorPosition.reset();
        } else if (isNumber(buttonPressed)){
            addDigit(expressionUnits, cursorPosition, buttonPressed);
        } else if (basicOperators.contains(buttonPressed)){
            addBasicOperator(expressionUnits, cursorPosition, buttonPressed);
        } else if (expOperators.contains(buttonPressed)) {
            addExpOperator(expressionUnits, cursorPosition, buttonPressed);
        } else if (function1Operators.contains(buttonPressed)) {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit(buttonPressed + "("));
            expressionUnits.add(cursorPosition.getValue(), new OperatorUnit(")"));
        } else {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit(buttonPressed));
        }

        return toString(expressionUnits, cursorPosition, true);
    }

    private static void addExpOperator(LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition,String buttonPressed) {
        if (buttonPressed.equals("e^x")){
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("exp("));
            expressionUnits.add(cursorPosition.getValue(), new OperatorUnit(")"));
        } else if (buttonPressed.equals("10^x")){
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("1"));
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("0"));
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("^"));
        } else {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("^"));
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("2"));
        }
    }

    public static String getResultDisplay (LinkedList<ExpressionUnit> expressionUnits){
        return Kernel.evaluate(expressionUnits);
    }

    // Java iterates stack from the bottom to the top. Method adds the cursor character.
    public static String toString (LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition, boolean isCursorVisible){
        String cursor = isCursorVisible ? "|" : " ";
        StringBuffer stringBuffer = new StringBuffer();
        int size = expressionUnits.size();
        Iterator<ExpressionUnit> iterator = expressionUnits.iterator();
        for (int i=0; i<size; ++i){
            if (cursorPosition.getValue() == i){
                stringBuffer.append(cursor);
            }
            stringBuffer.append(iterator.next().getText());
        }
        if (cursorPosition.getValue() == size){
            stringBuffer.append(cursor);
        }
        return stringBuffer.toString();
    }

    // Method does not add the cursor character.
    public static String toString (LinkedList<ExpressionUnit> expressionUnits){
        StringBuffer stringBuffer = new StringBuffer();
        for (ExpressionUnit expressionUnit : expressionUnits){
            stringBuffer.append(expressionUnit.getText());
        }
        return stringBuffer.toString();
    }


    // If the last character from expression is a basic operator, replace it except for a few cases.
    // ++ = +,  +- = -,  +* = *, +/ = /, -+ = +, -/ = /, -* = *, ** = *, */ = /, // = /, /+ = +, /* = *
    // -- = + , *- = *-,  *+ = +, /- = /-
    private static void addBasicOperator (LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition,  String op){

        if (cursorPosition.getValue() == 0 || endsWithOpenParenthesis(expressionUnits.get(cursorPosition.getValue() - 1).getText())){
            if (op.equals("-")){
                expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("-"));
            }
            return;
        }

        if (basicOperators.contains(expressionUnits.get(cursorPosition.getValue() - 1).getText())){
            String prevOp = expressionUnits.get(cursorPosition.getValue() - 1).getText();

            if (op.equals("-")){
                if (prevOp.equals("×") || prevOp.equals("/")) {
                    expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("-"));
                    return;
                }
                if (prevOp.equals("-")){
                    expressionUnits.remove(cursorPosition.decreaseAndGet());
                    if (cursorPosition.getValue() > 0 && expressionUnits.get(cursorPosition.getValue() - 1) instanceof DigitUnit){
                        expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("+"));
                    }
                    return;
                }
            }
            expressionUnits.remove(cursorPosition.decreaseAndGet());
            addBasicOperator(expressionUnits, cursorPosition, op);

        } else {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit(op));
        }
    }

    private static void addDigit (LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition, String digit){
        if (digit.equals(".") && isDotInsertionAllowed(expressionUnits, cursorPosition.getValue())){
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("."));
        } else if (! digit.equals(".")){
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit(digit));
        }
    }

    // Check if decimal point insertion is valid.
    private static boolean isDotInsertionAllowed (LinkedList<ExpressionUnit> expressionUnits, int location){

        LinkedList<ExpressionUnit> copy = new LinkedList<>(expressionUnits);
        copy.add(location, new DigitUnit("."));
        String resultantExpression = toString(copy);

        String regex = "\\.\\d*\\.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(resultantExpression);
        return ! matcher.find();
    }

    private static boolean isNumber (String buttonPressed){
        return numberButtons.contains(buttonPressed);
    }

    private static boolean endsWithOpenParenthesis (String s){
        return s.charAt(s.length() - 1) == '(';
    }

}
