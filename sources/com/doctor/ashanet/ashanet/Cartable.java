package com.doctor.ashanet.ashanet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Cartable extends AppCompatActivity {
    public static int typic;
    private CartableAdapter cartableAdapter;
    private List<CartableItems> cartableItems = new ArrayList();
    private DatabaseHelper db;
    private SQLiteDatabase mSqlidb;
    private RecyclerView recyclerView;
    String role = null;
    TextView title;
    String token;
    String userid;
    public String userids = Ashanet.userid;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_cartable);
        this.recyclerView = (RecyclerView) findViewById(R.id.cartablelist);
        this.cartableAdapter = new CartableAdapter(this.cartableItems, this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setAdapter(this.cartableAdapter);
        CartableView.load_left = 1;
        CartableView.load_right = 1;
        update(0);
        this.title = (TextView) findViewById(R.id.paneltitle);
    }

    public void setData(String title2, String desc, Integer id, Integer fid, Integer read) {
        this.cartableItems.add(new CartableItems(title2, desc, id, fid, read));
    }

    public void update(int type) {
        this.cartableItems.clear();
        this.cartableItems.removeAll(this.cartableItems);
        this.cartableAdapter.notifyDataSetChanged();
        this.db = new DatabaseHelper(this, AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
        this.mSqlidb = this.db.getReadableDatabase();
        if (type == 1) {
            typic = 1;
            Cursor a = this.mSqlidb.rawQuery("SELECT * FROM forwards WHERE Tashil=" + this.userids + " ORDER BY id DESC", (String[]) null);
            if (a.getCount() < 1) {
                TextView status = (TextView) findViewById(R.id.errorstatuss);
                status.setTextColor(getResources().getColor(R.color.colorPrimary));
                status.setText("پرونده ارسالی وجود ندارد");
            } else if (a.getCount() > 0) {
                TextView status2 = (TextView) findViewById(R.id.errorstatuss);
                status2.setTextColor(getResources().getColor(R.color.colorPrimary));
                status2.setText("تعداد پرونده ارسالی:" + String.valueOf(a.getCount()) + " عدد ");
            }
            while (a.moveToNext()) {
                if (a.getCount() > 0) {
                    setData(a.getString(a.getColumnIndex("title")), a.getString(a.getColumnIndex("body")), Integer.valueOf(a.getInt(a.getColumnIndex("ClinicalinfoId"))), Integer.valueOf(a.getInt(a.getColumnIndex("forwardid"))), Integer.valueOf(a.getInt(a.getColumnIndex("answer"))));
                }
            }
        } else {
            typic = 0;
            Cursor c = this.mSqlidb.rawQuery("SELECT * FROM forwards WHERE DoctorId=" + this.userids + " ORDER BY id DESC", (String[]) null);
            if (c.getCount() < 1) {
                ((TextView) findViewById(R.id.errorstatuss)).setText("پرونده دریافتی وجود ندارد");
            } else if (c.getCount() > 0) {
                ((TextView) findViewById(R.id.errorstatuss)).setText("تعداد پرونده دریافتی:" + String.valueOf(c.getCount()) + " عدد ");
            }
            while (c.moveToNext()) {
                if (c.getCount() > 0) {
                    setData(c.getString(c.getColumnIndex("title")), c.getString(c.getColumnIndex("body")), Integer.valueOf(c.getInt(c.getColumnIndex("ClinicalinfoId"))), Integer.valueOf(c.getInt(c.getColumnIndex("forwardid"))), Integer.valueOf(c.getInt(c.getColumnIndex("answer"))));
                }
            }
        }
        this.cartableAdapter.notifyDataSetChanged();
    }

    public void send(View view) {
        this.title.setText("پرونده ارسالی");
        this.title.setTextColor(getResources().getColor(R.color.sendgreen));
        update(1);
    }

    public void recive(View view) {
        this.title.setText("پرونده دریافتی");
        this.title.setTextColor(getResources().getColor(R.color.startblue));
        update(0);
    }
}
