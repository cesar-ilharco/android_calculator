package com.myapp.calculator;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.graphics.Typeface;
import android.widget.TextView;

import com.myapp.calculator.ast.ExpressionUnit;

import java.util.Iterator;


/**
 * Android calculator App
 */

public class CalculatorActivity extends AppCompatActivity{

    private TextView resultView;
    private TextView expressionView;
    ScrollView expressionScroller;
    private CalculatorState state;
    static private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        expressionView = (TextView) findViewById(R.id.expressionView);
        resultView = (TextView) findViewById(R.id.resultView);

        expressionScroller = (ScrollView) findViewById(R.id.expressionScroller);
        expressionView.setOnTouchListener(onTouchUpdateCursor());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializeScalePicker();
            expressionView.addTextChangedListener(textAutoResizeWatcher(expressionView, 25, 55));
            resultView.addTextChangedListener(textAutoResizeWatcher(resultView, 25, 70));
        } else {
            expressionView.addTextChangedListener(textAutoResizeWatcher(expressionView, 25, 70));
            resultView.addTextChangedListener(textAutoResizeWatcher(resultView, 25, 120));
        }

        if (savedInstanceState != null){
            super.onRestoreInstanceState(savedInstanceState);
            state = (CalculatorState) savedInstanceState.getSerializable("state");
            updateExpressionViewVisibleCursor();
            resultView.setText(savedInstanceState.getString("resultView", ""));
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                updateButtons();
            }
        } else {
            state = new CalculatorState();
        }

        blinkCursor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    // TODO: Serialize and backup expressionUnits.
    @Override // Backup data before changing view.
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state", state);
        outState.putString("resultView", resultView.getText().toString());
    }

    public void handleOnClick(View view){
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(25);
        view.startAnimation(buttonClick);

        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.buttonEquals:
                resultView.setText(DisplayHelper.getResultDisplay(state.getExpression().getUnits()));
                vibrator.vibrate(25);
                break;
            case R.id.buttonCopy:
                copyResult();
                break;
            case R.id.buttonUndo:
                // TODO: Implement button undo.
                break;
            case R.id.buttonBackward:
                if (state.getCursorPosition().getValue() > 0){
                    state.getCursorPosition().decreaseAndGet();
                    updateExpressionViewVisibleCursor();
                }
                break;
            case R.id.buttonForward:
                if (state.getCursorPosition().getValue() < state.getExpression().getUnits().size()){
                    state.getCursorPosition().increaseAndGet();
                    updateExpressionViewVisibleCursor();
                }
                break;
            case R.id.buttonInv:
                switchInv();
                break;
            case R.id.buttonHyp:
                switchHyp();
                break;
            default:
                expressionView.setText(DisplayHelper.getExpressionDisplay(
                        state.getExpression().getUnits(), state.getCursorPosition(), ((Button) view).getText().toString()));
                break;
        }
    }


    private void updateExpressionViewVisibleCursor() {
        expressionView.setText(DisplayHelper.toString(state.getExpression().getUnits(), state.getCursorPosition(), true));
    }

    private View.OnTouchListener onTouchUpdateCursor() {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Layout layout = ((TextView) v).getLayout();
                int x = (int)event.getX();
                int y = (int)event.getY();
                if (layout!=null){
                    int line = layout.getLineForVertical(y);
                    int offset = layout.getOffsetForHorizontal(line, x);
                    state.getCursorPosition().setValue(findBlockPosition(offset));
                    updateExpressionViewVisibleCursor();
                }
                return true;
            }

            private int findBlockPosition(int offset) {
                int blockPosition = 0;
                int accumulatedLength = 0;
                Iterator<ExpressionUnit> iterator = state.getExpression().getUnits().iterator();
                String textBlock = iterator.next().getText();
                while (accumulatedLength + textBlock.length() <= offset){
                    accumulatedLength += textBlock.length();
                    ++blockPosition;
                    if (!iterator.hasNext()){
                        break;
                    }
                    textBlock = iterator.next().getText();
                }
                return blockPosition;
            }
        };
    }


    private void blinkCursor(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlinkMs = 500;
                try{Thread.sleep(timeToBlinkMs);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (state.isCursorVisible()) {
                            state.setCursorVisible(false);
                            updateExpressionViewInvisibleCursor();
                        } else {
                            state.setCursorVisible(true);
                            updateExpressionViewVisibleCursor();
                        }
                        if (state.isRefreshResultView()) {
                            state.setRefreshResultView(false);
                            refreshResultView();
                        }
                        blinkCursor();
                    }
                });
            }

            private void refreshResultView() {
                resultView.setText(resultView.getText().toString());
            }

            private void updateExpressionViewInvisibleCursor() {
                expressionView.setText(DisplayHelper.toString(state.getExpression().getUnits(), state.getCursorPosition(), false));
            }
        }).start();
    }

    private void initializeScalePicker(){

        NumberPicker scalePicker = (NumberPicker) findViewById(R.id.scalePicker);
        scalePicker.setMinValue(0);
        scalePicker.setMaxValue(1000);
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

    private TextWatcher textAutoResizeWatcher(final TextView view, final int MIN_SP, final int MAX_SP) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adjustTextSize(view, MIN_SP, MAX_SP);
            }
        };
    }

    private void adjustTextSize(TextView view, int MIN_SP, int MAX_SP){
        final int widthLimitPixels = view.getWidth() - view.getPaddingRight() - view.getPaddingLeft();
        Paint paint = new Paint();
        float fontSizeSP = pixelsToSp(view.getTextSize());
        paint.setTextSize(spToPixels(fontSizeSP));

        String viewText = view.getText().toString();

        float widthPixels = paint.measureText(viewText);

        // Increase font size if necessary.
        if (widthPixels < widthLimitPixels){
            while (widthPixels < widthLimitPixels && fontSizeSP <= MAX_SP){
                ++fontSizeSP;
                paint.setTextSize(spToPixels(fontSizeSP));
                widthPixels = paint.measureText(viewText);
            }
            --fontSizeSP;
        }
        // Decrease font size if necessary.
        else {
            while (widthPixels > widthLimitPixels || fontSizeSP > MAX_SP) {
                if (fontSizeSP < MIN_SP) {
                    fontSizeSP = MIN_SP;
                    break;
                }
                --fontSizeSP;
                paint.setTextSize(spToPixels(fontSizeSP));
                widthPixels = paint.measureText(viewText);
            }
        }

        view.setTextSize(fontSizeSP);
    }


    private void copyResult() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("resultView", resultView.getText());
        clipboard.setPrimaryClip(clip);
    }

    private void switchInv() {
        state.setInv(!state.isInv());
        updateButtons();
    }

    private void switchHyp() {
        state.setHyp(!state.isHyp());
        updateButtons();
    }

    private void updateButtons() {

        String prefix = state.isInv() ? "arc" : "";
        String suffix = state.isHyp() ? "h"   : "";
        ((Button) findViewById(R.id.buttonSin)).setText(prefix + "sin" + suffix);
        ((Button) findViewById(R.id.buttonCos)).setText(prefix + "cos" + suffix);
        ((Button) findViewById(R.id.buttonTan)).setText(prefix + "tan" + suffix);

        int trigonometricColor = state.isHyp() || state.isInv() ? Color.BLACK : Color.WHITE;
        int logExpColor =  state.isInv() ? Color.BLACK : Color.WHITE;

        if (state.isInv()){
            ((Button) findViewById(R.id.buttonInv)).setTextColor(Color.BLACK);
            ((Button) findViewById(R.id.buttonInv)).setTypeface(null, Typeface.BOLD);
            ((Button) findViewById(R.id.buttonLn)).setText("e^x");
            ((Button) findViewById(R.id.buttonLog)).setText("10^x");
            ((Button) findViewById(R.id.buttonSqrt)).setText("x²");

        } else {
            ((Button) findViewById(R.id.buttonInv)).setTextColor(Color.WHITE);
            ((Button) findViewById(R.id.buttonInv)).setTypeface(null, Typeface.NORMAL);
            ((Button) findViewById(R.id.buttonLn)).setText("ln");
            ((Button) findViewById(R.id.buttonLog)).setText("log");
            ((Button) findViewById(R.id.buttonSqrt)).setText("√");
        }

        if (state.isHyp()){
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

    private float pixelsToSp(float px) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    private float spToPixels(float sp) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

}
