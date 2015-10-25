package com.myapp.calculator;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.graphics.Typeface;

import com.myapp.calculator.utils.AutoResizeTextView;

import java.util.Stack;


/**
 * Android calculator App
 */

public class CalculatorActivity extends AppCompatActivity implements OnClickListener {

    private Stack<ExpressionUnit> expressionUnits;
    private AutoResizeTextView expressionView;
    private AutoResizeTextView resultView;
    private boolean isHyp;
    private boolean isInv;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        expressionUnits = new Stack<>();
        expressionView = (AutoResizeTextView) findViewById(R.id.expressionView);
        resultView = (AutoResizeTextView) findViewById(R.id.resultView);
        isHyp = false;
        isInv = false;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializeScalePicker();
        }

        ScrollView expressionScroller = (ScrollView) findViewById(R.id.expressionScroller);
        expressionView.addTextChangedListener(scrollableWatcher(expressionScroller));


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
            case R.id.buttonUndo:
                // TODO: Implement button undo.
                vibrator.vibrate(30);
                break;
            case R.id.buttonInv:
                switchInv();
                vibrator.vibrate(30);
                break;
            case R.id.buttonHyp:
                switchHyp();
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

    private void initializeScalePicker(){

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

    private void switchInv() {
        isInv = ! isInv;
        updateButtons();
    }

    private void switchHyp() {
        isHyp = ! isHyp;
        updateButtons();
    }

    private void updateButtons() {

        String prefix = isInv ? "arc" : "";
        String suffix = isHyp ? "h"   : "";
        ((Button) findViewById(R.id.buttonSin)).setText(prefix + "sin" + suffix);
        ((Button) findViewById(R.id.buttonCos)).setText(prefix + "cos" + suffix);
        ((Button) findViewById(R.id.buttonTan)).setText(prefix + "tan" + suffix);

        int trigonometricColor = isHyp || isInv ? Color.BLACK : Color.WHITE;
        int logExpColor =  isInv ? Color.BLACK : Color.WHITE;

        if (isInv){
            ((Button) findViewById(R.id.buttonInv)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.buttonInv)).setTypeface(null, Typeface.BOLD);
            ((Button) findViewById(R.id.buttonLn)).setText("e^x");
            ((Button) findViewById(R.id.buttonLog)).setText("10^x");
            ((Button) findViewById(R.id.buttonSqrt)).setText("x^2");

        } else {
            ((Button) findViewById(R.id.buttonInv)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.buttonInv)).setTypeface(null, Typeface.NORMAL);
            ((Button) findViewById(R.id.buttonLn)).setText("ln");
            ((Button) findViewById(R.id.buttonLog)).setText("log");
            ((Button) findViewById(R.id.buttonSqrt)).setText("√");
        }

        if (isHyp){
            ((Button) findViewById(R.id.buttonHyp)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.buttonHyp)).setTypeface(null, Typeface.BOLD);
        } else {
            ((Button) findViewById(R.id.buttonHyp)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.buttonHyp)).setTypeface(null, Typeface.NORMAL);
        }

        ((Button) findViewById(R.id.buttonLn)).setTextColor(logExpColor);
        ((Button) findViewById(R.id.buttonLog)).setTextColor(logExpColor);
        ((Button) findViewById(R.id.buttonSqrt)).setTextColor(logExpColor);
        ((Button) findViewById(R.id.buttonSin)).setTextColor(trigonometricColor);
        ((Button) findViewById(R.id.buttonCos)).setTextColor(trigonometricColor);
        ((Button) findViewById(R.id.buttonTan)).setTextColor(trigonometricColor);
    }

}
