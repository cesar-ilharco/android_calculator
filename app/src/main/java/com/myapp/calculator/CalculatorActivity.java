package com.myapp.calculator;

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
import android.widget.Toast;

import com.myapp.calculator.ast.ExpressionUnit;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Android calculator App
 */

public class CalculatorActivity extends AppCompatActivity{

    private TextView resultView;
    private TextView expressionView;
    ScrollView expressionScroller;

    private CalculatorState state;
    private boolean cursorVisible;
    private boolean refreshResultView;

    static private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);
    ScheduledExecutorService executorService;

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

        cursorVisible = true;
        refreshResultView = true;

        if (savedInstanceState != null){
            super.onRestoreInstanceState(savedInstanceState);
            state = (CalculatorState) savedInstanceState.getSerializable("state");
            updateExpressionView();
            resultView.setText(savedInstanceState.getString("resultView", ""));
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                updateButtons();
            }
        } else {
            state = new CalculatorState();
        }

        executorService = Executors.newScheduledThreadPool(1);
        blinkCursor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        executorService.shutdown();
    }

    // TODO: Override onPause, onResume to stop, restart executorService.

    @Override // Backup data before changing view.
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("state", state);
        outState.putString("resultView", resultView.getText().toString());
    }

    // TODO: split into different methods, remove switch case.
    public void handleOnClick(View view){
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(25);
        view.startAnimation(buttonClick);

        int buttonId = view.getId();
        switch (buttonId) {
            case R.id.buttonEquals:
                // TODO: Asynchronous thread.
                resultView.setText(DisplayHelper.getResultDisplay(state.getExpression().getUnits()));
                vibrator.vibrate(25);
                break;
            case R.id.buttonCopy:
                copyResult();
                displayCopyMessage();
                break;
            case R.id.buttonUndo:
                // TODO: Implement button undo.
                break;
            case R.id.buttonForward:
                state.moveCursorForwards();
                cursorVisible = true;
                updateExpressionView();
                break;
            case R.id.buttonBackward:
                state.moveCursorBackwards();
                cursorVisible = true;
                updateExpressionView();
                break;
            case R.id.buttonInv:
                switchInv();
                break;
            case R.id.buttonHyp:
                switchHyp();
                break;
            default:
                cursorVisible = true;
                DisplayHelper.updateExpression(state.getExpression().getUnits(), state.getCursorPosition(), ((Button) view).getText().toString());
                updateExpressionView();
                scrollDown(expressionScroller);
                break;
        }
    }

    private View.OnTouchListener onTouchUpdateCursor() {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Layout layout = ((TextView) v).getLayout();
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (layout != null) {
                        int line = layout.getLineForVertical(y);
                        int offset = layout.getOffsetForHorizontal(line, x);
                        state.getCursorPosition().setValue(findBlockPosition(offset));
                        cursorVisible = true;
                        updateExpressionView();
                    }
                }
                return true;
            }

            private int findBlockPosition(int offset) {
                if (state.getExpression().getUnits().isEmpty()){
                    return 0;
                }
                int blockPosition = 0;
                int accumulatedLength = 0;
                Iterator<ExpressionUnit> iterator = state.getExpression().getUnits().iterator();
                String textBlock = iterator.next().getText();
                while (accumulatedLength + (textBlock.length()+1)/2 <= offset){
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
        final Runnable blink = new Runnable() {
            @Override
            public void run() {
                cursorVisible = !cursorVisible;
                updateExpressionView();

                if (refreshResultView) {
                    refreshResultView = false;
                    refreshResultView();
                }
            }
            private void refreshResultView() {
                resultView.setText(resultView.getText().toString());
            }
        };
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                handler.post(blink);
            }
        }, 500 /*FIXME: SHOULD RELY ON TIMING TO WORK */, 500, TimeUnit.MILLISECONDS);
    }

    // TODO: Move scale to CalculatorState, pass it as a parameter to the DisplayHelper on evaluation.
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
    private void scrollDown(ScrollView scrollView){
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private TextWatcher textAutoResizeWatcher(final TextView view, final int minSp, final int maxSp) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                adjustTextSize(view, minSp, maxSp);
            }
        };
    }

    private void adjustTextSize(TextView view, int minSp, int maxSp){
        int widthLimitPixels = getWidthLimitPixels(view);
        Paint paint = new Paint();

        float fontSizeSP = pixelsToSp(view.getTextSize());
        paint.setTextSize(spToPixels(fontSizeSP));

        String viewText = view.getText().toString();

        float widthPixels = paint.measureText(viewText);

        // Increase font size if necessary.
        if (widthPixels < widthLimitPixels){
            while (widthPixels < widthLimitPixels && fontSizeSP <= maxSp){
                fontSizeSP++;
                paint.setTextSize(spToPixels(fontSizeSP));
                widthPixels = paint.measureText(viewText);
            }
            fontSizeSP--;
        }
        // Decrease font size if necessary.
        else {
            while (widthPixels > widthLimitPixels || fontSizeSP > maxSp) {
                if (fontSizeSP < minSp) {
                    fontSizeSP = minSp;
                    break;
                }
                fontSizeSP--;
                paint.setTextSize(spToPixels(fontSizeSP));
                widthPixels = paint.measureText(viewText);
            }
        }

        view.setTextSize(fontSizeSP);
    }

    private void updateExpressionView() {
        expressionView.setText(generateExpressionText());
    }

    // Generate and wrap expression text to fit the view width limit.
    private String generateExpressionText(){

        int widthLimitPixels = getWidthLimitPixels(expressionView);

        Paint paint = new Paint();
        paint.setTextSize(expressionView.getTextSize());

        StringBuffer accumulatedText = new StringBuffer();
        StringBuffer line = new StringBuffer();
        String cursor = cursorVisible ? "|" : " ";
        int blockIndex = 0;

        Iterator<ExpressionUnit> iterator = state.getExpression().getUnits().iterator();

        while (iterator.hasNext() || state.getCursorPosition().getValue() == blockIndex){
            String blockText;
            // Add cursor or ExpressionUnit.
            if (state.getCursorPosition().getValue() == blockIndex){
                blockText = cursor;
            } else {
                ExpressionUnit nextBlock = iterator.next();
                blockText = nextBlock.getText();
            }
            // Break line when the current width overcomes the limit:
            if (paint.measureText(line + blockText) > widthLimitPixels){
                line = new StringBuffer();
                accumulatedText.append("\n");
            }

            line.append(blockText);
            accumulatedText.append(blockText);
            blockIndex++;
        }

        return accumulatedText.toString();
    }

    private void copyResult() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("resultView", resultView.getText());
        clipboard.setPrimaryClip(clip);
    }

    private void displayCopyMessage (){
        Toast.makeText(getApplicationContext(), R.string.copyMessage, Toast.LENGTH_SHORT).show();
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

        // TODO: Use superscript -1 for inverse trigonometric functions.
        String prefix = state.isInv() ? "arc" : "";
        String suffix = state.isHyp() ? "h"   : "";
        ((Button) findViewById(R.id.buttonSin)).setText(prefix + "sin" + suffix);
        ((Button) findViewById(R.id.buttonCos)).setText(prefix + "cos" + suffix);
        ((Button) findViewById(R.id.buttonTan)).setText(prefix + "tan" + suffix);

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

        int trigonometricColor = state.isHyp() || state.isInv() ? Color.BLACK : Color.WHITE;
        int logExpColor =  state.isInv() ? Color.BLACK : Color.WHITE;

        ((Button) findViewById(R.id.buttonLn)).setTextColor(logExpColor);
        ((Button) findViewById(R.id.buttonLog)).setTextColor(logExpColor);
        ((Button) findViewById(R.id.buttonSqrt)).setTextColor(logExpColor);
        ((Button) findViewById(R.id.buttonSin)).setTextColor(trigonometricColor);
        ((Button) findViewById(R.id.buttonCos)).setTextColor(trigonometricColor);
        ((Button) findViewById(R.id.buttonTan)).setTextColor(trigonometricColor);
    }

    private int getWidthLimitPixels(TextView view){
        return view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
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
