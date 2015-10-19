package com.myapp.calculator;

/**
 * Android calculator app
 */

// Helper for displaying input and output correctly.
public class Display {

    // TODO: Handle edge cases such as '.' + '.' = '.' or '-' + '-' = '+'.
    public static String getExpressionDisplay (String expression, String buttonPressed){
        return expression + buttonPressed;
    }

    // TODO: Handle evaluation exceptions.
    public static String getResultDisplay (String expression){
        return Kernel.evaluate(expression).toString();
    }

}
