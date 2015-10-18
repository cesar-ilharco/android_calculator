package com.myapp.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/**
 * Android calculator app
 */

public class CalculatorActivity extends AppCompatActivity implements OnClickListener {

    private TextView expression;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        expression = (TextView) findViewById(R.id.textView1);

        // Set buttons, handle land-view only cases.
        findViewById(R.id.button0).setOnClickListener(this);

        if (findViewById(R.id.buttonSqrt) != null) {
            findViewById(R.id.buttonSqrt).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        String buttonPressed = ((Button) view).getText().toString();
        if (buttonPressed.equals('=')){
            result = Display.updateResultDisplay(result, expression.toString());
        } else {
            expression = Display.updateExpressionDisplay(expression, buttonPressed);
        }
    }

    // TODO: Backup data when the view changes.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // TODO: Recover data when the view changes.
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
