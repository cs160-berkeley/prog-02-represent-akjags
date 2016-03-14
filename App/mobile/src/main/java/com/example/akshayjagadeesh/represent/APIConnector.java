package com.example.akshayjagadeesh.represent;

/**
 * Created by akshayjagadeesh on 3/11/16.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class APIConnector extends AsyncTask<String, String, JsonReader> {

    //JsonReader reader;
    Congressman[] congress;
    String url;

    public APIConnector(String zipcode){
        String call = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=";
        String apiKey = "&apikey=677b7bcb5aa646a2b2b0e3279fb2532c";
        this.url = call + zipcode + apiKey;
        this.congress = new Congressman[4];
    }

    public APIConnector(String latitude, String longitude){
        this.url = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude + "&longitude=" + longitude;
        String apiKey = "&apikey=677b7bcb5aa646a2b2b0e3279fb2532c";
        this.url += apiKey;
        this.congress = new Congressman[4];
    }

    protected JsonReader doInBackground(String ... u){
        JsonReader reader = null;
        try {
            URL url = new URL(u[0]);
            InputStream input = url.openStream();
            reader = new JsonReader(new InputStreamReader(input));
        }
        catch(Exception ex){System.out.println(ex);}
        System.out.println("do in background");
        this.congress = this.parseJSON(reader);
        return reader;
    }

    protected void onPostExecute(JsonReader reader){
        System.out.println("post execute");
    }

    private Bitmap getImageBitmap(String bioguide) {
        String imgURL = "https://theunitedstates.io/images/congress/225x275/" + bioguide + ".jpg";
        Bitmap bm = null;
        try {
            URL aURL = new URL(imgURL);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return bm;
    }

    public Congressman[] parseJSON(JsonReader reader) {
        Congressman[] congress = new Congressman[4];
        try {
            reader.beginObject();
            String firstName = reader.nextName();
            reader.beginArray();
            int i = 0;
            while(reader.hasNext()){
                reader.beginObject();
                congress[i] = new Congressman();
                while(reader.hasNext()){
                    String name = reader.nextName();
                    if(name.equals("last_name") || name.equals("first_name")){
                        congress[i].name = congress[i].name + reader.nextString() + " ";
                    }
                    else if(name.equals("oc_email")){
                        congress[i].emailAddress = reader.nextString();
                    }
                    else if(name.equals("title")){
                        congress[i].title = reader.nextString();
                    }
                    else if(name.equals("website")){
                        congress[i].website = reader.nextString();
                    }
                    else if(name.equals("bioguide_id")){
                        congress[i].bioguide = reader.nextString();
                        congress[i].bitmap = getImageBitmap(congress[i].bioguide);
                    }
                    else if(name.equals("party")){
                        String party = reader.nextString();
                        if (party.equals("D")){
                            congress[i].party = "Democrat";
                        }
                        else if(party.equals("R")){
                            congress[i].party = "Republican";
                        }
                        else if (party.equals("I")){
                            congress[i].party = "Independent";
                        }
                    }
                    else if(name.equals("twitter_id")){
                        congress[i].twitterID = reader.nextString();
                    }
                    else if(name.equals("term_end")){
                        congress[i].term_end = reader.nextString();
                    }
                    else if (name.equals("term_start")){
                        congress[i].term_start = reader.nextString();
                    }
                    else if(name.equals("district")){
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull();
                        }
                        else{
                            congress[i].district = " - " + reader.nextString();
                        }
                    }
                    else if(name.equals("state")){
                        congress[i].state = reader.nextString();
                    }
                    else{
                        reader.skipValue();
                    }
                }
                reader.endObject();
                i = i+1;
            }
            reader.endArray();
        }
        catch(Exception ex){ System.out.println(ex);}

        return congress;
    }


    public static void main(String[] args){

    }

}
