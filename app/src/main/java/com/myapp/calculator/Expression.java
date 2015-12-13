package com.myapp.calculator;

import com.myapp.calculator.miscellanea.Factorial;
import com.myapp.calculator.miscellanea.Fibonacci;

import java.util.ArrayList;
import java.math.BigDecimal;



/** 
 * Android calculator app
 */


/**
 * Modified version of Expression.java by Michael Kwayisi (see below)
 * 
 ***********************************************************************
 * $Id: Expression.java 329 2015-01-06 07:43:12Z mkwayisi $
 * ---------------------------------------------------------------------
 * Authored by Michael Kwayisi. Copyright (c) 2014. See license below.
 * Comments are appreciated - mic at kwayisi dot org | www.kwayisi.org
 * ---------------------------------------------------------------------
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are stringently met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions, and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions, and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *  3. The end-user documentation included with the redistribution,
 *     if any, must include the following acknowledgment:
 *       "This product includes software written by Michael Kwayisi."
 *     Alternately, this acknowledgment may appear in the software
 *     itself, if and wherever such 3rd-party acknowledgments appear.
 *  4. Neither the name of the software nor the name of its author
 *     and/or contributors may be used to endorse or promote products
 *     derived from this software without specific prior permission.
 ***********************************************************************
 */


public class Expression {
  //======================================================================
  // [DATA] Brackets, root, power, factorial, Fibonacci, isPrime
  public static final byte BRO = 0x00, BRACKET_OPEN = BRO;
  public static final byte BRC = 0x01, BRACKET_CLOSE = BRC;
  public static final byte SRT = 0x02, SQUARE_ROOT = SRT;
  public static final byte POW = 0x03, POWER = POW;
  public static final byte FCT = 0x04, FACTORIAL = FCT;
  public static final byte FIB = 0x05, FIBONACCI = FIB;
  public static final byte ISP = 0x06, IS_PRIME = ISP;
  
  //======================================================================
  // [DATA] Common mathematical functions.
  public static final byte SIN = 0x10, SINE = SIN;
  public static final byte COS = 0x11, COSINE = COS;
  public static final byte TAN = 0x12, TANGENT = TAN;
  public static final byte SIH = 0x13, HYP_SINE = SIH;
  public static final byte COH = 0x14, HYP_COSINE = COH;
  public static final byte TAH = 0x15, HYP_TANGENT = TAH;
  public static final byte LOG = 0x16, LOGARITHM = LOG;
  public static final byte NLG = 0x17, NATURAL_LOG = NLG;
  public static final byte NEG = 0x18, NEGATE = NEG;
  
  //======================================================================
  //[DATA] Inverse mathematical functions.
  public static final byte ASN = 0x20, ARC_SINE = ASN;
  public static final byte ACS = 0x21, ARC_COSINE = ACS;
  public static final byte ATN = 0x22, ARC_TANGENT = ATN;
  public static final byte ASH = 0x23, HYP_ARC_SINE = ASH;
  public static final byte ACH = 0x24, HYP_ARC_COSINE = ACH;
  public static final byte ATH = 0x25, HYP_ARC_TANGENT = ATH;
  public static final byte SQR = 0x26, SQUARED = SQR;
  public static final byte EXP = 0x27, EXPONENTIAL = EXP;
  public static final byte TEN = 0x28, POWER_OF_TEN = TEN;
  
  
  //======================================================================
  //[DATA] The ubiquitous binary operators.
  public static final byte MUL = 0x30, MULTIPLY = MUL;
  public static final byte DIV = 0x31, DIVIDE = DIV;
  public static final byte ADD = 0x32, ADDITION = ADD;
  public static final byte SUB = 0x33, SUBTRACTION = SUB;
  
  //======================================================================
  //[DATA] Mathematical constants.
  public static final byte EUL = 0x40;
  public static final byte NPI = 0x41;
  public static final byte PHI = 0x42;
  
  //======================================================================
  // [DATA] Class instance data fields.
  private ArrayList list = null;
  private Expression parent = null;
  
  //======================================================================
  // [FUNC] Primary class constructor.
  public Expression() {
  	this(null);
  }
  
  //======================================================================
  // [FUNC] Private constructor that makes objects with parents.
  private Expression(Expression parent) {
  	this.list = new ArrayList();
  	this.parent = parent;
  }
  
  //======================================================================
  // [FUNC] Returns a boolean value indicating whether this expression is
  // embedded within another expression.
  private boolean hasParent() {
  	return this.parent != null;
  }
  
