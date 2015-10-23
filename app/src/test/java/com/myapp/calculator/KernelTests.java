package com.myapp.calculator;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.Stack;

/**
 * Android calculator app
 */
public class KernelTests {

    @Test
    public void testKernelParser() throws IOException{
        Stack<ExpressionUnit> expressionUnits = new Stack<>();

        // 1+2
        Assert.assertEquals(null, Kernel.parse(expressionUnits));
        expressionUnits.push(new NumberUnit("1"));
        Assert.assertEquals("1", Kernel.parse(expressionUnits).getExpressionUnit().getText());
        expressionUnits.push(new OperatorUnit("+"));
        expressionUnits.push(new NumberUnit("2"));
        ExpressionNode expressionNode = Kernel.parse(expressionUnits);
        StringBuilder stringBuilder = new StringBuilder();
        printTree(expressionNode, stringBuilder);
        System.out.println(stringBuilder.toString());
        Assert.assertEquals("+", expressionNode.getExpressionUnit().getText());
        Assert.assertEquals("1", expressionNode.getLeft().getExpressionUnit().getText());
        Assert.assertEquals("2", expressionNode.getRight().getExpressionUnit().getText());

        // 1+2*3-4
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("3"));
        expressionUnits.push(new OperatorUnit("-"));
        expressionUnits.push(new NumberUnit("4"));
        expressionNode = Kernel.parse(expressionUnits);
        stringBuilder = new StringBuilder();
        printTree(expressionNode, stringBuilder);
        System.out.println(stringBuilder.toString());
        Assert.assertEquals("-", expressionNode.getExpressionUnit().getText());
        Assert.assertEquals("4", expressionNode.getRight().getExpressionUnit().getText());
        Assert.assertEquals("+", expressionNode.getLeft().getExpressionUnit().getText());
        Assert.assertEquals("*", expressionNode.getLeft().getRight().getExpressionUnit().getText());
        Assert.assertEquals("1", expressionNode.getLeft().getLeft().getExpressionUnit().getText());
        Assert.assertEquals("3", expressionNode.getLeft().getRight().getRight().getExpressionUnit().getText());
        Assert.assertEquals("2", expressionNode.getLeft().getRight().getLeft().getExpressionUnit().getText());

    }

    @Test
    public void testKernelEvaluator() throws IOException{
        Stack<ExpressionUnit> expressionUnits = new Stack<>();
        // 1 + 2 = 3
        expressionUnits.push(new NumberUnit("1"));
        expressionUnits.push(new OperatorUnit("+"));
        expressionUnits.push(new NumberUnit("2"));
        Assert.assertEquals("3", Kernel.evaluate(expressionUnits));

        // 1 + 2 * 4 - 3 = 6
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("4"));
        expressionUnits.push(new OperatorUnit("-"));
        expressionUnits.push(new NumberUnit("3"));
        Assert.assertEquals("6", Kernel.evaluate(expressionUnits));

        // 1 * 2 * 3 * 4 * 5 = 120
        expressionUnits.clear();
        expressionUnits.push(new NumberUnit("1"));
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("2"));
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("3"));
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("4"));
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("5"));
        Assert.assertEquals("120", Kernel.evaluate(expressionUnits));

        // 1 - 2 * 3 + 4 * 5 = 15
        expressionUnits.clear();
        expressionUnits.push(new NumberUnit("1"));
        expressionUnits.push(new OperatorUnit("-"));
        expressionUnits.push(new NumberUnit("2"));
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("3"));
        expressionUnits.push(new OperatorUnit("+"));
        expressionUnits.push(new NumberUnit("4"));
        expressionUnits.push(new OperatorUnit("*"));
        expressionUnits.push(new NumberUnit("5"));
        Assert.assertEquals("15", Kernel.evaluate(expressionUnits));

        expressionUnits.clear();
        expressionUnits.push(new NumberUnit("1"));
        expressionUnits.push(new OperatorUnit("/"));
        expressionUnits.push(new NumberUnit("3"));
        System.out.println("1/3 = " + Kernel.evaluate(expressionUnits));

    }


    private static void printTree(ExpressionNode root, StringBuilder stringBuilder) throws IOException {
        if (root.getRight() != null) {
            printTree(root.getRight(), stringBuilder, true, "");
        }
        printNodeValue(root, stringBuilder);
        if (root.getLeft() != null) {
            printTree(root.getLeft(), stringBuilder, false, "");
        }
    }
    private static void printNodeValue(ExpressionNode node, StringBuilder stringBuilder) throws IOException {
        if (node.getExpressionUnit() == null) {
            stringBuilder.append("<null>");
        } else {
            stringBuilder.append(node.getExpressionUnit().getText());
        }
        stringBuilder.append('\n');
    }

    private static void printTree(ExpressionNode node, StringBuilder stringBuilder, boolean isRight, String indent) throws IOException {
        if (node.getRight() != null) {
            printTree(node.getRight(), stringBuilder, true, indent + (isRight ? "        " : " |      "));
        }
        stringBuilder.append(indent);
        if (isRight) {
            stringBuilder.append(" /");
        } else {
            stringBuilder.append(" \\");
        }
        stringBuilder.append("----- ");
        printNodeValue(node, stringBuilder);
        if (node.getLeft() != null) {
            printTree(node.getLeft(), stringBuilder, false, indent + (isRight ? " |      " : "        "));
        }
    }

}
