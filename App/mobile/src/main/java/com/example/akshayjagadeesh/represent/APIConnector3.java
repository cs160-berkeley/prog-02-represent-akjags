package com.example.akshayjagadeesh.represent;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by akshayjagadeesh on 3/11/16.
 */

public class APIConnector3 extends AsyncTask<String, String, JsonReader> {
    String url;
    Boolean getCounty;
    String county;

    public APIConnector3(String latitude, String longitude, Boolean convertToCounty){
        this.getCounty = true;
        this.url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude + "&sensor=true";
        System.out.println(this.url);
        this.county = "";
    }

    public String getCounty(){
        if (this.county != null){
            return this.county;
        }
        else return "";
    }

    protected JsonReader doInBackground(String ... u){
        JsonReader reader = null;
        try {
            URL url = new URL(u[0]);
            InputStream input = url.openStream();
            reader = new JsonReader(new InputStreamReader(input));
        }
        catch(Exception ex){System.out.println(ex);}
        System.out.println("in background");
        this.county = parseJSONCounty(reader);
        return reader;
    }

    public String parseJSONCounty(JsonReader reader){
        String countyName = "";
        try{
            reader.beginObject();
            System.out.println("Results: " + reader.nextName()); // should equal results
            reader.beginArray();
            reader.beginObject();
            System.out.println("Address_Components: " +reader.nextName());
            reader.beginArray();
            while(reader.hasNext()){
                System.out.println("peek1: " + reader.peek());
                reader.beginObject();
                while(reader.hasNext()) {
                    System.out.println("peek2: " + reader.peek());
                    String name = reader.nextName();
                    System.out.println("next name " + name);
                    if (name.equals("long_name")) {
                        countyName = reader.nextString();
                        System.out.println(countyName);
                    } else if (name.equals("types")) {
                        reader.beginArray();
                        while(reader.hasNext()) {
                            if(reader.nextString().equals("administrative_area_level_2")){
                                return countyName;
                            }
                        }
                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            }
        }
        catch(Exception ex) {System.out.println(ex);}
        return countyName;
    }
}