  //======================================================================
  // [FUNC] Returns the parent expression of this expression.
  private Expression getParent() {
  	return this.parent;
  }
  
  //======================================================================
  // [FUNC] Returns a boolean value indicating whether the passed
  // parameter is an operator.
  private static boolean isOperator(Object obj) {
  	byte opr = obj instanceof Byte ? (byte) obj : -1;
  	return (opr >= BRO && opr <= ISP) || (opr >= SIN && opr <= NEG) ||
  			(opr >= ASN && opr <= TEN) || (opr >= MUL && opr <= SUB);
  }
  
  //======================================================================
  //[FUNC] Returns a boolean value indicating whether the passed
  //parameter is an operand (just BigDecimal for now).
  private static boolean isOperand(Object obj) {
  	return obj instanceof BigDecimal;
  }
  
  //======================================================================
  //[FUNC] Returns a boolean value indicating whether the passed
  //parameter is an operand (just BigDecimal for now).
  private static boolean isConstant(Object obj) {
  	return (obj >= EUL && obj <= PHI);
  }
  
  //======================================================================
  // [FUNC] Returns a boolean value indicating wheher the passed
  // parameter is an expression.
  private static boolean isExpression(Object obj) {
  	return obj instanceof Expression;
  }
  
  //======================================================================
  // [FUNC] Returns a boolean value indicating whether there are items on
  // the internal stack.
  public boolean hasItems() {
  	return list.size() > 0;
  }
  
  //======================================================================
  // [FUNC] Adds a new item onto the internal list.
  // Think of it like a stack ;)
  public Expression push(Object ... args) {
  	for (Object obj : args)
  		this.list.add(obj);
  	return this;
  }
  
  //======================================================================
  // [FUNC] Removes the last item (if any) from the internal stack.
  public Object pop() {
  	int index = list.size() - 1;
  	return (index >= 0) ? list.remove(index) : null;
  }
  
