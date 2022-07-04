package com.doctor.ashanet.ashanet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class www_ashanet_ir_splash extends AppCompatActivity {
    ImageView iv;
    TextView tv;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_www_ashanet_ir_splash);
        this.iv = (ImageView) findViewById(R.id.logo);
        this.tv = (TextView) findViewById(R.id.site);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.myeffect);
        this.tv.startAnimation(myanim);
        this.iv.startAnimation(myanim);
        final Intent i = new Intent(this, MainActivity.class);
        new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    www_ashanet_ir_splash.this.startActivity(i);
                    www_ashanet_ir_splash.this.finish();
                }
            }
        }.start();
    }
}
