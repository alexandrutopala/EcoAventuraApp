package gui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.connection.simpleclient.Controller;
import com.connection.simpleclient.R;

import org.apache.commons.io.IOUtils;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import email.GMailSender;

public class Report extends AppCompatActivity {

    private EditText reportMessage;
    private CheckBox agreement;
    private Button send;
    private ProgressDialog progressDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Raporteaza");

        reportMessage = (EditText) findViewById(R.id.editText5);
        agreement = (CheckBox) findViewById(R.id.checkBox);
        send = (Button) findViewById(R.id.button17);

        send.setEnabled(false);

        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (agreement.isChecked()) {
                            sendReport(reportMessage.getText().toString());
                        } else {
                            Controller.getInstance().display("Trebuie sa fii de acord sa trimiti raportul aplicatiei intai");
                        }
                    }
                }
        );

        agreement.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        send.setEnabled(b);
                    }
                }
        );

        handler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        String userMessage = (String) message.getData().getSerializable("message");
                        try {
                            Controller.getInstance().getMailSender().setOnDismissListener(new ProgressDialog.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Report.this.finish();
                                }
                            });
                            Controller.getInstance().getMailSender().sendMail(
                                    "REPORT " + new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy").format(Calendar.getInstance().getTime()),
                                    userMessage,
                                    GMailSender.CRUSH_LOGGED_MAIL,
                                    GMailSender.CRUSH_LOGGED_MAIL,
                                    Report.this,
                                    "",
                                    "Se trimite raportul aplicatiei...",
                                    true
                            );


                        } catch (Exception e) {
                            e.printStackTrace();
                            Controller.getInstance().display("Raportul nu a putut fi trimis");
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
                showCloseDialog();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        showCloseDialog();
    }

    private void showCloseDialog () {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Sigur doresti sa iesi?")
                .setMessage("Raportul creat nu va fi salvat")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Nu", null)
                .show();
    }

    public void sendReport (final String userMessage) {

        progressDialog = ProgressDialog.show(Report.this, "Se colecteaza datele", "");
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        PackageManager manager = Report.this.getPackageManager();
                        PackageInfo info = null;

                        try {
                            info = manager.getPackageInfo (Report.this.getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e2) {
                        }

                        String model = Build.MODEL;
                        if (!model.startsWith(Build.MANUFACTURER))
                            model = Build.MANUFACTURER + " " + model;

                        String message = "Android version: " +  Build.VERSION.SDK_INT + "\n" +
                            "Device: " + model + "\n" +
                            "App version: " + (info == null ? "(null)" : info.versionCode) + "\n\n***\n";

                        message += userMessage;
                        message += "\n***\n\n";
                        String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                                "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                                "logcat -d -v time";

                        Process process = null;
                        try {
                            process = Runtime.getRuntime().exec(cmd);

                            StringWriter writer = new StringWriter();
                            IOUtils.copy(process.getInputStream(), writer, "UTF-8");
                            message += writer.toString();
                            progressDialog.dismiss();

                            Message mess = handler.obtainMessage();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("message", message);
                            mess.setData(bundle);
                            handler.sendMessage(mess);

                        } catch (Exception e) {
                            e.printStackTrace();

                            if (progressDialog.isShowing()) progressDialog.dismiss();

                            Controller.getInstance().display("Raportul nu a putut fi trimis");
                        }
                    }
                }
        ).start();

    }


}
