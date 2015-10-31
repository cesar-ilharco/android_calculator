package com.myapp.calculator;

import com.myapp.calculator.miscellanea.Fibonacci;
import com.myapp.calculator.utils.Pair;

import junit.framework.Assert;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Android calculator app
 */
public class FibonacciTests {

    @Test
    public void testFibonacciCorrectness(){
        List<Integer> numbers = new ArrayList<>(Arrays.asList(0, 1, 2));
        for (Integer n : numbers){
            Assert.assertTrue(fibonacciAreEqual(n));
        }

        Random random = new Random();
        for(int i=0; i<10; i++) {
            int n = random.nextInt(1000);
            Assert.assertTrue(fibonacciAreEqual(n));
        }
    }

    @Test
    public void testFibonacciPerformance(){
        final int n = 1000000;

        long startNs = System.nanoTime();
        BigDecimal resultDP = Fibonacci.fibonacciDynamicProgramming(n);
        System.out.println("Dynamic Programming: Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        startNs = System.nanoTime();
        BigDecimal resultRec = Fibonacci.fibonacciEfficientRecursion(n);
        System.out.println("Efficient Recursion: Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        startNs = System.nanoTime();
        BigDecimal resultIt = Fibonacci.fibonacciEfficientIteration(n);
        System.out.println("Efficient Iteration: Time elapsed(ms) = " + (System.nanoTime() - startNs)/1000000L);

        Assert.assertEquals(resultDP, resultRec);
        Assert.assertEquals(resultDP, resultIt);
    }

    @Test
    public void testDoubleUpdate(){
        for (int i=0; i<=100; ++i){
            BigDecimal n1 = Fibonacci.fibonacciDynamicProgramming(i);
            BigDecimal n2 = Fibonacci.fibonacciDynamicProgramming(i+1);
            Pair<BigDecimal,BigDecimal> pair = Fibonacci.doubleUpdate(n1,n2);
            Assert.assertEquals(pair.getFirst(), Fibonacci.fibonacciDynamicProgramming(2*i));
            Assert.assertEquals(pair.getSecond(), Fibonacci.fibonacciDynamicProgramming(2*i+1));
        }
    }

    private static boolean fibonacciAreEqual(int n){
        BigDecimal n1 = Fibonacci.fibonacciDynamicProgramming(n);
        BigDecimal n2 = Fibonacci.fibonacciEfficientRecursion(n);
        BigDecimal n3 = Fibonacci.fibonacciEfficientIteration(n);
        return n1.equals(n2) && n1.equals(n3);
    }
}
