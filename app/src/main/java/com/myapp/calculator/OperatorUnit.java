package com.myapp.calculator;

/**
 * Android calculator app
 */
public class OperatorUnit implements ExpressionUnit {

    private final String operator;

    public OperatorUnit(String operator){
        this.operator = operator;
    }

    @Override
    public String getText() {
        return operator;
    }
}
