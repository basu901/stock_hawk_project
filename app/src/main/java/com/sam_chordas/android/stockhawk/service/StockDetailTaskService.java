package com.sam_chordas.android.stockhawk.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;
//import com.sam_chordas.android.stockhawk.ui.Charts;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by cse on 18-06-2016.
 */
public class StockDetailTaskService extends GcmTaskService {

    Context mContext;
    private OkHttpClient client = new OkHttpClient();
    String detailsUrl = "";
    StringBuilder detailsBuilder = new StringBuilder();
    String class_name=StockDetailTaskService.class.getSimpleName();
    String file_name="Stock_data";
    public StockDetailTaskService(Context context){
        mContext=context;
    }
    public StockDetailTaskService(){}

    String fetchData(String url) throws IOException{
        Log.v(class_name,"Method:fetchData");
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public int onRunTask(TaskParams params) {
        Log.v(class_name,"onRunTask!!!");
        if (params.getTag().equals("details")) {
            String symbol=params.getExtras().getString("symbol");
            //Log.v("Symbol isssss:", symbol);
            String startDate=params.getExtras().getString("startDate");
            String endDate=params.getExtras().getString("endDate");
            //Log.v("StartDatestart and end:",startDate+ endDate);
            detailsBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
            try {
                detailsUrl = URLEncoder.encode("select * from yahoo.finance.historicaldata where symbol = \"" +symbol+ "\" and startDate = \""+startDate+"\"" +
                        " and endDate = \""+endDate+"\"", "UTF-8");
                Log.v("The Encoded Query: ",detailsUrl);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            detailsBuilder.append(detailsUrl);
            detailsBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.\"\n" +
                    "            + \"org%2Falltableswithkeys&callback=");

        }
        String urlString;
        String getResponse;
        int result = GcmNetworkManager.RESULT_FAILURE;
        HashMap<String,Float> stockData=new HashMap<>();

        if (detailsBuilder != null){
            urlString = detailsBuilder.toString();
           try{
                getResponse = fetchData(urlString);
                result = GcmNetworkManager.RESULT_SUCCESS;
               Log.v("RESPONSE:",getResponse);
               try{
                   JSONObject json=new JSONObject(getResponse);
                   //Log.v("Statement imp:","After queryyyyy!!");
                   JSONObject query=json.getJSONObject("query");
                   int count=Integer.parseInt(query.getString("count"));
                   JSONObject results = query.getJSONObject("results");
                  if(count==1) {

                       JSONObject data = results.getJSONObject("quote");
                       String check=data.getString("Adj_Close");
                       stockData.put(data.getString("Date"),Float.parseFloat(data.getString("Adj_Close")));
                       Log.v("In count==1",check);
                   }
                   else if(count>1){
                       JSONArray data=results.getJSONArray("quote");
                       for(int i=0;i<data.length();i++) {
                           stockData.put(data.getJSONObject(i).getString("Date"),Float.parseFloat(data.getJSONObject(i).getString("Adj_Close")));
                           String check=data.getJSONObject(i).getString("Adj_Close");
                           String date=data.getJSONObject(i).getString("Date");
                           Log.v("In count>1",check+" "+date);
                       }


                   }
                   try {
                       FileOutputStream fileOutputStream = mContext.openFileOutput(file_name, mContext.MODE_PRIVATE);
                       ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
                       oos.writeObject(stockData);
                       fileOutputStream.close();
                   }catch(FileNotFoundException e){
                       e.printStackTrace();
                   }
               }catch(JSONException e){
                   e.printStackTrace();
               }
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return result;
    }
}
