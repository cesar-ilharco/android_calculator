package com.myapp.calculator;

import com.myapp.calculator.laziness.Number;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class NumberTests {

    @Test
    public void basicArithmetic() throws Exception {
        Random random = new Random(4903230525L);

        for(int i=0; i<10; i++) {
            com.myapp.calculator.laziness.Number a = nextNumber(random);
            Number b = nextNumber(random);
            Number c = nextNumber(random);

            assertTrue(additionIdentities(a));
            assertTrue(additionCommutes(a, b));
            assertTrue(additionAssociates(a, b, c));

            assertTrue(subtractionIdentities(a));

            assertTrue(multiplicationIdentities(a));
            assertTrue(multiplicationZeroIsZero(a));
            assertTrue(multiplicationCommutes(a,b));
            assertTrue(multiplicationAssociates(a,b,c));

            assertTrue(divisionIdentities(a));
        }
    }

    private static Number nextNumber(Random random) {
        return Number.create(random.nextLong() + random.nextDouble());
    }

    private static boolean additionCommutes(Number a, Number b) {
        return a.add(b).equals(b.add(a));
    }

    private static boolean additionAssociates(Number a, Number b, Number c) {
        return a.add(b.add(c)).equals(a.add(b).add(c));
    }

    private static boolean additionIdentities(Number a) {
        return a.add(Number.ZERO).equals(a) && Number.ZERO.add(a).equals(a);
    }

    private static boolean subtractionIdentities(Number a) {
        return a.subtract(Number.ZERO).equals(a) && a.subtract(a).equals(Number.ZERO);
    }
    private static boolean multiplicationCommutes(Number a, Number b) {
        return a.multiply(b).equals(b.multiply(a));
    }

    private static boolean multiplicationAssociates(Number a, Number b, Number c) {
        return a.multiply(b.multiply(c)).equals(a.multiply(b).multiply(c));
    }

    private static boolean multiplicationIdentities(Number a) {
        return a.multiply(Number.ONE).equals(a) && Number.ONE.multiply(a).equals(a);
    }

    private static boolean multiplicationZeroIsZero(Number a) {
        return a.multiply(Number.ZERO).equals(Number.ZERO) && Number.ZERO.multiply(a).equals(Number.ZERO);
    }

    private static boolean divisionIdentities(Number a) {
        return a.divide(Number.ONE).equals(a) && a.divide(a).equals(Number.ONE);
    }
}