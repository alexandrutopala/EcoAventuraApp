package com.connection.simpleclient;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.RelativeLayout;

import adapters.EchipeListAdapter;

/**
 * Created by Alexandru on 11.02.2017.
 */

public class ButtonTouchListener implements View.OnTouchListener {
    private float LEFT_LIMIT_X;
    private float RIGHT_LIMIT_X;
    private boolean mItemPressed = false;
    private boolean mSwiping = false;
    private int mSwipeSlop = -1;
    private float mDownX;
    private boolean match = false;
    private final RelativeLayout referenceView;
    private final AppCompatActivity mContext;
    private final ListView mListView;
    private boolean vibrate = false;

    public ButtonTouchListener (AppCompatActivity mContext, RelativeLayout referenceView, ListView mListView) {
        this.mContext = mContext;
        this.referenceView = referenceView;
        this.mListView = mListView;
        LEFT_LIMIT_X = referenceView.getX();
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mSwipeSlop < 0) {
            mSwipeSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        }
        LEFT_LIMIT_X = referenceView.getX() - 3;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN : {
                if (mItemPressed) return false;

                mItemPressed = true;
                mDownX = motionEvent.getX();
                RIGHT_LIMIT_X = view.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE : {
                float x = motionEvent.getX();
                float deltaX = x - mDownX;
                float DELTA_X = LEFT_LIMIT_X - RIGHT_LIMIT_X;

                if (!mSwiping) {
                    mSwiping = true;
                    mListView.requestDisallowInterceptTouchEvent(true);
                }

                Log.i("echipa", "deltaX : " + deltaX);

                if (mSwiping) {
                    if (RIGHT_LIMIT_X + deltaX < LEFT_LIMIT_X){
                        deltaX = DELTA_X;
                        //view.setTranslationX(LEFT_LIMIT_X);
                        if (!vibrate) {
                            Vibrator v = (Vibrator) this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(100);
                            vibrate = true;
                        }
                        match = true;
                    } else if (deltaX > 0) {
                        deltaX = 0;
                        view.setTranslationX(0);
                        vibrate = false;
                    } else {
                        view.setTranslationX(deltaX);
                        vibrate = false;
                    }
                    referenceView.setAlpha(deltaX / DELTA_X);

                }
                break;
            }
            case MotionEvent.ACTION_UP : {
                if (mSwiping) {
                    mListView.setEnabled(false);
                    view.animate().translationX(0).
                            setDuration(300);
                    referenceView.animate()
                            .alpha(0)
                            .setDuration(300);

                    mSwiping = false;
                    mListView.setEnabled(true);
                    if (match) {
                        ((EchipeListAdapter) mListView.getAdapter()).click();
                        view.performClick();
                    }
                }

                match = false;
                mItemPressed = false;
                break;
            }
            default:return false;

        }
        return false;
    }
}
