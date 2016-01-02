package com.myapp.calculator;

import com.myapp.calculator.ast.Expression;
import com.myapp.calculator.ast.ExpressionNode;
import com.myapp.calculator.ast.ExpressionUnit;
import com.myapp.calculator.ast.NumberUnit;
import com.myapp.calculator.ast.OperatorUnit;
import com.myapp.calculator.miscellanea.Factorial;
import com.myapp.calculator.miscellanea.Fibonacci;
import com.myapp.calculator.utils.MyInt;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Android calculator app
 */

// Process user's input expression and computes the output result.
public class Kernel {

    public static BigDecimal evaluate (ExpressionNode expressionNode, int scale, AngleUnit angleUnit)
                                                 throws SyntaxErrorException, InvalidInputException {

        if (expressionNode == null) return BigDecimal.ZERO;
        if (expressionNode.getExpressionUnit().getText().equals("-"))
            expressionNode = new ExpressionNode(new NumberUnit(BigDecimal.ONE.negate()), null, expressionNode.getRight();


        Calculation calculation = new Calculation();
        ExpressionUnit expressionUnit = null;
        BigDecimal lhs = null;
        BigDecimal rhs = null;
        ExpressionNode last = null;
        ExpressionNode next = null;
        String operator = null;
        

        // STEP 1: Compute constants
        ExpressionNode curr = expressionNode;
        while (curr != null) {
            expressionUnit = curr.getExpressionUnit();
            if (expressionUnit instanceof Constants) {
                switch (expressionUnit.getText()) {
                    case "e": lhr = calculation.computeE(); break;
                    case "pi": lhr = calculation.computePI(); break;
                    case "phi": lhr = calculation.computePHI(); break;
                }
                curr = new ExpressionNode(new NumberUnit(lhr.toString()), curr.getLeft(), curr.getRight());
                if (curr.getLeft() == null) expressionNode = curr;
            }
            curr = curr.getRight();
        }


        // STEP 2: Evaluate brackets to determine sub-expressions
        curr = expressionNode;
        while (curr != null) {
            expressionUnit = curr.getExpressionUnit();
            if (expressionUnit.getText().equals('(')) {
                // Initialization
                last = curr.getLeft();
                curr = curr.getRight();
                int nbBrackets = 1;
                ExpressionNode currChild = null;
                ExpressionNode headChild = null;
                // Loop will break when expression is finished or all brackets are matched
                while (curr != null) {
                    expressionUnit = curr.getExpressionUnit();
                    if (expressionUnit.getText().equals(')') {
                        nbBrackets--;
                        // Breaks if brackets are all matched
                        if (nbBrackets == 0) break;
                    } else if (expressionUnit.getText().equals('(')) nbBrackets++;
                    // Initialization of the child expression
                    if (currChild == null) {
                        currChild = new ExpressionNode(expressionUnit);
                        headChild = currChild;
                    } else {
                        // Adds node curr to child and updates currChild
                        currChild.setRight(new ExpressionNode(expressionUnit));
                        currChild = currChild.getRight();
                    }
                    // Updates current node
                    curr = curr.getRight();
                }
                // If there are more open brackets than close brackets
                if (curr == null) throw new SyntaxErrorException("Unmatched brackets");
                // Computes the child expression
                next = curr.getRight();
                curr.getLeft().setRight(null);
                resultChild = evaluate(headChild, scale, angleUnit);
                // Updates original expression with the result of the child expression
                curr = new ExpressionNode(new NumberUnit(resultChild.toString()), last, next);
                if (last == null) expressionNode = curr;
            } else if (expressionUnit.getText().equals(')'))
                // If there are more close brackets than open brackets
                throw new SyntaxErrorException("Unmatched brackets");
            curr = curr.getRight();
        }


        // STEP 3: Unary operators (to the left)
        curr = expressionNode;
        while (curr != null) {
            expressionUnit = curr.getExpressionUnit();
            //############################################################
            if (isUnaryOperator(expressionUnit)) {
                last = curr.getLeft();
                curr = curr.getRight();
                if (curr == null) throw new SyntaxErrorException("Missing operand");
                expressionUnit = curr.getExpressionUnit();
                
                while (isUnaryOperator(expressionUnit)) {
                    curr = curr.getRight();
                    if (curr == null) throw new SyntaxErrorException("Missing operand");
                    expressionUnit = curr.getExpressionUnit();
                }
                if (expressionUnit instanceof OperatorUnit) throw new SyntaxErrorException("Missing operand");
                rhs = new BigDecimal(expressionUnit.getText());
                next = curr.getRight();
                while (!curr.getLeft().equals(last)) {
                    curr = curr.getLeft();
                    rhs = compute(curr.getExpressionUnit(), rhs);
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
            operand = expressionUnit.getText();
            if (operand == "sqr" || operand == "!" || operand == "^") {
                next = curr.getRight();
                curr = curr.getLeft();
                if (curr == null) throw new SyntaxErrorException("Missing operand");
                expressionUnit = curr.getExpressionUnit();
                if (expressionUnit instanceof OperatorUnit) throw new SyntaxErrorException("Missing operand");
                lhs = new BigDecimal(expressionUnit.getText());
                last = curr.getLeft();
                switch (operand) {
                    case "sqr":
                        lhs = calculation.computeSqr(lhs);
                    case "!":
                        lhs = calculation.computeFactorial(lhs);
                    case "^":
                        if (next == null || next.getExpressionUnit() instanceof OperatorUnit)
                            throw new SyntaxErrorException("Missing operand");
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
                operand = curr.getExpressionUnit().getText();
                if (s == 0 && (operand.equals("*") || operand.equals("/")) ||
                                    s == 1 && (operand.equals("+") || operand.equals("-"))) {
                    last = curr.getLeft();
                    next = curr.getRight();
                    if (last == null || next == null) throw new SyntaxErrorException("Missing operand");
                    if (last.getExpressionUnit() instanceof OperatorUnit || next.getExpressionUnit() instanceof OperatorUnit)
                        throw new SyntaxErrorException("Missing operand");
                    lhs = new BigDecimal(last.getExpressionUnit().getText());
                    rhs = new BigDecimal(next.getExpressionUnit().getText());
                    switch (operand) {
                        case "*": lhs = calculation.computeMultiplication(lhs, rhs); break;
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



    // TODO: Create a global operator precedence and generalize the Syntax Tree construction (parse method).
    private static final Map<String, Integer> operatorPrecedence = new HashMap<String, Integer>(){{
        put("+", 1);
        put("-", 1);
        put("×", 2);
        put("/", 3);
    }};


    // Suits for parsing and evaluating basic operations only: +,-,×,/ and parenthesis.
    public static String evaluateBasic (LinkedList<ExpressionUnit> expressionUnits, int scale) {

        List<ExpressionUnit>  convertedList = digitsToNumber(expressionUnits);

        if (! isValid(convertedList)){
            return "formatting error";
        }

        BigDecimal result = null;
        try {
            ExpressionNode root = parse(convertedList);
            result = evaluateRecursive(root, scale);
        } catch (IOException e){

        }

        return result == null ? "" : result.toString();
    }

    private static BigDecimal evaluateRecursive (ExpressionNode root, int scale){
        if (root == null){
            return null;
        }
        switch(root.getExpressionUnit().getText()){
            case "+": return evaluateRecursive(root.getLeft(), scale).add(evaluateRecursive(root.getRight(), scale));
            case "-": return evaluateRecursive(root.getLeft(), scale).subtract(evaluateRecursive(root.getRight(), scale));
            case "×": return evaluateRecursive(root.getLeft(), scale).multiply(evaluateRecursive(root.getRight(), scale));
            case "/": return evaluateRecursive(root.getLeft(), scale).divide(evaluateRecursive(root.getRight(), scale), scale, BigDecimal.ROUND_HALF_EVEN);
        }
        return new BigDecimal(root.getExpressionUnit().getText());
    }

    // TODO: Implement expression verifier.
    static boolean isValid (final List<ExpressionUnit> expressionUnits){
        return true;
    }

    // Create Syntax Tree from list of expression units.
    public static ExpressionNode parse(final List<ExpressionUnit> expressionUnits) throws  IOException {
        if (expressionUnits.isEmpty()){
            return null;
        }
        return parseAddSub(expressionUnits, new MyInt(-1));
    }

    private static ExpressionNode parseAddSub(final List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = parseMulDiv(expressionUnits, index);
        if (index.getValue() < expressionUnits.size() - 1) {

            ExpressionUnit expressionUnit = expressionUnits.get(index.increaseAndGet());

            while (expressionUnit.getText().equals("+") || expressionUnit.getText().equals("-")) {
                root = new ExpressionNode(expressionUnit, root, parseMulDiv(expressionUnits, index));
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increaseAndGet());
                } else {
                    index.increaseAndGet();
                    break;
                }
            }
        }
        index.decreaseAndGet();
        // System.out.println("DEBUG: Exiting ParseAddSub, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }


    private static ExpressionNode parseMulDiv(final List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = parseSimpleTerm(expressionUnits, index);

        if (index.getValue() < expressionUnits.size() - 1) {
            ExpressionUnit expressionUnit = expressionUnits.get(index.increaseAndGet());

            while (expressionUnit.getText().equals("×") || expressionUnit.getText().equals("/")) {
                root = new ExpressionNode(expressionUnit, root, parseSimpleTerm(expressionUnits, index));
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increaseAndGet());
                } else {
                    index.increaseAndGet();
                    break;
                }
            }
        }
        index.decreaseAndGet();
        // System.out.println("DEBUG: Exiting ParseMultiDiv, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
    }

    private static ExpressionNode parseSimpleTerm(final List<ExpressionUnit> expressionUnits, MyInt index) throws IOException {
        ExpressionNode root = null;

        if (index.getValue() < expressionUnits.size() - 1) {
            ExpressionUnit expressionUnit = expressionUnits.get(index.increaseAndGet());
            if (expressionUnit instanceof NumberUnit) {
                root = new ExpressionNode(expressionUnit);
            } else if (expressionUnit.getText().equals("(")) {
                root = parseAddSub(expressionUnits, index);
                if (index.getValue() < expressionUnits.size() - 1) {
                    expressionUnit = expressionUnits.get(index.increaseAndGet());
                    if (!expressionUnit.getText().equals(")")) {
                        throw new IOException("Unbalanced parenthesis count");
                    }
                } else {
                    index.increaseAndGet();
                }
            }
        }
        // System.out.println("DEBUG: Exiting ParseSimpleTerm, index = " + index.getValue() + ", root = " + (root == null ? "null" : root.getExpressionUnit().getText()));
        return root;
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
