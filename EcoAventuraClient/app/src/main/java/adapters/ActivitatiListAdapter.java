package adapters;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.connection.simpleclient.Activitate;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;
import com.connection.simpleclient.TouchListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.HashMap;
import java.util.List;

import adapters.ActivitatiCompleteListAdaptor;
import dto.ActivitateDTO;
import dto.EchipaDTO;
import dto.JocDTO;

/**
 * Created by Alexandru on 9/1/2016.
 */
public class ActivitatiListAdapter extends MyBaseAdapter {
    private ActivitatiCompleteListAdaptor adaptor;
    private Context mContext;
    private List<Activitate> mActivitateList;
    private boolean isLocked = false;
    private TouchListener mTouchListener;
    private View.OnTouchListener auxTouchListener;
    private final View.OnClickListener clickListener;
    private final View.OnLongClickListener longClickListener;
    private final ListView mListView;
    private boolean isScrolling = false;
    private boolean refresh = true;
    private long closeIntentId = -1;
    private int closeIntentPos = -1;
    private FloatingActionButton fab;

    private final static int MIN_ITEMS_DIF = 2;
    //private View.OnTouchListener listener;

    // help
    private ShowcaseView showcase;
    private Target viewTarget;
    private Target emblemaTarget;
    private Target echipaTarget;
    private Target colorTarget;

