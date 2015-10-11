package com.myapp.calculator;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.InputMismatchException;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PerformanceTests {

    @Test
    public void factorial() throws Exception {

        Node<BigDecimal> list = null;
        for(int i=1; i<10000; i++) {
            list = new Node(Number.create(i).getThunk(), list);
        }

        Function2<BigDecimal,BigDecimal,BigDecimal> mult = new Function2<BigDecimal, BigDecimal, BigDecimal>() {
            @Override
            public BigDecimal apply(BigDecimal a, BigDecimal b) {
                return a.multiply(b);
            }
        };

        final AccumThunk<BigDecimal, BigDecimal> thunk = new AccumThunk<>(mult, Number.ONE.getThunk(), list);

        Continuable<BigDecimal> continuable = new Continuable<>(new Thunk<BigDecimal>() {
            @Override
            protected BigDecimal compute() {
                return thunk.get();
            }
        });

        int timeout = 10;
        while(!continuable.isComputed()) {
            System.out.printf("Trying for %d milliseconds\n", timeout);
            continuable.computeFor(timeout);
            timeout *= 2;
            if(!continuable.isComputed())
                System.out.println("Timed out");
        }

        System.out.println(continuable.get());

    }

}