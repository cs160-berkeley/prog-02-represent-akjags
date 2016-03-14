package com.example.akshayjagadeesh.represent;

import android.graphics.Bitmap;

/**
 * Created by akshayjagadeesh on 3/11/16.
 */
public class Congressman {

    public String title; // "rep" or "sen"
    public String name; // "FirstName LastName"
    public String website;
    public String emailAddress;
    public String district; // "null" if Senator
    public String state; // 2 letter acronym
    public String bioguide;
    public Bitmap bitmap;
    public String party; // "Democrat" or "Republican"
    public String term_end;
    public String term_start;
    public String twitterID;

    public static int numCongressmen = 0;

    public Congressman(){
        this.title = "";
        this.name = "";
        this.website = "";
        this.emailAddress = "";
        this.district = "";
        this.state = "";
        this.bioguide="";
        this.bitmap = null;
        this.term_end = "";
        this.term_start = "";
        this.twitterID = "";
        numCongressmen = numCongressmen+1;
    }

}
