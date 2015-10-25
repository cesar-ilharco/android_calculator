package com.myapp.calculator;


/**
 * Android calculator app
 */

// Used to build a Syntax Tree.
public class ExpressionNode {
    private ExpressionUnit expressionUnit;
    private ExpressionNode left;
    private ExpressionNode right;

    public ExpressionNode(ExpressionUnit expressionUnit) {
        this.expressionUnit = expressionUnit;
        this.left = null;
        this.right = null;
    }

    public ExpressionNode(ExpressionUnit expressionUnit, ExpressionNode left, ExpressionNode right) {
        this.expressionUnit = expressionUnit;
        this.left = left;
        this.right = right;
    }

    public ExpressionUnit getExpressionUnit() {
        return expressionUnit;
    }

    public void setExpressionUnit(ExpressionUnit expressionUnit) {
        this.expressionUnit = expressionUnit;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }
}
