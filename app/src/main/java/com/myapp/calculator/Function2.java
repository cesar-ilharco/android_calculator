package com.myapp.calculator;

/**
 * Android Calculator App
 */

public interface Function2<A, B, C> {
    C apply(A a, B b);
}
