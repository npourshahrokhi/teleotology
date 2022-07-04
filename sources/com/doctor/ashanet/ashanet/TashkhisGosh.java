package com.doctor.ashanet.ashanet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TashkhisGosh extends Fragment {
    public static int fileint;
    private DatabaseHelper db;
    EditText ed1;
    EditText editText;
    private SQLiteDatabase mSqlidb;
    public int userids = CartableAdapter.userid;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Cartable.typic == 0) {
            this.view = inflater.inflate(R.layout.tashkhis, container, false);
        } else {
            this.view = inflater.inflate(R.layout.send_tashkhis, container, false);
        }
        if (Cartable.typic == 1) {
            TextView tashkhis = (TextView) this.view.findViewById(R.id.tashkhis);
            String str = " select * from clinicalinfos where clinicalinfosid =  '" + this.userids;
            this.db = new DatabaseHelper(getActivity(), AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
            this.mSqlidb = this.db.getReadableDatabase();
            Cursor c = this.mSqlidb.rawQuery("SELECT * FROM clinicalinfos WHERE clinicalinfosid  =" + this.userids, (String[]) null);
            if (c.moveToFirst()) {
                tashkhis.setText(c.getString(c.getColumnIndex("DoctorComment")));
            }
        }
        this.editText = (EditText) this.view.findViewById(R.id.Dcomment);
        return this.view;
    }

    @Nullable
    public View getView() {
        return this.view;
    }
}
