package com.example.akshayjagadeesh.represent;


import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by akshayjagadeesh on 3/11/16.
 */
public class APIConnector5 extends AsyncTask<String, String, JsonReader> {
    String county;
    String[] results;
    Context context;

    public static String filename = "election-county-2012.json";

    public APIConnector5(String countyName, Context c){
        this.county = countyName;
        this.results = new String[2];
        this.context = c;
    }

    protected JsonReader doInBackground(String ... u){
        JsonReader reader = null;
        try {
            InputStream input = this.context.getAssets().open(filename);
            reader = new JsonReader(new InputStreamReader(input));
        }
        catch(Exception ex){System.out.println(ex);}
        this.results = parseJSON(reader);
        return reader;
    }

    public String[] parseJSON(JsonReader reader){
        String[] results = new String[2];
        try{
            reader.beginArray();
            while(reader.hasNext()){
                reader.beginObject();
                while(reader.hasNext()){
                    String name = reader.nextName();
                    if(name.equals("county-name")){
                        String countyname = reader.nextString();
                        System.out.println(countyname);
                        if(countyname.equals(this.county)){
                            results[0] = "";
                            results[1] = "";
                            while(reader.hasNext()){
                                String el = reader.nextName();
                                if (el.equals("obama-percentage")){
                                    results[0] = reader.nextString();
                                } else if(el.equals("romney-percentage")){
                                    results[1] = reader.nextString();
                                }
                                else{
                                    reader.skipValue();
                                }
                            }
                            return results;
                        }
                    } else{
                        reader.skipValue();
                    }
                }
                reader.endObject();
            }
        }
        catch(Exception ex) {System.out.println(ex);}
        return results;
    }
}
