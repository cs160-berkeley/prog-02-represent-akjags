package com.example.akshayjagadeesh.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class RepDetailActivity extends AppCompatActivity {

    Intent intent;
    TextView repTitle, repParty, repDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView bills = (TextView)findViewById(R.id.bills);
        TextView committees = (TextView)findViewById(R.id.committees);

        intent = getIntent();
        repTitle = (TextView)findViewById(R.id.repTitle);
        repParty = (TextView)findViewById(R.id.repParty);
        repDates = (TextView) findViewById(R.id.repDates);
        ImageView repImg = (ImageView)findViewById(R.id.repImg);

        String rep = intent.getStringExtra(MainActivity.REPRESENTATIVE);
        String party = intent.getStringExtra(MainActivity.PARTY);
        String bioguide = intent.getStringExtra(MainActivity.BIOGUIDE);
        byte[] byteArray = intent.getByteArrayExtra(MainActivity.BITMAP);
        Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        repTitle.setText(rep);
        repParty.setText(party);
        repDates.setText(intent.getStringExtra("repDates"));
        repImg.setImageBitmap(b);

        // First get bills information
        APIConnector2 api = new APIConnector2(bioguide, 1);
        String[] urls = {api.url};
        api.execute(urls);

        // Then get committees information
        APIConnector2 api2 = new APIConnector2(bioguide, 0);
        String[] urls2 = {api2.url};
        api2.execute(urls2);

        //Delay until output has been got
        while (api.output[0] == null || api2.output[0] == null) {
            android.os.SystemClock.sleep(500);
        }

        String committeeText ="";
        for (String committee: api2.output){
            committeeText += "- " + committee + "\n\n";
        }
        committees.setText(committeeText);

        String billText = "";
        for (String bill : api.output){
            billText += "- " + bill + "\n\n";
        }
        bills.setText(billText);

    }

}
