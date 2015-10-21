package com.myapp.calculator;

/**
 * Android calculator app
 */
public class OperatorUnit implements ExpressionUnit {

    static final OperatorUnit ADD = new OperatorUnit("+");

    private String operator;

    public OperatorUnit(String operator){
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperatorUnit)) return false;

        OperatorUnit that = (OperatorUnit) o;

        return operator.equals(that.operator);

    }

    @Override
    public int hashCode() {
        return operator.hashCode();
    }

    @Override
    public String getText() {
        return operator;
    }

    @Override
    public void del() {
        operator = "";
    }
}
