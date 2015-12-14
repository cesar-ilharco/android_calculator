package com.myapp.calculator;

/**
 * Android calculator app
 */

import java.math.BigDecimal;
//import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/*
 * TODO Break algorithms for multithreading
 *
 * Calculation may be used in multithreading
 */
public class Calculation {
    //Contexts used in computations
    private int DELTA_PRECISION = 10;
    private int PRECISION_DEFAULT = 20;
    private MathContext mc;
    private MathContext mcAux;

    //Math constants used in computations
    private BigDecimal PI = null;
    private BigDecimal LN2 = null;
    private BigDecimal SQRT2 = null;


    public Calculation() {
        super();
        this.mc = new MathContext(PRECISION_DEFAULT, RoundingMode.HALF_EVEN);
        this.mcAux = new MathContext(mc.getPrecision() + DELTA_PRECISION, RoundingMode.HALF_EVEN);
    }


    public Calculation (MathContext mc) {
        super();
        // TODO Choose maximum precision allowed
        if(mc.getPrecision() == 0)
            throw new IllegalArgumentException("Calculator can't calculate with unlimited precision");
        if(mc.getPrecision() > 2010)
            throw new IllegalArgumentException("The maximum precision allowed is 2000 digits");
        this.mc = mc;
        this.mcAux = new MathContext(mc.getPrecision() + DELTA_PRECISION, mc.getRoundingMode());
    }


    protected MathContext getContexto() {
        return this.mc;
    }


    private MathContext getContextoAux() {
        return this.mcAux;
    }


// Initialization of the global variables when needed

    private BigDecimal getPI() {
        if (PI == null)
            PI = computePi();
        return PI;
    }


    private BigDecimal getLn2() {
        if (LN2 == null)
            LN2 = computeLn2();
        return LN2;
    }


    private BigDecimal getSqrt2() {
        if (SQRT2 == null)
            SQRT2 = computeSqrt(new BigDecimal(2));
        return SQRT2;
    }


// Computes the necessary number of iterations needed for each function to yield the result with the desired precision

    private int getTrigIterations (int precision) {
        if (precision <= 20)
            return 20;
        if (precision <= 220)
            return precision;
        if (precision <= 440)
            return 220;
        return (int) (precision/2);
    }


    private int getAtanIterations (int precision) {
        if (precision <= 10)
            return 12;
        return (int) (1.2*precision);
    }


    private int getAsinIterations (int precision) {
        if (precision <= 10)
            return 33;
        return (int) (3.33*precision);
    }


    private int getLnIterations (int precision) {
        return (int) (2.1*precision);
    }


    private int getExpIterations (int precision) {
        if (precision <= 19)
            return 19;
        if (precision <= 190)
            return precision;
        if (precision <= 380)
            return 190;
        return (int) (precision/2);
    }


    private int getPiIterations (int precision) {
        if (precision < 10)
            return 4;
        return (int) (0.4*precision);
    }


    private int getEIterations (int precision) {
        if (precision <= 28)
            return 28;
        if (precision <= 272)
            return precision;
        if (precision <= 544)
            return 272;
        return (int) (precision/2);
    }


    private int getPhiIterations (int precision) {
        return (int) (1.7*precision);
    }


// All functions return the result with DELTA_PRECISION more digits than the number of digits asked, so we won't have a rounding problem if the result is used as argument in another function
// However, we do not guarantee the last digits (not shown to the user) to be correct

    public BigDecimal computeAddition (BigDecimal oper1, BigDecimal oper2) {
        return oper1.add(oper2, getContextoAux());
    }


    public BigDecimal computeSubtraction (BigDecimal oper1, BigDecimal oper2) {
        return oper1.subtract(oper2, getContextoAux());
    }


    public BigDecimal computeMultiplication (BigDecimal oper1, BigDecimal oper2) {
        return oper1.multiply(oper2, getContextoAux());
    }


    public BigDecimal computeDivision (BigDecimal numerator, BigDecimal denominator){
        if (denominator.compareTo(BigDecimal.ZERO) == 0)
            throw new IllegalArgumentException("Division by zero not allowed");
        return numerator.divide(denominator, getContextoAux());
    }


