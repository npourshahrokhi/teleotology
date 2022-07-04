package com.doctor.ashanet.ashanet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
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
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

public class Ashanet extends AppCompatActivity {
    public static String role = null;
    public static String token;
    public static String userid;
    TextView answer;
    private DatabaseHelper db;
    /* access modifiers changed from: private */
    public SQLiteDatabase mSqlidb;
    TextView msg;
    Snackbar mySnackbar;
    TableRow tcartable;
    TextView unread;
    Vibrator vibe;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_ashanet);
        this.vibe = (Vibrator) getSystemService("vibrator");
        this.msg = (TextView) findViewById(R.id.msg);
        this.answer = (TextView) findViewById(R.id.answer);
        this.unread = (TextView) findViewById(R.id.unread);
        update();
        count();
    }

    public void timer() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Ashanet.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Ashanet.this.count();
                    }
                });
            }
        }, 0, 700);
    }

    public void exit(View view) {
        finish();
        System.exit(0);
    }

    public void count() {
        try {
            this.db = new DatabaseHelper(this, AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
            this.mSqlidb = this.db.getReadableDatabase();
            Cursor c = this.mSqlidb.query("forwards", (String[]) null, (String) null, (String[]) null, (String) null, (String) null, (String) null);
            Cursor a = this.mSqlidb.rawQuery("SELECT * FROM forwards WHERE answer=10  ", (String[]) null);
            int msgcount = c.getCount();
            int answercount = a.getCount();
            int ekhtelaf = Integer.valueOf(msgcount).intValue() - Integer.valueOf(answercount).intValue();
            if (ekhtelaf < 0) {
                ekhtelaf = 0;
            }
            this.msg.setText(String.valueOf(msgcount));
            this.answer.setText(String.valueOf(answercount));
            this.unread.setText(String.valueOf(ekhtelaf));
        } catch (Exception e) {
        }
    }

    public void update() {
        if (AppConfig.isNetworkConnected(this)) {
            try {
                this.db = new DatabaseHelper(this, AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
                this.mSqlidb = this.db.getReadableDatabase();
                Cursor c = this.mSqlidb.query("users", (String[]) null, (String) null, (String[]) null, (String) null, (String) null, (String) null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    userid = c.getString(c.getColumnIndex("userid"));
                    token = c.getString(c.getColumnIndex("token"));
                    role = "doctor";
                    this.vibe.vibrate(30);
                    this.mySnackbar = Snackbar.make(getWindow().getDecorView(), (CharSequence) "در حال بروزرسانی نرم افزار.لطفا شکیبا باشید!", -1);
                    this.mySnackbar.show();
                    new SendPostRequest().execute(new String[0]);
                    return;
                }
                this.mSqlidb.execSQL("DELETE  FROM forwards");
                this.mSqlidb.execSQL("DELETE  FROM users");
                this.mSqlidb.execSQL("DELETE  FROM clinicalinfos");
                this.mySnackbar = Snackbar.make(getWindow().getDecorView(), (CharSequence) "خطایی رخ داده است لطفا مجدد وارد نرم افزار شوید", -1);
                this.mySnackbar.show();
            } catch (Exception e) {
            }
        } else {
            this.mySnackbar = Snackbar.make(getWindow().getDecorView(), (CharSequence) "اتصال اینترنت برقرار نمی باشد لطفا ابتدا به اینترنت متصل شوید", -1);
            this.mySnackbar.show();
        }
    }

    public void cartable(View view) {
        startActivity(new Intent(this, Cartable.class));
    }

    public void updatedb(View view) {
        try {
            update();
        } catch (Exception a) {
            Toast.makeText(this, a.getMessage().toString(), 0).show();
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
                URL url = new URL("http://teleotology.ir/users/Cartable");
                String uid = Ashanet.userid.toString();
                String tkn = Ashanet.token.toString();
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("uid", uid);
                postDataParams.put("tkn", tkn);
                Log.e("params", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Ashanet.this.getPostDataString(postDataParams));
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
            String str2 = result;
            try {
                JSONArray jsonArrays = new JSONObject(result).optJSONArray("Employee");
                int count = 0;
                for (int q = 0; q < jsonArrays.length(); q++) {
                    count++;
                }
                if (count > 0) {
                    Ashanet.this.mSqlidb.execSQL("DELETE  FROM forwards");
                }
                for (int i = 0; i < jsonArrays.length(); i++) {
                    JSONObject jsonObject = jsonArrays.getJSONObject(i);
                    String forwardid = jsonObject.optString("id").toString();
                    String ClinicalinfoId = jsonObject.optString("ClinicalinfoId").toString();
                    String DoctorId = jsonObject.optString("DoctorId").toString();
                    String UserId = jsonObject.optString("UserId").toString();
                    String Tashil = jsonObject.optString("Tashil").toString();
                    String SenderName = jsonObject.optString("SenderName").toString();
                    String title = jsonObject.optString("title").toString();
                    String body = jsonObject.optString("body").toString();
                    String ReadFW = jsonObject.optString("ReadFW").toString();
                    String answer = jsonObject.optString("answer").toString();
                    String CreateDate = jsonObject.optString("CreateDate").toString();
                    ContentValues cv = new ContentValues();
                    cv.put("forwardid", forwardid.toString());
                    cv.put("ClinicalinfoId", ClinicalinfoId.toString());
                    cv.put("DoctorId", DoctorId.toString());
                    cv.put("UserId", UserId.toString());
                    cv.put("Tashil", Tashil.toString());
                    cv.put("SenderName", SenderName.toString());
                    cv.put("title", title.toString());
                    cv.put("body", body.toString());
                    cv.put("answer", answer.toString());
                    cv.put("ReadFW", ReadFW.toString());
                    cv.put("CreateDate", CreateDate.toString());
                    Ashanet.this.mSqlidb.insert("forwards", (String) null, cv);
                }
                JSONArray jsonArrayss = new JSONObject(result).optJSONArray("Messages");
                int count2 = 0;
                for (int q2 = 0; q2 < jsonArrayss.length(); q2++) {
                    count2++;
                }
                if (count2 > 0) {
                    Ashanet.this.mSqlidb.execSQL("DELETE  FROM messages");
                }
                for (int i2 = 0; i2 < jsonArrayss.length(); i2++) {
                    JSONObject jsonObjects = jsonArrayss.getJSONObject(i2);
                    String senderid = jsonObjects.optString("UserID").toString();
                    String title2 = jsonObjects.optString("title").toString();
                    String MessageText = jsonObjects.optString("MessageText").toString();
                    String SendDate = jsonObjects.optString("SendDate").toString();
                    ContentValues cv2 = new ContentValues();
                    cv2.put("senderid", senderid.toString());
                    cv2.put("title", title2.toString());
                    cv2.put("MessageText", MessageText.toString());
                    cv2.put("SendDate", SendDate.toString());
                    Ashanet.this.mSqlidb.insert("messages", (String) null, cv2);
                }
                JSONArray jsonArrayclinic = new JSONObject(result).optJSONArray("Clinic");
                int count3 = 0;
                for (int q3 = 0; q3 < jsonArrayclinic.length(); q3++) {
                    count3++;
                }
                if (count3 > 0) {
                    Ashanet.this.mSqlidb.execSQL("DELETE  FROM clinicalinfos");
                }
                for (int i3 = 0; i3 < jsonArrayclinic.length(); i3++) {
                    JSONObject jsonclinic = jsonArrayclinic.getJSONObject(i3);
                    String clinicalinfosid = jsonclinic.optString("clinicalinfosid").toString();
                    String age = jsonclinic.optString("age").toString();
                    String gender = jsonclinic.optString("gender").toString();
                    String SenderName2 = jsonclinic.optString("SenderName").toString();
                    String emergency = jsonclinic.optString("emergency").toString();
                    String symptomatic = jsonclinic.optString("symptomatic").toString();
                    String allergies = jsonclinic.optString("allergies").toString();
                    String EarInfection = jsonclinic.optString("EarInfection").toString();
                    String EarDischarge = jsonclinic.optString("EarDischarge").toString();
                    String Dizziness = jsonclinic.optString("Dizziness").toString();
                    String DeepEarPain = jsonclinic.optString("DeepEarPain").toString();
                    String DifficultyInSleeping = jsonclinic.optString("DifficultyInSleeping").toString();
                    String ObjectInEar = jsonclinic.optString("ObjectInEar").toString();
                    String LoudVoice = jsonclinic.optString("LoudVoice").toString();
                    String HeadInjury = jsonclinic.optString("HeadInjury").toString();
                    String AirTravel = jsonclinic.optString("AirTravel").toString();
                    String HearingLoss = jsonclinic.optString("HearingLoss").toString();
                    String EarRight = jsonclinic.optString("EarRight").toString();
                    String EarLeft = jsonclinic.optString("EarLeft").toString();
                    String other = jsonclinic.optString("other").toString();
                    String RTympanicColor = jsonclinic.optString("RTympanicColor").toString();
                    String RTympanicStatus = jsonclinic.optString("RTympanicStatus").toString();
                    String RTympanicLocation = jsonclinic.optString("RTympanicLocation").toString();
                    String LTympanicColor = jsonclinic.optString("LTympanicColor").toString();
                    String LTympanicStatus = jsonclinic.optString("LTympanicStatus").toString();
                    String LTympanicLocation = jsonclinic.optString("LTympanicLocation").toString();
                    String RTmStatus = jsonclinic.optString("RTmStatus").toString();
                    String RTmOther = jsonclinic.optString("RTmOther").toString();
                    String LTmStatus = jsonclinic.optString("LTmStatus").toString();
                    String LTmOther = jsonclinic.optString("LTmOther").toString();
                    String RMaeStatus = jsonclinic.optString("RMaeStatus").toString();
                    String RMaeOther = jsonclinic.optString("RMaeOther").toString();
                    String LMaeStatus = jsonclinic.optString("LMaeStatus").toString();
                    String LMaeOther = jsonclinic.optString("LMaeOther").toString();
                    String RDiagnosisStatus = jsonclinic.optString("RDiagnosisStatus").toString();
                    String RDiagnosisOther = jsonclinic.optString("RDiagnosisOther").toString();
                    String LDiagnosisStatus = jsonclinic.optString("LDiagnosisStatus").toString();
                    String LDiagnosisOther = jsonclinic.optString("LDiagnosisOther").toString();
                    String RMiddleEarStatus = jsonclinic.optString("RMiddleEarStatus").toString();
                    String RMiddleEarOther = jsonclinic.optString("RMiddleEarOther").toString();
                    String LMiddleEarStatus = jsonclinic.optString("LMiddleEarStatus").toString();
                    String LMiddleEarOther = jsonclinic.optString("LMiddleEarOther").toString();
                    String DoctorComment = jsonclinic.optString("DoctorComment").toString();
                    String DoctorSubmitDate = jsonclinic.optString("DoctorSubmitDate").toString();
                    ContentValues cv3 = new ContentValues();
                    cv3.put("clinicalinfosid", clinicalinfosid.toString());
                    cv3.put("age", age.toString());
                    cv3.put("gender", gender.toString());
                    cv3.put("SenderName", SenderName2.toString());
                    cv3.put("emergency", emergency.toString());
                    cv3.put("symptomatic", symptomatic.toString());
                    cv3.put("allergies", allergies.toString());
                    cv3.put("EarInfection", EarInfection.toString());
                    cv3.put("EarDischarge", EarDischarge.toString());
                    cv3.put("Dizziness", Dizziness.toString());
                    cv3.put("DeepEarPain", DeepEarPain.toString());
                    cv3.put("DifficultyInSleeping", DifficultyInSleeping.toString());
                    cv3.put("ObjectInEar", ObjectInEar.toString());
                    cv3.put("LoudVoice", LoudVoice.toString());
                    cv3.put("HeadInjury", HeadInjury.toString());
                    cv3.put("AirTravel", AirTravel.toString());
                    cv3.put("HearingLoss", HearingLoss.toString());
                    cv3.put("EarRight", EarRight.toString());
                    cv3.put("EarLeft", EarLeft.toString());
                    cv3.put("other", other.toString());
                    cv3.put("RTympanicColor", RTympanicColor.toString());
                    cv3.put("RTympanicStatus", RTympanicStatus.toString());
                    cv3.put("RTympanicLocation", RTympanicLocation.toString());
                    cv3.put("LTympanicColor", LTympanicColor.toString());
                    cv3.put("LTympanicStatus", LTympanicStatus.toString());
                    cv3.put("LTympanicLocation", LTympanicLocation.toString());
                    cv3.put("RTmStatus", RTmStatus.toString());
                    cv3.put("RTmOther", RTmOther.toString());
                    cv3.put("LTmStatus", LTmStatus.toString());
                    cv3.put("LTmOther", LTmOther.toString());
                    cv3.put("RMaeStatus", RMaeStatus.toString());
                    cv3.put("RMaeOther", RMaeOther.toString());
                    cv3.put("LMaeStatus", LMaeStatus.toString());
                    cv3.put("LMaeOther", LMaeOther.toString());
                    cv3.put("RDiagnosisStatus", RDiagnosisStatus.toString());
                    cv3.put("RDiagnosisOther", RDiagnosisOther.toString());
                    cv3.put("LDiagnosisStatus", LDiagnosisStatus.toString());
                    cv3.put("LDiagnosisOther", LDiagnosisOther.toString());
                    cv3.put("RMiddleEarStatus", RMiddleEarStatus.toString());
                    cv3.put("RMiddleEarOther", RMiddleEarOther.toString());
                    cv3.put("LMiddleEarStatus", LMiddleEarStatus.toString());
                    cv3.put("LMiddleEarOther", LMiddleEarOther.toString());
                    cv3.put("DoctorComment", DoctorComment.toString());
                    cv3.put("DoctorSubmitDate", DoctorSubmitDate.toString());
                    Ashanet.this.mSqlidb.insert("clinicalinfos", (String) null, cv3);
                }
                Ashanet.this.mySnackbar = Snackbar.make(Ashanet.this.getWindow().getDecorView(), (CharSequence) "بروزرسانی با موفقیت انجام شد", -1);
                Ashanet.this.mySnackbar.show();
            } catch (Exception a) {
                Toast.makeText(Ashanet.this, a.getMessage(), 0).show();
            }
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
}
