package com.example.akshayjagadeesh.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class ElectActivity extends AppCompatActivity {

    String randCounty, randObama, randRomney, randState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("Elect Activity created.");

        Button randButton = (Button) findViewById(R.id.randbutton);

        Intent i = getIntent();
        String county = i.getStringExtra("County");

        System.out.println("County: " + county);

        TextView countyText = (TextView) findViewById(R.id.countyText);
        TextView obamaText = (TextView) findViewById(R.id.Opercent);
        TextView romneyText = (TextView) findViewById(R.id.Rpercent);

        countyText.setText(county);

        APIConnector5 api5 = new APIConnector5(county, getBaseContext());
        api5.execute();

        while (!(api5.results[0] != null)) {
            android.os.SystemClock.sleep(500);
        }
        System.out.println("done with forever while loop");

        obamaText.setText(api5.results[0]);
        romneyText.setText(api5.results[1]);

        randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int scaled = (int) (random.nextFloat() * 4000);
                System.out.println("Rand Int: " + scaled);

                JsonReader reader = null;
                try {
                    InputStream input = getAssets().open("election-county-2012.json");
                    reader = new JsonReader(new InputStreamReader(input));
                    reader.beginArray();
                    int i = 0;
                    while (reader.hasNext() && i < scaled) {
                        reader.beginObject();
                        while(reader.hasNext()) {
                            String nextName = reader.nextName();
                            if (nextName.equals("county-name")) {
                                randCounty = reader.nextString();
                            } else if(nextName.equals("state-postal")){
                                randState = reader.nextString();
                            } else if (nextName.equals("obama-percentage")) {
                                randObama = reader.nextString();
                            } else if (nextName.equals("romney-percentage")) {
                                randRomney = reader.nextString();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                        i = i + 1;
                    }
                    TextView countyText = (TextView) findViewById(R.id.countyText);
                    TextView obamaText = (TextView) findViewById(R.id.Opercent);
                    TextView romneyText = (TextView) findViewById(R.id.Rpercent);
                    countyText.setText(randCounty + ", " + randState);
                    obamaText.setText(randObama + "%");
                    romneyText.setText(randRomney + "%");
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

        });
    }
}