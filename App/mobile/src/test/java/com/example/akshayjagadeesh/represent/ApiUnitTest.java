package com.example.akshayjagadeesh.represent;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.akshayjagadeesh.represent.APIConnector;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by akshayjagadeesh on 3/11/16.
 */
public class ApiUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        String call = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=";
        String apiKey = "&apikey=677b7bcb5aa646a2b2b0e3279fb2532c";
        String u = call + "95070" + apiKey;
        URL url = new URL(u);
        InputStream web = (InputStream) url.getContent();
        System.out.println(web);
        InputStream input = url.openStream();
        System.out.println(input.toString());
        InputStreamReader i = new InputStreamReader(input, "UTF-8");
        char[] cbuf = new char[1000];
        System.out.println("" + i.read(cbuf, 0, 100));
        System.out.println(cbuf.toString());
        System.out.println(i.getEncoding());
    }
}