    public BigDecimal computeSquare (BigDecimal x) {
	    return x.multiply(x, getContextoAux());
	}


    public BigDecimal computeSin (BigDecimal angle, AngleUnit unity) {
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.RADIANS)) {
            BigDecimal remainder = angle.remainder(pi.multiply(new BigDecimal(2)));
            remainder = remainder.round(getContextoAux());
            BigDecimal piOverFour = pi.divide(new BigDecimal(4));
            BigDecimal result;
            if (remainder.compareTo(piOverFour) < 0) {
                result = sin(remainder);
            } else if (remainder.compareTo(piOverFour.multiply(new BigDecimal(3))) < 0) {
                result = cos(pi.divide(new BigDecimal(2)).subtract(remainder));
            } else if (remainder.compareTo(piOverFour.multiply(new BigDecimal(5))) < 0) {
                result = sin(pi.subtract(remainder));
            } else if (remainder.compareTo(piOverFour.multiply(new BigDecimal(7))) < 0) {
                result = cos(pi.multiply(new BigDecimal(3)).divide(new BigDecimal(2)).subtract(remainder)).negate();
            } else
                result = sin(pi.multiply(new BigDecimal(2)).subtract(remainder)).negate();
            return result;
        }
        return computeSin(degreesToRadians(angle), AngleUnit.RADIANS);
    }


    public BigDecimal computeCos (BigDecimal angle, AngleUnit unity) {
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.RADIANS)) {
            BigDecimal remainder = angle.remainder(pi.multiply(new BigDecimal(2)));
            remainder = remainder.round(getContextoAux());
            BigDecimal piOverFour = pi.divide(new BigDecimal(4));
            BigDecimal result;
            if (remainder.compareTo(piOverFour) < 0) {
                result = cos(remainder);
            } else if (remainder.compareTo(piOverFour.multiply(new BigDecimal(3))) < 0) {
                result = sin(pi.divide(new BigDecimal(2)).subtract(remainder));
            } else if (remainder.compareTo(piOverFour.multiply(new BigDecimal(5))) < 0) {
                result = cos(pi.subtract(remainder)).negate();
            } else if (remainder.compareTo(piOverFour.multiply(new BigDecimal(7))) < 0) {
                result = sin(pi.divide(new BigDecimal(2)).multiply(new BigDecimal(3)).subtract(remainder)).negate();
            } else
                result = sin(pi.multiply(new BigDecimal(2)).subtract(remainder)).negate();
            return result;
        }
        return computeCos(degreesToRadians(angle), AngleUnit.RADIANS);
    }


    public BigDecimal computeCsc (BigDecimal angle, AngleUnit unity) {
        BigDecimal pi = getPI();
        BigDecimal remainder = angle.remainder(pi);
        if (remainder.compareTo(BigDecimal.ZERO) == 0)
            throw new IllegalArgumentException("Illegal argument: division by zero");
        return BigDecimal.ONE.divide(computeSin(angle, unity), getContextoAux());
    }


    public BigDecimal computeSec (BigDecimal angle, AngleUnit unity) {
        BigDecimal pi = getPI();
        BigDecimal remainder = angle.remainder(pi);
        if (remainder.compareTo(pi.divide(new BigDecimal(2))) == 0)
            throw new IllegalArgumentException("Illegal argument: division by zero");
        return BigDecimal.ONE.divide(computeCos(angle, unity), getContextoAux());
    }


    public BigDecimal computeTan (BigDecimal angle, AngleUnit unity) {
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.RADIANS)) {
            BigDecimal remainder = angle.remainder(pi);
            if (remainder.compareTo(BigDecimal.ZERO) == 0)
                return BigDecimal.ZERO;
            if (remainder.compareTo(pi.divide(new BigDecimal(2))) == 0)
                throw new IllegalArgumentException("Illegal argument: division by zero");
        } else {
            BigDecimal remainder = angle.remainder(new BigDecimal(180));
            if (remainder.compareTo(BigDecimal.ZERO) == 0)
                return BigDecimal.ZERO;
            if (remainder.compareTo(new BigDecimal(90)) == 0)
                throw new IllegalArgumentException("Illegal argument: division by zero");
        }
        return computeSin(angle, unity).divide(computeCos(angle, unity), getContextoAux());
    }


    public BigDecimal computeCot (BigDecimal angle, AngleUnit unity) {
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.RADIANS)) {
            BigDecimal remainder = angle.remainder(pi);
            if (remainder.compareTo(BigDecimal.ZERO) == 0)
                throw new IllegalArgumentException("Illegal argument: division by zero");
            if (remainder.compareTo(pi.divide(new BigDecimal(2))) == 0)
                return BigDecimal.ZERO;
        } else {
            BigDecimal remainder = angle.remainder(new BigDecimal(180));
            if (remainder.compareTo(BigDecimal.ZERO) == 0)
                throw new IllegalArgumentException("Illegal argument: division by zero");
            if (remainder.compareTo(new BigDecimal(90)) == 0)
                return BigDecimal.ZERO;
        }
        return computeCos(angle, unity).divide(computeSin(angle, unity), getContextoAux());
    }


    private BigDecimal degreesToRadians (BigDecimal degrees){
        BigDecimal pi = getPI();
        return degrees.multiply(pi).divide(new BigDecimal(180), getContextoAux());
    }


    private BigDecimal sin (BigDecimal radians) {
        int iterations = getTrigIterations(getContexto().getPrecision());
        BigDecimal result = BigDecimal.ZERO;
        if(radians.equals(BigDecimal.ZERO))
            return result;
        int n = 1;
        BigDecimal b = radians;
        BigDecimal y = radians.pow(2);
        BigDecimal z = radians.pow(4);
        BigDecimal factorial = BigDecimal.ONE;
        while(n < iterations){
            BigDecimal oper1 = b.divide(factorial, getContextoAux());
            BigDecimal oper2 = factorial.multiply(new BigDecimal(n + 1)).multiply(new BigDecimal(n + 2), getContextoAux());
            BigDecimal oper3 = b.multiply(y).divide(oper2, getContextoAux());
            result = result.add(oper1).subtract(oper3);
            factorial = oper2.multiply(new BigDecimal(n + 3)).multiply(new BigDecimal(n + 4), getContextoAux());
            b = b.multiply(z, getContextoAux());
            n += 4;
        }
        return result.round(getContextoAux());
    }


    private BigDecimal cos (BigDecimal radians) {
        int iterations = getTrigIterations(getContexto().getPrecision());
        BigDecimal result = BigDecimal.ONE;
        if (radians.equals(BigDecimal.ZERO))
            return result;
        int n = 2;
        BigDecimal b = radians.pow(2);
        BigDecimal y = radians.pow(2);
        BigDecimal z = radians.pow(4);
        BigDecimal factorial = new BigDecimal(2);
        while (n < iterations) {
            BigDecimal oper1 = b.divide(factorial, getContextoAux());
            BigDecimal oper2 = factorial.multiply(new BigDecimal(n + 1)).multiply(new BigDecimal(n + 2), getContextoAux());
            BigDecimal oper3 = b.multiply(y).divide(oper2, getContextoAux());
            result = result.subtract(oper1).add(oper3);
            factorial = oper2.multiply(new BigDecimal(n + 3)).multiply(new BigDecimal(n + 4), getContextoAux());
            b = b.multiply(z, getContextoAux());
            n += 4;
        }
        return result.round(getContextoAux());
    }


    public BigDecimal computeSinh (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeSinh(x.negate()).negate();
        BigDecimal exp = computeExponential(x);
        BigDecimal num = exp.pow(2).subtract(BigDecimal.ONE);
        return num.divide(exp.multiply(new BigDecimal(2)), getContextoAux());
    }


    public BigDecimal computeCosh (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ONE;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeCosh(x.negate());
        BigDecimal exp = computeExponential(x);
        BigDecimal num = exp.pow(2).add(BigDecimal.ONE);
        return num.divide(exp.multiply(new BigDecimal(2)), getContextoAux());
    }


    public BigDecimal computeTanh (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeTanh(x.negate()).negate();
        BigDecimal exp = computeExponential(x).pow(2);
        BigDecimal num = exp.subtract(BigDecimal.ONE);
        BigDecimal den = exp.add(BigDecimal.ONE);
        return num.divide(den, getContextoAux());
    }


    public BigDecimal computeCsch (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            throw new IllegalArgumentException("Illegal argument: division by zero");
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeCsch(x.negate()).negate();
        BigDecimal exp = computeExponential(x);
        BigDecimal den = exp.pow(2).subtract(BigDecimal.ONE);
        return exp.multiply(new BigDecimal(2)).divide(den, getContextoAux());
    }


    public BigDecimal computeSech (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ONE;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeSech(x.negate());
        BigDecimal exp = computeExponential(x);
        BigDecimal den = exp.pow(2).add(BigDecimal.ONE);
        return exp.multiply(new BigDecimal(2)).divide(den, getContextoAux());
    }


    public BigDecimal computeCoth (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            throw new IllegalArgumentException("Illegal argument: division by zero");
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeCoth(x.negate()).negate();
        BigDecimal exp = computeExponential(x).pow(2);
        BigDecimal num = exp.add(BigDecimal.ONE);
        BigDecimal den = exp.subtract(BigDecimal.ONE);
        return num.divide(den, getContextoAux());
    }


    public BigDecimal computeAtan (BigDecimal x, AngleUnit unity) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeAtan(x.negate(), unity).negate();
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.DEGREE))
            return radiansToDegrees(computeAtan(x, AngleUnit.RADIANS));
        if (x.compareTo(BigDecimal.ONE) == 0)
            return pi.divide(new BigDecimal(4));
        BigDecimal sqrt2 = getSqrt2();
        if (x.compareTo(BigDecimal.ONE.add(sqrt2)) > 0)
            return pi.divide(new BigDecimal(2)).subtract(computeAtan(BigDecimal.ONE.divide(x, getContextoAux()), AngleUnit.RADIANS));
        if (x.compareTo(BigDecimal.ONE) > 0)
            return pi.divide(new BigDecimal(4)).add(computeAtan((x.subtract(BigDecimal.ONE)).divide(x.add(BigDecimal.ONE), getContextoAux()), AngleUnit.RADIANS));
        if (x.compareTo(sqrt2.subtract(BigDecimal.ONE)) > 0)
            return pi.divide(new BigDecimal(4)).subtract(computeAtan((BigDecimal.ONE.subtract(x)).divide(x.add(BigDecimal.ONE), getContextoAux()), AngleUnit.RADIANS));
        return atan(x);
    }


    private BigDecimal atan (BigDecimal x) {
        int iterations = getAtanIterations(getContexto().getPrecision());
        int n = 0;
        BigDecimal y = x.pow(2).divide(BigDecimal.ONE.add(x.pow(2)), getContextoAux());
        BigDecimal result = x.divide(BigDecimal.ONE.add(x.pow(2)), getContextoAux());
        BigDecimal factor = result;
        while (n < iterations) {
            n += 1;
            factor = factor.multiply(new BigDecimal(2*n)).multiply(y).divide(BigDecimal.ONE.add(new BigDecimal(2*n)), getContextoAux());
            result = result.add(factor);
        }
        return result.round(getContextoAux());
    }


    public BigDecimal computeAsin (BigDecimal x, AngleUnit unity) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return computeAsin(x.negate(), unity).negate();
        if (x.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("Answer not real");
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.DEGREE))
            return radiansToDegrees(computeAsin(x, AngleUnit.RADIANS));
        BigDecimal sqrt2 = getSqrt2();
        if (x.compareTo(BigDecimal.ONE.divide(sqrt2, getContextoAux())) > 0)
            return pi.divide(new BigDecimal(2)).subtract(computeAsin(computeSqrt(BigDecimal.ONE.subtract(x.pow(2))), AngleUnit.RADIANS));
        return asin(x);
    }


    // Accepts up to a 6900-digit precision only
    private BigDecimal asin (BigDecimal x) {
        int iterations = getAsinIterations(getContexto().getPrecision());
        int n = 0;
        BigDecimal y = x.pow(2);
        BigDecimal factor = x;
        BigDecimal result = x;
        while (n < iterations) {
            n += 1;
            factor = factor.multiply(y.multiply(new BigDecimal(2*n - 1).pow(2))).divide(new BigDecimal(2*n*(2*n + 1)), getContextoAux());
            result = result.add(factor);
        }
        return result.round(getContextoAux());
    }


    public BigDecimal computeAcos (BigDecimal x, AngleUnit unity) {
        if (x.compareTo(BigDecimal.ONE) == 0)
            return BigDecimal.ZERO;
        if (unity.equals(AngleUnit.DEGREE))
            return new BigDecimal(90).subtract(computeAsin(x, AngleUnit.DEGREE));
        BigDecimal pi = getPI();
        return pi.divide(new BigDecimal(2), getContextoAux()).subtract(computeAsin(x, AngleUnit.RADIANS));
    }


    public BigDecimal computeAcsc (BigDecimal x, AngleUnit unity) {
        if (x.abs().compareTo(BigDecimal.ONE) < 0)
            throw new IllegalArgumentException("Answer not real");
        return computeAsin(BigDecimal.ONE.divide(x, getContextoAux()), unity);
    }


    public BigDecimal computeAsec (BigDecimal x, AngleUnit unity) {
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.DEGREE))
            return radiansToDegrees(computeAsec(x, AngleUnit.RADIANS));
        if (x.compareTo(BigDecimal.ONE.negate()) <= 0)
            return pi.subtract(computeAtan(computeSqrt(x.pow(2).subtract(BigDecimal.ONE)), AngleUnit.RADIANS));
        if (x.compareTo(BigDecimal.ONE) >= 0)
            return computeAtan(computeSqrt(x.pow(2).subtract(BigDecimal.ONE)), AngleUnit.RADIANS);
        throw new IllegalArgumentException("Answer not real");
    }


    public BigDecimal computeAcot (BigDecimal x, AngleUnit unity) {
        BigDecimal pi = getPI();
        if (unity.equals(AngleUnit.DEGREE))
            return radiansToDegrees(computeAcot(x, AngleUnit.RADIANS));
        if (x.compareTo(BigDecimal.ZERO) < 0)
            return pi.divide(new BigDecimal(-2)).subtract(computeAtan(x, AngleUnit.RADIANS));
        return pi.divide(new BigDecimal(2)).subtract(computeAtan(x, AngleUnit.RADIANS));
    }


    private BigDecimal radiansToDegrees (BigDecimal rad){
        BigDecimal pi = getPI();
        return rad.multiply(new BigDecimal(180)).divide(pi, getContextoAux());
    }


    public BigDecimal computeAsinh (BigDecimal x) {
        return asinh(x);
    }


    public BigDecimal computeAcosh (BigDecimal x) {
        if (x.compareTo(BigDecimal.ONE) < 0)
            throw new IllegalArgumentException("Answer not real");
        return acosh(x);
    }


    public BigDecimal computeAtanh (BigDecimal x) {
        if (x.abs().compareTo(BigDecimal.ONE) >= 0)
            throw new IllegalArgumentException("Answer not real");
        return atanh(x);
    }


    public BigDecimal computeAcsch (BigDecimal x) {
        return asinh(BigDecimal.ONE.divide(x, getContextoAux()));
    }


    public BigDecimal computeAsech (BigDecimal x) {
        if (x.compareTo(BigDecimal.ONE) > 0 || x.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Answer not real");
        return acosh(BigDecimal.ONE.divide(x, getContextoAux()));
    }


    public BigDecimal computeAcoth (BigDecimal x) {
        if (x.abs().compareTo(BigDecimal.ONE) <= 0)
            throw new IllegalArgumentException("Answer not real");
        return atanh(BigDecimal.ONE.divide(x, getContextoAux()));
    }


    private BigDecimal asinh (BigDecimal x) {
        return computeLn(x.add(computeSqrt(x.pow(2).add(BigDecimal.ONE))));
    }


    private BigDecimal acosh (BigDecimal x) {
        return computeLn(x.add(computeSqrt(x.pow(2).subtract(BigDecimal.ONE))));
    }


    private BigDecimal atanh (BigDecimal x) {
        return computeLn((x.add(BigDecimal.ONE)).divide(BigDecimal.ONE.subtract(x), getContextoAux())).divide(new BigDecimal(2), getContextoAux());
    }


    public BigDecimal computePower (BigDecimal x, BigDecimal y) {
        if (x.compareTo(BigDecimal.ZERO) == 0) {
            if (y.compareTo(BigDecimal.ZERO) == 0)
                throw new IllegalArgumentException("Undefined");
            return BigDecimal.ZERO;
        }
        if (x.compareTo(BigDecimal.ZERO) < 0) {
            if (y.scale() > 0)
                throw new IllegalArgumentException("Answer not real");
            if (x.compareTo(BigDecimal.ONE.negate()) == 0)
                return BigDecimal.ONE.subtract(new BigDecimal(2).multiply(y.remainder(new BigDecimal(2))));
            return computePower(x.negate(), y).multiply(BigDecimal.ONE.subtract(new BigDecimal(2).multiply(y.remainder(new BigDecimal(2)))));
        }
        if (x.compareTo(BigDecimal.ONE) == 0)
            return BigDecimal.ONE;
        if (y.compareTo(new BigDecimal(2).pow(30)) > 0)
            return computePower(x, y.divide(new BigDecimal(4))).pow(4, getContextoAux());
        return x.pow(y.intValue(), getContextoAux()).multiply(computeExponential(y.subtract(new BigDecimal(y.intValue())).multiply(computeLn(x), getContextoAux())), getContextoAux());
    }

    //TODO Update this function so it won't be stupid when the value of x is an integer, and create a global constant ln(10)
    public BigDecimal computePower10 (BigDecimal x) {
		return computePower(BigDecimal.TEN, x);
	}
    

    public BigDecimal computeLn (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Answer not real");
        BigDecimal two = new BigDecimal(2);
        if (x.compareTo(two) == 0)
            return getLn2();
        if (x.compareTo(new BigDecimal(0.5)) < 0)
            return computeLn(BigDecimal.ONE.divide(x, getContextoAux())).negate();
        BigDecimal ln2 = getLn2();
        BigDecimal exp2 = BigDecimal.ONE;
        int k = 0;
        while (exp2.compareTo(x) < 0) {
            exp2 = exp2.multiply(two);
            k += 1;
        }
        return ln2.multiply(new BigDecimal(k)).add(ln(x.divide(exp2))).round(getContextoAux());
    }


    private BigDecimal ln (BigDecimal x) {
        int iterations = getLnIterations(getContexto().getPrecision());
        if (x.compareTo(BigDecimal.ONE) == 0)
            return BigDecimal.ZERO;
        int n = 1;
        BigDecimal s = (x.subtract(BigDecimal.ONE)).divide(x.add(BigDecimal.ONE), getContextoAux());
        BigDecimal y = s.pow(2);
        BigDecimal result = s;
        while (n < iterations) {
            n += 2;
            s = s.multiply(y, getContextoAux());
            result = result.add(s.divide(new BigDecimal(n), getContextoAux()));
        }
        return result.multiply(new BigDecimal(2));
    }


    public BigDecimal computeLog10 (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Answer not real");
        return computeLn(x).divide(computeLn(BigDecimal.TEN), getContextoAux());
    }


    public BigDecimal computeSqrt (BigDecimal x) {
        if (x.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;
        if (x.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Answer not real");
        if (x.compareTo(BigDecimal.ONE) == 0)
            return BigDecimal.ONE;
        return computeExponential(computeLn(x).divide(new BigDecimal(2)));
    }


    // Ask 10 digits more than demanded by the user
    public BigDecimal computeExponential (BigDecimal x) {
        int iterations = getExpIterations(getContexto().getPrecision());
        if (x.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ONE.divide(computeExponential(x.negate()), getContextoAux());
        }
        if (x.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }
        BigDecimal ln2 = getLn2();
        BigDecimal[] array = x.divideAndRemainder(ln2);
        BigDecimal remainderExp = array[1];
        BigDecimal exp = BigDecimal.ONE;
        BigDecimal fact = BigDecimal.ONE;
        int n = 1;
        while (n < iterations) {
            fact = fact.multiply(new BigDecimal(n), getContextoAux());
            exp = exp.add(remainderExp.divide(fact, getContextoAux()));
            remainderExp = remainderExp.multiply(array[1], getContextoAux());
            n += 1;
        }
        if (x.compareTo(new BigDecimal(2).pow(29)) > 0)
            return computeExponential(x.divide(new BigDecimal(2))).pow(2, getContextoAux());
        return exp.multiply(new BigDecimal(2).pow(array[0].intValue(), getContextoAux()), getContextoAux());
    }


    public BigDecimal computePi() {
        int iterations = getPiIterations(getContexto().getPrecision());
        int n = 0;
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal a = new BigDecimal(-32);
        BigDecimal b = new BigDecimal(-1);
        BigDecimal c = new BigDecimal(256);
        BigDecimal d = new BigDecimal(-64);
        BigDecimal e = new BigDecimal(-4);
        BigDecimal exp2 = b.divide(new BigDecimal(1024), getContextoAux());
        while (n < iterations) {
            BigDecimal oper1 = a.divide(new BigDecimal(4*n + 1), getContextoAux()).add(b.divide(new BigDecimal(4*n + 3), getContextoAux()));
            BigDecimal oper2 = c.divide(new BigDecimal(10*n + 1), getContextoAux()).add(d.divide(new BigDecimal(10*n + 3), getContextoAux()));
            BigDecimal oper3 = e.divide(new BigDecimal(10*n + 5), getContextoAux()).add(e.divide(new BigDecimal(10*n + 7), getContextoAux())).add(BigDecimal.ONE.divide(new BigDecimal(10*n + 9), getContextoAux()));
            result = result.add(exp2.pow(n).multiply(oper1.add(oper2).add(oper3)));
            n += 1;
        }
        return result.divide(new BigDecimal(64), getContextoAux());
    }


    public BigDecimal computeE () {
        int iterations = getEIterations(getContexto().getPrecision());
        int n = 3;
        BigDecimal num = BigDecimal.ONE;
        BigDecimal den = new BigDecimal(2);
        while (n < iterations) {
            BigDecimal a = new BigDecimal(n);
            num = num.multiply(a, getContextoAux()).add(BigDecimal.ONE);
            den = den.multiply(a, getContextoAux());
            n += 1;
        }
        return num.divide(den, getContextoAux()).add(new BigDecimal(2));
    }


    private BigDecimal computeLn2 () {
        int iterations = getLnIterations(getContexto().getPrecision());
        int n = 1;
        BigDecimal num = BigDecimal.ONE;
        BigDecimal den = new BigDecimal(3);
        BigDecimal nine = new BigDecimal(9);
        BigDecimal m = BigDecimal.ONE;
        while (n < iterations) {
            n += 2;
            BigDecimal a = new BigDecimal(n);
            num = num.multiply(a).multiply(nine).add(m);
            den = den.multiply(a).multiply(nine);
            m = m.multiply(a);
        }
        return num.multiply(new BigDecimal(2)).divide(den, getContextoAux());
    }


    public BigDecimal computePhi () {
        int iterations = getPhiIterations(getContexto().getPrecision());
        int n = 1;
        BigDecimal result = new BigDecimal(13).divide(new BigDecimal(8));
        BigDecimal ft = new BigDecimal(-1).divide(new BigDecimal(128));
        while (n < iterations) {
            result = result.add(ft);
            ft = ft.multiply(new BigDecimal(-2*n - 1)).divide(new BigDecimal(8*n + 16), getContextoAux());
            n += 1;
        }
        return result.round(getContextoAux());
    }


    // Works up to 4.6*10^18
    public boolean isPrime (BigDecimal N) {
        long n = N.longValue();
        if (N.compareTo(new BigDecimal(n)) != 0)
            return false;
        if (n == 2 || n == 3)
            return true;
        if (n < 2 || n%2 == 0)
            return false;
        if (n < 9)
            return true;
        if (n%3 == 0)
            return false;
        int k = 5;
        long root = computeSqrt(N).longValue();
        while (k <= root) {
            if (n%k == 0)
                return false;
            if (n%(k+2) == 0)
                return false;
            k += 6;
        }
        return true;
    }

}

