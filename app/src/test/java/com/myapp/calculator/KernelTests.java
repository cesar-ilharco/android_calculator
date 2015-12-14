package com.myapp.calculator;

import com.myapp.calculator.ast.DigitUnit;
import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.ast.ExpressionNode;
import com.myapp.calculator.ast.ExpressionUnit;
import com.myapp.calculator.ast.NumberUnit;
import com.myapp.calculator.ast.OperatorUnit;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Android calculator app
 */
public class KernelTests {

    @Test
    public void testKernelParser() throws IOException{
        LinkedList<ExpressionUnit> expressionUnits = new LinkedList<>();

        // 1+2
        Assert.assertEquals(null, Kernel.parse(expressionUnits));
        expressionUnits.addLast(new NumberUnit("1"));
        Assert.assertEquals("1", Kernel.parse(expressionUnits).getExpressionUnit().getText());
        expressionUnits.addLast(new OperatorUnit("+"));
        expressionUnits.addLast(new NumberUnit("2"));
        ExpressionNode expressionNode = Kernel.parse(expressionUnits);
        StringBuilder stringBuilder = new StringBuilder();
        printTree(expressionNode, stringBuilder);
        System.out.println(stringBuilder.toString());
        Assert.assertEquals("+", expressionNode.getExpressionUnit().getText());
        Assert.assertEquals("1", expressionNode.getLeft().getExpressionUnit().getText());
        Assert.assertEquals("2", expressionNode.getRight().getExpressionUnit().getText());

        // 1+2×3-4
        expressionUnits.addLast(new OperatorUnit("×"));
        expressionUnits.addLast(new NumberUnit("3"));
        expressionUnits.addLast(new OperatorUnit("-"));
        expressionUnits.addLast(new NumberUnit("4"));
        expressionNode = Kernel.parse(expressionUnits);
        stringBuilder = new StringBuilder();
        printTree(expressionNode, stringBuilder);
        System.out.println(stringBuilder.toString());
        Assert.assertEquals("-", expressionNode.getExpressionUnit().getText());
        Assert.assertEquals("4", expressionNode.getRight().getExpressionUnit().getText());
        Assert.assertEquals("+", expressionNode.getLeft().getExpressionUnit().getText());
        Assert.assertEquals("×", expressionNode.getLeft().getRight().getExpressionUnit().getText());
        Assert.assertEquals("1", expressionNode.getLeft().getLeft().getExpressionUnit().getText());
        Assert.assertEquals("3", expressionNode.getLeft().getRight().getRight().getExpressionUnit().getText());
        Assert.assertEquals("2", expressionNode.getLeft().getRight().getLeft().getExpressionUnit().getText());

    }

    @Test
    public void testKernelBasicEvaluator() throws IOException{
        Expression expression = new Expression();
        // 1 + 2 = 3
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        expression.getUnits().addLast(new DigitUnit("2"));
        Assert.assertEquals("3", Kernel.evaluateBasic(expression.getUnits(), 0));

        // 1 + 2 × 4 - 3 = 6
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("4"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("3"));
        Assert.assertEquals("6", Kernel.evaluateBasic(expression.getUnits(), 0));

        // 1 × 2 × 3 × 4 × 5 = 120
        expression.getUnits().clear();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("3"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("4"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("5"));
        Assert.assertEquals("120", Kernel.evaluateBasic(expression.getUnits(), 0));

        // 1 - 2 × 3 + 4 × 5 = 15
        expression.getUnits().clear();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("2"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("3"));
        expression.getUnits().addLast(new OperatorUnit("+"));
        expression.getUnits().addLast(new DigitUnit("4"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new DigitUnit("5"));
        Assert.assertEquals("15", Kernel.evaluateBasic(expression.getUnits(), 0));

        // 1 / 3 = 0.333...
        expression.getUnits().clear();
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("/"));
        expression.getUnits().addLast(new DigitUnit("3"));
        double val = Double.valueOf(Kernel.evaluateBasic(expression.getUnits(), 10));
        Assert.assertTrue(val > 0.33333 && val < 0.33334);

        // -1 × -2 = 2
        expression.getUnits().clear();
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("1"));
        expression.getUnits().addLast(new OperatorUnit("×"));
        expression.getUnits().addLast(new OperatorUnit("-"));
        expression.getUnits().addLast(new DigitUnit("2"));
        Assert.assertEquals("2", Kernel.evaluateBasic(expression.getUnits(), 0));

    }

    @Test
    public void testDigitsToNumber(){
        LinkedList<ExpressionUnit> expressionUnits = new LinkedList<>();

        // 123
        expressionUnits.addLast(new DigitUnit("1"));
        expressionUnits.addLast(new DigitUnit("2"));
        expressionUnits.addLast(new DigitUnit("3"));
        List<ExpressionUnit> convertedExpression = Kernel.digitsToNumber(expressionUnits);
        Assert.assertEquals(Arrays.asList(new NumberUnit("123")), convertedExpression);

        // -50-1×-23+4
        expressionUnits.clear();
        expressionUnits.addLast(new OperatorUnit("-"));
        expressionUnits.addLast(new DigitUnit("5"));
        expressionUnits.addLast(new DigitUnit("0"));
        expressionUnits.addLast(new OperatorUnit("-"));
        expressionUnits.addLast(new DigitUnit("1"));
        expressionUnits.addLast(new OperatorUnit("×"));
        expressionUnits.addLast(new OperatorUnit("-"));
        expressionUnits.addLast(new DigitUnit("2"));
        expressionUnits.addLast(new DigitUnit("3"));
        expressionUnits.addLast(new OperatorUnit("+"));
        expressionUnits.addLast(new DigitUnit("4"));
        convertedExpression = Kernel.digitsToNumber(expressionUnits);
        List<ExpressionUnit> expectedResult = Arrays.asList(
                new NumberUnit("-50"),
                new OperatorUnit("-"),
                new NumberUnit("1"),
                new OperatorUnit("×"),
                new NumberUnit("-23"),
                new OperatorUnit("+"),
                new NumberUnit("4")
        );

        Assert.assertEquals(expectedResult, convertedExpression);
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