  //======================================================================
  // [FUNC] Evaluates and returns the result of this expression.
  public BigDecimal evaluate(LinkedList<ExpressionUnit> expressionUnits, Calculations calc, AngleUnity unity)
  	throws SyntaxErrorException, InvalidInputException, UnknownOperatorException {
  	Object obj = null;
  	Expression curr = this;
  	BigDecimal lhs = null, rhs = null;
  	
  	// STEP 0: Evaluate brackets to determine sub-expressions
  	for (int i = 0; i < list.size(); i++) {
  		obj = list.get(i);
  		if (obj.equals(BRO)) {
  			if (this.equals(curr)) {
  				curr = new Expression(curr);
  				list.set(i, curr);
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
  		throw new SyntaxErrorException("Unmatched brackets");
  	
  	// STEP 1: Compute constants
  		for (int i = 0; i < list.size(); i++) {
  			obj = list.get(i);
  			if (isConstant(obj)) switch ((byte) obj) {
  				case EUL: list.set(i, calc.computeE()); break;
  				case NPI: list.set(i, calc.computePI()); break;
  				case PHI: list.set(i, calc.computePHI()); break;
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
  				
  				/*if (rhs.compareTo(BigDecimal.ZERO) < 0)
  					throw new ArithmeticException("Root of negative no.");
  				*/
  				rhs = calc.computeSqrt(rhs);
  				list.set(i, rhs);
  				list.remove(i + 1);
  				i = Math.max(i - 2, -1);
  				break;
  				
  			case SQUARED:
  				obj = i > 0 ? list.get(i - 1) : -1;
  				if (isOperand(obj)) lhs = (BigDecimal) obj;
  				else if (isExpression(obj)) lhs = ((Expression) obj).eval();
  				else throw new SyntaxErrorException("Missing operand");
  				lhs = calc.computeSquare(lhs);
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
  				lhs = calc.computePower(lhs, rhs);
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
  				list.set(i, calc.isPrime(rhs) ? BigDecimal.ONE : BigDecimal.ZERO);
  				list.remove(i + 1);
  				i = Math.max(i - 2, -1);
  				break;
  				
  			case EXPONENTIAL:
  				obj = i + 1 < list.size() ? list.get(i + 1) : -1;
  				if (isOperand(obj)) rhs = (BigDecimal) obj;
  				else if (isExpression(obj)) rhs = ((Expression) obj).eval();
  				else throw new SyntaxErrorException("Missing operand");
  				rhs = calc.computeExponential(rhs);
  				list.set(i, rhs);
  				list.remove(i + 1);
  				break;
  				
  			case POWER_OF_TEN:
  				obj = i + 1 < list.size() ? list.get(i + 1) : -1;
  				if (isOperand(obj)) rhs = (BigDecimal) obj;
  				else if (isExpression(obj)) rhs = ((Expression) obj).eval();
  				else throw new SyntaxErrorException("Missing operand");
  				rhs = calc.computePower10(rhs);
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
  				case SIN: rhs = calc.computeSin(rhs, unity); break;
  				case COS: rhs = calc.computeCos(rhs, unity); break;
  				case TAN: rhs = clac.computeTan(rhs, unity); break;
  				case SIH: rhs = calc.computeSinh(rhs); break;
  				case COH: rhs = calc.computeCosh(rhs); break;
  				case TAH: rhs = clac.computeTanh(rhs); break;
  				case ASN: rhs = calc.computeAsin(rhs, unity); break;
  				case ACS: rhs = calc.computeAcos(rhs, unity); break;
  				case ATN: rhs = clac.computeAtan(rhs, unity); break;
  				case ASH: rhs = calc.computeAsinh(rhs); break;
  				case ACH: rhs = calc.computeAcosh(rhs); break;
  				case ATH: rhs = clac.computeAtanh(rhs); break;
  				case LOG: rhs = calc.computeLog10(rhs); break;
  				case NLG: rhs = calc.computeLn(rhs); break;
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
  				case MUL: lhs = calc.computeMultiplication(lhs, rhs); break;
  				case DIV: lhs = calc.computeDivision(lhs, rhs); break;
  				case ADD: lhs = calc.computeAddition(lhs, rhs); break;
  				case SUB: lhs = calc.computeSubtraction(lhs, rhs); break;
  			}
  			list.set(i - 1, lhs);
  			list.remove(i);
  			list.remove(i);
  			i -= 1;
  		} else if (isExpression(obj)) {
  			list.set(i, rhs = ((Expression) obj).eval());
  			obj = i > 0 ? list.get(i - 1) : -1;
  			if (isOperand(obj)) {
  				list.set(i - 1, rhs = calc.computeMultiplication(rhs, (BigDecimal) obj));
  				list.remove(i);
  				i -= 1;
  			}
  			obj = i + 1 < list.size() ? list.get(i + 1) : -1;
  			if (isOperand(obj)) {
  				list.set(i, calc.computeMultiplication(rhs, (BigDecimal) obj));
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
  		
  		list.set(0, calc.computeMultiplication(lhs, rhs));
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
  
  //======================================================================
  // [FUNC] Returns the string representation of this expression.
  public String toString() {
  	String ret = "";
  	Object obj = null;
  	
  	for (int i = 0; i < list.size(); i++) {
  		obj = list.get(i);
  		if (obj.equals(BRO)) ret += "(";
  		else if (obj.equals(BRC)) ret += ")";
  		else if (isExpression(obj)) ret += "[" + obj + "]";
  		
  		else if (obj.equals(SRT)) ret += "\u221A";
  		else if (obj.equals(CRT)) ret += "\u221B";
  		else if (obj.equals(REC)) ret += "\u02C9\u00B9";
  		else if (obj.equals(SQR)) ret += "\u00B2";
  		else if (obj.equals(CUB)) ret += "\u00B3";
  		else if (obj.equals(POW)) ret += " ^ ";
  		else if (obj.equals(FCT)) ret += "!";
  		
  		else if (obj.equals(SIN)) ret += " sin";
  		else if (obj.equals(COS)) ret += " cos";
  		else if (obj.equals(TAN)) ret += " tan";
  		else if (obj.equals(LOG)) ret += " log";
  		else if (obj.equals(NLG)) ret += " ln";
  		else if (obj.equals(INT)) ret += "\u222B";
  		else if (obj.equals(NEG)) ret += "-";
  		
  		else if (obj.equals(MUL)) ret += " \u00D7 ";
  		else if (obj.equals(DIV)) ret += " \u00F7 ";
  		else if (obj.equals(MOD)) ret += " mod ";
  		else if (obj.equals(ADD)) ret += " \u002B ";
  		else if (obj.equals(SUB)) ret += " - ";
  		else if (i > 0 && (list.get(i - 1).equals(SRT) ||
  			list.get(i - 1).equals(CRT) || list.get(i - 1).equals(NEG)))
  				ret += obj;
  		else ret += " " + obj;
  	}
  	
  	ret = ret.replaceAll("\\s\\s+", " ");
  	ret = ret.replaceAll("\\(\\s+", "(");
  	return ret.trim();
  }
}
