package com.example.testdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BarGraphView extends View implements ValueAnimator.AnimatorUpdateListener {
    private Paint mBarPaint = new Paint();
    private Paint mGridPaint = new Paint();
    private Paint mGuideLinePaint = new Paint();
    private ValueAnimator mAnimator;
    private float mAnimatingFraction;
    private float mPaddingTop;
    private float mPaddingLeft;
    private float mPaddingBottom;
    private float mColumnSpacing;
    float top;
    private float spacing;
    private float[] data;
    float[] dataInPercentage;
    public BarGraphView(Context context) {
        super(context);
        inIt();
    }
    public BarGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }
    public BarGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inIt();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.doDrawing(canvas);
    }

    /** This is method inefficient so use <link href="#setData">setData(List<Float> data )<link/>
     *
     * */
    @Deprecated
    public void setData(float[] data) {
        this.data = data;
        final List <Float> floats = new ArrayList <> () ;
        for (int i = 0; i < data.length; i++){
            floats.add (data[i]);
        }
        setData(floats);
    }

    public void setData(List<Float> data) {
        dataInPercentage = new float[data.size ()];
        for (int i = 0; i < data.size (); i++){
            // Dividing by 100 is inefficient
            //dataInPercentage[i] = data.get ( i )  / 100;
            //Rather divide by the Maximum Value in Data or DataSet given
            dataInPercentage[i] = (data.get ( i ) / Collections.max ( data ) ) ;
        }
    }

    private void  initPaddingAndSpacing(){
        mPaddingTop = 4 * getResources ().getDisplayMetrics ().density ;
        mPaddingLeft = 2 * getResources ().getDisplayMetrics ().density ;
        mPaddingBottom = 4 * getResources ().getDisplayMetrics ().density ;
        mColumnSpacing = 4 * getResources ().getDisplayMetrics ().density ;
        spacing = 4 * getResources ().getDisplayMetrics ().density ;
    }
    private void inIt(){
        initTestData();
        initPaddingAndSpacing();
        initPaint();
        initAnimator();
    }

    private void initTestData() {
        data = new float[]{9,78,30,100,50,45,80,95};
        dataInPercentage = new float[ data.length];

        for (int i = 0; i < data.length; i++){
            // Dividing by 100 is inefficient. Note that is for test only
            dataInPercentage[i] = data[i] / 100;
        }
    }

    private void initPaint() {
        final float guidelineThicknessInPx = 5 * getResources ().getDisplayMetrics ().density ;
        final float gridThicknessInPx = 5 * getResources ().getDisplayMetrics ().density ;
        final int barColor = Color.MAGENTA;

        mBarPaint.setStyle(Paint.Style.FILL);
        mBarPaint.setColor(barColor);

        mGridPaint.setStrokeWidth(gridThicknessInPx);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setColor(Color.DKGRAY);

        mGuideLinePaint.setStrokeWidth(guidelineThicknessInPx);
        mGuideLinePaint.setStyle(Paint.Style.STROKE);
        mGuideLinePaint.setColor(Color.RED);
    }

    private void initAnimator() {
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(600);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addUpdateListener(this);
        mAnimator.setFloatValues(0f, 1f);
    }

    private void doDrawing(Canvas canvas) {

        final int width = getWidth();
        final int height = getHeight();
        final float gridLeft = mPaddingLeft;
        final float gridBottom = height - mPaddingBottom;
        final float gridTop = mPaddingTop;
        final float gridRight = width - mPaddingLeft;
        final float gridHeight = gridBottom - gridTop;

        canvas.drawLine(gridLeft, gridBottom, gridLeft, gridTop, mGridPaint);
        canvas.drawLine(gridLeft, gridBottom, gridRight, gridBottom, mGridPaint);

        float guidLineSpacing = (gridBottom - gridTop) / 10f;
        float y;

        for (int i = 0; i < 10; i++){
            y = gridTop + i * guidLineSpacing;
            canvas.drawLine(gridLeft, y, gridRight, y, mGuideLinePaint);
        }

        float totalColumnSpacing = spacing * (data.length);
        float columnWidth = (gridRight - gridLeft - totalColumnSpacing) / data.length;
        float columnLeft = gridLeft + spacing;
        float columnRight = columnLeft + columnWidth;

        for (float percentage : dataInPercentage){
            top =  mPaddingTop + gridHeight * (1f - (percentage * mAnimatingFraction));
            canvas.drawRect(columnLeft, top, columnRight, gridBottom, mBarPaint);
            columnLeft = columnRight + mColumnSpacing;
            columnRight = columnLeft + columnWidth;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mAnimatingFraction = animation.getAnimatedFraction();
        postInvalidate ();
    }

    public void playGraph(){
        mAnimator.setRepeatCount (0);
        mAnimator.start();
    }

    public void playRepeat(){
        mAnimator.getRepeatMode ();
        mAnimator.setRepeatCount ( ValueAnimator.INFINITE );
        mAnimator.start();
    }

    public void playReverse(){
        if(mAnimator.isRunning ()){
            mAnimator.reverse ();
        }
    }
    public void stop(){
        if(mAnimator.isRunning ()){
            mAnimator.end ();
        }
    }
}