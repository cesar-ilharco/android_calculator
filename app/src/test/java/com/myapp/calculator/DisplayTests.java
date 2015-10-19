package com.myapp.calculator;

import junit.framework.Assert;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * Android calculator app
 */
public class DisplayTests {

    @Test
    public void testDisplayInputDecimalPoint(){

        if (Display.expressionDisplayHelper) {

            // True if decimal point insertion is possible, false otherwise.
            Map<String, Boolean> expressions = new HashMap<String, Boolean>() {{
                put("", true);
                put("1", true);
                put("1234567890", true);
                put("log(28", true);
                put("1+2", true);
                put("10.", false);
                put("3.1415926535", false);
                put(".1", false);
                put(".", false);
                put("1*", true);
                put(".5/", true);
                put("1.23+", true);
                put("1.23+.", false);
                put("1.23+.6", false);
                put("1.23+4.57", false);
            }};

            for (Map.Entry<String, Boolean> entry : expressions.entrySet()) {
                String append = entry.getValue() ? "." : "";
                Assert.assertEquals(entry.getKey() + append, Display.getExpressionDisplay(entry.getKey(), "."));
            }
        }
    }

    @Test // "+", "-", "*", "/".
    public void testDisplayInputBasicOperator(){

        if (Display.expressionDisplayHelper) {

            // Maps Pair<Expression, ButtonOperator> to the expected resulting Expression.
            Map<Pair<String, String>, String> tests = new HashMap<Pair<String, String>, String>() {{
                put(Pair.create("1", "+"), "1+");
                put(Pair.create("", "-"), "-");
                put(Pair.create("", "/"), "");
                put(Pair.create("100", "/"), "100/");
                put(Pair.create("1+2*3-4", "*"), "1+2*3-4*");
                put(Pair.create("1+", "+"), "1+");
                put(Pair.create("1+2+", "-"), "1+2-");
                put(Pair.create("1+2+", "-"), "1+2-");
                put(Pair.create("1+2+3+4+5+6+7+8+9-", "-"), "1+2+3+4+5+6+7+8+9+");
                put(Pair.create("-", "-"), "");
                put(Pair.create("1-", "-"), "1+");
                put(Pair.create("2+", "*"), "2*");
                put(Pair.create("72/", "*"), "72*");
                put(Pair.create("72/", "*"), "72*");
                put(Pair.create("1/", "/"), "1/");
                put(Pair.create("2/-", "/"), "2/");
                put(Pair.create("3/-", "+"), "3+");
                put(Pair.create("4/-", "/"), "4/");
                put(Pair.create("0+", "/"), "0/");
                put(Pair.create("1*-", "-"), "1*");
            }};

            for (Map.Entry<Pair<String, String>, String> entry : tests.entrySet()) {
                String result = Display.getExpressionDisplay(entry.getKey().getFirst(), entry.getKey().getSecond());
                Assert.assertEquals(entry.getValue(), result);
            }
        }
    }
}
