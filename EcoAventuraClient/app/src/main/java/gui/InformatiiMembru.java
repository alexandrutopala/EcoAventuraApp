package gui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.connection.simpleclient.Activitate;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;

import java.util.ArrayList;
import java.util.List;

import dto.AnimatorDTO;
import dto.EchipaDTO;
import dto.JocDTO;
import dto.JocGeneralDTO;
import dto.MembruEchipaDTO;
import dto.Message;
import receiver.DataListener;

public class InformatiiMembru extends AppCompatActivity{
    private MembruEchipaDTO membru;
    private EchipaDTO echipa;
    private Button cautaMembru;
    private Button penalizeaza;
    private EditText puncte;
    private int clicksInARow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informatii_membru);
        setTitle("Informatii");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Controller.getInstance().setCurrentActivity(this);
        cautaMembru = (Button) findViewById(R.id.button8);
        penalizeaza = (Button) findViewById(R.id.button13);
        puncte = (EditText) findViewById(R.id.editText10);

        try {

            membru = (MembruEchipaDTO) getIntent().getExtras().getSerializable("membru");
            ((TextView) findViewById(R.id.textView20)).setText(membru.getEchipa().toString());
            ((TextView) findViewById(R.id.textView25)).setText(membru.getNumeMembruEchipa());
            ((TextView) findViewById(R.id.textView26)).setText(membru.getEchipa().getScoalaEchipa());
            ((TextView) findViewById(R.id.textView27)).setText(membru.getEchipa().getProfEhipa());
            ((TextView) findViewById(R.id.textView28)).setText(membru.getEchipa().getCuloareEchipa());
            ((TextView) findViewById(R.id.textView60)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.textView61)).setVisibility(View.INVISIBLE);
            cautaMembru.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            echipa = (EchipaDTO) getIntent().getExtras().getSerializable("echipa");
            ((TextView) findViewById(R.id.textView20)).setText(echipa.toString());
            ((TextView) findViewById(R.id.textView26)).setText(echipa.getScoalaEchipa());
            ((TextView) findViewById(R.id.textView27)).setText(echipa.getProfEhipa());
            ((TextView) findViewById(R.id.textView28)).setText(echipa.getCuloareEchipa());

            ((TextView) findViewById(R.id.textView25)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.textView21)).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.textView61)).setText(echipa.getMembriEchipa().size() + "");
        }

        cautaMembru.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(InformatiiMembru.this, MembriContent.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("membri", (ArrayList<MembruEchipaDTO>) echipa.getMembriEchipa());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
        );

        penalizeaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicksInARow++;
                if (clicksInARow >= 2) {
                    Controller.getInstance().display("Tine apasat pentru a penaliza");
                }
            }
        });

        penalizeaza.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        clicksInARow = 0;
                        try {
                            if (Integer.parseInt(String.valueOf(puncte.getText())) == 0){
                                return false;
                            }

                            JocGeneralDTO jg = new JocGeneralDTO();
                            jg.setIdJocGeneral(1);
                            jg.setNumeJocGeneral("penalizare");
                            JocDTO joc = new JocDTO();
                            joc.setJocGeneral(jg);
                            List<EchipaDTO> echipe = new ArrayList<EchipaDTO>();
                            List<AnimatorDTO> animatori = new ArrayList<AnimatorDTO>();
                            echipe.add(echipa != null ? echipa : membru.getEchipa());
                            joc.setEchipe(echipe);

                            AnimatorDTO animatorDTO = new AnimatorDTO();
                            animatorDTO.setDisponibilAnimator(true);
                            try { animatorDTO.setNumeAnimator(Controller.getInstance().getUser().getUsername()); } catch (Exception e){return false;}
                            animatori.add(animatorDTO);
                            joc.setAnimatori(animatori);

                            joc.setPunctaj(Integer.parseInt(String.valueOf(puncte.getText())));
                            joc.setAllowsAnimatorPrincipal(false);
                            joc.setDetalii("Penalizare : " + joc.getPunctaj());
                            joc.setLocatie(joc.getPunctaj() + "");
                            joc.setPerioada(echipe.get(0).getNumeEchipa());
                            joc.setPost("");

                            Activitate activitate = new Activitate(null, joc, joc.getEchipe());
                            Controller.getInstance().getAdapterActivitatiComplete().addElement(activitate);
                            Controller.getInstance().getAdapterActivitatiComplete().notifyDataSetChanged();
                            puncte.setText("");

                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            Controller.getInstance().display("Penalizat!");

                        } catch (Exception e) {
                            Controller.getInstance().display("Penalizarea nu a putut fi inregistrata");
                        }
                        return false;
                    }

                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        Controller.getInstance().setCurrentActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
