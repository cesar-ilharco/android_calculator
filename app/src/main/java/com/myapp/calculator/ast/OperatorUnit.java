package com.myapp.calculator.ast;

/**
 * Android calculator app
 */
public class OperatorUnit implements ExpressionUnit {

    private final String operator;

    public OperatorUnit(String operator){
        this.operator = operator;
    }

    @Override
    public String getText() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperatorUnit)) return false;

        OperatorUnit that = (OperatorUnit) o;

        return !(operator != null ? !operator.equals(that.operator) : that.operator != null);

    }

    @Override
    public int hashCode() {
        return operator != null ? operator.hashCode() : 0;
    }
}
