package com.myapp.calculator;

import com.myapp.calculator.ast.ExpressionNode;
import com.myapp.calculator.ast.ExpressionUnit;
import com.myapp.calculator.ast.NumberUnit;
import com.myapp.calculator.ast.OperatorUnit;
import com.myapp.calculator.ast.ConstantUnit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Android calculator app
 */

// Process user's input expression and computes the output result.
public class Kernel {

    public static BigDecimal evaluate (ExpressionNode expressionNode, Calculation calculation, AngleUnit angleUnit) {

        if (expressionNode == null) return BigDecimal.ZERO;
        if (expressionNode.getExpressionUnit().getText().equals("-"))
            expressionNode = new ExpressionNode(new NumberUnit("-1"), null, expressionNode.getRight());


        BigDecimal resultChild;
        BigDecimal lhs = null;
        BigDecimal rhs = null;
        ExpressionUnit expressionUnit = null;
        ExpressionNode last = null;
        ExpressionNode next = null;
        String operator = null;
        

        // STEP 1: Compute constants
        ExpressionNode curr = expressionNode;
        while (curr != null) {
            expressionUnit = curr.getExpressionUnit();
            if (expressionUnit instanceof ConstantUnit) {
                switch (expressionUnit.getText()) {
                    // TODO change to getE()...
                    case "e": lhs = calculation.computeE(); break;
                    case "pi": lhs = calculation.computePi(); break;
                    case "phi": lhs = calculation.computePhi(); break;
                }
                curr = new ExpressionNode(new NumberUnit(lhs.toString()), curr.getLeft(), curr.getRight());
                if (curr.getLeft() == null) expressionNode = curr;
            }
            curr = curr.getRight();
        }


        // STEP 2: Evaluate brackets to determine sub-expressions
        curr = expressionNode;
        while (curr != null) {
            expressionUnit = curr.getExpressionUnit();
            if (expressionUnit.getText().equals("(")) {
                // Initialization
                last = curr.getLeft();
                curr = curr.getRight();
                int nbBrackets = 1;
                ExpressionNode currChild = null;
                ExpressionNode headChild = null;
                // Loop will break when expression is finished or all brackets are matched
                while (curr != null) {
                    expressionUnit = curr.getExpressionUnit();
                    if (expressionUnit.getText().equals(")")) {
                        nbBrackets--;
                        // Breaks if brackets are all matched
                        if (nbBrackets == 0) break;
                    } else if (expressionUnit.getText().equals("(")) nbBrackets++;
                    // Initialization of the child expression
                    if (currChild == null) {
                        currChild = new ExpressionNode(expressionUnit);
                        headChild = currChild;
                    } else {
                        // Adds node curr to child and updates currChild
                        currChild.setRight(new ExpressionNode(expressionUnit));
                        currChild.getRight().setLeft(currChild);
                        currChild = currChild.getRight();
                    }
                    // Updates current node
                    curr = curr.getRight();
                }
                // If there are more open brackets than close brackets
                if (curr == null) throw new ArithmeticException("Unmatched brackets");
                // Computes the child expression
                next = curr.getRight();
                curr.getLeft().setRight(null);
                resultChild = evaluate(headChild, calculation, angleUnit);
                // Updates original expression with the result of the child expression
                curr = new ExpressionNode(new NumberUnit(resultChild.toString()), last, next);
                if (last == null) expressionNode = curr;
            } else if (expressionUnit.getText().equals(")"))
                // If there are more close brackets than open brackets
                throw new ArithmeticException("Unmatched brackets");
            curr = curr.getRight();
        }


        // STEP 3: Unary operators (to the left)
        curr = expressionNode;
        while (curr != null) {
            expressionUnit = curr.getExpressionUnit();
            if (isUnaryOperator(expressionUnit)) {
                last = curr.getLeft();
                curr = curr.getRight();
                if (curr == null) throw new ArithmeticException("Missing operand");
                expressionUnit = curr.getExpressionUnit();
                
                while (isUnaryOperator(expressionUnit)) {
                    curr = curr.getRight();
                    if (curr == null) throw new ArithmeticException("Missing operand");
                    expressionUnit = curr.getExpressionUnit();
                }
                if (expressionUnit instanceof OperatorUnit) throw new ArithmeticException("Missing operand");
                rhs = new BigDecimal(expressionUnit.getText());
                next = curr.getRight();
                while (!curr.getLeft().equals(last)) {
                    curr = curr.getLeft();
                    rhs = compute(curr.getExpressionUnit().getText(), rhs, calculation, angleUnit);
                }
                curr = new ExpressionNode(new NumberUnit(rhs.toString()), last, next);
                if (last == null) expressionNode = curr;
            }
            curr = curr.getRight();
        }


        // STEP 4: Square, factorial and power
        curr = expressionNode;
        while (curr != null) {
            expressionUnit = curr.getExpressionUnit();
            operator = expressionUnit.getText();
            if (operator.equals("²") || operator.equals("!") || operator.equals("^")) {
                next = curr.getRight();
                curr = curr.getLeft();
                if (curr == null) throw new ArithmeticException("Missing operand");
                expressionUnit = curr.getExpressionUnit();
                if (expressionUnit instanceof OperatorUnit) throw new ArithmeticException("Missing operand");
                lhs = new BigDecimal(expressionUnit.getText());
                last = curr.getLeft();
                switch (operator) {
                    case "²":
                        lhs = calculation.computeSquare(lhs);
                        break;
                    case "!":
                        lhs = calculation.computeFactorial(lhs);
                        break;
                    case "^":
                        if (next == null || next.getExpressionUnit() instanceof OperatorUnit)
                            throw new ArithmeticException("Missing operand");
                        rhs = new BigDecimal(next.getExpressionUnit().getText());
                        lhs = calculation.computePower(lhs, rhs);
                        next = next.getRight();
                }       
                curr = new ExpressionNode(new NumberUnit(lhs.toString()), last, next);
                if (last == null) expressionNode = curr;
            }
            curr = curr.getRight();
        }   
            

        // STEP 5: Multiplicative and additive operations
        for (int s = 0; s < 2; s++) {
            curr = expressionNode;
            while (curr != null) {
                operator = curr.getExpressionUnit().getText();
                if (s == 0 && (operator.equals("×") || operator.equals("/")) ||
                                    s == 1 && (operator.equals("+") || operator.equals("-"))) {
                    last = curr.getLeft();
                    next = curr.getRight();
                    if (last == null || next == null) throw new ArithmeticException("Missing operand");
                    if (last.getExpressionUnit() instanceof OperatorUnit || next.getExpressionUnit() instanceof OperatorUnit)
                        throw new ArithmeticException("Missing operand");
                    lhs = new BigDecimal(last.getExpressionUnit().getText());
                    rhs = new BigDecimal(next.getExpressionUnit().getText());
                    switch (operator) {
                        case "×": lhs = calculation.computeMultiplication(lhs, rhs); break;
                        case "/": lhs = calculation.computeDivision(lhs, rhs); break;
                        case "+": lhs = calculation.computeAddition(lhs, rhs); break;
                        case "-": lhs = calculation.computeSubtraction(lhs, rhs); break;
                    }
                    curr = new ExpressionNode(new NumberUnit(lhs.toString()), last.getLeft(), next.getRight());
                    if (last.getLeft() == null) expressionNode = curr;
                }
                curr = curr.getRight();
            }
        }


        // STEP 6: Multiply the remaining items, if any
        lhs = new BigDecimal(expressionNode.getExpressionUnit().getText());
        curr = expressionNode.getRight();
        while (curr != null) {
            rhs = new BigDecimal(curr.getExpressionUnit().getText());
            lhs = calculation.computeMultiplication(lhs, rhs);
            curr = curr.getRight();
        }
        return lhs.stripTrailingZeros();
    }

    
    private static boolean isUnaryOperator (ExpressionUnit expressionUnit) {
        if (expressionUnit instanceof NumberUnit) return false;
        switch (expressionUnit.getText()) {
            case "^":
            case "²":
            case "!":
            case "+":
            case "-":
            case "×":
            case "/": return false;
            default: return true;
        }
    }


