package com.myapp.calculator;



import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.utils.MyInt;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Android calculator app
 */
public class CalculatorState implements Serializable{

    private Deque<Expression> expressions;
    private MyInt cursorPosition;
    private int scale;
    private boolean hyp;
    private boolean inv;

    private static final int UNDOING_LIMIT = 99;

    public CalculatorState() {
        this.expressions = new LinkedList<>();
        this.expressions.addFirst(new Expression());
        this.cursorPosition = new MyInt(0);
        this.scale = 10;
        this.hyp = false;
        this.inv = false;
    }

    public Expression getExpression() {
        return expressions.peekFirst();
    }

    public void pushExpression (Expression expression) {
        if (expressions.size() == UNDOING_LIMIT + 1){
            expressions.removeLast();
        }
        expressions.addFirst(expression);
    }

    public void undoExpression(){
        if (expressions.size() > 1){
            expressions.removeFirst();
        }
        cursorPosition.setValue(Math.min(getExpression().getUnits().size(), cursorPosition.getValue()));
    }

    public MyInt getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(MyInt cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public void moveCursorForwards(){
        if (cursorPosition.getValue() < getExpression().getUnits().size()){
            cursorPosition.increaseAndGet();
        }
    }

    public void moveCursorBackwards(){
        if (cursorPosition.getValue() > 0){
            cursorPosition.decreaseAndGet();
        }
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
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
