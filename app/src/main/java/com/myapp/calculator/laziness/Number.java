package com.myapp.calculator.laziness;

import java.math.BigDecimal;

/**
 *  Android Calculator App
 */

// BigDecimal wrapper that uses Thunks to cache computed results.
public class Number {
    Thunk<BigDecimal> thunk;

    public static Number ZERO = Number.create(BigDecimal.ZERO);
    public static Number ONE = Number.create(BigDecimal.ONE);
    public static Number TEN = Number.create(BigDecimal.TEN);

    private Number() { }

    public static Number create(BigDecimal decimal) {
        Number num = new Number();
        num.thunk = new ConstThunk<>(decimal);
        return num;
    }


    public static Number create(long l) {
        return create(new BigDecimal(l));
    }

    public static Number create(double d) {
        return create(new BigDecimal(d));
    }

    private static Number createWithThunk(Thunk<BigDecimal> thunk) {
        Number number = new Number();
        number.thunk = thunk;
        return number;
    }

    public static Number add (final Number a, final Number b) {
        return createWithThunk(new Thunk<BigDecimal>() {
            @Override
            protected BigDecimal compute() {
                return a.compute().add(b.compute());
            }
        });
    }

    public Number add(Number n) {
        return add(this, n);
    }

    public static Number subtract (final Number a, final Number b) {
        return createWithThunk(new Thunk<BigDecimal>() {
            @Override
            protected BigDecimal compute() {
                return a.compute().subtract(b.compute());
            }
        });
    }

    public Number subtract(Number n) {
        return subtract(this, n);
    }
    public static Number multiply (final Number a, final Number b) {
        return createWithThunk(new Thunk<BigDecimal>() {
            @Override
            protected BigDecimal compute() {
                return a.compute().multiply(b.compute());
            }
        });
    }

    public Number multiply(Number n) {
        return multiply(this, n);
    }

    public static Number divide (final Number a, final Number b) {
        return createWithThunk(new Thunk<BigDecimal>() {
            @Override
            protected BigDecimal compute() {
                return a.compute().divide(b.compute());
            }
        });
    }

    public Number divide(Number n) {
        return divide(this, n);
    }

    public BigDecimal compute() {
        return thunk.get();
    }

    public Thunk<BigDecimal> getThunk() {
        return thunk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Number number = (Number) o;

        return thunk.equals(number.thunk);

    }

    @Override
    public int hashCode() {
        return thunk.hashCode();
    }
}
