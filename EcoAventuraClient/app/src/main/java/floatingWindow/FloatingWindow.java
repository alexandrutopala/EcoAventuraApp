package floatingWindow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.R;

/**
 * Created by Alexandru on 15.04.2017.
 */

public class FloatingWindow extends Service {
    private  WindowManager wm;
    private static RelativeLayout myLayout;
    private static Button dismiss;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate () {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MyApplication.getMyApplicationContext())) {
                return;
            }
        }

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) MyApplication.getMyApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        myLayout = (RelativeLayout) inflater.inflate(R.layout.popup_window_layout, null);

        if (myLayout == null) {
            myLayout = new RelativeLayout(this);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            myLayout.setBackgroundResource(R.drawable.loader);
            myLayout.setLayoutParams(params);
        }

        Display display = ((WindowManager) MyApplication.getMyApplicationContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams wParams = new WindowManager.LayoutParams(size.x,
                size.y,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        wParams.x = 0;
        wParams.y = 0;
        wParams.gravity = Gravity.CENTER | Gravity.CENTER;
        wParams.windowAnimations = android.R.style.Animation_Translucent;

        try {

            wm.addView(myLayout, wParams);
            myLayout.setVisibility(View.GONE);
            dismiss = (Button) myLayout.findViewById(R.id.button18);

            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeLoadingScreenDisappear();
                }
            });
            dismiss.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            view.setBackground(FloatingWindow.this.getDrawable(R.drawable.state_button_pushed));

                            //pushed.setBounds( view.getLeft(), view.getTop(), view.getRight(), view.getBottom() );
                            //((Button)view).setCompoundDrawables( pushed, null, null, null );

                            //view.setBackgroundColor(0xd24304);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            view.setBackground(FloatingWindow.this.getDrawable(R.drawable.circle_label));

                            //released.setBounds( view.getLeft(), view.getTop(), view.getRight(), view.getBottom() );
                            //((Button)view).setCompoundDrawables( released, null, null, null );

                            //view.setBackgroundColor(0xFFFFFF);
                            break;
                        }
                        case MotionEvent.ACTION_CANCEL: {
                            view.setBackground(FloatingWindow.this.getDrawable(R.drawable.circle_label));

                            //view.setBackgroundColor(0xFFFFFF);
                            break;
                        }
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            myLayout = null; // permission denied
        }

    }

    public static void makeLoadingScreenDisappear () {
        if (myLayout != null) {
            /* not working propertly
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.disappear);
            anim.setDuration(3000);
            anim.setFillAfter(false);
            myLayout.startAnimation(anim);
            */
            myLayout.setVisibility(View.GONE);
        }
    }

    public static void makeLoadingScreenAppear () {
        if (myLayout != null) {
            myLayout.setVisibility(View.VISIBLE);
            /* not working propertly
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.appear);
            anim.setDuration(3000);
            anim.setFillAfter(false);
            myLayout.startAnimation(anim);
            */
        }
    }

    public static boolean isLoadingScreenVisible () {
        if (myLayout == null) return false;
        return (myLayout.getVisibility() == View.VISIBLE);
    }

    public static void setMessage (String msg) {
        if(myLayout == null) return;
        TextView text = (TextView) myLayout.findViewById(R.id.textView56);
        text.setText(msg);
    }


}
