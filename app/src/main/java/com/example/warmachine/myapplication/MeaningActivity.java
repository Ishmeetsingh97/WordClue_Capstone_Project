package com.example.warmachine.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.vstechlab.easyfonts.EasyFonts;

public class MeaningActivity extends AppCompatActivity {

    static String[] value;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Intent intent = getIntent();
        value = intent.getStringArrayExtra("strings");
        String bullet = getResources().getString(R.string.bullet);

        if (value[1] != null) {
            TextView word = (TextView) findViewById(R.id.wordd);
            word.setText(value[1]);
            word.setTypeface(EasyFonts.robotoMediumItalic(this));
    }
        else{
            TextView word = (TextView) findViewById(R.id.wordd);
            word.setText(getResources().getString(R.string.NoResult));
            word.setTypeface(EasyFonts.robotoMediumItalic(this));
        }
        if (value[0] != null) {
            TextView type = (TextView) findViewById(R.id.type);
            type.setText(" "+value[0]);
            type.setTypeface(EasyFonts.robotoBoldItalic(this));
        }
        if (value[2] != null) {
            TextView fMean = (TextView) findViewById(R.id.fMean);
            fMean.setText(bullet+value[2]);
            fMean.setTypeface(EasyFonts.caviarDreamsBold(this));
            }
        if (value[3] != null) {
            TextView mMean1 = (TextView) findViewById(R.id.mMean1);
            mMean1.setText(bullet+ value[3]);
            mMean1.setTypeface(EasyFonts.caviarDreamsBold(this));
                }
        if (value[4] != null) {
            TextView mMean2 = (TextView) findViewById(R.id.mMean2);
            mMean2.setText( bullet+value[4]);
            mMean2.setTypeface(EasyFonts.caviarDreamsBold(this));
                    }

        if (value[5] != null) {
        TextView mMean3 = (TextView) findViewById(R.id.mMean3);
        mMean3.setText(bullet+value[5]);
        mMean3.setTypeface(EasyFonts.caviarDreamsBold(this));
                        }



    }

}
