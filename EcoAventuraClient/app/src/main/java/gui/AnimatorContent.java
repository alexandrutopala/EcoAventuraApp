package gui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.connection.simpleclient.Activitate;
import com.connection.simpleclient.Controller;
import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapters.ActivitatiListAdapter;
import dto.ActivitateDTO;
import dto.AnimatorDTO;
import dto.JocDTO;
import dto.Message;
import floatingWindow.FloatingWindow;
import receiver.DataListener;

public class AnimatorContent extends AppCompatActivity {
    private AnimatorDTO animator;
    private List<Activitate> mActivitateList;
    private ActivitatiListAdapter activitatiListAdapter;
    private ListView lvActivitati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_content);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Controller.getInstance().setCurrentActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvActivitati = (ListView) findViewById(R.id.listView2);
        View emptyView = View.inflate(this, R.layout.empty_view, null);
        emptyView.setVisibility(View.GONE);
        emptyView.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.FILL_PARENT));

        lvActivitati.setEmptyView(emptyView);

        animator = (AnimatorDTO) getIntent().getExtras().getSerializable("animator");
        mActivitateList = (List<Activitate>) getIntent().getExtras().getSerializable("activitati");

        setTitle(animator.getNumeAnimator());

        if (animator.getSarcini() != null) { // daca activitatile animatorului a fost repartizate separat
            mActivitateList = new ArrayList<>();
            for (Object o : animator.getSarcini()) {
                if (o.getClass() == ActivitateDTO.class) {
                    ((ArrayList<Activitate>) mActivitateList).add(new Activitate((ActivitateDTO) o, null, ((ActivitateDTO) o).getEchipe()));
                } else {
                    ((ArrayList<Activitate>) mActivitateList).add(new Activitate(null, (JocDTO) o, ((JocDTO) o).getEchipe()));
                }
            }
        } else {
            Collections.sort(mActivitateList, new Comparator<Activitate>() {
                @Override
                public int compare(Activitate activitate, Activitate t1) {
                    int ord1 = activitate.getActivitateDTO() != null ? activitate.getActivitateDTO().getOrdin() : activitate.getJocDTO().getOrdin();
                    int ord2 = t1.getActivitateDTO() != null ? t1.getActivitateDTO().getOrdin() : t1.getJocDTO().getOrdin();
                    return (ord1 - ord2);
                }
            });
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Controller.getInstance().showLoadingScreenDelayed(0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int i = lvActivitati.getPositionForView(view);
                        Intent intent = new Intent(AnimatorContent.this, ViewActivitate.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("activitate", mActivitateList.get(i));
                        bundle.putSerializable("lock", true);
                        bundle.putSerializable("modVizualizare", true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }, 500);

            }
        };

        View.OnTouchListener touchListener = new View.OnTouchListener() {
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
        };

        activitatiListAdapter = new ActivitatiListAdapter(MyApplication.getMyApplicationContext(), mActivitateList, null, null,
                clickListener, null, lvActivitati, touchListener);
        activitatiListAdapter.lockContent(true);

        activitatiListAdapter.refresh();
        lvActivitati.setAdapter(activitatiListAdapter);
        activitatiListAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FloatingWindow.isLoadingScreenVisible()) {
            Controller.getInstance().hideLoadingScreenDelayed(500);
        }
        Controller.getInstance().setCurrentActivity(this);
        try {activitatiListAdapter.refresh(); } catch (Exception e) {}
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
