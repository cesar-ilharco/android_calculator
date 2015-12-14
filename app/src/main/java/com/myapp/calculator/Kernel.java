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

    public static BigDecimal evaluate (Expression expression, int scale, AngleUnit angleUnit) throws IOException {

        Calculation calculation = new Calculation();

        ExpressionUnit expressionUnit = null;
        BigDecimal lhs = null;
        BigDecimal rhs = null;

        // Maps an expression to its parent expression.
        Map<Expression, Expression> expressionParents = new HashMap<>();
        List<ExpressionUnit> expressionUnits = expression.getUnits();

        // STEP 0: Evaluate brackets to determine sub-expressions
        for (int i = 0; i < expressionUnits.size(); i++) {
            obj = expressionUnits.get(i);
            if (obj.equals(BRO)) {
                if (this.equals(curr)) {
                    curr = new Expression(curr);
                    expressionUnits.set(i, curr);
                    continue;
                } else {
                    curr = new Expression(curr);
                    curr.getParent().push(curr);
                }
            } else if (obj.equals(BRC)) {
                curr = curr.getParent();
                if (curr == null) break;
            } else if (this.equals(curr)) {
                if (!(isOperator(obj) || isExpression(obj)))
                    list.set(i, new BigDecimal(obj.toString()));
                continue;
            } else curr.push(obj);
            list.remove(i--);
        }

        if (!this.equals(curr))
            throw new IOException("Unmatched brackets");

        // STEP 1: Compute constants
        for (int i = 0; i < list.size(); i++) {
            obj = list.get(i);
            if (isConstant(obj)) switch ((byte) obj) {
                case EUL: list.set(i, calculation.computeE()); break;
                case NPI: list.set(i, calculation.computePI()); break;
                case PHI: list.set(i, calculation.computePHI()); break;
            }
        }


        // STEP 2: Roots, powers, factorial, Fibonacci, isPrime.
        for (int i = 0; i < list.size(); i++) {
            obj = list.get(i);
            if (isOperator(obj)) switch ((byte) obj) {
                case SQUARE_ROOT:
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (obj.equals(SRT)) continue;
                    else if (isOperand(obj)) rhs = (BigDecimal) obj;
                    else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");

                    if (rhs.compareTo(BigDecimal.ZERO) < 0)
                        throw new ArithmeticException("Root of negative no.");

                    rhs = calculation.computeSqrt(rhs);
                    list.set(i, rhs);
                    list.remove(i + 1);
                    i = Math.max(i - 2, -1);
                    break;

                case SQUARED:
                    obj = i > 0 ? list.get(i - 1) : -1;
                    if (isOperand(obj)) lhs = (BigDecimal) obj;
                    else if (isExpression(obj)) lhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    lhs = calculation.computeSquare(lhs);
                    list.set(i - 1, lhs);
                    list.remove(i);
                    list.remove(i);
                    // Must test, possibly wrong
                    i = Math.max(i - 2, -1);
                    break;

                case POWER:
                    obj = i + 2 < list.size() ? list.get(i + 2) : -1;
                    if (obj.equals(POW)) continue;
                    obj = i > 0 ? list.get(i - 1) : -1;
                    if (isOperand(obj)) lhs = (BigDecimal) obj;
                    else if (isExpression(obj)) lhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (isOperand(obj)) rhs = (BigDecimal) obj;
                    else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    lhs = calculation.computePower(lhs, rhs);
                    list.set(i - 1, lhs);
                    list.remove(i);
                    list.remove(i);
                    i = Math.max(i - 3, -1);
                    break;

                case FACTORIAL:
                    obj = i > 0 ? list.get(i - 1) : -1;
                    if (isOperand(obj)) lhs = (BigDecimal) obj;
                    else if (isExpression(obj)) lhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    //if (lhs.compareTo(BigDecimal.ZERO) < 0)
                    //	throw new InvalidInputException("Factorial input less than zero");
                    //else if (lhs.compareTo(new BigDecimal(5000)) > 0)
                    //	throw new InvalidInputException("Factorial input too large (>5000)");

                    //#################################################################################
                    // Test if lhs has integer value
                    lhs = Factorial.apply(lhs.intValue())
                    list.set(i - 1, lhs);
                    //#################################################################################

                    list.remove(i);
                    i -= 1;
                    break;

                case FIBONACCI:
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (obj.equals(FIB)) continue;
                    else if (isOperand(obj)) rhs = (BigDecimal) obj;
                    else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    //#########################################################
                    rhs = Fibonacci.apply(rhs);
                    list.set(i, rhs);
                    list.remove(i + 1);
                    i = Math.max(i - 2, -1);
                    break;

                case IS_PRIME:
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (isOperand(obj)) rhs = (BigDecimal) obj;
                    else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    list.set(i, calculation.isPrime(rhs) ? BigDecimal.ONE : BigDecimal.ZERO);
                    list.remove(i + 1);
                    i = Math.max(i - 2, -1);
                    break;

                case EXPONENTIAL:
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (isOperand(obj)) rhs = (BigDecimal) obj;
                    else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    rhs = calculation.computeExponential(rhs);
                    list.set(i, rhs);
                    list.remove(i + 1);
                    break;

                case POWER_OF_TEN:
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (isOperand(obj)) rhs = (BigDecimal) obj;
                    else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    rhs = calculation.computePower10(rhs);
                    list.set(i, rhs);
                    list.remove(i + 1);
                    break;
            }
        }

        // STEP 3: Common mathematical functions.
        for (int i = list.size() - 1; i >= 0; i--) {
            obj = list.get(i);
            if (obj.equals(SIN) || obj.equals(COS) || obj.equals(TAN) ||
                    obj.equals(LOG) || obj.equals(NLG) || obj.equals(FIB) ||
                    obj.equals(NEG))
            {
                obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                if (isOperand(obj)) rhs = (BigDecimal) obj;
                else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                else throw new SyntaxErrorException("Missing operand");
                switch ((byte) list.get(i)) {
                    case SIN: rhs = calculation.computeSin(rhs, angleUnit); break;
                    case COS: rhs = calculation.computeCos(rhs, angleUnit); break;
                    case TAN: rhs = calculation.computeTan(rhs, angleUnit); break;
                    case SIH: rhs = calculation.computeSinh(rhs); break;
                    case COH: rhs = calculation.computeCosh(rhs); break;
                    case TAH: rhs = calculation.computeTanh(rhs); break;
                    case ASN: rhs = calculation.computeAsin(rhs, angleUnit); break;
                    case ACS: rhs = calculation.computeAcos(rhs, angleUnit); break;
                    case ATN: rhs = calculation.computeAtan(rhs, angleUnit); break;
                    case ASH: rhs = calculation.computeAsinh(rhs); break;
                    case ACH: rhs = calculation.computeAcosh(rhs); break;
                    case ATH: rhs = calculation.computeAtanh(rhs); break;
                    case LOG: rhs = calculation.computeLog10(rhs); break;
                    case NLG: rhs = calculation.computeLn(rhs); break;
                    case NEG: rhs = rhs.negate(); break;
                    default: continue;
                }
                list.set(i, rhs);
                list.remove(i + 1);
            }
        }

        // STEP 4: Multiplicative and additive operations.
        for (int s = 0; s < 2; s++)
            for (int i = 0; i < list.size(); i++) {
                obj = list.get(i);
                if (s == 0 && (obj.equals(MUL) || obj.equals(DIV) ||
                        s == 1 && (obj.equals(ADD) || obj.equals(SUB)))
                {
                    obj = i > 0 ? list.get(i - 1) : -1;
                    if (isOperand(obj)) lhs = (BigDecimal) obj;
                    else if (isExpression(obj)) lhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (isOperand(obj)) rhs = (BigDecimal) obj;
                    else if (isExpression(obj)) rhs = ((Expression) obj).eval();
                    else throw new SyntaxErrorException("Missing operand");
                    switch ((byte) list.get(i)) {
                        case MUL: lhs = calculation.computeMultiplication(lhs, rhs); break;
                        case DIV: lhs = calculation.computeDivision(lhs, rhs); break;
                        case ADD: lhs = calculation.computeAddition(lhs, rhs); break;
                        case SUB: lhs = calculation.computeSubtraction(lhs, rhs); break;
                    }
                    list.set(i - 1, lhs);
                    list.remove(i);
                    list.remove(i);
                    i -= 1;
                } else if (isExpression(obj)) {
                    list.set(i, rhs = ((Expression) obj).eval());
                    obj = i > 0 ? list.get(i - 1) : -1;
                    if (isOperand(obj)) {
                        list.set(i - 1, rhs = calculation.computeMultiplication(rhs, (BigDecimal) obj));
                        list.remove(i);
                        i -= 1;
                    }
                    obj = i + 1 < list.size() ? list.get(i + 1) : -1;
                    if (isOperand(obj)) {
                        list.set(i, calculation.computeMultiplication(rhs, (BigDecimal) obj));
                        list.remove(i + 1);
                    }
                }
            }

        // STEP 5: Multiply any remaining items. A cheap way to get my math right :)
        // For example, 2 sin 30 == 2 * sin 30
        while (list.size() > 1) {
            obj = list.get(0);
            if (isExpression(obj))
                lhs = ((Expression) obj).eval();
            else if (isOperand(obj))
                lhs = (BigDecimal) obj;
            else throw new UnknownOperatorException();
            obj = list.get(1);
            if (isExpression(obj))
                rhs = ((Expression) obj).eval();
            else if (isOperand(obj))
                rhs = (BigDecimal) obj;
            else throw new UnknownOperatorException();

            list.set(0, calculation.computeMultiplication(lhs, rhs));
            list.remove(1);
        }

        if (list.size() == 0)
            throw new SyntaxErrorException("Empty "
                    + (this.hasParent() ? "brackets" : "expression"));
        else if (isExpression(list.get(0)))
            list.set(0, ((Expression) list.get(0)).eval());

        lhs = (BigDecimal) list.get(0);
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
