package com.connection.simpleclient;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

import adapters.ActivitatiListAdapter;
import adapters.MyBaseAdapter;
import gui.MainContent;

/**
 * Created by Alexandru on 08.02.2017.
 */

public class TouchListener implements View.OnTouchListener {
    private final static int SWIPE_DURATION = 250;
    private final static int MOVE_DURATION = 150;
    private final static float MIN_DELTA_X = 30.0f;

    private float mDownX;
    private int mSwipeSlop = -1;
    private Context mContext;
    private ListView mListView;
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    //private BackgroundContainer mBackgroundContainer;

    // long click vars
    private final static int LONG_CLICK_MILLIS = 1100;
    private final static int TIME_INTERVAL = 1000;
    private long startTime = -1;
    private boolean longClickPerformed = false;
    private View referenceView = null;
    private long lastLongPressTime = 0;


    public TouchListener (AppCompatActivity mContext,ListView mListView) {
        this.mContext = mContext;
        this.mListView = mListView;
        //mBackgroundContainer = (BackgroundContainer) mContext.findViewById(R.id.listViewBackground);

    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        if (mSwipeSlop < 0) {
            mSwipeSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        }

        if ((System.currentTimeMillis() - startTime) >= LONG_CLICK_MILLIS && view != null && view == referenceView && startTime != -1) {
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
            case MotionEvent.ACTION_DOWN :
                Log.i("animate", "DOWN");
                view.setBackgroundColor(Color.LTGRAY);

                referenceView = view;
                startTime = System.currentTimeMillis();

                if (mItemPressed) {
                    return false;
                }
                mItemPressed = true;
                mDownX = motionEvent.getX();
                break;
            case MotionEvent.ACTION_CANCEL:
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setBackgroundColor(Color.WHITE);
                mItemPressed = false;
                startTime = -1;
                longClickPerformed = false;
                break;
            case MotionEvent.ACTION_MOVE:{

                float x = motionEvent.getX() + view.getTranslationX();
                float deltaX = x - mDownX;
                float deltaXAbs = Math.abs(deltaX);


                Log.i("anim", "MOVE : view pos X : " + view.getX());
                if (deltaXAbs <= MIN_DELTA_X) {
                    mSwiping = false;
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    //mBackgroundContainer.hideBackground();
                    //mListView.requestDisallowInterceptTouchEvent(false);
                    return false;
                }

                if (!mSwiping) {
                    mSwiping = true;
                    mListView.requestDisallowInterceptTouchEvent(true);
                    //mBackgroundContainer.showBackground(view.getTop(), view.getHeight());
                }
                if (mSwiping) {
                    view.setTranslationX((x - mDownX));
                    view.setAlpha(1 - deltaXAbs / view.getWidth());
                }

            }
                break;
            case MotionEvent.ACTION_UP :{
                if (mSwiping) {
                    float x = motionEvent.getX() + view.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    float fractionCovered;
                    float endX;
                    float endAlpha;
                    final boolean remove;

                    ActivitatiListAdapter adapter = (ActivitatiListAdapter) mListView.getAdapter();
                    int position = mListView.getPositionForView(view);

                    Log.i("anim", "UP : deltaX : " + deltaX);

                    if (deltaXAbs > view.getWidth() / 4 && adapter.canRemove(position)) {
                        fractionCovered = deltaXAbs / view.getWidth();
                        endX = deltaX < 0 ? -view.getWidth() : view.getWidth();
                        endAlpha = 0;
                        remove = true;
                    } else {
                        fractionCovered = 1 - (deltaXAbs / view.getWidth());
                        endX = 0;
                        endAlpha = 1;
                        remove = false;
                    }

                    final boolean rejected = (deltaXAbs > view.getWidth() / 4);
                    long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                    mListView.setEnabled(false);

                    view.animate().setDuration(duration)
                            .alpha(endAlpha)
                            .translationX(endX)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    view.setAlpha(1);
                                    view.setTranslationX(0);
                                    if (remove) {
                                        animateRemoval(mListView, view);
                                    } else {
                                        if (rejected){
                                            Toast.makeText(mContext, "Toate echipele trebuie sa termine activitatea inainte de a fi eliminata", Toast.LENGTH_SHORT).show();
                                            animateNotAcceptedAction(view);
                                        }
                                        //mBackgroundContainer.hideBackground();
                                        mSwiping = false;
                                        mListView.setEnabled(true);
                                    }
                                }
                            });
                } else {
                    if (!longClickPerformed) {
                        view.performClick();
                        ((ActivitatiListAdapter) mListView.getAdapter()).notifyListTouch(false);
                    }
                }

                startTime = -1;
                longClickPerformed = false;
                referenceView = null;

                //mBackgroundContainer.hideBackground();
                mItemPressed = false;
                view.setBackgroundColor(Color.WHITE);

                break;
            }
            default:
                view.setBackgroundColor(Color.WHITE);
                return false;

        }
        return true;
    }

    private static boolean isSwiping = false;
    public static void animateRemoval (final ListView listview, View viewToRemove) {
        final HashMap<Long, Integer> mItemIdTopMap = new HashMap<>();

        //mBackgroundContainer.showBackground(viewToRemove.getTop(), viewToRemove.getHeight());
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (viewToRemove != child) {
                int position = firstVisiblePosition + i;
                long itemId = Controller.getInstance().getAdapterActivitati().getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }

        int position = viewToRemove != null ? listview.getPositionForView(viewToRemove) : -1;
        if (position != -1) {
            ((MyBaseAdapter) listview.getAdapter()).removeElement(
                    (Activitate) ((MyBaseAdapter) listview.getAdapter()).getItem(position)
            );
        }

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();

                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = Controller.getInstance().getAdapterActivitati().getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();

                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        //mBackgroundContainer.hideBackground();
                                        isSwiping = false;
                                        listview.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    //mBackgroundContainer.hideBackground();
                                    isSwiping = false;
                                    listview.setEnabled(true);
                                }
                            });
                        }
                    }
                }
                //mBackgroundContainer.hideBackground();
                Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    public void animateNotAcceptedAction (View view) {
        view.animate().translationX(0).setDuration(100);
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        Vibrator v = (Vibrator) TouchListener.this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
        view.startAnimation(anim);

    }
}
