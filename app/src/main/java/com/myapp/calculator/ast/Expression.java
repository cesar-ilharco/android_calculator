package com.myapp.calculator.ast;


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

    public Expression (Expression other){
        this.expressionUnits = new LinkedList<>(other.getUnits());
    }

    public LinkedList<ExpressionUnit> getUnits() {
        return expressionUnits;
    }
}
