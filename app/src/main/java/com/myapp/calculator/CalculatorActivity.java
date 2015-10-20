package com.myapp.calculator;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * Android calculator app
 */

public class CalculatorActivity extends AppCompatActivity implements OnClickListener {

    private TextView expression;
    private TextView result;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        expression = (TextView) findViewById(R.id.expressionView);
        result = (TextView) findViewById(R.id.resultView);

        initializeButtons();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializeLandscapeButtons();
        }

        ScrollView expressionScroller = (ScrollView) findViewById(R.id.expressionScroller);
        expression.addTextChangedListener(scrollableWatcher(expressionScroller));

    }

    @Override
    public void onClick(View view) {
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        String buttonPressed = ((Button) view).getText().toString();
        switch (buttonPressed) {
            case "=":
                result.setText(Display.getResultDisplay(expression.getText().toString()));
                vibrator.vibrate(40);
                break;
            case "copy":
                copyResult();
                vibrator.vibrate(30);
                break;
            case "paste":
                pasteExpression();
                vibrator.vibrate(30);
                break;
            default:
                expression.setText(Display.getExpressionDisplay(expression.getText().toString(), buttonPressed));
                vibrator.vibrate(25);
                break;
        }
    }

    @Override // Backup data before changing view.
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("expression", expression.getText().toString());
        outState.putString("result", result.getText().toString());
    }

    @Override // Recover data after changing view.
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        expression.setText(savedInstanceState.getString("expression", ""));
        result.setText(savedInstanceState.getString("result", ""));
    }

    private void initializeButtons(){
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.buttonDot).setOnClickListener(this);
        findViewById(R.id.buttonEquals).setOnClickListener(this);
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSubtract).setOnClickListener(this);
        findViewById(R.id.buttonMultiply).setOnClickListener(this);
        findViewById(R.id.buttonDivide).setOnClickListener(this);
        findViewById(R.id.buttonBackspace).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        findViewById(R.id.buttonCopy).setOnClickListener(this);
        findViewById(R.id.buttonPaste).setOnClickListener(this);
    }

    private void initializeLandscapeButtons(){
        findViewById(R.id.button00).setOnClickListener(this);
        findViewById(R.id.buttonOpenP).setOnClickListener(this);
        findViewById(R.id.buttonCloseP).setOnClickListener(this);
        findViewById(R.id.buttonSqrt).setOnClickListener(this);
        findViewById(R.id.buttonSin).setOnClickListener(this);
        findViewById(R.id.buttonCos).setOnClickListener(this);
        findViewById(R.id.buttonTan).setOnClickListener(this);
        findViewById(R.id.buttonFact).setOnClickListener(this);
        findViewById(R.id.buttonFib).setOnClickListener(this);
        findViewById(R.id.buttonLog).setOnClickListener(this);
        findViewById(R.id.buttonExp).setOnClickListener(this);
    }

    // Makes scroll view automatically scroll to the bottom when text is added.
    private TextWatcher scrollableWatcher (final ScrollView scrollView){
        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
        };
    }

    private void copyResult() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("result", result.getText());
        clipboard.setPrimaryClip(clip);
    }

    private void pasteExpression() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            expression.setText(expression.getText().toString() + clipboard.getPrimaryClip().getItemAt(0).getText());
        }
    }

}
