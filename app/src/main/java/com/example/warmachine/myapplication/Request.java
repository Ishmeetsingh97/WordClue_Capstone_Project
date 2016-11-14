package com.example.warmachine.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by WarMachine on 10/26/2016.
 */
public class Request {
    private static final String LOG_TAG = Request.class.getSimpleName() ;
    static String fMean;
    static String type;
    static String mWord;
    static String mMean1;
    static String mMean2;
    static String mMean3;


    public Request(){

    }

    public static URL createUrl(String stringUrl){

        URL url = null;
        try{
            url = new URL(stringUrl);
        }
        catch (Exception e){

        }
        return url;
    }

    public static String makeHttpRequest(URL url){

        HttpURLConnection urlConnection;
        InputStream inputStream;
        String jsonResponse = " ";

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        }catch(Exception e){

        }

        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static String[] jsonParse(String jsonString) {

        String[] arrl = new String[6];
        Log.v(LOG_TAG,"data: " + jsonString);
        try {

            JSONObject root = new JSONObject(jsonString);
            JSONArray arr = root.getJSONArray("def");
            JSONObject second = arr.getJSONObject(0);

            //word
            mWord = second.getString("text");
            arrl[0]=mWord;

            //type
            JSONArray tr = second.getJSONArray("tr");
            JSONObject third = tr.getJSONObject(0);
            type = third.getString("pos");
            arrl[1]=type;

            //fMean
            fMean = third.getString("text");
            arrl[2]=fMean;

            //mMean1
            JSONArray syn = third.getJSONArray("syn");
            JSONObject fSyn = syn.getJSONObject(0);
            mMean1 = fSyn.getString("text");
            arrl[3]=mMean1;

            //mMean2
            JSONObject fSyn2 = syn.getJSONObject(1);
            mMean2 = fSyn2.getString("text");
            arrl[4]=mMean2;

            //mMean3
            JSONObject fSyn3 = syn.getJSONObject(2);
            mMean3 = fSyn3.getString("text");
            arrl[5]=mMean3;

        } catch (Exception e) {

        }
        Log.v(LOG_TAG,"Parsed data: " + fMean);
        return arrl;
    }

}
