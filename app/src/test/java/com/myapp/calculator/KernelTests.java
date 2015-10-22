package com.myapp.calculator;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        expressionNode.printTree(stringBuilder);
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
        expressionNode.printTree(stringBuilder);
        System.out.println(stringBuilder.toString());
        Assert.assertEquals("-", expressionNode.getExpressionUnit().getText());
        Assert.assertEquals("4", expressionNode.getRight().getExpressionUnit().getText());
        Assert.assertEquals("+", expressionNode.getLeft().getExpressionUnit().getText());
        Assert.assertEquals("*", expressionNode.getLeft().getRight().getExpressionUnit().getText());
        Assert.assertEquals("1", expressionNode.getLeft().getLeft().getExpressionUnit().getText());
        Assert.assertEquals("3", expressionNode.getLeft().getRight().getRight().getExpressionUnit().getText());
        Assert.assertEquals("2", expressionNode.getLeft().getRight().getLeft().getExpressionUnit().getText());


    }

}
