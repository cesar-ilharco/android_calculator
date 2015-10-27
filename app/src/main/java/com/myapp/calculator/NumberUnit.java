package com.myapp.calculator;

/**
 * Android calculator app
 */
public class NumberUnit implements ExpressionUnit {

    final String number;

    public NumberUnit(String number) {
        this.number = number;
    }

    @Override
    public String getText() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumberUnit)) return false;

        NumberUnit that = (NumberUnit) o;

        return !(number != null ? !number.equals(that.number) : that.number != null);

    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }
}