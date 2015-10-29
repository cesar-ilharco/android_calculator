package com.myapp.calculator;


import java.io.Serializable;
import java.util.LinkedList;

/**
 * Android calculator app
 */
public class Expression implements Serializable {
    private LinkedList<ExpressionUnit> expressionUnits;

    public Expression (){
        expressionUnits = new LinkedList<>();
    }

    public LinkedList<ExpressionUnit> getUnits() {
        return expressionUnits;
    }
}
