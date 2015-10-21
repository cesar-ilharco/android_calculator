package com.myapp.calculator;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    static final Set<String> allowedOpOverlap = new HashSet<>(Arrays.asList("*-", "/-"));
    static final Map<String, String> exceptionOpOverlap = new HashMap<String, String>() {{
        put("--", "+");
        put("*+", "*");
        put("/+", "/");
    }};

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
            if (!expressionUnits.isEmpty() && expressionUnits.peek() instanceof NumberUnit){
                String number = ((NumberUnit) expressionUnits.peek()).getNumber();
                if (buttonPressed.equals(".")){
                    number = addDecimalPoint(number);
                } else {
                    number = number + buttonPressed;
                }
                ((NumberUnit) expressionUnits.peek()).setNumber(number);
            }
        } else {
            if (!expressionUnits.isEmpty() && expressionUnits.peek() instanceof OperatorUnit){
                String operator = ((OperatorUnit) expressionUnits.peek()).getOperator();
                operator = addBasicOperator(operator, buttonPressed);
                ((OperatorUnit) expressionUnits.peek()).setOperator(operator);
            } else {
                expressionUnits.push(new OperatorUnit(buttonPressed));
            }
        }

        return toString(expressionUnits);
    }

    public static String getResultDisplay (Stack<ExpressionUnit> expressionUnits){
        return Kernel.evaluate(expressionUnits);
    }

    // Java iterates stack from the bottom to the top.
    public static String toString (Stack<ExpressionUnit> expressionUnits){
        StringBuffer stringBuffer = new StringBuffer();
        for (ExpressionUnit expressionUnit : expressionUnits){
            stringBuffer.append(expressionUnit.toString());
        }
        return stringBuffer.toString();
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

    // If the last character from expression is a basic operator, replace it except for a few cases.
    // ++ = +,  +- = -,  +* = *, +/ = /, -+ = +, -/ = /, -* = *, ** = *, */ = /, // = /, /+ = /, /* = *
    // -- = + , *- = *-,  *+ = *, /- = /-
    private static String addBasicOperator (String expression, String op){
        if (expression.isEmpty()){
            return op.equals("-") ? op : "";
        }
        int l = expression.length();
        String lastChar = "" + expression.charAt(l-1);
        if (basicOperators.contains(lastChar)){
            if (allowedOpOverlap.contains(lastChar + op)){
                return expression + op;
            } else if (exceptionOpOverlap.containsKey(lastChar + op)){
                return addBasicOperator(expression.substring(0,l-1), exceptionOpOverlap.get(lastChar + op));
            } else {
                return addBasicOperator(expression.substring(0,l-1), op);
            }
        } else {
            return expression + op;
        }
    }

    private static boolean isNumber (String buttonPressed){
        return numberButtons.contains(buttonPressed);
    }

}
