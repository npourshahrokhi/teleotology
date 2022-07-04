package com.doctor.ashanet.ashanet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    private DatabaseHelper db;
    String ids = null;
    TextView lbl;
    private SQLiteDatabase mSqlidb;
    EditText pass;
    ProgressBar progressBar;
    String token;
    EditText username;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.username = (EditText) findViewById(R.id.username);
        this.pass = (EditText) findViewById(R.id.password);
        this.db = new DatabaseHelper(this, AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
        this.mSqlidb = this.db.getWritableDatabase();
        this.btn1 = (Button) findViewById(R.id.btn_login);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.lbl = (TextView) findViewById(R.id.labels);
        check();
        this.btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    if (AppConfig.isNetworkConnected(MainActivity.this.getApplicationContext())) {
                        MainActivity.this.progressBar.setVisibility(0);
                        new SendPostRequest().execute(new String[0]);
                        return;
                    }
                    Toast.makeText(MainActivity.this.getApplicationContext(), "اتصال اینترنت برقرار نمی باشد لطفا ابتدا به اینترنت متصل شوید", 0).show();
                } catch (Exception a) {
                    Toast.makeText(MainActivity.this, a.getMessage().toString(), 0).show();
                }
            }
        });
    }

    public void check() {
        Cursor c = this.mSqlidb.query("users", (String[]) null, (String) null, (String[]) null, (String) null, (String) null, (String) null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            Snackbar.make(getWindow().getDecorView(), (CharSequence) "خوش آمدید", -1).show();
            startActivity(new Intent(this, Ashanet.class));
            finish();
        }
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        public SendPostRequest() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://teleotology.ir/SoftwareLogin");
                String uname = MainActivity.this.username.getText().toString();
                String paswd = MainActivity.this.pass.getText().toString();
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", uname);
                postDataParams.put(NotificationCompat.CATEGORY_EMAIL, paswd);
                Log.e("params", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(MainActivity.this.getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    return new String("false : " + responseCode);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = in.readLine();
                if (line != null) {
                    sb.append(line);
                }
                in.close();
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            String str = result.toString();
            try {
                JSONObject Data = new JSONObject(result).getJSONObject("data");
                String login = Data.getString("loginSuccess");
                MainActivity.this.ids = Data.getString("user_id");
                String rls = Data.getString("role");
                MainActivity.this.token = Data.getString("token");
                MainActivity.this.progressBar.setVisibility(8);
                MainActivity.this.checkLogin(login, MainActivity.this.ids, rls, MainActivity.this.token);
            } catch (JSONException e) {
                MainActivity.this.progressBar.setVisibility(8);
                Toast.makeText(MainActivity.this, "خطایی رخ داده است !کد خطا 1", 0).show();
                e.printStackTrace();
            }
        }
    }

    public void checkLogin(String success, String ID, String role, String tkn) {
        if (!success.matches("1")) {
            Toast.makeText(this, "wrong", 0).show();
        } else if (role.matches("doctor") && !this.token.matches("")) {
            login();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while (itr.hasNext()) {
            String key = itr.next();
            Object value = params.get(key);
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    public void login() {
        try {
            ContentValues cv = new ContentValues();
            cv.put("username", this.username.getText().toString());
            cv.put("password", this.pass.getText().toString());
            cv.put("token", this.token.toString());
            cv.put("UserId", this.ids.toString());
            this.mSqlidb.insert("users", (String) null, cv);
            Toast.makeText(this, "خوش آمدید ", 0).show();
            startActivity(new Intent(this, Ashanet.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage().toString(), 0).show();
        }
    }
}