    public ActivitatiListAdapter(final Context mContext, final List<Activitate> mActivitateList, ActivitatiCompleteListAdaptor adaptor,
                                 TouchListener mTouchListener, View.OnClickListener clickListener,
                                 View.OnLongClickListener longClickListener, final ListView mListView,
                                 View.OnTouchListener auxTouchListener) {
        this.mContext = mContext;
        this.mActivitateList = mActivitateList;
        this.adaptor = adaptor;
        this.mTouchListener = mTouchListener;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.mListView = mListView;
        this.auxTouchListener = auxTouchListener;

        mListView.setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                        if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            isScrolling = false;

                            if (fab != null) {
                                fab.show();
                            }
                        } else {
                            isScrolling = true;

                            if (fab != null){
                                fab.hide();
                            }
                        }
                        notifyListTouch(false);
                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) { }
                }
        );

        mListView.addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                        refresh = false;
                    }
                }
        );

        //setam id-urile noilor activitati pt a le putea recunoaste cand realizam animatia

        for (long i = 0; i < mActivitateList.size(); ++i) {
            mActivitateList.get((int) i).setId((int) i);
        }

        //this.listener = listener;
    }

    public void setFab (FloatingActionButton fab) {
        this.fab = fab;
    }

    public long getCloseIntentId () {
        return closeIntentId;
    }

    public void setCloseIntentId(long id){
        closeIntentId = id;
    }

    public int getCloseIntentPos () {
        return closeIntentPos;
    }

    public void setCloseIntentPos (int pos) {
        closeIntentPos = pos;
    }

    public void bringToFront (Activitate a) {
    }

    public void pushDown (Activitate a) {
    }

    public void lockContent (boolean locked) {
        isLocked = locked;
    }

    public ActivitatiCompleteListAdaptor getAdaptor() {
        return adaptor;
    }

    public void setAdaptor(ActivitatiCompleteListAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    @Override
    public int getCount() {
        return mActivitateList.size();
    }

    @Override
    public Object getItem(int i) {
        return mActivitateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        try {
            return mActivitateList.get(i).getId();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public boolean hasStableIds () {
        return true;
    }

    public void addElement (Activitate a) {
        mActivitateList.add(a);
    }

    public boolean removeElement (Activitate a) {
        try {
            return mActivitateList.remove(a);
        } catch (Exception e) {
            return false;
        }
    }

    public Activitate remove (int i) {
        return mActivitateList.remove(i);
    }

    public boolean canRemove (int position) {
        try {
            return (mActivitateList.get(position).getEchipe().isEmpty());
        } catch (Exception e) { return false; }
    }

    public void refresh() {
        refresh = true;
    }

    public synchronized void notifyListTouch (boolean instantAnimation) {
        try {
            if (getCloseIntentId() == -1) return;


            if (getCloseIntentPos() >= mListView.getFirstVisiblePosition() && getCloseIntentPos() <= mListView.getLastVisiblePosition()) {
                int childPos = getCloseIntentPos() - mListView.getFirstVisiblePosition();

                final View aux = mListView.getChildAt(childPos);
                final TextView v = (TextView) aux.findViewById(R.id.textView41);


                final ValueAnimator currentAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                        Color.WHITE,
                        Controller.getInstance().getColor(getCloseIntentPos()));


                final Drawable drawable = mContext.getResources().getDrawable(R.drawable.circle_label);
                currentAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(final ValueAnimator animator) {
                        drawable.setColorFilter((Integer) animator.getAnimatedValue(),
                                PorterDuff.Mode.MULTIPLY);
                        v.setBackground(drawable);
                    }

                });
                currentAnimation.setDuration(instantAnimation ? 0 : 300);
                currentAnimation.start();
                v.setTextColor(Color.WHITE);
                //v.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

                if (mActivitateList.get(getCloseIntentPos()).getActivitateDTO() != null) {
                    v.setText("A");
                } else {
                    v.setText("J");
                    if (mActivitateList.get(getCloseIntentPos()).getJocDTO().getJocGeneral().toString().equalsIgnoreCase("penalizare")){
                        v.setText("P");
                    }
                }
                //v.setTextSize(50);
            }
        } catch (Exception e) {
            Log.i("thread", "Desincronizare");
        }
        closeIntentId = -1;
        closeIntentPos = -1;
    }

    @Override
    public void notifyDataSetChanged () {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.item_activitate_list, null);

        // add touch listener to detect swipe motion
        if (mTouchListener != null) {
            v.setOnTouchListener(mTouchListener);
        } else {
            v.setOnTouchListener(auxTouchListener);
        }
        v.setOnClickListener(clickListener);
        v.setOnLongClickListener(longClickListener);

        TextView t1 = (TextView) v.findViewById(R.id.userName);
        TextView t2 = (TextView) v.findViewById(R.id.textView2);
        TextView t3 = (TextView) v.findViewById(R.id.textView3);
        TextView t4 = (TextView) v.findViewById(R.id.textView4);
        final TextView label = (TextView) v.findViewById(R.id.textView41);
        final TextView nrEchipe = (TextView) v.findViewById(R.id.textView54);

        try {
            nrEchipe.setVisibility(View.INVISIBLE);
            nrEchipe.setText("(" + mActivitateList.get(i).getEchipe().size() + ")");
            nrEchipe.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
        //t2.setWidth((int) (b.getX() - t2.getX() - 10));
        //t3.setWidth((int) (b.getX() - t3.getX() - 10));
        //t4.setWidth((int) (b.getX() - t4.getX() - 10));

        ActivitateDTO activitateDTO = mActivitateList.get(i).getActivitateDTO();
        JocDTO jocDTO = mActivitateList.get(i).getJocDTO();
        t1.setText("");
        t1.setBackgroundColor(Color.WHITE);

        if (mActivitateList.get(i).isHighPiority()){
            t1.setBackgroundColor(Controller.getInstance().getColor(2));
        }

        //label.setTextColor(Controller.getInstance().getRandomColor());

        if (activitateDTO != null){
            t2.setText(activitateDTO.getActivitateGenerala().getNumeActivitateGenerala());
            t3.setText(activitateDTO.getPerioada());
            t4.setText(activitateDTO.getLocatie());
            label.setText("A");
        } else if (jocDTO != null) {
            t2.setText(jocDTO.getJocGeneral().getNumeJocGeneral());
            t3.setText(jocDTO.getPerioada());
            t4.setText(jocDTO.getLocatie());
            label.setText("J");

            if (jocDTO.getJocGeneral().toString().equalsIgnoreCase("penalizare")) {
                label.setText("P");
            }
        }

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.circle_label);
        drawable.setColorFilter(Controller.getInstance().getColor(i),
                PorterDuff.Mode.MULTIPLY);
        label.setBackground(drawable);
        label.setTextColor(Color.WHITE);

        v.setTag(mActivitateList.get(i).getId());

        if (!isLocked) {
            label.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public synchronized  void onClick(final View view) {
                            try {
                                if (closeIntentId == -1 || closeIntentId != getItemId(i)) {
                                    if (closeIntentId != getItemId(i)) {
                                        notifyListTouch(true);
                                    }
                                    closeIntentId = getItemId(i);
                                    closeIntentPos = i;
                                    final ValueAnimator currentAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                                            Controller.getInstance().getColor(i),
                                            Color.WHITE);


                                    final Drawable drawable = mContext.getResources().getDrawable(R.drawable.circle_label);
                                    currentAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                        @Override
                                        public void onAnimationUpdate(final ValueAnimator animator) {
                                            drawable.setColorFilter((Integer) animator.getAnimatedValue(),
                                                    PorterDuff.Mode.MULTIPLY);
                                            view.setBackground(drawable);
                                        }

                                    });
                                    currentAnimation.setDuration(300);
                                    currentAnimation.start();
                                    //label.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                                    //label.setTextSize(50);
                                    label.setTextColor(Color.LTGRAY);
                                    label.setText("X");
                                    return;
                                }
                                //if (i == 0) { // am anulat eliminarea in ordine
                                if (mActivitateList.get(i).getActivitateDTO() != null)
                                    if (mActivitateList.get(i).getActivitateDTO().getEchipe().isEmpty()) {
                                        mActivitateList.get(i).setDone(true);
                                        //adaptor.addElement(mActivitateList.get(i));
                                        //removeElement(mActivitateList.get(i));
                                        mTouchListener.animateRemoval(mListView, view);
                                        notifyDataSetChanged();
                                    } else {
                                        if (mActivitateList.get(i).getJocDTO() != null && mActivitateList.get(i).getJocDTO().getEchipe().isEmpty()) {
                                            mActivitateList.get(i).setDone(true);
                                            //adaptor.addElement(mActivitateList.get(i));
                                            //removeElement(mActivitateList.get(i));
                                            mTouchListener.animateRemoval(mListView, view);
                                            notifyDataSetChanged();
                                        } else {
                                            mTouchListener.animateNotAcceptedAction(view);
                                            Toast.makeText(mContext, "Toate echipele trebuie sa termine activitatea inainte de a fi eliminata", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                else if (mActivitateList.get(i).getJocDTO() != null)
                                    if (mActivitateList.get(i).getJocDTO().getEchipe().isEmpty()) {
                                        mActivitateList.get(i).setDone(true);
                                        //adaptor.addElement(mActivitateList.get(i));
                                        //removeElement(mActivitateList.get(i));
                                        mTouchListener.animateRemoval(mListView, view);
                                        notifyDataSetChanged();
                                    } else {
                                        mTouchListener.animateNotAcceptedAction(view);
                                        Toast.makeText(mContext, "Toate echipele trebuie sa termine activitatea inainte de a fi eliminata", Toast.LENGTH_SHORT).show();
                                    }
                                else {
                                    mTouchListener.animateNotAcceptedAction(view);
                                    Toast.makeText(mContext, "Toate echipele trebuie sa termine activitatea inainte de a fi eliminata", Toast.LENGTH_SHORT).show();
                                }
                                //} else {
                                //    Toast.makeText(mContext, "Activitatile trebuie completate in ordine", Toast.LENGTH_SHORT).show();
                                //}
                                //Toast.makeText(mContext, "ai selectat" + i, Toast.LENGTH_SHORT).show();
                                notifyListTouch(false);
                            } catch (Exception e) { Log.i("thread", "Desincronizare click"); }
                        }
                    }
            );
        }

        //v.setOnTouchListener(listener);
        if (isRequestingAnimation()) {
            Animation animation = AnimationUtils.loadAnimation(
                    mContext, R.anim.slide_from_bottom_to_top
            );
            v.startAnimation(animation);
        }

        return v;
    }

    public List<Activitate> getmActivitateList() {
        return mActivitateList;
    }

    private boolean isRequestingAnimation () {
        if (isScrolling) {
            return true;
        } else if (refresh) {
            return true;
        }
        return false;
    }

    private int step;
    private final static int TOTAL_STEPS = 5;

    public void showTutorial (AppCompatActivity activity) {
        makeTutorial(activity);
    }

    private void makeTutorial (AppCompatActivity activity) {
        step = 0;
        int pos = mListView.getFirstVisiblePosition();
        View view = mListView.getChildAt(pos);

        viewTarget = new ViewTarget(view);
        emblemaTarget = new ViewTarget(R.id.textView41, activity);
        echipaTarget = new ViewTarget(R.id.textView54, activity);
        colorTarget = new ViewTarget(R.id.userName, activity);

        showcase = new ShowcaseView.Builder(activity)
                .setStyle(R.style.TransparentStyle)
                .hideOnTouchOutside()
                .setTarget(viewTarget)
                .setContentTitle("Activitate")
                .setContentText("Aceasta este o activitate. Pentru a o realiza sau a vedea mai multe detalii, apasa pe ea.\n" +
                        "Pentru a finaliza activitatea, gliseaz-o in lateral. O activitate poate fi finalizata numai daca toate echipele au realizat-o")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performClick(view);
                    }
                })
                .build();

        showcase.setButtonText("Urmatorul");
        showcase.show();
        step++;
    }

    private void performClick (View view) {
        switch (step) {
            case 0 : break;
            case 1 :
                showcase.setShowcase(emblemaTarget, true);
                showcase.setContentTitle("Emblema");
                showcase.setContentText("Semnifica tipul de sarcina : \nA - activitate\nJ - joc\nP - penalizare");
                break;
            case 2 :
                showcase.setContentText("Apasa de doua ori pe aceasta pentru a finaliza sarcina.");
                break;
            case 3 :
                showcase.setShowcase(echipaTarget, true);
                showcase.setContentText("Arata cate echipe trebuie sa realizeze sarcina.");
                showcase.setContentTitle("Numar echipe");
                break;
            case 4 :
                showcase.setShowcase(colorTarget, true);
                showcase.setContentTitle("Marcator");
                showcase.setContentText("Tine apasat pe activitate pentru a o marca. Aceasta componenta se va inrosi. " +
                        "Pentru a o demarca, repeta procedeul.");
                showcase.setButtonText("Gata");
                break;
            default:
                showcase.hide();
        }
        step++;
    }
}
