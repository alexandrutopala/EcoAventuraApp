package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.connection.simpleclient.Activitate;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;
import com.connection.simpleclient.SecondTouchListener;
import com.connection.simpleclient.TouchListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import dto.ActivitateDTO;
import dto.EchipaDTO;
import dto.JocDTO;

/**
 * Created by Alexandru on 9/17/2016.
 */
public class ActivitatiCompleteListAdaptor extends MyBaseAdapter {
    final private List<Activitate> mActivitatiCompleteList;
    final private Context mContext;
    private View.OnClickListener clickListener;
    private SecondTouchListener mTouchListener;
    private boolean sendAllActivitati = false;
    final private ListView mListView;
    private boolean sendOneActivitate = false;
    private View.OnLongClickListener longClickListener;
    private boolean isOneDialogOpen = false;


    // ajutor
    private ShowcaseView showcase;
    private Target viewTarget;
    private Target emblemaTarget;
    private Target colorTarget;

    public ActivitatiCompleteListAdaptor (final Context mContext, List<Activitate> mActivitatiCompleteList,
                                          View.OnClickListener clickListener,
                                          SecondTouchListener mTouchListener,
                                          final ListView mListView){
        this.mContext = mContext;
        this.mActivitatiCompleteList = (mActivitatiCompleteList != null ? mActivitatiCompleteList : new ArrayList<Activitate>());
        this.clickListener = clickListener;
        this.mTouchListener = mTouchListener;
        this.mListView = mListView;

        mListView.addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                        if (sendAllActivitati) {
                            Vibrator v = (Vibrator) ActivitatiCompleteListAdaptor.this.mContext.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(100);
                        }
                        sendAllActivitati = false;
                        sendOneActivitate = false;
                    }
                }
        );

        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                if (isOneDialogOpen) return false;
                TextView title = (TextView) view.findViewById(R.id.textView2);
                if (title.getText().toString().equalsIgnoreCase("penalizare")) {
                    isOneDialogOpen = true;
                    new AlertDialog.Builder(mContext)
                            .setTitle("Confirmare")
                            .setMessage("Sigur doresti sa stergi aceasta penalizare?")
                            .setNegativeButton("TOTUSI NU", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    isOneDialogOpen = false;
                                }
                            })
                            .setPositiveButton("SIGUR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    isOneDialogOpen = false;
                                    int pos = mListView.getPositionForView(view);
                                    ActivitatiCompleteListAdaptor.this.mActivitatiCompleteList.remove(pos);
                                    if (pos != ListView.INVALID_POSITION) {
                                        notifyDataSetChanged();
                                    }
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    isOneDialogOpen = false;
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    isOneDialogOpen = false;
                                }
                            })
                            .show();
                }
                return false;
            }
        };

    }

    public ArrayList<Activitate> getmActivitatiCompleteList () {
        return (ArrayList<Activitate>) this.mActivitatiCompleteList;
    }

    public void addEchipa (EchipaDTO e, Activitate a) {
        Activitate existent = findActivitate(a);

        if (existent != null) {
            existent.getEchipe().add(e);
            existent.getECHIPE().add(e);
            existent.setSent(false);
            if (a.getJocDTO() != null) {
                //existent.getJocDTO().setFormulas(a.getJocDTO().getFormulas());
                //existent.getJocDTO().setEchipeAbsente(a.getJocDTO().getEchipeAbsente());
                existent.getJocDTO().getFormulas().put(e, a.getJocDTO().getFormulas().get(e));
                if (a.getJocDTO().getEchipeAbsente().contains(e)) {
                    existent.getJocDTO().getEchipeAbsente().add(e);
                }
            }
            notifyDataSetChanged();
            return;
        }

        a.setEchipe(new ArrayList<EchipaDTO>());
        a.getEchipe().add(e);
        a.getECHIPE().add(e);
        a.setSent(false);
        mActivitatiCompleteList.add(a);
        notifyDataSetChanged();
    }

    public void addElement (Activitate a) {
        if (a.getJocDTO() != null) {
            if (a.getJocDTO().getJocGeneral().getNumeJocGeneral().equals("penalizare")){
                mActivitatiCompleteList.add(a);
                return;
            }
        }
        for (int i = 0; i < mActivitatiCompleteList.size(); ++i) {
            Activitate a2 = mActivitatiCompleteList.get(i);
            if (a.getActivitateDTO() != null && a2.getActivitateDTO() != null &&
                a.getActivitateDTO().getPerioada().equalsIgnoreCase(a2.getActivitateDTO().getPerioada()) &&
                a.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala().equalsIgnoreCase(a2.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala())){

                return;

            } else if (a.getJocDTO() != null && a2.getJocDTO() != null &&
                    a.getJocDTO().getPerioada().equalsIgnoreCase(a.getJocDTO().getPerioada()) &&
                    a.getJocDTO().getJocGeneral().getNumeJocGeneral().equalsIgnoreCase(a2.getJocDTO().getJocGeneral().getNumeJocGeneral())) {

                return;

            }
        }

        mActivitatiCompleteList.add(a);
    }

    public Activitate findActivitate (Activitate a) {
        for (int i = 0; i < mActivitatiCompleteList.size(); ++i) {
            Activitate a2 = mActivitatiCompleteList.get(i);
            if (a.getActivitateDTO() != null && a2.getActivitateDTO() != null &&
                    a.getActivitateDTO().getPerioada().equalsIgnoreCase(a2.getActivitateDTO().getPerioada()) &&
                    a.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala().equalsIgnoreCase(a2.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala())){

                return a2;

            } else if (a.getJocDTO() != null && a2.getJocDTO() != null &&
                    a.getJocDTO().getPerioada().equalsIgnoreCase(a2.getJocDTO().getPerioada()) &&
                    a.getJocDTO().getJocGeneral().getNumeJocGeneral().equalsIgnoreCase(a2.getJocDTO().getJocGeneral().getNumeJocGeneral())) {

                return a2;

            }
        }
        return null;
    }

    public boolean removeElement (Activitate a) {
        try {
            return mActivitatiCompleteList.remove(a);
        } catch (Exception e) {return false;}
    }

    public void notifySendOneActivitate () {
        sendOneActivitate = true;
        sendAllActivitati = false;
    }

    @Override
    public int getCount() {
        return mActivitatiCompleteList.size();
    }

    @Override
    public Object getItem(int i) {
        return mActivitatiCompleteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mActivitatiCompleteList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.item_activitate_list, null);

        // add events
        v.setOnClickListener(clickListener);
        v.setOnTouchListener(mTouchListener);

        TextView t1 = (TextView) v.findViewById(R.id.userName);
        TextView t2 = (TextView) v.findViewById(R.id.textView2);
        TextView t3 = (TextView) v.findViewById(R.id.textView3);
        TextView t4 = (TextView) v.findViewById(R.id.textView4);
        TextView label = (TextView) v.findViewById(R.id.textView41);

        final TextView nrEchipe = (TextView) v.findViewById(R.id.textView54);

        try {
            nrEchipe.setVisibility(View.INVISIBLE);
            nrEchipe.setText("(" + mActivitatiCompleteList.get(i).getECHIPE().size() + ")");
            nrEchipe.setVisibility(View.VISIBLE);
        } catch (Exception e) {}

        //t2.setWidth((int) (b.getX() - t2.getX() - 10));
        //t3.setWidth((int) (b.getX() - t3.getX() - 10));
        //t4.setWidth((int) (b.getX() - t4.getX() - 10));

        ActivitateDTO activitateDTO = mActivitatiCompleteList.get(i).getActivitateDTO();
        JocDTO jocDTO = mActivitatiCompleteList.get(i).getJocDTO();
        t1.setText("");
        t1.setBackgroundColor(Color.WHITE);

        if (mActivitatiCompleteList.get(i).isDone()){
            t1.setBackgroundColor(Controller.getInstance().getColor(8));
        }

        if (mActivitatiCompleteList.get(i).isSent()) {
            t1.setBackgroundColor(Controller.getInstance().getColor(8));
        }

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
                v.setOnLongClickListener(longClickListener);
                label.setText("P");
            }
        }

        v.setTag(mActivitatiCompleteList.get(i).getId());

        if (!sendAllActivitati && ! sendOneActivitate) {
            Animation animation = AnimationUtils.loadAnimation(
                    mContext, R.anim.slide_from_bottom_to_top
            );
            v.startAnimation(animation);
        } else if (!sendOneActivitate){
            v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.send_animation));
        }

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.circle_label);
        drawable.setColorFilter(Controller.getInstance().getColor(i),
                PorterDuff.Mode.MULTIPLY);
        label.setBackground(drawable);
        label.setTextColor(Color.WHITE);

        return v;
    }

    public int getToBeSendItemsCount() {
        int counter = 0;
        for (Activitate a : mActivitatiCompleteList) {
            if (!a.isSent()){
                counter++;
            }
        }
        return counter;
    }

    public void markAllAsSent () {
        for (Activitate a : mActivitatiCompleteList){
            a.setSent(true);
        }

        sendAllActivitati = true;
        sendOneActivitate = false;
        notifyDataSetChanged();
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
        colorTarget = new ViewTarget(R.id.userName, activity);

        showcase = new ShowcaseView.Builder(activity)
                .setStyle(R.style.TransparentStyle)
                .hideOnTouchOutside()
                .setTarget(viewTarget)
                .setContentTitle("Activitate")
                .setContentText("Aceasta este o activitate finalizata. Pentru a o trimite, gliseaz-o in lateral. Apasa pe ea pentru a " +
                        "vizualiza mai multe detalii.")
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
                showcase.setContentText("In cazul in care vrei sa retrimiti o activitate, gliseaz-o in lateral si apasa butonul 'RETRIMITE'");
                break;
            case 1 :
                showcase.setContentText("Penalizarile vor fi tratate putin diferit : \n" +
                        " - daca o vei retrimite, aceasta isi va dubla punctajul fata de cel actual\n" +
                        " - o poti sterge tinand apasat pe penalizarea respectiva");
                break;
            case 2 :
                showcase.setContentText("Activitatea va putea fi trimisa numai in modul Online");
                break;
            case 3 :
                showcase.setShowcase(emblemaTarget, true);
                showcase.setContentTitle("Emblema");
                showcase.setContentText("Semnifica tipul de sarcina : \nA - activitate\nJ - joc\nP - penalizare");
                break;
            case 4 :
                showcase.setShowcase(colorTarget, true);
                showcase.setContentTitle("Marcator");
                showcase.setContentText("Acesta semnifica stadiul activitatii: \nalb - in asteptare\nverde - trimisa cu succes");
                showcase.setButtonText("Gata");
                break;
            default:
                showcase.hide();
        }
        step++;
    }
}
