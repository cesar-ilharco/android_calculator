package com.myapp.calculator;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Android calculator app
 */

// Helper for displaying input and output correctly.
public class DisplayHelper {

    static final Set<String> numberButtons = new HashSet<>(Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "00", "."));

    static final Set<String> basicOperators = new HashSet<>(Arrays.asList("+", "-", "/", "*"));

    static final Set<String> function1Operators = new HashSet<>(Arrays.asList(
            "ln", "log", "sin", "cos", "tan", "Fib", "isPrime", "sinh", "cosh", "tanh",
            "arcsin", "arccos", "arctan", "arcsinh", "arccosh", "arctanh" ));

    static final Set<String> expOperators = new HashSet<>(Arrays.asList("e^x", "10^x", "x^2"));

    // TODO: Handle edge cases such as '.' + '.' = '.' or '-' + '-' = '+'.
    public static String getExpressionDisplay (Stack<ExpressionUnit> expressionUnits, String buttonPressed){
        if (buttonPressed.equals("del")){
            if (! expressionUnits.isEmpty()){
                expressionUnits.peek().del();
                if (expressionUnits.peek().getText().isEmpty()){
                    expressionUnits.pop();
                }
            }
        } else if (buttonPressed.equals("clear")){
            expressionUnits.clear();
        } else if (isNumber(buttonPressed)){
            addDigit(expressionUnits, buttonPressed);
        } else if (basicOperators.contains(buttonPressed)){
            addBasicOperator(expressionUnits, buttonPressed);
        } else if (expOperators.contains(buttonPressed)) {
            addExpOperator(expressionUnits, buttonPressed);
        } else if (function1Operators.contains(buttonPressed)) {
            expressionUnits.push(new OperatorUnit(buttonPressed + "("));
        } else {
            expressionUnits.push(new OperatorUnit(buttonPressed));
        }

        return toString(expressionUnits);
    }

    private static void addExpOperator(Stack<ExpressionUnit> expressionUnits, String buttonPressed) {
        if (buttonPressed.equals("e^x")){
            expressionUnits.push(new OperatorUnit("exp("));
        } else if (buttonPressed.equals("10^x")){
            expressionUnits.push(new NumberUnit("10"));
            expressionUnits.push(new OperatorUnit("^"));
        } else {
            expressionUnits.push(new OperatorUnit("^"));
            expressionUnits.push(new NumberUnit("2"));
        }
    }

    public static String getResultDisplay (Stack<ExpressionUnit> expressionUnits){
        return Kernel.evaluate(expressionUnits);
    }

    // Java iterates stack from the bottom to the top.
    public static String toString (Stack<ExpressionUnit> expressionUnits){
        StringBuffer stringBuffer = new StringBuffer();
        for (ExpressionUnit expressionUnit : expressionUnits){
            stringBuffer.append(expressionUnit.getText());
        }
        return stringBuffer.toString();
    }


    // If the last character from expression is a basic operator, replace it except for a few cases.
    // ++ = +,  +- = -,  +* = *, +/ = /, -+ = +, -/ = /, -* = *, ** = *, */ = /, // = /, /+ = +, /* = *
    // -- = + , *- = *-,  *+ = +, /- = /-
    private static void addBasicOperator (Stack<ExpressionUnit> expressionUnits,  String op){

        if (expressionUnits.isEmpty() || endsWithOpenParenthesis(expressionUnits.peek().getText())){
            if (op.equals("-")){
                expressionUnits.push(new OperatorUnit("-"));
            }
            return;
        }

        if (basicOperators.contains(expressionUnits.peek().getText())){
            String prevOp = ((OperatorUnit) expressionUnits.peek()).getOperator();

            if (op.equals("-")){
                if (prevOp.equals("*") || prevOp.equals("/")) {
                    expressionUnits.push(new OperatorUnit("-"));
                    return;
                }
                if (prevOp.equals("-")){
                    expressionUnits.pop();
                    if (!expressionUnits.isEmpty() && expressionUnits.peek() instanceof NumberUnit){
                        expressionUnits.push(new OperatorUnit("+"));
                    }
                    return;
                }
            }
            expressionUnits.pop();
            addBasicOperator(expressionUnits, op);

        } else {
            expressionUnits.push(new OperatorUnit(op));
        }
    }

    private static void addDigit (Stack<ExpressionUnit> expressionUnits,  String digit){
        if (!expressionUnits.isEmpty() && expressionUnits.peek() instanceof NumberUnit){
            String number = ((NumberUnit) expressionUnits.peek()).getNumber();
            if (digit.equals(".")){
                number = addDecimalPoint(number);
            } else {
                number = number + digit;
            }
            ((NumberUnit) expressionUnits.peek()).setNumber(number);
        } else {
            expressionUnits.push(new NumberUnit(digit));
        }
    }

    // Check if decimal point insertion is valid.
    private static String addDecimalPoint (String expression){
        String regex = ".*\\.\\d*\\z";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()){
            return expression;        // Invalid insertion.
        } else{
            return expression + ".";  // Valid insertion.
        }
    }

    private static boolean isNumber (String buttonPressed){
        return numberButtons.contains(buttonPressed);
    }

    private static boolean endsWithOpenParenthesis (String s){
        return s.charAt(s.length() - 1) == '(';
    }

}
