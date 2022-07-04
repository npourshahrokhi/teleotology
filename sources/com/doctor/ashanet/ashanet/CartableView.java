package com.doctor.ashanet.ashanet;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

public class CartableView extends AppCompatActivity {
    private static final String TAG = "tashkhisss";
    public static FloatingActionButton fab;
    public static int load_left = 1;
    public static int load_right = 1;
    EditText DoctorComment = null;
    EditText LDiagnosisOther = null;
    CheckBox LMaeAuralpolyps;
    CheckBox LMaeCanalstenosis;
    CheckBox LMaeHeamatoma;
    CheckBox LMaeInjury = null;
    CheckBox LMaeMass;
    EditText LMaeOther = null;
    CheckBox LMaeOtitisexerna;
    CheckBox LMaeOtorhea;
    CheckBox LMaeWax;
    CheckBox LMaeexternalobject;
    CheckBox LMaenormal;
    CheckBox LMiddleAOM;
    CheckBox LMiddleAtelectasi;
    CheckBox LMiddleCOM;
    CheckBox LMiddleEffusion;
    CheckBox LMiddleMEE;
    CheckBox LMiddleOttorhea;
    CheckBox LMiddlenormal;
    EditText LTmOther = null;
    EditText LeftEarColorSomakh;
    EditText LeftEarPositionSomakh = null;
    EditText LeftEarStatusSomakh;
    CheckBox LtashkhisAOM;
    CheckBox LtashkhisCSOM;
    CheckBox LtashkhisCholesteatoma;
    CheckBox LtashkhisExostoses;
    CheckBox LtashkhisHearingloss;
    CheckBox LtashkhisOME;
    CheckBox LtashkhisOtitisextemabactaria;
    CheckBox LtashkhisOtitisextemafungal;
    CheckBox LtashkhisTMretraction = null;
    CheckBox Ltashkhisnormal;
    EditText RDiagnosisOther;
    CheckBox RMaeAuralpolyps;
    CheckBox RMaeCanalstenosis;
    CheckBox RMaeHeamatoma;
    CheckBox RMaeInjury = null;
    CheckBox RMaeMass;
    EditText RMaeOther;
    CheckBox RMaeOtitisexerna;
    CheckBox RMaeOtorhea;
    CheckBox RMaeWax;
    CheckBox RMaeexternalobject;
    CheckBox RMaenormal;
    CheckBox RMiddleAOM;
    CheckBox RMiddleAtelectasi;
    CheckBox RMiddleCOM;
    CheckBox RMiddleEffusion;
    CheckBox RMiddleMEE;
    CheckBox RMiddleOttorhea = null;
    CheckBox RMiddlenormal;
    EditText RTmOther;
    EditText RightEarColorSomakh = null;
    EditText RightEarPositionSomakh;
    EditText RightEarStatusSomakh;
    CheckBox RtashkhisAOM;
    CheckBox RtashkhisCSOM;
    CheckBox RtashkhisCholesteatoma;
    CheckBox RtashkhisExostoses;
    CheckBox RtashkhisHearingloss;
    CheckBox RtashkhisOME;
    CheckBox RtashkhisOtitisextemabactaria;
    CheckBox RtashkhisOtitisextemafungal;
    CheckBox RtashkhisTMretraction = null;
    CheckBox Rtashkhisnormal;
    Checkable TMLeftAtropicatelectasi;
    Checkable TMLeftHyperemia;
    Checkable TMLeftNormal;
    Checkable TMLeftOttorhea;
    Checkable TMLeftPerforations;
    Checkable TMLeftRetraction = null;
    Checkable TMLeftTymanesclerosis;
    CheckBox TMLeftbulging;
    Checkable TMLefteffusion;
    Checkable TMLeftneotympan;
    Checkable TMLeftvesselCongestion;
    CheckBox TMRighteffusion;
    CheckBox TMRightneotympan;
    CheckBox TMrightAtropicatelectasi;
    CheckBox TMrightHyperemia;
    CheckBox TMrightNormal;
    CheckBox TMrightOttorhea;
    CheckBox TMrightPerforations;
    CheckBox TMrightRetraction = null;
    CheckBox TMrightTymanesclerosis;
    CheckBox TMrightbulging = null;
    CheckBox TMrightvesselCongestion;
    Context context;
    /* access modifiers changed from: private */
    public DatabaseHelper db;
    GoshRast goshRast;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /* access modifiers changed from: private */
    public SQLiteDatabase mSqlidb;
    private ViewPager mViewPager;
    private RadioButton radioButtonLeft;
    private RadioButton radioButtonRight;
    private RadioGroup radioGroupLeft;
    private RadioGroup radioGroupRight;
    TashkhisGosh tashkhisss;
    tajvizdata tjvz;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_cartable_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.mViewPager = (ViewPager) findViewById(R.id.container);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(0);
        this.mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(this.mViewPager));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Cartable.typic == 0) {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Snackbar.make(CartableView.this.getWindow().getDecorView(), (CharSequence) "در حال پردازش اطلاعات لطفا صبر نمایید", 0).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            CartableView.this.savedata();
                        }
                    }, 1700);
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cartable_view, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    balinidata balinidata = new balinidata();
                    if (Cartable.typic != 0) {
                        return balinidata;
                    }
                    CartableView.fab.hide();
                    return balinidata;
                case 1:
                    imagesdata rastimg = new imagesdata();
                    rastimg.name = "rast";
                    if (Cartable.typic == 0) {
                        CartableView.fab.hide();
                    }
                    return rastimg;
                case 2:
                    CartableView.this.goshRast = new GoshRast();
                    if (Cartable.typic == 0) {
                        CartableView.fab.hide();
                    }
                    return CartableView.this.goshRast;
                case 3:
                    imagesdata chapimg = new imagesdata();
                    chapimg.name = "chap";
                    if (Cartable.typic == 0) {
                        CartableView.fab.hide();
                    }
                    return chapimg;
                case 4:
                    CartableView.this.tjvz = new tajvizdata();
                    if (Cartable.typic == 0) {
                        CartableView.fab.hide();
                    }
                    return CartableView.this.tjvz;
                case 5:
                    CartableView.this.tashkhisss = new TashkhisGosh();
                    if (Cartable.typic == 0) {
                        CartableView.fab.show();
                    }
                    return CartableView.this.tashkhisss;
                default:
                    return null;
            }
        }

        public int getCount() {
            return 6;
        }
    }

    public void savedata() {
        try {
            EditText DoctorComment2 = (EditText) this.tashkhisss.getView().findViewById(R.id.Dcomment);
            if (DoctorComment2 == null) {
                Log.e(TAG, "savedata: et null");
            } else if (DoctorComment2.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "پزشک گرامی لطفا تشخیص خود را وارد کنید", 0).show();
                DoctorComment2.requestFocus();
            } else {
                ContentValues cv = new ContentValues();
                ContentValues av = new ContentValues();
                this.radioGroupRight = (RadioGroup) this.goshRast.getView().findViewById(R.id.ranggoshrast);
                this.radioButtonRight = (RadioButton) this.goshRast.getView().findViewById(this.radioGroupRight.getCheckedRadioButtonId());
                this.radioGroupLeft = (RadioGroup) this.tjvz.getView().findViewById(R.id.ranggoshchap);
                this.radioButtonLeft = (RadioButton) this.tjvz.getView().findViewById(this.radioGroupLeft.getCheckedRadioButtonId());
                cv.put("RTympanicColor", this.radioButtonRight.getText().toString());
                cv.put("LTympanicColor", this.radioButtonLeft.getText().toString());
                CheckBox TMrightNormal2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightNormal);
                CheckBox TMRighteffusion2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMRighteffusion);
                CheckBox TMrightPerforations2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightPerforations);
                CheckBox TMrightHyperemia2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightHyperemia);
                CheckBox TMRightneotympan2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMRightneotympan);
                CheckBox TMrightvesselCongestion2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightvesselCongestion);
                CheckBox TMrightTymanesclerosis2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightTymanesclerosis);
                CheckBox TMrightOttorhea2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightOttorhea);
                CheckBox TMrightAtropicatelectasi2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightAtropicatelectasi);
                CheckBox TMrightRetraction2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightRetraction);
                CheckBox TMrightbulging2 = (CheckBox) this.goshRast.getView().findViewById(R.id.TMrightbulging);
                ArrayList arrayList = new ArrayList();
                if (TMrightNormal2.isChecked()) {
                    arrayList.add(TMrightNormal2.getText().toString());
                }
                if (TMRighteffusion2.isChecked()) {
                    arrayList.add(TMRighteffusion2.getText().toString());
                }
                if (TMrightPerforations2.isChecked()) {
                    arrayList.add(TMrightPerforations2.getText().toString());
                }
                if (TMrightHyperemia2.isChecked()) {
                    arrayList.add(TMrightHyperemia2.getText().toString());
                }
                if (TMRightneotympan2.isChecked()) {
                    arrayList.add(TMRightneotympan2.getText().toString());
                }
                if (TMrightvesselCongestion2.isChecked()) {
                    arrayList.add(TMrightvesselCongestion2.getText().toString());
                }
                if (TMrightTymanesclerosis2.isChecked()) {
                    arrayList.add(TMrightTymanesclerosis2.getText().toString());
                }
                if (TMrightOttorhea2.isChecked()) {
                    arrayList.add(TMrightOttorhea2.getText().toString());
                }
                if (TMrightAtropicatelectasi2.isChecked()) {
                    arrayList.add(TMrightAtropicatelectasi2.getText().toString());
                }
                if (TMrightRetraction2.isChecked()) {
                    arrayList.add(TMrightRetraction2.getText().toString());
                }
                if (TMrightbulging2.isChecked()) {
                    arrayList.add(TMrightbulging2.getText().toString());
                }
                if (arrayList.size() > 0) {
                    cv.put("RTmStatus", TextUtils.join(", ", arrayList));
                }
                CheckBox TMLeftNormal2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftNormal);
                CheckBox TMLefteffusion2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLefteffusion);
                CheckBox TMLeftPerforations2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftPerforations);
                CheckBox TMLeftHyperemia2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftHyperemia);
                CheckBox TMLeftneotympan2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftneotympan);
                CheckBox TMLeftvesselCongestion2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftvesselCongestion);
                CheckBox TMLeftTymanesclerosis2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftTymanesclerosis);
                CheckBox TMLeftOttorhea2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftOttorhea);
                CheckBox TMLeftAtropicatelectasi2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftAtropicatelectasi);
                CheckBox TMLeftRetraction2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftRetraction);
                CheckBox TMLeftbulging2 = (CheckBox) this.tjvz.getView().findViewById(R.id.TMLeftbulging);
                ArrayList arrayList2 = new ArrayList();
                if (TMLeftNormal2.isChecked()) {
                    arrayList2.add(TMLeftNormal2.getText().toString());
                }
                if (TMLefteffusion2.isChecked()) {
                    arrayList2.add(TMLefteffusion2.getText().toString());
                }
                if (TMLeftPerforations2.isChecked()) {
                    arrayList2.add(TMLeftPerforations2.getText().toString());
                }
                if (TMLeftHyperemia2.isChecked()) {
                    arrayList2.add(TMLeftHyperemia2.getText().toString());
                }
                if (TMLeftneotympan2.isChecked()) {
                    arrayList2.add(TMLeftneotympan2.getText().toString());
                }
                if (TMLeftvesselCongestion2.isChecked()) {
                    arrayList2.add(TMLeftvesselCongestion2.getText().toString());
                }
                if (TMLeftTymanesclerosis2.isChecked()) {
                    arrayList2.add(TMLeftTymanesclerosis2.getText().toString());
                }
                if (TMLeftOttorhea2.isChecked()) {
                    arrayList2.add(TMLeftOttorhea2.getText().toString());
                }
                if (TMLeftAtropicatelectasi2.isChecked()) {
                    arrayList2.add(TMLeftAtropicatelectasi2.getText().toString());
                }
                if (TMLeftRetraction2.isChecked()) {
                    arrayList2.add(TMLeftRetraction2.getText().toString());
                }
                if (TMLeftbulging2.isChecked()) {
                    arrayList2.add(TMLeftbulging2.getText().toString());
                }
                if (arrayList2.size() > 0) {
                    cv.put("LTmStatus", TextUtils.join(", ", arrayList2));
                }
                cv.put("RTmOther", ((EditText) this.goshRast.getView().findViewById(R.id.RTmOther)).getText().toString());
                cv.put("LTmOther", ((EditText) this.tjvz.getView().findViewById(R.id.LTmOther)).getText().toString());
                CheckBox RMaenormal2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaenormal);
                CheckBox RMaeHeamatoma2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeHeamatoma);
                CheckBox RMaeOtitisexerna2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeOtitisexerna);
                CheckBox RMaeCanalstenosis2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeCanalstenosis);
                CheckBox RMaeOtorhea2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeOtorhea);
                CheckBox RMaeWax2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeWax);
                CheckBox RMaeAuralpolyps2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeAuralpolyps);
                CheckBox RMaeMass2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeMass);
                CheckBox RMaeexternalobject2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeexternalobject);
                CheckBox RMaeInjury2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMaeInjury);
                ArrayList arrayList3 = new ArrayList();
                if (RMaenormal2.isChecked()) {
                    arrayList3.add(RMaenormal2.getText().toString());
                }
                if (RMaeHeamatoma2.isChecked()) {
                    arrayList3.add(RMaeHeamatoma2.getText().toString());
                }
                if (RMaeOtitisexerna2.isChecked()) {
                    arrayList3.add(RMaeOtitisexerna2.getText().toString());
                }
                if (RMaeCanalstenosis2.isChecked()) {
                    arrayList3.add(RMaeCanalstenosis2.getText().toString());
                }
                if (RMaeOtorhea2.isChecked()) {
                    arrayList3.add(RMaeOtorhea2.getText().toString());
                }
                if (RMaeWax2.isChecked()) {
                    arrayList3.add(RMaeWax2.getText().toString());
                }
                if (RMaeAuralpolyps2.isChecked()) {
                    arrayList3.add(RMaeAuralpolyps2.getText().toString());
                }
                if (RMaeMass2.isChecked()) {
                    arrayList3.add(RMaeMass2.getText().toString());
                }
                if (RMaeexternalobject2.isChecked()) {
                    arrayList3.add(RMaeexternalobject2.getText().toString());
                }
                if (RMaeInjury2.isChecked()) {
                    arrayList3.add(RMaeInjury2.getText().toString());
                }
                if (arrayList3.size() > 0) {
                    cv.put("RMaeStatus", TextUtils.join(", ", arrayList3));
                }
                CheckBox LMaenormal2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaenormal);
                CheckBox LMaeHeamatoma2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeHeamatoma);
                CheckBox LMaeOtitisexerna2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeOtitisexerna);
                CheckBox LMaeCanalstenosis2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeCanalstenosis);
                CheckBox LMaeOtorhea2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeOtorhea);
                CheckBox LMaeWax2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeWax);
                CheckBox LMaeAuralpolyps2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeAuralpolyps);
                CheckBox LMaeMass2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeMass);
                CheckBox LMaeexternalobject2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeexternalobject);
                CheckBox LMaeInjury2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMaeInjury);
                List<String> LMaeList = new ArrayList<>();
                if (LMaenormal2.isChecked()) {
                    LMaeList.add(LMaenormal2.getText().toString());
                }
                if (LMaeHeamatoma2.isChecked()) {
                    LMaeList.add(LMaeHeamatoma2.getText().toString());
                }
                if (LMaeOtitisexerna2.isChecked()) {
                    LMaeList.add(LMaeOtitisexerna2.getText().toString());
                }
                if (LMaeCanalstenosis2.isChecked()) {
                    LMaeList.add(LMaeCanalstenosis2.getText().toString());
                }
                if (LMaeOtorhea2.isChecked()) {
                    LMaeList.add(LMaeOtorhea2.getText().toString());
                }
                if (LMaeWax2.isChecked()) {
                    LMaeList.add(LMaeWax2.getText().toString());
                }
                if (LMaeAuralpolyps2.isChecked()) {
                    LMaeList.add(LMaeAuralpolyps2.getText().toString());
                }
                if (LMaeMass2.isChecked()) {
                    LMaeList.add(LMaeMass2.getText().toString());
                }
                if (LMaeexternalobject2.isChecked()) {
                    LMaeList.add(LMaeexternalobject2.getText().toString());
                }
                if (LMaeInjury2.isChecked()) {
                    LMaeList.add(LMaeInjury2.getText().toString());
                }
                if (LMaeList.size() > 0) {
                    cv.put("LMaeStatus", TextUtils.join(", ", LMaeList));
                }
                cv.put("RMaeOther", ((EditText) this.goshRast.getView().findViewById(R.id.RMaeOther)).getText().toString());
                cv.put("LMaeOther", ((EditText) this.tjvz.getView().findViewById(R.id.LMaeOther)).getText().toString());
                CheckBox Rtashkhisnormal2 = (CheckBox) this.goshRast.getView().findViewById(R.id.Rtashkhisnormal);
                CheckBox RtashkhisAOM2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisAOM);
                CheckBox RtashkhisOME2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisOME);
                CheckBox RtashkhisCSOM2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisCSOM);
                CheckBox RtashkhisHearingloss2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisHearingloss);
                CheckBox RtashkhisExostoses2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisExostoses);
                CheckBox RtashkhisOtitisextemabactaria2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisOtitisextemabactaria);
                CheckBox RtashkhisOtitisextemafungal2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisOtitisextemafungal);
                CheckBox RtashkhisCholesteatoma2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisCholesteatoma);
                CheckBox RtashkhisTMretraction2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RtashkhisTMretraction);
                ArrayList arrayList4 = new ArrayList();
                if (Rtashkhisnormal2.isChecked()) {
                    arrayList4.add(Rtashkhisnormal2.getText().toString());
                }
                if (RtashkhisAOM2.isChecked()) {
                    arrayList4.add(RtashkhisAOM2.getText().toString());
                }
                if (RtashkhisOME2.isChecked()) {
                    arrayList4.add(RtashkhisOME2.getText().toString());
                }
                if (RtashkhisCSOM2.isChecked()) {
                    arrayList4.add(RtashkhisCSOM2.getText().toString());
                }
                if (RtashkhisHearingloss2.isChecked()) {
                    arrayList4.add(RtashkhisHearingloss2.getText().toString());
                }
                if (RtashkhisExostoses2.isChecked()) {
                    arrayList4.add(RtashkhisExostoses2.getText().toString());
                }
                if (RtashkhisOtitisextemabactaria2.isChecked()) {
                    arrayList4.add(RtashkhisOtitisextemabactaria2.getText().toString());
                }
                if (RtashkhisOtitisextemafungal2.isChecked()) {
                    arrayList4.add(RtashkhisOtitisextemafungal2.getText().toString());
                }
                if (RtashkhisCholesteatoma2.isChecked()) {
                    arrayList4.add(RtashkhisCholesteatoma2.getText().toString());
                }
                if (RtashkhisTMretraction2.isChecked()) {
                    arrayList4.add(RtashkhisTMretraction2.getText().toString());
                }
                if (arrayList4.size() > 0) {
                    cv.put("RDiagnosisStatus", TextUtils.join(", ", arrayList4));
                }
                CheckBox Ltashkhisnormal2 = (CheckBox) this.tjvz.getView().findViewById(R.id.Ltashkhisnormal);
                CheckBox LtashkhisAOM2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisAOM);
                CheckBox LtashkhisOME2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisOME);
                CheckBox LtashkhisCSOM2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisCSOM);
                CheckBox LtashkhisHearingloss2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisHearingloss);
                CheckBox LtashkhisExostoses2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisExostoses);
                CheckBox LtashkhisOtitisextemabactaria2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisOtitisextemabactaria);
                CheckBox LtashkhisOtitisextemafungal2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisOtitisextemafungal);
                CheckBox LtashkhisCholesteatoma2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisCholesteatoma);
                CheckBox LtashkhisTMretraction2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LtashkhisTMretraction);
                ArrayList arrayList5 = new ArrayList();
                if (Ltashkhisnormal2.isChecked()) {
                    arrayList5.add(Ltashkhisnormal2.getText().toString());
                }
                if (LtashkhisAOM2.isChecked()) {
                    arrayList5.add(LtashkhisAOM2.getText().toString());
                }
                if (LtashkhisOME2.isChecked()) {
                    arrayList5.add(LtashkhisOME2.getText().toString());
                }
                if (LtashkhisCSOM2.isChecked()) {
                    arrayList5.add(LtashkhisCSOM2.getText().toString());
                }
                if (LtashkhisHearingloss2.isChecked()) {
                    arrayList5.add(LtashkhisHearingloss2.getText().toString());
                }
                if (LtashkhisExostoses2.isChecked()) {
                    arrayList5.add(LtashkhisExostoses2.getText().toString());
                }
                if (LtashkhisOtitisextemabactaria2.isChecked()) {
                    arrayList5.add(LtashkhisOtitisextemabactaria2.getText().toString());
                }
                if (LtashkhisOtitisextemafungal2.isChecked()) {
                    arrayList5.add(LtashkhisOtitisextemafungal2.getText().toString());
                }
                if (LtashkhisCholesteatoma2.isChecked()) {
                    arrayList5.add(LtashkhisCholesteatoma2.getText().toString());
                }
                if (LtashkhisTMretraction2.isChecked()) {
                    arrayList5.add(LtashkhisTMretraction2.getText().toString());
                }
                if (arrayList5.size() > 0) {
                    cv.put("LDiagnosisStatus", TextUtils.join(", ", arrayList5));
                }
                String rd = ((EditText) this.goshRast.getView().findViewById(R.id.RDiagnosisOther)).getText().toString();
                rd.replaceAll("\n", " ");
                rd.replaceAll(System.getProperty("line.separator"), " ");
                String ld = ((EditText) this.tjvz.getView().findViewById(R.id.LDiagnosisOther)).getText().toString();
                ld.replaceAll("\n", " ");
                ld.replaceAll(System.getProperty("line.separator"), " ");
                cv.put("RDiagnosisOther", rd.toString());
                cv.put("LDiagnosisOther", ld.toString());
                CheckBox RMiddlenormal2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMiddlenormal);
                CheckBox RMiddleMEE2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMiddleMEE);
                CheckBox RMiddleEffusion2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMiddleEffusion);
                CheckBox RMiddleCOM2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMiddleCOM);
                CheckBox RMiddleAOM2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMiddleAOM);
                CheckBox RMiddleAtelectasi2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMiddleAtelectasi);
                CheckBox RMiddleOttorhea2 = (CheckBox) this.goshRast.getView().findViewById(R.id.RMiddleOttorhea);
                ArrayList arrayList6 = new ArrayList();
                if (RMiddlenormal2.isChecked()) {
                    arrayList6.add(RMiddlenormal2.getText().toString());
                }
                if (RMiddleMEE2.isChecked()) {
                    arrayList6.add(RMiddleMEE2.getText().toString());
                }
                if (RMiddleEffusion2.isChecked()) {
                    arrayList6.add(RMiddleEffusion2.getText().toString());
                }
                if (RMiddleCOM2.isChecked()) {
                    arrayList6.add(RMiddleCOM2.getText().toString());
                }
                if (RMiddleAOM2.isChecked()) {
                    arrayList6.add(RMiddleAOM2.getText().toString());
                }
                if (RMiddleAtelectasi2.isChecked()) {
                    arrayList6.add(RMiddleAtelectasi2.getText().toString());
                }
                if (RMiddleOttorhea2.isChecked()) {
                    arrayList6.add(RMiddleOttorhea2.getText().toString());
                }
                if (arrayList6.size() > 0) {
                    cv.put("RMiddleEarStatus", TextUtils.join(", ", arrayList6));
                }
                CheckBox LMiddlenormal2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMiddlenormal);
                CheckBox LMiddleMEE2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMiddleMEE);
                CheckBox LMiddleEffusion2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMiddleEffusion);
                CheckBox LMiddleCOM2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMiddleCOM);
                CheckBox LMiddleAOM2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMiddleAOM);
                CheckBox LMiddleAtelectasi2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMiddleAtelectasi);
                CheckBox LMiddleOttorhea2 = (CheckBox) this.tjvz.getView().findViewById(R.id.LMiddleOttorhea);
                ArrayList arrayList7 = new ArrayList();
                if (LMiddlenormal2.isChecked()) {
                    arrayList7.add(LMiddlenormal2.getText().toString());
                }
                if (LMiddleMEE2.isChecked()) {
                    arrayList7.add(LMiddleMEE2.getText().toString());
                }
                if (LMiddleEffusion2.isChecked()) {
                    arrayList7.add(LMiddleEffusion2.getText().toString());
                }
                if (LMiddleCOM2.isChecked()) {
                    arrayList7.add(LMiddleCOM2.getText().toString());
                }
                if (LMiddleAOM2.isChecked()) {
                    arrayList7.add(LMiddleAOM2.getText().toString());
                }
                if (LMiddleAtelectasi2.isChecked()) {
                    arrayList7.add(LMiddleAtelectasi2.getText().toString());
                }
                if (LMiddleOttorhea2.isChecked()) {
                    arrayList7.add(LMiddleOttorhea2.getText().toString());
                }
                if (arrayList7.size() > 0) {
                    cv.put("LMiddleEarStatus", TextUtils.join(", ", arrayList7));
                }
                String drc = DoctorComment2.getText().toString();
                drc.replaceAll("\n", " ");
                drc.replaceAll(System.getProperty("line.separator"), " ");
                cv.put("DoctorComment", drc.toString());
                this.db = new DatabaseHelper(this, AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
                this.mSqlidb = this.db.getWritableDatabase();
                Snackbar.make(getWindow().getDecorView(), (CharSequence) "اطلاعات با موفقیت ثبت شد", -1).show();
                try {
                    long update = (long) this.mSqlidb.update("clinicalinfos", cv, "clinicalinfosid=" + balinidata.fileint, (String[]) null);
                    av.put("answer", "10");
                    long update2 = (long) this.mSqlidb.update("forwards", av, "forwardid=" + CartableAdapter.fid, (String[]) null);
                    new SendPostRequest().execute(new String[0]);
                    startActivity(new Intent(this, Cartable.class));
                } catch (Exception e) {
                    Toast.makeText(this, "خطایی رخ داده است ! اطلاعات  ثبت نشد", 0).show();
                }
            }
        } catch (Exception e2) {
            Toast.makeText(this, "خطایی رخ داده است !لطفا نظر پزک را کامل کنید و سپس کمی صبر نمایید و بعد اقدام به ثبت مجدد کنید", 0).show();
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
                DatabaseHelper unused = CartableView.this.db = new DatabaseHelper(CartableView.this, AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
                SQLiteDatabase unused2 = CartableView.this.mSqlidb = CartableView.this.db.getReadableDatabase();
                Cursor c = CartableView.this.mSqlidb.rawQuery("SELECT * FROM clinicalinfos WHERE clinicalinfosid  =" + balinidata.fileint, (String[]) null);
                c.moveToFirst();
                int count = c.getCount();
                String RTympanicColor = c.getString(c.getColumnIndex("RTympanicColor"));
                String RTympanicStatus = c.getString(c.getColumnIndex("RTympanicStatus"));
                String RTympanicLocation = c.getString(c.getColumnIndex("RTympanicLocation"));
                String LTympanicColor = c.getString(c.getColumnIndex("LTympanicColor"));
                String LTympanicStatus = c.getString(c.getColumnIndex("LTympanicStatus"));
                String LTympanicLocation = c.getString(c.getColumnIndex("LTympanicLocation"));
                String RTmStatus = c.getString(c.getColumnIndex("RTmStatus"));
                String RTmOther = c.getString(c.getColumnIndex("RTmOther"));
                String LTmStatus = c.getString(c.getColumnIndex("LTmStatus"));
                String LTmOther = c.getString(c.getColumnIndex("LTmOther"));
                String RMaeStatus = c.getString(c.getColumnIndex("RMaeStatus"));
                String RMaeOther = c.getString(c.getColumnIndex("RMaeOther"));
                String LMaeStatus = c.getString(c.getColumnIndex("LMaeStatus"));
                String LMaeOther = c.getString(c.getColumnIndex("LMaeOther"));
                String RDiagnosisStatus = c.getString(c.getColumnIndex("RDiagnosisStatus"));
                String RDiagnosisOther = c.getString(c.getColumnIndex("RDiagnosisOther"));
                String LDiagnosisStatus = c.getString(c.getColumnIndex("LDiagnosisStatus"));
                String LDiagnosisOther = c.getString(c.getColumnIndex("LDiagnosisOther"));
                String RMiddleEarStatus = c.getString(c.getColumnIndex("RMiddleEarStatus"));
                String RMiddleEarOther = c.getString(c.getColumnIndex("RMiddleEarOther"));
                String LMiddleEarStatus = c.getString(c.getColumnIndex("LMiddleEarStatus"));
                String LMiddleEarOther = c.getString(c.getColumnIndex("LMiddleEarOther"));
                String DoctorComment = c.getString(c.getColumnIndex("DoctorComment"));
                URL url = new URL("http://teleotology.ir/users/updateclinicalinfos");
                JSONObject postDataParams = new JSONObject();
                int pid = balinidata.fileint;
                String uid = Ashanet.userid;
                String tkn = Ashanet.token;
                postDataParams.put("PostId", pid);
                postDataParams.put("ForwardId", CartableAdapter.fid);
                postDataParams.put("uid", uid);
                postDataParams.put("tkn", tkn);
                postDataParams.put("RTympanicColor", RTympanicColor);
                postDataParams.put("RTympanicStatus", RTympanicStatus);
                postDataParams.put("RTympanicLocation", RTympanicLocation);
                postDataParams.put("LTympanicColor", LTympanicColor);
                postDataParams.put("LTympanicStatus", LTympanicStatus);
                postDataParams.put("LTympanicLocation", LTympanicLocation);
                postDataParams.put("RTmStatus", RTmStatus);
                postDataParams.put("RTmOther", RTmOther);
                postDataParams.put("LTmStatus", LTmStatus);
                postDataParams.put("LTmOther", LTmOther);
                postDataParams.put("RMaeStatus", RMaeStatus);
                postDataParams.put("RMaeOther", RMaeOther);
                postDataParams.put("LMaeStatus", LMaeStatus);
                postDataParams.put("LMaeOther", LMaeOther);
                postDataParams.put("RDiagnosisStatus", RDiagnosisStatus);
                postDataParams.put("RDiagnosisOther", RDiagnosisOther);
                postDataParams.put("LDiagnosisStatus", LDiagnosisStatus);
                postDataParams.put("LDiagnosisOther", LDiagnosisOther);
                postDataParams.put("RMiddleEarStatus", RMiddleEarStatus);
                postDataParams.put("RMiddleEarOther", RMiddleEarOther);
                postDataParams.put("LMiddleEarStatus", LMiddleEarStatus);
                postDataParams.put("LMiddleEarOther", LMiddleEarOther);
                postDataParams.put("DoctorComment", DoctorComment);
                Log.e("params", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(CartableView.this.getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    return new String("false : " + responseCode);
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer("");
                String line = bufferedReader.readLine();
                if (line != null) {
                    stringBuffer.append(line);
                }
                bufferedReader.close();
                return stringBuffer.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            String str = result.toString();
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
