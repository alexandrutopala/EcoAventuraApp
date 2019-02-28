package gui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;


import java.util.ArrayList;
import java.util.List;

import dto.MembruEchipaDTO;
import dto.Message;
import floatingWindow.FloatingWindow;
import receiver.DataListener;

public class MembriContent extends AppCompatActivity {
    private ListView lvMembri;
    private SearchView search;
    private List<MembruEchipaDTO> membriList;
    private ArrayAdapter<MembruEchipaDTO> membriAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membri_content);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Membri");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        membriList = (List<MembruEchipaDTO>) getIntent().getExtras().getSerializable("membri");

        Controller.getInstance().setCurrentActivity(this);
        lvMembri = (ListView) findViewById(R.id.listView3);
        search = (SearchView) findViewById(R.id.searchView);

        membriAdapter = new ArrayAdapter<MembruEchipaDTO>(this,
                android.R.layout.simple_list_item_1, membriList);

        lvMembri.setAdapter(membriAdapter);

        search.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        membriAdapter.getFilter().filter(newText);
                        return false;
                    }
                }
        );

        lvMembri.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MembriContent.this, InformatiiMembru.class);
                        Bundle bundle = new Bundle () ;
                        bundle.putSerializable("membru", membriAdapter.getItem(i));
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

    /*
    private class MyFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            ArrayList<String> f = new ArrayList<String>();
            ArrayList<String> p = new ArrayList<String>();
            for(String product : membriList){
                p.add(product);
            }
            if(constraint != null && constraint.toString().length() > 0)
            {
                for(int i = 0; i < p.size(); i++)
                {
                    String product = p.get(i);
                    if(product.toLowerCase().contains(constraint))
                        f.add(product);
                }
                result.count = f.size();
                result.values = f;
            }
            else
            {
                result.values = p;
                result.count = p.size();
            }
            return result;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            yourFilteredData = (ArrayList<String>)results.values;
            notifyDataSetChanged();
        }

    }*/

}
