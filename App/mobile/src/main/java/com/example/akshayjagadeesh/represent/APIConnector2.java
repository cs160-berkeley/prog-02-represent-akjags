package com.example.akshayjagadeesh.represent;

/**
 * Created by akshayjagadeesh on 3/11/16.
 */

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class APIConnector2 extends AsyncTask<String, String, JsonReader> {

    //JsonReader reader;
    String[] output;
    String url;
    int bills;

    public APIConnector2(String bioguide, int billsOrCommittees){
        String call1 = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=";
        String call2 = "http://congress.api.sunlightfoundation.com/committees?member_ids=";
        String apiKey = "&apikey=677b7bcb5aa646a2b2b0e3279fb2532c";
        if (billsOrCommittees == 1) {
            this.bills = 1;
            this.url = call1 + bioguide + apiKey;
        }
        else if(billsOrCommittees == 0) {
            this.bills = 0;
            this.url = call2 + bioguide + apiKey;
        }
        this.output = new String[3];
    }

    protected JsonReader doInBackground(String ... u){
        JsonReader reader = null;
        try {
            URL url = new URL(u[0]);
            InputStream input = url.openStream();
            reader = new JsonReader(new InputStreamReader(input));
        }
        catch(Exception ex){System.out.println(ex);}

        if (this.bills == 1){
            this.output = this.parseJSONbills(reader);
        }
        else{
            this.output = this.parseJSONcommittees(reader);
        }
        return reader;

    }

    protected void onPostExecute(JsonReader reader){
        System.out.println("post execute");

    }

    public String[] parseJSONbills(JsonReader reader) {
        String[] bills = new String[3];
        try {
            reader.beginObject();
            //assert(reader.nextName().equals("results"));
            reader.nextName();
            reader.beginArray();
            int i = 0;
            while(reader.hasNext() && i<3){
                reader.beginObject();
                bills[i] = "";
                while(reader.hasNext()){
                    String name = reader.nextName();
                    if(name.equals("bill_id")){
                        bills[i] += reader.nextString();
                    }
                    else if(name.equals("official_title")){
                        bills[i] += ": " + reader.nextString();
                    }
                    else{
                        reader.skipValue();
                    }
                }
                reader.endObject();
                i = i+1;
            }
            //reader.endArray();
        }
        catch(Exception ex){ System.out.println("bbb" + ex);}

        return bills;
    }


    public String[] parseJSONcommittees(JsonReader reader) {
        String[] committees = new String[3];
        try {
            reader.beginObject();
            //assert(reader.nextName().equals("results"));
            System.out.println(reader.nextName());
            reader.beginArray();
            int i = 0;
            while(reader.hasNext() && i<3){
                reader.beginObject();
                committees[i] = "";
                while(reader.hasNext()){
                    String name = reader.nextName();
                    if(name.equals("committee_id")){
                        committees[i] += reader.nextString();
                    }
                    else if(name.equals("name")){
                        committees[i] += ": " + reader.nextString();
                    }
                    else{
                        reader.skipValue();
                    }
                }
                reader.endObject();
                i = i+1;
            }
            //reader.endArray();
        }
        catch(Exception ex){ System.out.println("aaa" + ex);}

        return committees;
    }

    public static void main(String[] args){

    }

}
