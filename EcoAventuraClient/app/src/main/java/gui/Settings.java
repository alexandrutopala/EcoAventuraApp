package gui;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.connection.simpleclient.Controller;
import com.connection.simpleclient.MyApplication;
import com.connection.simpleclient.R;

import adapters.SetariAdapter;
import dto.Message;
import floatingWindow.FloatingWindow;
import receiver.DataListener;

public class Settings extends AppCompatActivity implements DataListener {
    private ListView settingsList;
    private SetariAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Setari");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Controller.getInstance().setCurrentActivity(this);

        if (Controller.getInstance().getReceiver() != null) {
            Controller.getInstance().getReceiver().addListener(this);
        }

        settingsList = (ListView) findViewById(R.id.listView5);

        adapter = new SetariAdapter(MyApplication.getMyApplicationContext());
        settingsList.setAdapter(adapter);
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
    protected void onDestroy () {
        super.onDestroy();

        if (Controller.getInstance().getReceiver() != null) {
            Controller.getInstance().getReceiver().removeListener(this);
        }
    }

    @Override
    public void update(Object... o) {
        Message m;
        if (o[0] instanceof Message) {
            m = (Message) o[0];
        } else return;

        switch (m) {
            case CONFIRM_CONNECTION:
                adapter.notifyDataSetChanged();
                break;
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
