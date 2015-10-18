package com.myapp.calculator;

import android.widget.TextView;

/**
 * Android calculator app
 */

// Helper for displaying input and output correctly.
public class Display {

    // TODO: Handle edge cases such as '.' + '.' = '.' or '-' + '-' = '+'.
    public static TextView updateExpressionDisplay (TextView expression, String buttonPressed){
        expression.append(buttonPressed);
        return expression;
    }

    // TODO: Handle evaluation exceptions.
    public static TextView updateResultDisplay (TextView result, String expression){
        result.setText(Kernel.evaluate(expression).toString());
        return result;
    }

}
