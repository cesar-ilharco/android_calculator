package com.myapp.calculator;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.Assert.*;
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

        assertTrue(calc.computeSin(BigDecimal.ZERO, AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeSin(BigDecimal.ZERO, AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeSin(PI.divide(new BigDecimal(2)), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSin(new BigDecimal(90), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSin(PI, AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeSin(new BigDecimal(180), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);

        assertTrue(calc.computeCos(BigDecimal.ZERO, AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCos(BigDecimal.ZERO, AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCos(PI.divide(new BigDecimal(2)), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeCos(new BigDecimal(90), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeCos(PI, AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeCos(new BigDecimal(180), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeTan(BigDecimal.ZERO, AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeTan(BigDecimal.ZERO, AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeTan(PI.divide(new BigDecimal(4)), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeTan(new BigDecimal(45), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeTan(PI.multiply(new BigDecimal("0.75")), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeTan(new BigDecimal(135), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeCsc(PI.divide(new BigDecimal(2)), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCsc(new BigDecimal(90), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCsc(PI.multiply(new BigDecimal(1.5)), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeCsc(new BigDecimal(270), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeSec(BigDecimal.ZERO, AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSec(BigDecimal.ZERO, AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeSec(PI, AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);
        assertTrue(calc.computeSec(new BigDecimal(180), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE.negate()) == 0);

        assertTrue(calc.computeCot(PI.divide(new BigDecimal(4)), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCot(new BigDecimal(45), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ONE) == 0);
        assertTrue(calc.computeCot(PI.divide(new BigDecimal(2)), AngleUnit.RADIANS).round(mc).compareTo(BigDecimal.ZERO) == 0);
        assertTrue(calc.computeCot(new BigDecimal(90), AngleUnit.DEGREE).round(mc).compareTo(BigDecimal.ZERO) == 0);
    }


    @Test
    @Ignore
    public void testTrigonometric(){
        String[] vector1 = new String[] {"-23525642.432", "655427.342", "1834297943.344", "-1.34", "1"};
        for(String i : vector1){
            System.out.format("%s:\n sin: %s\n cos: %s\n tan: %s\n", i, calc.computeSin(new BigDecimal(i), AngleUnit.RADIANS), calc.computeCos(new BigDecimal(i), AngleUnit.RADIANS), calc.computeTan(new BigDecimal(i), AngleUnit.RADIANS));
            //System.out.format("%s:\n %s\n %s\n %s\n", i, calc.computeMultiplication(calc.computeSin(new BigDecimal(i), AngleUnit.RADIANS), calc.computeCsc(new BigDecimal(i), AngleUnit.RADIANS)).subtract(BigDecimal.ONE), calc.computeMultiplication(calc.computeCos(new BigDecimal(i), AngleUnit.RADIANS), calc.computeSec(new BigDecimal(i), AngleUnit.RADIANS)).subtract(BigDecimal.ONE), calc.computeMultiplication(calc.computeTan(new BigDecimal(i), AngleUnit.RADIANS), calc.computeCot(new BigDecimal(i), AngleUnit.RADIANS)).subtract(BigDecimal.ONE));
        }

        String[] vector2 = new String[] {"-4", "-3.5", "-3", "-2.5", "-2", "-1.5", "-1", "-0.5", "0"};
        for(String i : vector2){
            System.out.format("%s:\n sin: %s\n cos: %s\n tan: %s\n", i, calc.computeSin(new BigDecimal(i), AngleUnit.RADIANS), calc.computeCos(new BigDecimal(i), AngleUnit.RADIANS), calc.computeTan(new BigDecimal(i), AngleUnit.RADIANS));
        }

    }


    @Test
    @Ignore
    public void testTrigonometricExtremes(){
        BigDecimal pi = calc.computePi();

        //System.out.format("%s", calc.computeTan(pi.divide(new BigDecimal(2)), AngleUnit.RADIANS));
        //System.out.format("%s", calc.computeSec(pi.divide(new BigDecimal(2)), AngleUnit.RADIANS));
        //System.out.format("%s", calc.computeCsc(pi, AngleUnit.RADIANS));
        System.out.format("%s", calc.computeCot(pi, AngleUnit.RADIANS));
    }


    @Test
    @Ignore
    public void testConstants(){
        System.out.format("%s\n%s\n%s", calc.computePi(), calc.computeE(), calc.computePhi());
    }

    @Test

	public void generalTest() throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader("Test.txt"));
		String line = br.readLine();
		MathContext mc1 = new MathContext(200, RoundingMode.HALF_EVEN);
		Calculation calc1 = new Calculation(mc1);
		assertEquals(line, calc1.computeCot(calc1.computePhi().add(new BigDecimal(3).multiply(calc1.computeE()).multiply(calc1.computeAsin(calc1.computePower(new BigDecimal("3.34434"), calc1.computePi().negate()).divide(calc1.computeAsech(new BigDecimal("0.43575")), new MathContext(210, RoundingMode.HALF_EVEN)), AngleUnit.RADIANS))), AngleUnit.RADIANS).round(mc1).toString());
		
		line = br.readLine();
		MathContext mc2 = new MathContext(1010, RoundingMode.HALF_EVEN);
		Calculation calc2 = new Calculation(mc2);
		assertEquals(line, calc2.computeSin(calc2.computeAtan(calc2.computeExponential(new BigDecimal("2.54683")).divide(calc2.computeLn(new BigDecimal("4.677234")), new MathContext(1010, RoundingMode.HALF_EVEN)), AngleUnit.RADIANS), AngleUnit.RADIANS).round(new MathContext(1000, RoundingMode.HALF_EVEN)).toString());
		
		line = br.readLine();
		MathContext mc3 = new MathContext(10, RoundingMode.HALF_EVEN);
		Calculation calc3 = new Calculation(mc3);
		assertEquals(line, calc3.computeCos(calc3.computeLn(calc3.computeAcsch(new BigDecimal("9.34945"))), AngleUnit.RADIANS).round(mc3).toString());
		
		line = br.readLine();
		MathContext mc4 = new MathContext(20, RoundingMode.HALF_EVEN);
		Calculation calc4 = new Calculation(mc4);
		assertEquals(line, calc4.computeLog10(calc4.computePower(new BigDecimal("4.657"), calc4.computeTan(calc4.computeE(), AngleUnit.RADIANS))).round(mc4).toString());
		
		line = br.readLine();
		MathContext mc5 = new MathContext(50, RoundingMode.HALF_EVEN);
		Calculation calc5 = new Calculation(mc5);
		assertEquals(line, calc5.computeAcos(calc5.computeAsinh(new BigDecimal("-0.4578")).add(calc5.computeAcosh(new BigDecimal("1.9438"))), AngleUnit.RADIANS).round(mc5).toString());
		
		line = br.readLine();
		MathContext mc6 = new MathContext(500, RoundingMode.HALF_EVEN);
		Calculation calc6 = new Calculation(mc6);
		assertEquals(line, calc6.computeTanh(calc6.computeSech(calc6.computeSqrt(calc6.computeAcsc(new BigDecimal("457565869.569"), AngleUnit.RADIANS)))).round(mc6).toString());
		
		line = br.readLine();
		MathContext mc7 = new MathContext(100, RoundingMode.HALF_EVEN);
		Calculation calc7 = new Calculation(mc7);
		assertEquals(line, calc7.computeCsc(calc7.computeSec(calc7.computeAcot(new BigDecimal("-34975.24"), AngleUnit.RADIANS), AngleUnit.RADIANS), AngleUnit.RADIANS).round(mc7).toString());
		
		line = br.readLine();
		MathContext mc8 = new MathContext(1000, RoundingMode.HALF_EVEN);
		Calculation calc8 = new Calculation(mc8);
		assertEquals(line, calc8.computeCoth(calc8.computeAtanh(calc8.computeLn(calc8.computeAcoth(new BigDecimal("2.5685"))))).round(mc8).toString());
		
		line = br.readLine();
		MathContext mc9 = new MathContext(2000, RoundingMode.HALF_EVEN);
		Calculation calc9 = new Calculation(mc9);
		assertEquals(line, calc9.computeSinh(calc9.computeAsec(calc9.computeCosh(new BigDecimal("2.24358")), AngleUnit.RADIANS)).round(mc9).toString());
		
		line = br.readLine();
		MathContext mc10 = new MathContext(3000, RoundingMode.HALF_EVEN);
		Calculation calc10 = new Calculation(mc10);
		assertEquals(line, calc10.computeCsch(calc10.computeSec(calc10.computeAsin(calc10.computeLog10(new BigDecimal("8.3497")), AngleUnit.RADIANS), AngleUnit.RADIANS)).round(mc10).toString());
		br.close();
	}

}
