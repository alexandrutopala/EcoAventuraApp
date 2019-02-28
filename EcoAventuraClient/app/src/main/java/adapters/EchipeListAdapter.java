package adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.connection.simpleclient.Activitate;
import com.connection.simpleclient.ButtonTouchListener;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import dto.EchipaDTO;
import gui.GameActivity;
import gui.MainContent;

/**
 * Created by Alexandru on 9/18/2016.
 */
public class EchipeListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EchipaDTO> mEchipeList;
    private Activitate activitate;
    private boolean isLocked = false;
    private AppCompatActivity activity;
    private ButtonTouchListener mTouchListener;
    private final ListView mListView;
    private boolean canClick = false;
    private View.OnClickListener mClickListener;

    // ajutor
    private ShowcaseView showcase;
    private Target viewTarget;
    private Target buttonTarget;

    public EchipeListAdapter (Context mContext, List<EchipaDTO> mEchipeList, Activitate activitate, AppCompatActivity activity,
                              ListView mListView, View.OnClickListener mClickListener){
        this.mContext = mContext;
        this.mEchipeList = mEchipeList;
        this.activitate = activitate;
        this.activity = activity;
        this.mListView = mListView;
        this.mClickListener = mClickListener;
    }

    public void setLocked (boolean locked) {
        isLocked = locked;
    }

    public void removeEchipa (EchipaDTO e) { mEchipeList.remove(e); }

    @Override
    public int getCount() {
        return mEchipeList != null ? mEchipeList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mEchipeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mEchipeList.get(i).getId();
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    public void click () {
        canClick = true;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.echipa_item, null);
        TextView numeEchipa = (TextView) v.findViewById(R.id.textView16);
        final Button done = (Button) v.findViewById(R.id.button7);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.relativeLayout1);
        if (v != null) {
            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            view.setBackgroundColor(Color.LTGRAY);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            view.setBackgroundColor(Color.WHITE);
                            view.performClick();
                            break;
                        }
                        case MotionEvent.ACTION_CANCEL:{
                            view.setBackgroundColor(Color.WHITE);
                            break;
                        }
                        //default: view.setBackgroundColor(Color.WHITE);
                    }
                    return true;
                }
            });
            v.setOnClickListener(mClickListener);
        }
        numeEchipa.setText(mEchipeList.get(i).getNumeEchipa());

        if (isLocked) {
            done.setVisibility(View.INVISIBLE);
        } else {
            layout.setAlpha(0);
            done.setOnTouchListener(new ButtonTouchListener(activity, layout, mListView));
        }

        if (activitate.getActivitateDTO() != null) {
            done.setText("Done!");
        } else {
            if (activitate.getJocDTO().isAllowsAnimatorPrincipal()) {
                if (Controller.getInstance().getUser().getUsername().equalsIgnoreCase(activitate.getJocDTO().getAnimatori().get(0).getNumeAnimator())){
                    done.setText("Joaca!");
                } else {
                    done.setText("Done!");
                }
            } else {
                done.setText("Joaca!");
            }
        }
        done.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // de aici voi trimite echipa la o activitate terminata
                        if (!canClick) return;
                        canClick = false;

                        if (done.getText() == "Joaca!") {
                            if (activitate.getJocDTO().getFormulas().get(mEchipeList.get(i)) == null) {
                                if (( (Activitate) Controller.getInstance().getAdapterActivitati().getItem(activitate.getListItem())).removeEchipa(mEchipeList.get(i))) { // daca s-a putut sterge echipa si din lista principala
                                    activitate.removeEchipa(mEchipeList.get(i));                                                                                          // atunci o putem elimina
                                    Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();
                                    //mEchipeList.remove(i);
                                    notifyDataSetChanged();
                                }
                                new AlertDialog.Builder(mContext)
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .setTitle("Ops")
                                        .setMessage("Se pare ca echipa a fost eliminata din program de catre coordonator...")
                                        .setPositiveButton("Ok", null)
                                        .show();
                                return;
                            }
                            Controller.getInstance().showLoadingScreenDelayed(0);
                            Intent intent = new Intent (activity, GameActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("echipa", mEchipeList.get(i));
                            bundle.putSerializable("formula",
                                    activitate.getJocDTO().getFormulas().get(mEchipeList.get(i))
                            );
                            bundle.putSerializable("joc", activitate.getJocDTO());
                            intent.putExtras(bundle);
                            //((AppCompatActivity) activity).startActivityForResult(intent, Activity.RESULT_OK);

                            ((AppCompatActivity) activity).startActivityForResult(intent, 1);

                        } else if (( (Activitate) Controller.getInstance().getAdapterActivitati().getItem(activitate.getListItem())).removeEchipa(mEchipeList.get(i))) { // daca s-a putut sterge echipa si din lista principala

                            Activitate copy = new Activitate(
                                    MainContent.copyActivitate(activitate.getActivitateDTO()),
                                    MainContent.copyJoc(activitate.getJocDTO()),
                                    new ArrayList<EchipaDTO>()
                            );
                            Controller.getInstance().getAdapterActivitatiComplete().addEchipa(
                                    mEchipeList.get(i),
                                    copy
                            );
                            activitate.removeEchipa(mEchipeList.get(i));                                                                                          // atunci o putem elimina
                            Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();

                            //mEchipeList.remove(i);
                            notifyDataSetChanged();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Controller.getInstance().salveazaActivitati();
                                }
                            }).start();
                        }
                    }

                }
        );

        Animation animation = AnimationUtils.loadAnimation(
                mContext, R.anim.slide_from_bottom_to_top
        );
        animation.setDuration(500);
        v.startAnimation(animation);

        return v;
    }

    private int step;
    private final static int TOTAL_STEPS = 2;

    public void showTutorial (AppCompatActivity activity) {
        makeTutorial(activity);
    }

    private void makeTutorial (AppCompatActivity activity) {
        step = 0;

        int pos = mListView.getFirstVisiblePosition();
        View view = mListView.getChildAt(pos);
        viewTarget = new ViewTarget(view);
        buttonTarget = new ViewTarget(R.id.button7, activity);

        showcase = new ShowcaseView.Builder(activity)
                .hideOnTouchOutside()
                .setTarget(viewTarget)
                .setContentTitle("Echipa")
                .setContentText("Aceasta este una dintre echipele participante la activitatea selectata. Apasa pentru a vedea mai mult.")
                .setStyle(R.style.TransparentStyle)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performClick(view);
                    }
                })
                .build();

        showcase.setButtonText("Urmatorul");

        if (isLocked) showcase.setButtonText("Gata");
        showcase.show();
    }

    private void performClick (View view) {
        switch (step) {
            case 0 : break;
            case 1 :
                if (!isLocked) {
                    showcase.setShowcase(buttonTarget, true);
                    showcase.setContentTitle("Actiune");
                    showcase.setContentText("Gliseaza butonul spre stanga pentru a initia activitatea cu echipa selectata.");
                    showcase.setButtonText("Gata");
                } else {
                    showcase.hide();
                }
                break;
            default:
                showcase.hide();
        }
        step++;
    }

}
