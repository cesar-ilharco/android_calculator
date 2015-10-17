package com.myapp.calculator;

import android.support.annotation.VisibleForTesting;

import java.math.BigDecimal;

/**
 * Android Calculator App
 */

// F[0] := 1, F[1] := 1, F[n] := F[n-1] + F[n-2] for n >= 2.
public class Fibonacci implements Function1<Integer, BigDecimal>{

    @Override
    public BigDecimal apply(Integer n) {
        return fibonacciEfficientIteration(n);
    }

    @VisibleForTesting
    static BigDecimal fibonacciDynamicProgramming (int n){
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
    @VisibleForTesting
    static BigDecimal fibonacciEfficientRecursion(int n) {
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
    // TODO: Implement recursion iteratively.
    @VisibleForTesting
    static BigDecimal fibonacciEfficientIteration(int n) {
        return BigDecimal.ZERO;
    }

}
