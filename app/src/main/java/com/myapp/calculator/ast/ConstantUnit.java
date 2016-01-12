package com.myapp.calculator.ast;

/**
 * Android calculator app
 */

public class ConstantUnit implements ExpressionUnit {

    private final String constant;

    public ConstantUnit(String constant){
        this.constant = constant;
    }

    @Override
    public String getText() {
        return constant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstantUnit)) return false;

        ConstantUnit that = (ConstantUnit) o;

        return !(constant != null ? !constant.equals(that.constant) : that.constant != null);

    }

    @Override
    public int hashCode() {
        return constant != null ? constant.hashCode() : 0;
    }
}
