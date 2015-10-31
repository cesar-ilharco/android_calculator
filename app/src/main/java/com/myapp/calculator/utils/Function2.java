package com.myapp.calculator.utils;

/**
 * Android Calculator App
 */

// Function f : A x B -> C
public interface Function2<A, B, C> {
    C apply(A a, B b);
}
