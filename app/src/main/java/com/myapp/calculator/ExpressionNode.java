package com.myapp.calculator;

import java.io.IOException;

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

    public void printTree(StringBuilder stringBuilder) throws IOException {
        if (right != null) {
            right.printTree(stringBuilder, true, "");
        }
        printNodeValue(stringBuilder);
        if (left != null) {
            left.printTree(stringBuilder, false, "");
        }
    }
    private void printNodeValue(StringBuilder stringBuilder) throws IOException {
        if (expressionUnit == null) {
            stringBuilder.append("<null>");
        } else {
            stringBuilder.append(expressionUnit.getText());
        }
        stringBuilder.append('\n');
    }

    private void printTree(StringBuilder stringBuilder, boolean isRight, String indent) throws IOException {
        if (right != null) {
            right.printTree(stringBuilder, true, indent + (isRight ? "        " : " |      "));
        }
        stringBuilder.append(indent);
        if (isRight) {
            stringBuilder.append(" /");
        } else {
            stringBuilder.append(" \\");
        }
        stringBuilder.append("----- ");
        printNodeValue(stringBuilder);
        if (left != null) {
            left.printTree(stringBuilder, false, indent + (isRight ? " |      " : "        "));
        }
    }
}
