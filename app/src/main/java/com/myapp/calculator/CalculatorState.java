package com.myapp.calculator;



import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.utils.MyInt;

import java.io.Serializable;

/**
 * Android calculator app
 */
public class CalculatorState implements Serializable{
    private Expression expression;
    private MyInt cursorPosition;
    private boolean hyp;
    private boolean inv;
    private boolean cursorVisible;
    private boolean refreshResultView;

    public CalculatorState() {
        this.expression = new Expression();
        this.cursorPosition = new MyInt(0);
        this.hyp = false;
        this.inv = false;
        this.cursorVisible = true;
        this.refreshResultView = true;
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

    public boolean isCursorVisible() {
        return cursorVisible;
    }

    public void setCursorVisible(boolean cursorVisible) {
        this.cursorVisible = cursorVisible;
    }

    public boolean isRefreshResultView() {
        return refreshResultView;
    }

    public void setRefreshResultView(boolean refreshResultView) {
        this.refreshResultView = refreshResultView;
    }

}
