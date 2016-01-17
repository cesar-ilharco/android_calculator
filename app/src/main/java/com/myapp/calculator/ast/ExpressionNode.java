package com.myapp.calculator.ast;

import java.util.List;

/**
 * Android calculator app
 */

// TODO: Make this an interface, implemented by operators and number.
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
        if (left != null) left.setRight(this);
        if (right != null) right.setLeft(this);
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

    public static ExpressionNode constructExpressionNodes (List<ExpressionUnit> expressionUnits) {
        if (expressionUnits.size() == 0) return null;
        ExpressionNode head = new ExpressionNode(expressionUnits.get(0));
        ExpressionNode current = head;
        for (int i = 1; i < expressionUnits.size(); i++)
            current = new ExpressionNode(expressionUnits.get(i), current, null);
        return head;
    }
}
