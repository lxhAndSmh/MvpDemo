package com.liu.mvpdemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.liu.mvpdemo.R;


/**
 * QuickIndexView
 *
 * Created by weiTeng on 2016/6/12
 */
public class QuickIndexView extends View {

    private static final String mDefault = "A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|#";

    private Rect mBoundRect;
    private RectF[] mTextRects;
    private Paint mPaint;

    private String[] mTexts;

    private int mTextColor;
    private int mSelectedColor;

    private int mGap;
    private int mTextSize;
    private int mIndex = -1;

    private OnQuickIndexListener onQuickIndexListener;

    public QuickIndexView(Context context) {
        this(context, null);
    }

    public QuickIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexView);
        mTextSize = ta.getDimensionPixelSize(R.styleable.QuickIndexView_index_text_size,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        mTextColor = ta.getColor(R.styleable.QuickIndexView_index_text_color, Color.parseColor("#3d3d3d"));
        mSelectedColor = ta.getColor(R.styleable.QuickIndexView_index_text_select_color, Color.parseColor("#3095ef"));
        String indexText = ta.getString(R.styleable.QuickIndexView_index_texts);
        mGap = ta.getDimensionPixelSize(R.styleable.QuickIndexView_index_text_gap,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, getResources().getDisplayMetrics()));
        ta.recycle();

        initPaint();
        createIndexText(indexText);
    }

    private void createIndexText(String indexText) {
        if (indexText == null || "".equals(indexText)) {
            mTexts = mDefault.split("\\|");
        } else {
            mTexts = indexText.split("\\|");
        }
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG | Paint.DITHER_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
    }

    private void setTextSize(int size) {
        if (size != mTextSize) {
            mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics()));
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int realHeight = heightSize - getPaddingBottom() - getPaddingTop();

        int width, height;

        Rect textRect = new Rect();
        mPaint.getTextBounds(mTexts[0], 0, mTexts[0].length(), textRect);
        int tempHeight = textRect.height();
        if (heightMode == MeasureSpec.EXACTLY) {
            height = realHeight;
        } else {
            height = Math.min(tempHeight, realHeight);
        }

        int maxWidth = 0;
        for (int i = 0; i < mTexts.length; i++) {
            maxWidth += (textRect.width() + 2 * mGap);
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = realWidth;
        } else {
            width = Math.min(maxWidth, realWidth);
        }

        createRect(width, height);
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height + getPaddingTop() + dpToPx(60, getResources()) + getPaddingBottom());
    }

    private void createRect(int width, int height) {
        if (mBoundRect == null) {
            mBoundRect = new Rect();
        }

        int cellWeight = width / mTexts.length;
        mBoundRect.left = getPaddingLeft();
        mBoundRect.right = getPaddingLeft() + width;
        mBoundRect.top = getPaddingTop() - dpToPx(200, getResources());
        mBoundRect.bottom = getPaddingTop() + height;

        if (mTextRects == null) {
            mTextRects = new RectF[mTexts.length];
        }
        for (int i = 0; i < mTexts.length; i++) {
            if (mTextRects[i] == null) {
                mTextRects[i] = new RectF();
            }

            RectF rect = mTextRects[i];
            rect.left = getPaddingLeft() + i * cellWeight;
            rect.top = getPaddingTop() + dpToPx(60, getResources());
            rect.right = rect.left + cellWeight;
            rect.bottom = rect.top + height;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                float mStartX = event.getX();
                float mStartY = event.getY();

                int index = -1;
                for (int i = 0; i < mTextRects.length; i++) {
                    final RectF rect = mTextRects[i];
                    if (rect.contains(mStartX, mStartY)) {
                        index = i;
                        break;
                    }
                }

                if (index != -1 && mIndex != index) {
                    mIndex = index;
                    if (onQuickIndexListener != null) {
                        onQuickIndexListener.onQuickIndex(mIndex, mTexts[mIndex]);
                    }
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                mIndex = -1;
                setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg)));
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background
        super.onDraw(canvas);

        // Draw text
        mPaint.setColor(mTextColor);
        for (int i = 0; i < mTextRects.length; i++) {
            RectF rect = mTextRects[i];
            if (i != mIndex) {
                mPaint.setColor(mTextColor);
            } else {
                mPaint.setColor(mSelectedColor);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.text_select);
                canvas.drawBitmap(bitmap, rect.centerX() - dpToPx(18, getResources()), rect.centerY() - dpToPx(60, getResources()), null);
                canvas.drawText(mTexts[i], rect.centerX(), rect.centerY() - dpToPx(38, getResources()), mPaint);
            }
            canvas.drawText(mTexts[i], rect.centerX() , rect.centerY(), mPaint);
        }
    }

    public void setOnQuickIndexListener(OnQuickIndexListener onQuickIndexListener) {
        this.onQuickIndexListener = onQuickIndexListener;
    }

    public interface OnQuickIndexListener {
        void onQuickIndex(int index, String word);
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

}
