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
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Stack;


/**
 * Android calculator app
 */

public class CalculatorActivity extends AppCompatActivity implements OnClickListener {

    private Stack<ExpressionUnit> expressionUnits;
    private TextView expressionView;
    private TextView resultView;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        expressionUnits = new Stack<>();
        expressionView = (TextView) findViewById(R.id.expressionView);
        resultView = (TextView) findViewById(R.id.resultView);

        initializeButtons();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializeLandscapeButtons();
        }

        ScrollView expressionScroller = (ScrollView) findViewById(R.id.expressionScroller);
        expressionView.addTextChangedListener(scrollableWatcher(expressionScroller));

    }

    @Override
    public void onClick(View view) {

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.buttonEquals:
                resultView.setText(DisplayHelper.getResultDisplay(expressionUnits));
                vibrator.vibrate(40);
                break;
            case R.id.buttonCopy:
                copyResult();
                vibrator.vibrate(30);
                break;
            case R.id.buttonPaste:
                pasteExpression();
                vibrator.vibrate(30);
                break;
            default:
                expressionView.setText(DisplayHelper.getExpressionDisplay(expressionUnits, ((Button)view).getText().toString()));
                vibrator.vibrate(25);
                break;
        }
    }

    // TODO: Serialize and backup expressionUnits.
    @Override // Backup data before changing view.
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("expressionView", expressionView.getText().toString());
        outState.putString("resultView", resultView.getText().toString());
    }

    // TODO: Deserialize and restore expressionUnits.
    @Override // Recover data after changing view.
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        expressionView.setText(savedInstanceState.getString("expressionView", ""));
        resultView.setText(savedInstanceState.getString("resultView", ""));
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
    }

    private void initializeLandscapeButtons(){

        findViewById(R.id.button00).setOnClickListener(this);
        findViewById(R.id.buttonOpenP).setOnClickListener(this);
        findViewById(R.id.buttonCloseP).setOnClickListener(this);
        findViewById(R.id.buttonSin).setOnClickListener(this);
        findViewById(R.id.buttonCos).setOnClickListener(this);
        findViewById(R.id.buttonTan).setOnClickListener(this);
        findViewById(R.id.buttonLn).setOnClickListener(this);
        findViewById(R.id.buttonLog).setOnClickListener(this);
        findViewById(R.id.buttonSqrt).setOnClickListener(this);
        findViewById(R.id.buttonExp).setOnClickListener(this);
        findViewById(R.id.buttonMod).setOnClickListener(this);
        findViewById(R.id.buttonFact).setOnClickListener(this);
        findViewById(R.id.buttonFib).setOnClickListener(this);
        findViewById(R.id.buttonPi).setOnClickListener(this);
        findViewById(R.id.buttonPhi).setOnClickListener(this);
        findViewById(R.id.buttonE).setOnClickListener(this);
        findViewById(R.id.buttonIsPrime).setOnClickListener(this);
        findViewById(R.id.buttonInv).setOnClickListener(this);
        findViewById(R.id.buttonHyp).setOnClickListener(this);

        NumberPicker scalePicker = (NumberPicker) findViewById(R.id.scalePicker);
        scalePicker.setMinValue(0);
        scalePicker.setMaxValue(100);
        scalePicker.setValue(Kernel.getScale());
        scalePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                Kernel.setScale(newValue);
            }
        });
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
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        };
    }

    private void copyResult() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("resultView", resultView.getText());
        clipboard.setPrimaryClip(clip);
    }

    private void pasteExpression() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            expressionView.setText(expressionView.getText().toString() + clipboard.getPrimaryClip().getItemAt(0).getText());
        }
    }

}
