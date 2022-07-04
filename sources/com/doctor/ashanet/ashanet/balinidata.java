package com.doctor.ashanet.ashanet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class balinidata extends Fragment {
    public static int fileint;
    private DatabaseHelper db;
    private SQLiteDatabase mSqlidb;
    public int userids = CartableAdapter.userid;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.balini, container, false);
        CheckBox orjansis = (CheckBox) rootView.findViewById(R.id.orjansi);
        CheckBox alergys = (CheckBox) rootView.findViewById(R.id.alergy);
        CheckBox tarashohgoshs = (CheckBox) rootView.findViewById(R.id.tarashohgosh);
        CheckBox deepfeelings = (CheckBox) rootView.findViewById(R.id.deepfeeling);
        CheckBox jesmkharejidargoshs = (CheckBox) rootView.findViewById(R.id.jesmkharejidargosh);
        CheckBox kaheshshenavayis = (CheckBox) rootView.findViewById(R.id.kaheshshenavayi);
        CheckBox tabbalas = (CheckBox) rootView.findViewById(R.id.tabbala);
        CheckBox ofonatgoshs = (CheckBox) rootView.findViewById(R.id.ofonatgosh);
        CheckBox gijis = (CheckBox) rootView.findViewById(R.id.giji);
        CheckBox sleepproblems = (CheckBox) rootView.findViewById(R.id.sleepproblem);
        CheckBox asibsars = (CheckBox) rootView.findViewById(R.id.asibsar);
        CheckBox airtravels = (CheckBox) rootView.findViewById(R.id.airtravel);
        CheckBox loadsounds = (CheckBox) rootView.findViewById(R.id.loadsound);
        TextView txt = (TextView) rootView.findViewById(R.id.other);
        TextView age = (TextView) rootView.findViewById(R.id.age);
        TextView sendername = (TextView) rootView.findViewById(R.id.sender);
        TextView gender = (TextView) rootView.findViewById(R.id.gender);
        String str = " select * from clinicalinfos where clinicalinfosid =  '" + this.userids;
        this.db = new DatabaseHelper(getActivity(), AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
        this.mSqlidb = this.db.getReadableDatabase();
        Cursor c = this.mSqlidb.rawQuery("SELECT * FROM clinicalinfos WHERE clinicalinfosid  =" + this.userids, (String[]) null);
        if (c.moveToFirst()) {
            txt.setText(c.getString(c.getColumnIndex("other")));
            String sname = c.getString(c.getColumnIndex("SenderName"));
            if (!sname.matches("") || !sname.isEmpty()) {
                sendername.setText(c.getString(c.getColumnIndex("SenderName")));
            } else {
                sendername.setText("نامشخص");
            }
            String ages = c.getString(c.getColumnIndex("age"));
            if (!ages.matches("") || !ages.isEmpty()) {
                age.setText(c.getString(c.getColumnIndex("age")));
            } else {
                age.setText("نامشخص");
            }
            String genders = c.getString(c.getColumnIndex("other"));
            if (genders == "1") {
                gender.setText("مرد");
            }
            if (genders == "2") {
                gender.setText("زن");
            } else {
                gender.setText("نامشخص");
            }
            fileint = Integer.valueOf(c.getString(c.getColumnIndex("clinicalinfosid"))).intValue();
            if (c.getString(c.getColumnIndex("emergency")).matches("1")) {
                orjansis.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("emergency")).matches("1")) {
                orjansis.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("allergies")).matches("1")) {
                alergys.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("LoudVoice")).matches("1")) {
                loadsounds.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("DeepEarPain")).matches("1")) {
                deepfeelings.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("ObjectInEar")).matches("1")) {
                jesmkharejidargoshs.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("symptomatic")).matches("1")) {
                tabbalas.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("EarInfection")).matches("1")) {
                ofonatgoshs.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("EarDischarge")).matches("1")) {
                tarashohgoshs.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("Dizziness")).matches("1")) {
                gijis.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("DifficultyInSleeping")).matches("1")) {
                sleepproblems.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("HeadInjury")).matches("1")) {
                asibsars.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("AirTravel")).matches("1")) {
                airtravels.setChecked(true);
            }
            if (c.getString(c.getColumnIndex("HearingLoss")).matches("1")) {
                kaheshshenavayis.setChecked(true);
            }
        }
        return rootView;
    }
}
