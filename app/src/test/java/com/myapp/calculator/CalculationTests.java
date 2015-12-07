package com.myapp.calculator;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Android calculator app
 */

public class CalculationTests {
    MathContext mc = new MathContext(200, RoundingMode.HALF_EVEN);
    Calculation calc = new Calculation(mc);

    @Test
    @Ignore
    public void testAssertTrigonometric() {
        BigDecimal PI = calc.computePi();

        assertTrue(calc.computeSin(BigDecimal.ZERO, AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeSin(BigDecimal.ZERO, AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeSin(PI.divide(new BigDecimal(2)), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSin(new BigDecimal(90), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSin(PI, AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeSin(new BigDecimal(180), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);

        assertTrue(calc.computeCos(BigDecimal.ZERO, AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCos(BigDecimal.ZERO, AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCos(PI.divide(new BigDecimal(2)), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeCos(new BigDecimal(90), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeCos(PI, AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeCos(new BigDecimal(180), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeTan(BigDecimal.ZERO, AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeTan(BigDecimal.ZERO, AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeTan(PI.divide(new BigDecimal(4)), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeTan(new BigDecimal(45), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeTan(PI.multiply(new BigDecimal("0.75")), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeTan(new BigDecimal(135), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeCsc(PI.divide(new BigDecimal(2)), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCsc(new BigDecimal(90), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCsc(PI.multiply(new BigDecimal(1.5)), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeCsc(new BigDecimal(270), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeSec(BigDecimal.ZERO, AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSec(BigDecimal.ZERO, AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSec(PI, AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeSec(new BigDecimal(180), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeCot(PI.divide(new BigDecimal(4)), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCot(new BigDecimal(45), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCot(PI.divide(new BigDecimal(2)), AngleUnity.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeCot(new BigDecimal(90), AngleUnity.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
    }


    @Test
    @Ignore
    public void testTrigonometric(){
        String[] vector1 = new String[] {"-23525642.432", "655427.342", "1834297943.344", "-1.34", "1"};
        for(String i : vector1){
            System.out.format("%s:\n sin: %s\n cos: %s\n tan: %s\n", i, calc.computeSin(new BigDecimal(i), AngleUnity.RADIANS), calc.computeCos(new BigDecimal(i), AngleUnity.RADIANS), calc.computeTan(new BigDecimal(i), AngleUnity.RADIANS));
            //System.out.format("%s:\n %s\n %s\n %s\n", i, calc.computeMultiplication(calc.computeSin(new BigDecimal(i), AngleUnity.RADIANS), calc.computeCsc(new BigDecimal(i), AngleUnity.RADIANS)).subtract(BigDecimal.ONE), calc.computeMultiplication(calc.computeCos(new BigDecimal(i), AngleUnity.RADIANS), calc.computeSec(new BigDecimal(i), AngleUnity.RADIANS)).subtract(BigDecimal.ONE), calc.computeMultiplication(calc.computeTan(new BigDecimal(i), AngleUnity.RADIANS), calc.computeCot(new BigDecimal(i), AngleUnity.RADIANS)).subtract(BigDecimal.ONE));
        }

        String[] vector2 = new String[] {"-4", "-3.5", "-3", "-2.5", "-2", "-1.5", "-1", "-0.5", "0"};
        for(String i : vector2){
            System.out.format("%s:\n sin: %s\n cos: %s\n tan: %s\n", i, calc.computeSin(new BigDecimal(i), AngleUnity.RADIANS), calc.computeCos(new BigDecimal(i), AngleUnity.RADIANS), calc.computeTan(new BigDecimal(i), AngleUnity.RADIANS));
        }

    }


    @Test
    @Ignore
    public void testTrigonometricExtremes(){
        BigDecimal pi = calc.computePi();

        //System.out.format("%s", calc.computeTan(pi.divide(new BigDecimal(2)), AngleUnity.RADIANS));
        //System.out.format("%s", calc.computeSec(pi.divide(new BigDecimal(2)), AngleUnity.RADIANS));
        //System.out.format("%s", calc.computeCsc(pi, AngleUnity.RADIANS));
        System.out.format("%s", calc.computeCot(pi, AngleUnity.RADIANS));
    }


    @Test
    @Ignore
    public void testConstants(){
        System.out.format("%s\n%s\n%s", calc.computePi(), calc.computeE(), calc.computePhi());
    }

    @Test
    public void teste(){
        System.out.format("%s", calc.computeCot(calc.computePhi().add(new BigDecimal(3).multiply(calc.computeE()).multiply(calc.computeAsin(calc.computePower(new BigDecimal("3.34434"), calc.computePi().negate()), AngleUnity.RADIANS))).divide(calc.computeAsech(new BigDecimal("0.43575")), new MathContext(210, RoundingMode.HALF_EVEN)), AngleUnity.RADIANS));
    }

}
