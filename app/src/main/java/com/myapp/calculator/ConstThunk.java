package com.myapp.calculator;

/**
 *  Android Calculator App
 */
public class ConstThunk<T> extends Thunk<T> {

    private final T val;

    public ConstThunk(T val) {
        this.val = val;
    }
    @Override
    protected T compute() {
        return val;
    }
}
