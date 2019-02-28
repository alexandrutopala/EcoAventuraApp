package gui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.ListView;

import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;

import java.util.List;

import dto.EchipaDTO;
import dto.Message;
import floatingWindow.FloatingWindow;
import receiver.DataListener;

public class EchipeContent extends AppCompatActivity{
    private ListView lvEchipe;
    private SearchView search;
    private ArrayAdapter<EchipaDTO> adapterEchipe;
    private List<EchipaDTO> echipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echipe_content);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Echipe");
        Controller.getInstance().setCurrentActivity(this);
        echipe = (List<EchipaDTO>) getIntent().getExtras().getSerializable("echipe");

        lvEchipe = (ListView) findViewById(R.id.listView4);
        search = (SearchView) findViewById(R.id.searchView2);

        adapterEchipe = new ArrayAdapter<EchipaDTO>(this, android.R.layout.simple_list_item_1, echipe);
        lvEchipe.setAdapter(adapterEchipe);

        search.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapterEchipe.getFilter().filter(s);
                        return false;
                    }
                }
        );

        lvEchipe.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(EchipeContent.this, InformatiiMembru.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("echipa", adapterEchipe.getItem(i));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Controller.getInstance().setCurrentActivity(this);

        if (FloatingWindow.isLoadingScreenVisible()) {
            Controller.getInstance().hideLoadingScreenDelayed(750);
        }
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