    private static BigDecimal compute (String operator, BigDecimal rhs, Calculation calculation, AngleUnit angleUnit) {
        switch (operator) {
            case "sin": return calculation.computeSin(rhs, angleUnit);
            case "cos": return calculation.computeCos(rhs, angleUnit);
            case "tan": return calculation.computeTan(rhs, angleUnit);
            case "asin": return calculation.computeAsin(rhs, angleUnit);
            case "arccos": return calculation.computeAcos(rhs, angleUnit);
            case "arctan": return calculation.computeAtan(rhs, angleUnit);
            case "sinh": return calculation.computeSinh(rhs);
            case "cosh": return calculation.computeCosh(rhs);
            case "tanh": return calculation.computeTanh(rhs);
            case "arcsinh": return calculation.computeAsinh(rhs);
            case "arccosh": return calculation.computeAcosh(rhs);
            case "arctanh": return calculation.computeAtanh(rhs);
            case "exp": return calculation.computeExponential(rhs);
            case "ln": return calculation.computeLn(rhs);
            case "log": return calculation.computeLog10(rhs);
            case "√": return calculation.computeSqrt(rhs);
            case "Fib": return calculation.computeFibonacci(rhs);
            case "isPrime": return calculation.isPrime(rhs);
            default: return null;
        }
    }

    // TODO: Interpret sign - always as an operator (subtraction or negation).
    // Convert DigitUnits into NumberInit and return it as an ArrayList.
    public static List<ExpressionUnit> digitsToNumber (LinkedList<ExpressionUnit> expressionUnits){
        List<ExpressionUnit> result = new ArrayList<>();

        StringBuilder currentNumber = new StringBuilder();
        for (ExpressionUnit expressionUnit : expressionUnits){
            if (expressionUnit instanceof OperatorUnit){
                // Operator "-" can sometimes be the prefix of a negative number.
                if (expressionUnit.getText().equals("-") && currentNumber.length() == 0){
                    currentNumber.append("-");
                }
                // Check if a number was being built. If so, add it to the result.
                else {
                    if (currentNumber.length() > 0){
                        // "-" might negate an expression rather then a number, e.g. 1*-(2+3)
                        if (currentNumber.equals("-")){
                            result.add(new OperatorUnit("-"));
                        } else {
                            result.add(new NumberUnit(currentNumber.toString()));
                        }
                        currentNumber = new StringBuilder();
                    }
                    result.add(expressionUnit);
                }
            }
            // If it's a digit, append it to the currentNumber.
            else {
                currentNumber.append(expressionUnit.getText());
            }
        }
        // Add last number if existent.
        if (currentNumber.length() > 0){
            result.add(new NumberUnit(currentNumber.toString()));
        }
        return result;
    }

}
