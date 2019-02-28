package gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.connection.simpleclient.Activitate;
import com.connection.simpleclient.ClassIDs;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import adapters.EchipeListAdapter;
import dto.AnimatorDTO;
import dto.EchipaDTO;
import dto.Message;
import floatingWindow.FloatingWindow;
import formula.Formula;
import receiver.DataListener;

public class ViewActivitate extends AppCompatActivity {
    private final static int ID_ITEM_DESCRIERE = 69;
    private EchipeListAdapter adapter;
    private Activitate activitate;
    private TextView activitateView;
    private TextView perioadaView;
    private TextView locatieView;
    private TextView postView;
    private TextView animatoriView;
    private TextView detaliiView;
    private TextView perioadaLabel;
    private TextView locatieLabel;
    private TextView hint;
    private Button doneBut;
    private ListView lvEchipe;
    private boolean isLocked = false;
    private boolean echipeIntegrale = false;
    private View.OnClickListener mClickListener;

    //ajutor

    private ShowcaseView showcase;
    private Target doneTarget;
    private static boolean madeTutorial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activitate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("InfoActivitate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Controller.getInstance().setCurrentActivity(this);
        activitateView = (TextView) findViewById(R.id.textView5);
        perioadaView = (TextView) findViewById(R.id.textView7);
        locatieView = (TextView) findViewById(R.id.textView9);
        postView = (TextView) findViewById(R.id.textView11);
        animatoriView = (TextView) findViewById(R.id.textView13);
        detaliiView = (TextView) findViewById(R.id.textView15);
        doneBut = (Button) findViewById(R.id.button6);
        lvEchipe = (ListView) findViewById(R.id.listView);
        hint = (TextView) findViewById(R.id.textView38);

        perioadaLabel = (TextView) findViewById(R.id.textView6);
        locatieLabel = (TextView) findViewById(R.id.textView8);

        hint.setVisibility(View.GONE);
        hint.setText("* Doar acest animator va putea puncta jocul");

        activitate = (Activitate) getIntent().getExtras().getSerializable("activitate");

        try {
            isLocked = (boolean) getIntent().getExtras().getSerializable("lock");
        } catch (Exception e) {}

        try {
            echipeIntegrale = (boolean) getIntent().getExtras().getSerializable("modVizualizare");
        } catch (Exception e) {}

        if (activitate == null) {
            finish();
        }

        if (isLocked) {
            doneBut.setVisibility(View.INVISIBLE);
        }

        if (activitate.getJocDTO() != null) {
            if (activitate.getJocDTO().getJocGeneral().toString().equalsIgnoreCase("penalizare")) {
                perioadaLabel.setText("Echipa : ");
                locatieLabel.setText("Penalizare : ");
            }
        }

        if (activitate.getActivitateDTO() != null) {
            if (activitate.getActivitateDTO().getDetalii() == null) activitate.getActivitateDTO().setDetalii("");
            if (activitate.getActivitateDTO().getLocatie() == null) activitate.getActivitateDTO().setLocatie("");
            if (activitate.getActivitateDTO().getPerioada() == null) activitate.getActivitateDTO().setPerioada("");
            if (activitate.getActivitateDTO().getPost() == null) activitate.getActivitateDTO().setPost("");
        } else {
            if (activitate.getJocDTO().getDetalii() == null) activitate.getJocDTO().setDetalii("");
            if (activitate.getJocDTO().getLocatie() == null) activitate.getJocDTO().setLocatie("");
            if (activitate.getJocDTO().getPerioada() == null) activitate.getJocDTO().setPerioada("");
            if (activitate.getJocDTO().getPost() == null) activitate.getJocDTO().setPost("");
        }

        activitateView.setText(
                activitate.getActivitateDTO() != null ?
                        activitate.getActivitateDTO().getActivitateGenerala().getNumeActivitateGenerala() :
                        activitate.getJocDTO().getJocGeneral().getNumeJocGeneral());

        perioadaView.setText(
                activitate.getActivitateDTO() != null ?
                        activitate.getActivitateDTO().getPerioada() :
                        activitate.getJocDTO().getPerioada()
        );

        locatieView.setText(
                activitate.getActivitateDTO() != null ?
                        (activitate.getActivitateDTO().getLocatie().equals("") ? "N/A" : activitate.getActivitateDTO().getLocatie() ):
                        (activitate.getJocDTO().getLocatie().equals("") ? "N/A" : activitate.getJocDTO().getLocatie())
        );

        postView.setText(
                activitate.getActivitateDTO() != null ?
                        (activitate.getActivitateDTO().getPost().equals("") ? "N/A" : activitate.getActivitateDTO().getPost()) :
                        (activitate.getJocDTO().getPost().equals("") ? "N/A" : activitate.getJocDTO().getPost())
        );

        detaliiView.setText(
                activitate.getActivitateDTO() != null ?
                        (activitate.getActivitateDTO().getDetalii().equals("") ? "N/A" : activitate.getActivitateDTO().getDetalii()) :
                        (activitate.getJocDTO().getDetalii().equals("") ? "N/A" : activitate.getJocDTO().getDetalii())
        );

        String s = "";

        if (activitate.getActivitateDTO() != null) {
            for (AnimatorDTO a : activitate.getActivitateDTO().getAnimatori()){
                s += a + " ";
            }
        } else {
            for (AnimatorDTO a : activitate.getJocDTO().getAnimatori()){
                s += a;
                if (activitate.getJocDTO().isAllowsAnimatorPrincipal() && activitate.getJocDTO().getAnimatori().indexOf(a) == 0) {
                    s += "* ";
                    if (hint.getVisibility() == View.GONE){
                        hint.setVisibility(View.VISIBLE);
                    }
                } else {
                    s += " ";
                }
            }
        }

        animatoriView.setText(s);

        List<EchipaDTO> echipe = null;


        if (activitate.getActivitateDTO() != null) {
            echipe = activitate.getActivitateDTO().getEchipe();
        } else if (activitate.getJocDTO() != null) {
            echipe = activitate.getJocDTO().getEchipe();
        }

        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                int i = lvEchipe.getPositionForView(view);

                if (i == ListView.INVALID_POSITION) return;

                Intent intent = new Intent(ViewActivitate.this, InformatiiMembru.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("echipa", (EchipaDTO) adapter.getItem(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };

        adapter = new EchipeListAdapter(this, echipeIntegrale ? activitate.getECHIPE() : echipe, this.activitate, this, lvEchipe, mClickListener);
        adapter.setLocked(isLocked);

        lvEchipe.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        /* // am inlaturat eliminarea in ordinea din lista
        if (activitate.isDone() || activitate.getListItem() != 0){
            doneBut.setEnabled(false);
        } else {
            doneBut.setEnabled(true);
        }
        */
        
        doneBut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (activitate.getActivitateDTO() != null)
                            if (activitate.getActivitateDTO().getEchipe().isEmpty()) {
                                activitate.setDone(true);
                                //Controller.getInstance().getAdapterActivitatiComplete().addElement(activitate);
                                Controller.getInstance().getAdapterActivitati().removeElement(activitate);
                                Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();
                                finish();
                            } else {
                                if (activitate.getJocDTO() != null && activitate.getJocDTO().getEchipe().isEmpty()) {
                                    activitate.setDone(true);
                                    //Controller.getInstance().getAdapterActivitatiComplete().addElement(activitate);
                                    Controller.getInstance().getAdapterActivitati().removeElement(activitate);
                                    Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();
                                } else {
                                    Toast.makeText(Controller.getInstance().getApplicationContext() , "Toate echipele trebuie sa termine activitatea inainte de a fi eliminata", Toast.LENGTH_SHORT).show();
                                }
                            }
                        else if (activitate.getJocDTO() != null)
                            if (activitate.getJocDTO().getEchipe().isEmpty()) {
                                activitate.setDone(true);
                                //Controller.getInstance().getAdapterActivitatiComplete().addElement(activitate);
                                Controller.getInstance().getAdapterActivitati().removeElement(activitate);
                                Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();
                                finish();
                            } else {
                                Toast.makeText(Controller.getInstance().getApplicationContext() , "Toate echipele trebuie sa termine activitatea inainte de a fi eliminata", Toast.LENGTH_SHORT).show();
                            }
                        else {
                            Toast.makeText(Controller.getInstance().getApplicationContext() , "Toate echipele trebuie sa termine activitatea inainte de a fi eliminata", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        if (activitate.getJocDTO() != null) {
            if (activitate.getJocDTO().getJocGeneral().getNumeJocGeneral().equals("penalizare")) {
                //lvEchipe.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu, menu);
        if (activitate.getJocDTO() != null) {
            menu.add(Menu.NONE, ID_ITEM_DESCRIERE, Menu.FIRST, "Descriere");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1 : { // ajutor
                makeTutorial();
                break;
            }
            case ID_ITEM_DESCRIERE : {
                AlertDialog.Builder builder = new AlertDialog.Builder (ViewActivitate.this)
                        .setTitle("Descriere")
                        .setMessage(activitate.getJocDTO().getJocGeneral().getDescriereJoc())
                        .setPositiveButton("AM INTELES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                builder.create().show();
                break;
            }
            case android.R.id.home : {
                finish();
                break;
            }
            case R.id.idProgram: {
                Controller.getInstance().display("ID: " + Controller.getInstance().getProgramID(this));
            }
            default: super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Formula f = (Formula) data.getExtras().getSerializable("formula");
            EchipaDTO e = (EchipaDTO) data.getExtras().getSerializable("echipa");
            boolean b = (Boolean) data.getExtras().getSerializable("absent");
            activitate.getJocDTO().getFormulas().put(e, f);

            if (b) {
                activitate.getJocDTO().addEchipaAbsenta(e);
            }

            if (( (Activitate) Controller.getInstance().getAdapterActivitati().getItem(activitate.getListItem())).removeEchipa(e)) {
                Activitate copy = new Activitate(
                        MainContent.copyActivitate(activitate.getActivitateDTO()),
                        MainContent.copyJoc(activitate.getJocDTO()),
                        new ArrayList<EchipaDTO>()
                );
                Controller.getInstance().getAdapterActivitatiComplete().addEchipa(
                        e,
                        copy
                );

                activitate.removeEchipa(e);                                  // atunci o putem elimina
                Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();
                //mEchipeList.remove(i);
                adapter.notifyDataSetChanged();
            } else {
                Controller.getInstance().getAdapterActivitati().notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Controller.getInstance().salveazaActivitati();
                }
            }).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkResources()) {
            finish();
            return;
        }

        Controller.getInstance().setCurrentActivity(this);

        if (FloatingWindow.isLoadingScreenVisible()) {
            Controller.getInstance().hideLoadingScreenDelayed(750);
        }

        if (!madeTutorial) {
            boolean madeTutorialBefore = Controller.getInstance().wasActivityUsedBefore(ClassIDs.VIEW_ACTIVITATE, this);
            if (!madeTutorialBefore) {
                Controller.getInstance().setFirstUseActivity(ClassIDs.VIEW_ACTIVITATE, true, this);
                makeTutorial();
            }
            madeTutorial = true;
        }
    }


    private boolean checkResources () {
        if (activitate == null) {
            return false;
        }
        return true;
    }

    private int step;

    private void makeTutorial () {
        step = 0;
        doneTarget = new ViewTarget(R.id.button6, this);

        showcase = new ShowcaseView.Builder(this)
                .setStyle(R.style.TransparentStyle)
                .setContentTitle("Informatii activitate")
                .setContentText("Aici poti vedea toate informatiile privitoare la activitatea selectata.")
                .hideOnTouchOutside()
                .setTarget(Target.NONE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (step == 0 && !isLocked) {
                            showcase.setShowcase(doneTarget, true);
                            showcase.setContentTitle("Buton Finalizare");
                            showcase.setContentText("Apasa pentru a finaliza rapid activitatea");
                            step++;
                        } else {
                            showcase.hide();
                            if (adapter != null)
                                if (adapter.getCount() > 0)
                                    adapter.showTutorial(ViewActivitate.this);
                        }
                        if (isLocked) step++;
                    }
                })
                .build();
        showcase.setButtonText("Urmatorul");
        showcase.show();
    }

}
