package com.myapp.calculator;

/**
 * Android calculator app
 */
public class NumberUnit implements ExpressionUnit {

    final String number;

    public NumberUnit(String number) {
        this.number = number;
    }

    @Override
    public String getText() {
        return number;
    }

}