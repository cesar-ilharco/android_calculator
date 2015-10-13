package com.myapp.calculator;

/**
 * Android Calculator App
 */

// Function f : A x B -> C
public interface Function2<A, B, C> {
    C apply(A a, B b);
}
