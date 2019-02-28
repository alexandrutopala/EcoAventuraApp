package com.connection.simpleclient;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

import adapters.ActivitatiCompleteListAdaptor;
import gui.MainContent;

/**
 * Ascultatorul este folosit doar pentru activitatile complete
 * Created by Alexandru on 10.02.2017.
 */

public class SecondTouchListener implements View.OnTouchListener {
    private static final float MIN_DELTA_X = 30.0f;
    private float X_LIMIT = 500;
    private Context mContext;
    private ListView mListView;
    private int mSwipeSlop = -1;
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    private float mDownX;
    //private BackgroundContainer mBackgroundContainer;
    private boolean animateBack = false;
    private boolean send = false;

    // long touch

    private final static int LONG_CLICK_MILLIS = 1100;
    private final static int TIME_INTERVAL = 1000;
    private View referenceView;
    private long startTime;
    private boolean longClickPerformed = false;
    private long lastLongPressTime = 0;

    public SecondTouchListener (AppCompatActivity mContext, ListView mListView){
        this.mContext = mContext;
        this.mListView = mListView;
        //mBackgroundContainer = (BackgroundContainer) mContext.findViewById(R.id.listViewBackground);
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        X_LIMIT = (float) view.getWidth() / 4;
        if (mSwipeSlop < 0) {
            mSwipeSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        }


        if (animateBack) return true;

        if (System.currentTimeMillis() - startTime >= LONG_CLICK_MILLIS && view != null && view == referenceView && startTime != -1) {
            if (!mSwiping) {
                try {
                    if (System.currentTimeMillis() - lastLongPressTime >= TIME_INTERVAL) {
                        view.performLongClick();
                        lastLongPressTime = System.currentTimeMillis();
                    }
                } catch (Exception e) {
                }
                longClickPerformed = true;
            } else {
                startTime = -1;
            }
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                view.setBackgroundColor(Color.LTGRAY);
                if (mItemPressed) {
                    return false;
                }

                referenceView = view;
                startTime = System.currentTimeMillis();

                mItemPressed = true;
                mDownX = motionEvent.getX();
                send = false;
                break;
            }
            case MotionEvent.ACTION_MOVE:{

                float x = motionEvent.getX() + view.getTranslationX();
                float deltaX = x - mDownX;
                float deltaXAbs = Math.abs(deltaX);


                if (deltaXAbs <= MIN_DELTA_X) {
                    mSwiping = false;
                    view.setTranslationX(0);
                    return false;
                }

                if (!mSwiping) {
                    mSwiping = true;
                    mListView.requestDisallowInterceptTouchEvent(true);
                   // mBackgroundContainer.showBackground(view.getTop(), view.getHeight());
                }

                if (mSwiping) {
                    startTime = -1;
                    if (deltaX > X_LIMIT) deltaX = X_LIMIT;
                    else if (deltaX < -X_LIMIT) deltaX = -X_LIMIT;
                    view.setTranslationX(deltaX);
                    if (deltaX == X_LIMIT || deltaX == -X_LIMIT) {
                        Vibrator v = (Vibrator) SecondTouchListener.this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(100);
                        send = true;
                    }
                    //view.setAlpha(1 - deltaXAbs / view.getWidth());
                }

                break;
            }
            case MotionEvent.ACTION_UP:{
                if (mSwiping) {
                    mListView.setEnabled(false);
                    animateBack = true;
                    view.animate().translationX(0)
                            .alpha(1)
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    // ce se intampla cand o activitate a fost eliminata
                                    animateBack = false;
                                    mDownX = 0;
                                    if (send) {
                                        try {
                                            ((MainContent) mContext).sendActivitate(
                                                    (Activitate) ((ActivitatiCompleteListAdaptor) mListView.getAdapter()).getItem(
                                                            mListView.getPositionForView(view)
                                                    )
                                            );
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                    mListView.setEnabled(true);
                    //mBackgroundContainer.hideBackground();
                } else {
                    if (!longClickPerformed) {
                        view.performClick();
                    }
                }

                startTime = -1;
                longClickPerformed = false;
                referenceView = null;
                mSwiping = false;
                mItemPressed = false;
                view.setBackgroundColor(Color.WHITE);
                break;
            }
            case MotionEvent.ACTION_CANCEL : {
                view.setTranslationX(0);
                view.setBackgroundColor(Color.WHITE);
                mItemPressed = false;
                startTime = -1;
                longClickPerformed = false;
                break;
            }
            default: view.setBackgroundColor(Color.WHITE); return false;
        }
        return true;
    }
}
