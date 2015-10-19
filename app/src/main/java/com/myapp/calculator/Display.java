package com.myapp.calculator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Android calculator app
 */

// Helper for displaying input and output correctly.
public class Display {

    static final Set<String> basicOperators = new HashSet<>(Arrays.asList("+", "-", "/", "*"));
    static final Map<String, String> operatorsOverlap = new HashMap<String, String>() {{
        put("--", "+");
        put("*-", "*-");
        put("*+", "*");
        put("/-", "/-");
    }};

    // TODO: Handle edge cases such as '.' + '.' = '.' or '-' + '-' = '+'.
    public static String getExpressionDisplay (String expression, String buttonPressed){
        if (buttonPressed.equals(".")){
            return addDecimalPoint(expression);
        } else if (basicOperators.contains(buttonPressed)){
            return addBasicOperator(expression, buttonPressed);
        }
        return expression + buttonPressed;
    }

    // TODO: Handle evaluation exceptions.
    public static String getResultDisplay (String expression){
        return Kernel.evaluate(expression).toString();
    }

    // Check if decimal point insertion is valid.
    private static String addDecimalPoint (String expression){
        String regex = "\\w*\\d*\\.\\d*";
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
            if (operatorsOverlap.containsKey(lastChar + op)){
                return expression.substring(0, l-1) + operatorsOverlap.get(lastChar + op);
            } else {
                return expression.substring(0, l-1) + op;
            }
        } else {
            return expression + op;
        }
    }

}
