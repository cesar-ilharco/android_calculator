package com.myapp.calculator.miscellanea;


import com.myapp.calculator.Function1;
import com.myapp.calculator.Pair;

import java.math.BigDecimal;

/**
 * Android Calculator App
 */

// F[0] := 1, F[1] := 1, F[n] := F[n-1] + F[n-2] for n >= 2.
public class Fibonacci implements Function1<Integer, BigDecimal> {

    @Override
    public BigDecimal apply(Integer n) {
        return fibonacciEfficientIteration(n);
    }

    public static BigDecimal fibonacciDynamicProgramming (int n){
        BigDecimal previous = BigDecimal.ZERO;
        BigDecimal current = BigDecimal.ONE;
        while (n --> 0){
            BigDecimal next = current.add(previous);
            previous = current;
            current = next;
        }
        return n>0 ? current : previous;
    }

//  F[0] := 0, F[1] := 1
//  F[2n] = F[n]*(2*F[n+1] - F[n])
//  F[2n+1] = F[n+1]^2 + F[n]^2
    public static BigDecimal fibonacciEfficientRecursion(int n) {
        if (n == 0 || n == 1) {
            return new BigDecimal(n);
        }
        // Even number
        if ((n&1) == 0){
            BigDecimal n1 = fibonacciDynamicProgramming(n/2);
            BigDecimal n2 = fibonacciDynamicProgramming(n/2 + 1);
            return n1.multiply(n2.multiply(new BigDecimal(2)).subtract(n1));
        } else {
            BigDecimal n1 = fibonacciDynamicProgramming(n/2);
            BigDecimal n2 = fibonacciDynamicProgramming(n/2 + 1);
            return n1.multiply(n1).add(n2.multiply(n2));
        }
    }

//  F[0] := 0, F[1] := 1
//  F[2n] = F[n]*(2*F[n+1] - F[n])
//  F[2n+1] = F[n+1]^2 + F[n]^2
//  Look at the bits of n, from left to right
//  If the bit is 1, add then multiply. If it's zero, just multiply.
    public static BigDecimal fibonacciEfficientIteration(int n) {
        if (n == 0){
            return BigDecimal.ZERO;
        }
        if (n == 1 || n == 2){
            return BigDecimal.ONE;
        }
        BigDecimal n1 = BigDecimal.ONE;     // F[2]
        BigDecimal n2 = new BigDecimal(2);  // F[3]
        int bit = Integer.highestOneBit(n) >> 1; // Bit after the first set bit.
        while (bit > 1) {
            if ((n & bit) != 0) {
                BigDecimal sum = n1.add(n2);
                n1 = n2;
                n2 = sum;
            }
            Pair<BigDecimal, BigDecimal> pair = doubleUpdate(n1, n2);
            n1 = pair.getFirst();
            n2 = pair.getSecond();
            bit >>= 1;
        }
        return (n&bit) == 0 ? n1 : n2;
    }

    public static Pair<BigDecimal, BigDecimal> doubleUpdate (BigDecimal n1, BigDecimal n2) {
        return Pair.create(n1.multiply(n2.multiply(new BigDecimal(2)).subtract(n1)),
                n1.multiply(n1).add(n2.multiply(n2)));
    }

}
