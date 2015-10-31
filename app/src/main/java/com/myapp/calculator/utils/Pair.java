package com.myapp.calculator.utils;

/**
 * Android calculator app
 */
public class Pair<A,B> {

    private A first;
    private B second;

    private Pair () {}

    public static <A,B> Pair<A,B> create (A a, B b){
        Pair<A,B> pair = new Pair<>();
        pair.first = a;
        pair.second = b;
        return pair;
    }

    public B getSecond() {
        return second;
    }

    public A getFirst() {
        return first;
    }


}
