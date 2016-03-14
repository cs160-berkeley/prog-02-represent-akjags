package com.example.akshayjagadeesh.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

//import com.twitter.sdk.android.core.Callback;
//import com.twitter.sdk.android.core.models.Tweet;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.tweetui.TweetUtils;
//import com.twitter.sdk.android.tweetui.TweetView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    EditText zipText;
    TextView outputText, congressman;
    ImageButton currLoc;
    TableLayout congressTable;
    TableRow r1, r2, r3, r4;
    MainActivity act;
    String rep, party;
    GoogleApiClient mGoogleApiClient;
    public Boolean isCurrLoc;
    String filepath = "/Users/akshayjagadeesh/Documents/Berkeley/Spring_2016/CS160/Represent/mobile/src/main/java/com/example/akshayjagadeesh/represent";

    Bitmap b1, b2, b3, b4;
    public final static String REPRESENTATIVE = "com.example.akshayjagadeesh.represent.REPRESENTATIVE";
    public final static String PARTY = "com.example.akshayjagadeesh.represent.PARTY";
    public final static String BITMAP = "com.example.akshayjagadeesh.represent.BITMAP";
    public final static String BIOGUIDE = "com.example.akshayjagadeesh.represent.BIOGUIDE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isCurrLoc = false;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        outputText = (TextView)findViewById(R.id.outputText);
        zipText = (EditText)findViewById(R.id.zipText);
        congressTable = (TableLayout)findViewById(R.id.congressTable);
        r1 = (TableRow)findViewById(R.id.row1);
        r2 = (TableRow)findViewById(R.id.row2);
        r3 = (TableRow)findViewById(R.id.row3);
        r4 = (TableRow)findViewById(R.id.row4);

        act = this;

        congressTable.setVisibility(View.INVISIBLE);
        r4.setVisibility(View.INVISIBLE);

        zipText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable e) {
                String t = e.toString();
                ImageView i1, i2, i3;
                if (e.length() == 5 || isCurrLoc == true) {

                    APIConnector api;

                    System.out.println("EDIT TEXT: " + t);
                    if (isCurrLoc) {
                        String[] split = t.substring(1, t.length() - 1).split(",");
                        System.out.println("Latitude: " + split[0]);
                        System.out.println("Longitude: " + split[1]);
                        api = new APIConnector(split[0], split[1]);
                    } else {
                        api = new APIConnector(t);
                    }

                    String[] urls = {api.url};
                    api.execute(urls);

                    i1 = (ImageView) findViewById(R.id.i1);
                    i2 = (ImageView) findViewById(R.id.i2);
                    i3 = (ImageView) findViewById(R.id.i3);

                    Congressman[] congress = api.congress;
                    while (congress[0] == null || congress[1] == null || congress[2] == null) {
                        android.os.SystemClock.sleep(500);
                        congress = api.congress;
                    }

                    setTextView(R.id.tweet1, "@" + congress[0].twitterID + ": ICYMI: Impt work by @SFChronicle highlighting #AIDS survivors. Let’s continue our fight for an AIDS-free generation.");
                    setTextView(R.id.tweet2, "@" + congress[1].twitterID + ": Proud to represent #CA17 & to use my position on the powerful Appropriations Committee to bring $1.3 billion to #SV ");
                    setTextView(R.id.tweet3, "@" + congress[2].twitterID + ": Learn more about the Personal Care Products Safety Act here: http://1.usa.gov/1mda5Jg  #SafeProducts #ExpoWest");
                    setTextView(R.id.congressman1, congress[0].title + " " + congress[0].name);
                    setTextView(R.id.congressman2, congress[1].title + " " + congress[1].name);
                    setTextView(R.id.congressman3, congress[2].title + " " + congress[2].name);
                    setTextView(R.id.email1, "Website: " + congress[0].website + "\nEmail Address: " + congress[0].emailAddress);
                    setTextView(R.id.email2, "Website: " + congress[1].website + "\nEmail Address: " + congress[1].emailAddress);
                    setTextView(R.id.email3, "Website: " + congress[2].website + "\nEmail Address: " + congress[2].emailAddress);
                    setTextView(R.id.party1, congress[0].party + ", " + congress[0].state + congress[0].district);
                    setTextView(R.id.party2, congress[1].party + ", " + congress[1].state + congress[1].district);
                    setTextView(R.id.party3, congress[2].party + ", " + congress[2].state + congress[2].district);

                    i1.setImageBitmap(congress[0].bitmap);
                    b1 = congress[0].bitmap;
                    i2.setImageBitmap(congress[1].bitmap);
                    b2 = congress[1].bitmap;
                    i3.setImageBitmap(congress[2].bitmap);
                    b3 = congress[2].bitmap;

                    if (congress[3] != null) {
                        r4.setVisibility(View.VISIBLE);
                        ImageView i4 = (ImageView) findViewById(R.id.i4);
                        setTextView(R.id.congressman4, congress[3].title + " " + congress[3].name);
                        setTextView(R.id.email4, "Website: " + congress[3].website + "\nEmail Address: " + congress[0].emailAddress);
                        i4.setImageBitmap(congress[3].bitmap);
                        b4 = congress[3].bitmap;
                        setTextView(R.id.tweet4, congress[3].twitterID + ": No surprise that @CarlyFiorina has endorsed a radical candidate who tried to shut down the government over Planned Parenthood funding.");
                        clickRow(R.id.row4, R.id.party4, R.id.congressman4, b4, congress[3].bioguide, congress[3].term_start, congress[3].term_end);
                    } else {
                        r4.setVisibility(View.INVISIBLE);
                    }
                    if (congressTable.getVisibility() != View.VISIBLE) {
                        congressTable.setVisibility(View.VISIBLE);
                        System.out.println(congress[0].name);
                    }
                    clickRow(R.id.row1, R.id.party1, R.id.congressman1, b1, congress[0].bioguide, congress[0].term_start, congress[0].term_end);
                    clickRow(R.id.row2, R.id.party2, R.id.congressman2, b2, congress[1].bioguide, congress[1].term_start, congress[1].term_end);
                    clickRow(R.id.row3, R.id.party3, R.id.congressman3, b3, congress[2].bioguide, congress[2].term_start, congress[2].term_end);

                }
                if (e.length() != 5 && isCurrLoc == false) {
                    if (congressTable.getVisibility() != View.INVISIBLE) {
                        congressTable.setVisibility(View.INVISIBLE);
                    }
                }
                isCurrLoc = false;
            }

            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        currLoc = (ImageButton)findViewById(R.id.locButton);
        currLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (myLocation != null){
                    isCurrLoc = true;
                    getSetCounty("" + myLocation.getLatitude(), "" + myLocation.getLongitude());
                    zipText.setText("(" + myLocation.getLatitude() + "," + myLocation.getLongitude() + ")");
                }
            }
        });

        Button election = (Button)findViewById(R.id.electionButton);
        election.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("election button has been clicked");
                Intent i = new Intent(getBaseContext(), ElectActivity.class);
                i.putExtra("County", "Santa Clara");
                startActivity(i);
                System.out.println("activity started");
            }
        });
    }

    public void getSetCounty(String latitude, String longitude){
        APIConnector3 countyAPI = new APIConnector3(latitude, longitude, true);
        String[] urlArray = {countyAPI.url};
        System.out.println("URL: " + countyAPI.url);
        countyAPI.execute(urlArray);

        while (! (countyAPI.getCounty().length() > 0)) {
            android.os.SystemClock.sleep(500);
        }

        TextView county = (TextView) findViewById(R.id.county);
        System.out.println("api output: " + countyAPI.getCounty());
        county.setText(countyAPI.getCounty());
    }

    @Override
    public void onConnected(Bundle connectionHint){
        System.out.println("Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            // Blank for a moment...
        }
        else {
            System.out.println("!!!! Latitude: !!!" + location.getLatitude());
            System.out.println("!!!! Longitude:!!!" + location.getLongitude());
        };

    }



    public void onConnectionSuspended(int a){
        System.out.println("Location services suspended.");
    }

    public void onConnectionFailed(ConnectionResult c){

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public void setTextView(int textView_id, String textToSet){
        TextView tv = (TextView)findViewById(textView_id);
        tv.setText(textToSet);
    }

    public void clickRow(int rowId, int partyId, int congressmanId, Bitmap b, String bg, String term_start, String term_end){
        TableRow row = (TableRow)findViewById(rowId);
        final int par = partyId;
        final int con = congressmanId;
        final String bioguide = bg;

        final String repDates = "Term Start: " + term_start + "\nTerm End: " + term_end;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (b!=null) {
            b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        final byte[] byteArray = stream.toByteArray();


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Intent weartent = new Intent(act, PhoneToWatchService.class);
                Intent intent = new Intent(act, RepDetailActivity.class);
                party = ((TextView)findViewById(par)).getText().toString();
                rep = ((TextView)findViewById(con)).getText().toString();
                intent.putExtra(REPRESENTATIVE, rep);
                intent.putExtra(PARTY, party);
                intent.putExtra(BITMAP, byteArray);
                intent.putExtra(BIOGUIDE, bioguide);
                intent.putExtra("repDates", repDates);
//                weartent.putExtra(REPRESENTATIVE, rep);
//                weartent.putExtra(PARTY, party);
//                weartent.putExtra(BITMAP, byteArray);

                startActivity(intent);
                //startService(weartent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
