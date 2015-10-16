package com.myapp.calculator;

import junit.framework.Assert;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class FactorialTests {

    @Test
    public void testFactorialCorrectness() throws Exception{

        List<Integer> numbers = new ArrayList<>(Arrays.asList(0, 1, 10));
        for (Integer n : numbers){
            Assert.assertTrue(factorialAreEqual(n));
        }

        Random random = new Random();
        for(int i=0; i<10; i++) {
            int n = random.nextInt(1000);
            Assert.assertTrue(factorialAreEqual(n));
        }
    }

    @Test
    public void testFactorialPerformance(){
        final int n = 100000;

        long startNs = System.nanoTime();
        BigDecimal resultRaw = Factorial.rawFactorial(n);
        System.out.println("Raw:      Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        startNs = System.nanoTime();
        BigDecimal resultThunk = Factorial.thunkFactorial(n);
        System.out.println("Thunk:    Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        startNs = System.nanoTime();
        BigDecimal resultParallel = Factorial.parallelFactorial(n);
        System.out.println("Parallel: Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        Assert.assertEquals(resultRaw, resultThunk);
        Assert.assertEquals(resultRaw, resultParallel);
    }

    @Test
    public void testPartition(){
        Assert.assertEquals(Factorial.getPartition(40, 4), Arrays.asList(0, 10, 20, 30, 40));
        Assert.assertEquals(Factorial.getPartition(4, 8), Arrays.asList(0, 1, 2, 3, 4));
    }

    private boolean factorialAreEqual (int n){
        BigDecimal f1 = Factorial.rawFactorial(n);
        BigDecimal f2 = Factorial.rawFactorial(n);
        BigDecimal f3 = Factorial.rawFactorial(n);
        return f1.equals(f2) && f1.equals(f3);
    }

}