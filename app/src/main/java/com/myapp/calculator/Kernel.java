package com.myapp.calculator;

import android.support.annotation.VisibleForTesting;

import java.math.BigDecimal;

/**
 * Android calculator app
 */

// Process user's input expression and computes the output result.
public class Kernel {

    // TODO: Implement expression evaluation. Handle exceptions properly.
    public static String evaluate (String expression){
        if (! isValid(expression)){
            return "formatting error";
        }

    }


    // TODO: Implement expression verifier.
    @VisibleForTesting
    static boolean isValid (String expression){
        return true;
    }
}
