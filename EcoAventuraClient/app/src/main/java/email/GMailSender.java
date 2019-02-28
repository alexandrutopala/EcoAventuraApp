package email;

/**
 * Created by Alexandru on 16.02.2017.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.connection.simpleclient.Controller;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.HashMap;
import java.util.Properties;

import temporizator.Event;
import temporizator.Temporizator;

import static android.R.id.message;

public class GMailSender extends javax.mail.Authenticator {
    private final static String TIMEOUT = "15000";
    public final static String CRUSH_LOGGED_MAIL = "ecoaventuracrushlogger@gmail.com";
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private ProgressDialog progressDialog;
    private ProgressDialog.OnDismissListener dismissListener;
    private Temporizator temp;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        props.put("mail.smtp.connectiontimeout", TIMEOUT);
        props.put("mail.smtp.timeout", TIMEOUT);

        session = Session.getDefaultInstance(props, this);
    }

    public void setOnDismissListener (ProgressDialog.OnDismissListener listener) {
        this.dismissListener = listener;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    class SendEmailTask extends AsyncTask<String, Void, String> {
        private Context context;

        public SendEmailTask (Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String subject = strings[0];
            String body = strings[1];
            String from = strings[2];
            String to = strings[3];
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setContent(body, "text/plain; charset=utf-8");

                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
                Controller.getInstance().display("Raportul nu a putut fi trimis");
                //Controller.getInstance().saveCrushLog(subject, body, context);
                return null;
            } finally {
            }
            return subject;
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog.setOnDismissListener(null);
            }
            if (s == null) {
                Log.i("mail service", "Raportul nu a putut fi trimis");
                return;
            }
            Log.i("mail service", "Report sent");
            HashMap<String, String> register = Controller.getInstance().getCrushRegister(context);
            register.remove(s);
            Controller.getInstance().saveCrushRegister(register, context);// nu salvam de fiecare data
        }
    }

    public void sendMail(String subject, String body, String from, String to, Context context, String title, String text, boolean showProgressDialog) throws Exception {
        if (showProgressDialog) {
            progressDialog = ProgressDialog.show(context, title, text, true);

            progressDialog.setOnDismissListener(dismissListener);
            progressDialog.setCanceledOnTouchOutside(true);
        }
        
        /*temp = new temporizator.Temporizator(new Event() {
            @Override
            public void doAction() {
                if (progressDialog == null) return;
                progressDialog.setCanceledOnTouchOutside(true);
            }
        }, 5, temporizator.Temporizator.SECONDS);
        temp.doScheduledEvent();*/


        SendEmailTask task = new SendEmailTask(context);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, subject, body, from, to);
        else
            task.execute(subject, body, from, to);

    }

    public void dismiss () {
        if (progressDialog == null) return;
        if (temp != null) temp.cancelEvent();
        progressDialog.dismiss();
    }

    public String getUserMailAdress () {
        return user;
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}