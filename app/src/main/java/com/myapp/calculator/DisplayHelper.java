package com.myapp.calculator;


import com.myapp.calculator.ast.DigitUnit;
import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.ast.ExpressionUnit;
import com.myapp.calculator.ast.ExpressionNode;
import com.myapp.calculator.ast.OperatorUnit;
import com.myapp.calculator.utils.MyInt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Android calculator app
 */

// Helper for displaying input and output correctly.
public class DisplayHelper {

    static final Set<String> numberButtons = new HashSet<>(Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."));

    static final Set<String> basicOperators = new HashSet<>(Arrays.asList("+", "-", "/", "×"));

    static final Set<String> unaryOperators = new HashSet<>(Arrays.asList(
            "ln", "log", "sin", "cos", "tan", "Fib", "isPrime", "sinh", "cosh", "tanh",
            "arcsin", "arccos", "arctan", "arcsinh", "arccosh", "arctanh" ));

    static final Set<String> expOperators = new HashSet<>(Arrays.asList("e^x", "10^x", "x²"));

    static final String regexDecimalPoint= "\\.\\d*\\.";
    static final Pattern patternDecimalPoint = Pattern.compile(regexDecimalPoint);


    // Method handles edge cases in order to prevent malformed expressions.
    public static Expression updateExpression(Expression expression, MyInt cursorPosition, String buttonPressed){

        Expression updatedExpression = new Expression(expression);
        LinkedList<ExpressionUnit> expressionUnits = updatedExpression.getUnits();

        if (buttonPressed.equals("del")){
            if (cursorPosition.getValue() > 0){
                expressionUnits.remove(cursorPosition.decreaseAndGet());
            }
        } else if (buttonPressed.equals("clear")){
            expressionUnits.clear();
            cursorPosition.reset();
        } else if (numberButtons.contains(buttonPressed)){
            addDigit(expressionUnits, cursorPosition, buttonPressed);
        } else if (basicOperators.contains(buttonPressed)){
            addBasicOperator(expressionUnits, cursorPosition, buttonPressed);
        } else if (expOperators.contains(buttonPressed)) {
            addExpOperator(expressionUnits, cursorPosition, buttonPressed);
        } else if (unaryOperators.contains(buttonPressed)) {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit(buttonPressed + "("));
            expressionUnits.add(cursorPosition.getValue(), new OperatorUnit(")"));
        } else {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit(buttonPressed));
        }

        return updatedExpression;
    }

    // TODO: Receive an AngleUnit as a parameter and add try/catch
    public static String getResultDisplay (Expression expression, int scale){
        MathContext context = new MathContext(scale, RoundingMode.HALF_EVEN);
        Calculation calculation = new Calculation(context);
        List<ExpressionUnit> expressionUnits = Kernel.digitsToNumber(expression.getUnits());
        ExpressionNode expressionNode = ExpressionNode.constructExpressionNodes(expressionUnits);
        BigDecimal result = Kernel.evaluate(expressionNode, calculation, AngleUnit.DEGREE).round(context);
        if (result.compareTo(new BigDecimal("1000000000")) < 0)
            return result.toPlainString();
        else return result.toString();
    }

    // Method does not add the cursor character.
    public static String convertToString(LinkedList<ExpressionUnit> expressionUnits){
        StringBuffer stringBuffer = new StringBuffer();
        for (ExpressionUnit expressionUnit : expressionUnits){
            stringBuffer.append(expressionUnit.getText());
        }
        return stringBuffer.toString();
    }

    private static void addDigit (LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition, String digit){
        if (digit.equals(".")){
            if (isDotInsertionAllowed(expressionUnits, cursorPosition.getValue())){
                expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("."));
            }
        } else {
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit(digit));
        }
    }

    // If the last character from expression is a basic operator, replace it except for a few cases.
    // ++ = +,  +- = -,  +* = *, +/ = /, -+ = +, -/ = /, -* = *, ** = *, */ = /, // = /, /+ = +, /* = *
    // -- = + , *- = *-,  *+ = +, /- = /-
    private static void addBasicOperator (LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition,  String op){

        if (cursorPosition.getValue() == 0 || endsWithOpenParenthesis(expressionUnits.get(cursorPosition.getValue() - 1).getText())){
            if (op.equals("-")){
                expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("-"));
            }
            return;
        }

        if (basicOperators.contains(expressionUnits.get(cursorPosition.getValue() - 1).getText())){
            String prevOp = expressionUnits.get(cursorPosition.getValue() - 1).getText();

            if (op.equals("-")){
                if (prevOp.equals("×") || prevOp.equals("/")) {
                    expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("-"));
                    return;
                }
                if (prevOp.equals("-")){
                    expressionUnits.remove(cursorPosition.decreaseAndGet());
                    if (cursorPosition.getValue() > 0 && expressionUnits.get(cursorPosition.getValue() - 1) instanceof DigitUnit){
                        expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("+"));
                    }
                    return;
                }
            }
            expressionUnits.remove(cursorPosition.decreaseAndGet());
            addBasicOperator(expressionUnits, cursorPosition, op);

        } else {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit(op));
        }
    }


    private static void addExpOperator(LinkedList<ExpressionUnit> expressionUnits, MyInt cursorPosition,String buttonPressed) {
        // TODO Change e^x to exp(x)
        if (buttonPressed.equals("e^x")){
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("e"));
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("^"));
        } else if (buttonPressed.equals("10^x")){
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("1"));
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("0"));
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("^"));
        } else {
            expressionUnits.add(cursorPosition.getAndIncrease(), new OperatorUnit("^"));
            expressionUnits.add(cursorPosition.getAndIncrease(), new DigitUnit("2"));
        }
    }

    // Check if decimal point insertion is valid.
    private static boolean isDotInsertionAllowed (LinkedList<ExpressionUnit> expressionUnits, int location){

        LinkedList<ExpressionUnit> copy = new LinkedList<>(expressionUnits);
        copy.add(location, new DigitUnit("."));
        String resultantExpression = convertToString(copy);

        Matcher matcher = patternDecimalPoint.matcher(resultantExpression);
        return !matcher.find();
    }


    private static boolean endsWithOpenParenthesis (String s){
        return !s.isEmpty() && s.charAt(s.length() - 1) == '(';
    }

}
