package adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.List;

import dto.AnimatorDTO;

/**
 * Created by Alexandru on 10/7/2016.
 */
public class AnimatoriAdapter extends BaseAdapter {
    private List<AnimatorDTO> mAnimatoriList;
    private Context mContext;
    private View.OnClickListener clickListener;
    private View.OnTouchListener mTouchListener;

    //ajutor
    private ShowcaseView showcase;
    private Target viewTarget;

    public AnimatoriAdapter (Context context, List<AnimatorDTO> animatori, View.OnClickListener clickListener){
        mContext = context;
        mAnimatoriList = animatori;
        this.clickListener = clickListener;
        mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(Color.LTGRAY);
                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundColor(Color.WHITE);
                    view.performClick();
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    view.setBackgroundColor(Color.WHITE);
                }

                return true;
            }
        };
    }

    @Override
    public int getCount() {
        return mAnimatoriList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAnimatoriList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mAnimatoriList.get(i).getIdAnimator();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View v = View.inflate(mContext, R.layout.item_animatori_list, null);

        v.setOnClickListener(clickListener);
        v.setOnTouchListener(mTouchListener);

        TextView capitalLetter = (TextView) v.findViewById(R.id.textView17);
        TextView animatorName = (TextView) v.findViewById(R.id.textView18);

        try {
            ((GradientDrawable) capitalLetter.getBackground()).setColor(Controller.getInstance().getColor(i));
        } catch (Exception e) {
            capitalLetter.setBackgroundColor(Controller.getInstance().getColor(i));
        }

        capitalLetter.setText(mAnimatoriList.get(i).getNumeAnimator().toUpperCase().substring(0, 1));
        capitalLetter.setTextColor(Color.WHITE);

        animatorName.setText(mAnimatoriList.get(i).getNumeAnimator());

        Animation animation = AnimationUtils.loadAnimation(
                mContext, R.anim.slide_from_bottom_to_top
        );
        v.startAnimation(animation);

        return v;
    }

    private int step;
    private final static int TOTAL_STEPS = 1;

    public void showTutorial (AppCompatActivity activity, ListView list) {
        makeTutorial(activity, list);
    }

    private void makeTutorial (AppCompatActivity activity, ListView mListView) {
        step = 0;
        int pos = mListView.getFirstVisiblePosition();
        View view = mListView.getChildAt(pos);

        viewTarget = new ViewTarget(view);

        showcase = new ShowcaseView.Builder(activity)
                .setStyle(R.style.TransparentStyle)
                .hideOnTouchOutside()
                .setTarget(Target.NONE)
                .setContentTitle("Meniul animatorilor")
                .setContentText("Aici poti vedea toti animatorii implicati in programul de activitati. Apasa pe oricare dintre " +
                        "ei pentru a-i vedea programul.")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performClick(view);
                    }
                })
                .build();

        showcase.setButtonText("Urmatorul");
        showcase.show();
    }

    private void performClick (View view) {
        switch (step) {
            case 0 :
                showcase.setShowcase(viewTarget, true);
                showcase.setContentTitle("Animator");
                showcase.setContentText("Aceasta este unul din animatorii implicati in program. Apasa pentru mai multe detalii.");
                showcase.setButtonText("Gata");
                break;
            default:
                showcase.hide();
        }
        step++;
    }
}
