package com.myapp.calculator;

import android.support.annotation.VisibleForTesting;

import java.util.Stack;

/**
 * Android calculator app
 */

// Process user's input expression and computes the output result.
public class Kernel {

    // TODO: Implement expression evaluation. Handle exceptions properly.
    public static String evaluate (Stack<ExpressionUnit> expressionUnits){
        if (! isValid(expressionUnits)){
            return "formatting error";
        }
        return DisplayHelper.toString(expressionUnits);
    }


    // TODO: Implement expression verifier.
    @VisibleForTesting
    static boolean isValid (Stack<ExpressionUnit> expressionUnits){
        return true;
    }

    // TODO: Create Syntax Tree from list of expression units.
    @VisibleForTesting
    static ExpressionNode parse (Stack<ExpressionUnit> expressionUnits) {
        return null;
    }

}
