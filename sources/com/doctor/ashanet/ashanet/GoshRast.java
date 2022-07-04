package com.doctor.ashanet.ashanet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GoshRast extends Fragment {
    public static int fileint;
    private DatabaseHelper db;
    private SQLiteDatabase mSqlidb;
    public int userids = CartableAdapter.userid;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Cartable.typic == 0) {
            this.view = inflater.inflate(R.layout.gosh_rast, container, false);
        } else {
            this.view = inflater.inflate(R.layout.send_gosh_rast, container, false);
        }
        if (Cartable.typic == 1) {
            TextView color = (TextView) this.view.findViewById(R.id.color);
            TextView tmstatus = (TextView) this.view.findViewById(R.id.tmstatus);
            TextView canalgosh = (TextView) this.view.findViewById(R.id.canalgosh);
            TextView tashkhis = (TextView) this.view.findViewById(R.id.tashkhis);
            TextView tozihtashkhis = (TextView) this.view.findViewById(R.id.tozihtashkhis);
            String str = " select * from clinicalinfos where clinicalinfosid =  '" + this.userids;
            this.db = new DatabaseHelper(getActivity(), AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
            this.mSqlidb = this.db.getReadableDatabase();
            Cursor c = this.mSqlidb.rawQuery("SELECT * FROM clinicalinfos WHERE clinicalinfosid  =" + this.userids, (String[]) null);
            if (c.moveToFirst()) {
                color.setText(c.getString(c.getColumnIndex("RTympanicColor")));
                tmstatus.setText(c.getString(c.getColumnIndex("RTmStatus")));
                canalgosh.setText(c.getString(c.getColumnIndex("RMaeStatus")));
                tashkhis.setText(c.getString(c.getColumnIndex("RDiagnosisStatus")));
                tozihtashkhis.setText(c.getString(c.getColumnIndex("RDiagnosisOther")));
            }
        }
        return this.view;
    }

    @Nullable
    public View getView() {
        return this.view;
    }
}
