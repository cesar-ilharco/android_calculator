package com.myapp.calculator;



import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.utils.MyInt;

import java.io.*;

/**
 * Android calculator app
 */
public class CalculatorState implements Serializable{
    private Expression expression;
    private MyInt cursorPosition;
    private boolean hyp;
    private boolean inv;

    public CalculatorState() {
        this.expression = new Expression();
        this.cursorPosition = new MyInt(0);
        this.hyp = false;
        this.inv = false;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public MyInt getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(MyInt cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public void moveCursorForwards(){
        if (cursorPosition.getValue() < expression.getUnits().size()){
            cursorPosition.increaseAndGet();
        }
    }

    public void moveCursorBackwards(){
        if (cursorPosition.getValue() > 0){
            cursorPosition.decreaseAndGet();
        }
    }

    public boolean isHyp() {
        return hyp;
    }

    public void setHyp(boolean hyp) {
        this.hyp = hyp;
    }

    public boolean isInv() {
        return inv;
    }

    public void setInv(boolean inv) {
        this.inv = inv;
    }

}
