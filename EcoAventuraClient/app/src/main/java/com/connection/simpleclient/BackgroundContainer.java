package com.connection.simpleclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Alexandru on 08.02.2017.
 */

public class BackgroundContainer extends FrameLayout {
    private boolean mShowing = false;
    private Drawable mShowedBackground;
    private int mOpenAreaTop, mOpenAreaBotton, mOpenAreaHeight;
    private boolean mUpdateBounds = false;

    public BackgroundContainer(Context context) {
        super(context);
        init();
    }

    public BackgroundContainer (Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public BackgroundContainer (Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        init();
    }

    public void init () {
        mShowedBackground = getContext().getResources().getDrawable(R.drawable.shadowed_background);
    }

    public void showBackground (int top, int bottom) {
        setWillNotDraw(false);
        mOpenAreaTop = top;
        mOpenAreaHeight = bottom;
        mShowing = true;
        mUpdateBounds = true;
    }

    public void hideBackground () {
        setWillNotDraw(true);
        mShowing = false;
    }

    @Override
    protected void onDraw (Canvas canvas) {
        if (mShowing) {
            if (mUpdateBounds) {
                mShowedBackground.setBounds(0, 0, getWidth(), mOpenAreaHeight);
            }
            canvas.save();
            canvas.translate(0, mOpenAreaTop);
            mShowedBackground.draw(canvas);
            canvas.restore();
        }
    }
}
