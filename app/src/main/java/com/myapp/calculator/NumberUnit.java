package com.myapp.calculator;

/**
 * Android calculator app
 */
public class NumberUnit implements ExpressionUnit {

    String number;

    public NumberUnit(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getText() {
        return number;
    }

    @Override
    public void del() {
        if (!number.isEmpty()){
            number = number.substring(0, number.length() - 1);
        }
    }
}
