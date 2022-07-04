package com.doctor.ashanet.ashanet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class imagesdata extends Fragment {
    private ImageAdapter adapters;
    private DatabaseHelper db;
    ImageView imageView;
    boolean isImageFitToScreen;
    private List<ImageLiseItem> items = new ArrayList();
    private SQLiteDatabase mSqlidb;
    String name;
    private RecyclerView recyclerView;
    public int userids = CartableAdapter.userid;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ptimages, container, false);
        ImageView imageView2 = (ImageView) rootView.findViewById(R.id.imgs);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rviewimage);
        this.adapters = new ImageAdapter(this.items);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapters);
        String str = " select * from clinicalinfos where clinicalinfosid =  '" + this.userids;
        this.db = new DatabaseHelper(getActivity(), AppConfig.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, AppConfig.DATABASE_VERSION.intValue());
        this.mSqlidb = this.db.getReadableDatabase();
        Cursor c = this.mSqlidb.rawQuery("SELECT * FROM clinicalinfos WHERE clinicalinfosid  =" + this.userids, (String[]) null);
        if (c.moveToFirst()) {
            if (this.name == "rast" && CartableView.load_right == 1) {
                for (String item : c.getString(c.getColumnIndex("EarRight")).split(",")) {
                    if (item != "" && !item.matches("") && !item.isEmpty()) {
                        String input = ("http://teleotology.ir/documents/" + item).replace(" ", "");
                        Toast.makeText(getActivity(), "درحال بارگزاری تصاویر!این فرایند ممکن است زمان بر باشد.لطفا شکیبا باشید", 1).show();
                        right_setdata(input, "گوش راست");
                    }
                }
                this.adapters.notifyDataSetChanged();
                CartableView.load_right = 2;
            }
            if (this.name == "chap" && CartableView.load_left == 1) {
                for (String item2 : c.getString(c.getColumnIndex("EarLeft")).split(",")) {
                    if (item2 != "" && !item2.matches("") && !item2.isEmpty()) {
                        String input2 = ("http://teleotology.ir/documents/" + item2).replace(" ", "");
                        Toast.makeText(getActivity(), "درحال بارگزاری تصاویر!این فرایند ممکن است زمان بر باشد.لطفا شکیبا باشید", 1).show();
                        left_setdata(input2, "گوش چپ");
                    }
                }
                this.adapters.notifyDataSetChanged();
                CartableView.load_left = 2;
            }
        }
        return rootView;
    }

    public void right_setdata(String url, String title) {
        if (CartableView.load_right == 1) {
            this.items.add(new ImageLiseItem(url, title));
        }
    }

    public void left_setdata(String url, String title) {
        if (CartableView.load_left == 1) {
            this.items.add(new ImageLiseItem(url, title));
        }
    }
}
