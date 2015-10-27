package com.myapp.calculator;

/**
 * Android calculator app
 */
public class MyInt {
    private int value;
    public MyInt (int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public int increaseAndGet(){
        return ++value;
    }
    public int decreaseAndGet(){
        return --value;
    }
    public int getAndIncrease(){
        return value++;
    }
    public int getAndDecrease(){
        return value--;
    }
    public void reset() {value = 0;}
}