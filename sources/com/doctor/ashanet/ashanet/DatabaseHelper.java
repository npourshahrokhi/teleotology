package com.doctor.ashanet.ashanet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (id  INTEGER PRIMARY KEY AUTOINCREMENT , username TEXT,password TEXT,userid TEXT,token TEXT)");
        sqLiteDatabase.execSQL("create table `forwards` (id INTEGER PRIMARY KEY AUTOINCREMENT , forwardid TEXT,ClinicalinfoId TEXT,DoctorId TEXT,UserId TEXT,Tashil TEXT,SenderName TEXT,title TEXT,body TEXT,ReadFW TEXT,answer TEXT,CreateDate TEXT)");
        sqLiteDatabase.execSQL("create table `messages` (id INTEGER PRIMARY KEY AUTOINCREMENT , senderid  TEXT,title TEXT,MessageText  TEXT,SendDate TEXT)");
        sqLiteDatabase.execSQL("create table `clinicalinfos` (id INTEGER PRIMARY KEY AUTOINCREMENT , clinicalinfosid  TEXT,age TEXT,gender TEXT,SenderName TEXT,emergency  INTEGER,symptomatic  INTEGER,allergies  INTEGER,EarInfection  INTEGER,EarDischarge  INTEGER,Dizziness  INTEGER,DeepEarPain  INTEGER,DifficultyInSleeping  INTEGER,ObjectInEar  INTEGER,LoudVoice  INTEGER,HeadInjury  INTEGER,AirTravel  INTEGER,HearingLoss  INTEGER,EarRight  TEXT,EarLeft  TEXT,other  TEXT,RTympanicColor  TEXT,RTympanicStatus  TEXT,RTympanicLocation  TEXT,LTympanicColor  TEXT,LTympanicStatus  TEXT,LTympanicLocation  TEXT,RTmStatus  TEXT,RTmOther  TEXT,LTmStatus  TEXT,LTmOther  TEXT,RMaeStatus  TEXT,RMaeOther  TEXT,LMaeStatus  TEXT,LMaeOther  TEXT,RDiagnosisStatus  TEXT,RDiagnosisOther  TEXT,LDiagnosisStatus  TEXT,LDiagnosisOther  TEXT,RMiddleEarStatus  TEXT,RMiddleEarOther  TEXT,LMiddleEarStatus  TEXT,LMiddleEarOther  TEXT,DoctorComment  TEXT,DoctorSubmitDate TEXT)");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
