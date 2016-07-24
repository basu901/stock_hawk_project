package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Charts extends AppCompatActivity {

    //ArrayList<Float> dataList=(ArrayList<Float>)getIntent().getSerializableExtra("data");
    ArrayList<String> labels=new ArrayList<>();
    LineChartView lineChartView;
    String file_name="Stock_data";
    HashMap<String,Float> stock_data=new HashMap<>();
    String[] mlabels;
    float[] mdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        try{
            FileInputStream fis=openFileInput(file_name);
            ObjectInputStream ois=new ObjectInputStream(fis);
            Object ob=ois.readObject();
            stock_data=(HashMap<String,Float>)ob;

        }catch(FileNotFoundException e){
            Log.v(Charts.class.getSimpleName(), "No file Found");
        }
        catch(IOException e){

        }
        catch(ClassNotFoundException f){

        }
        mlabels=new String[stock_data.size()];
        mdata=new float[stock_data.size()];
        String label;
        int i=0;
        Iterator iterator=stock_data.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry=(Map.Entry)iterator.next();
            label=(String)entry.getKey();
            label=label.substring(5);
            mlabels[i]=label;
            mdata[i]=(float)entry.getValue();
            i++;
            Log.v(Charts.class.getSimpleName(),Integer.toString(i));
            iterator.remove();
        }

        TextView stock_symbol=(TextView)findViewById(R.id.stock_symbol);
        String stock=getIntent().getExtras().getString("symbol");
        stock_symbol.setText(stock);

        TextView start_Date=(TextView)findViewById(R.id.start_date);
        String startDate=getIntent().getExtras().getString("startDate");
        start_Date.setText(startDate);

        TextView end_Date=(TextView)findViewById(R.id.end_date);
        String endDate=getIntent().getExtras().getString("endDate");
        end_Date.setText(endDate);

        lineChartView=(LineChartView)findViewById(R.id.linechart);
        LineSet dataset=new LineSet(mlabels,mdata);
        dataset.setColor(Color.parseColor("#F9FBE7"))
                .setDotsColor(Color.parseColor("#8BC34A"))
                .setThickness(2);
        lineChartView.addData(dataset);
        lineChartView.show();

    }
}

/*
public class Charts extends AppCompatActivity {
    StringBuilder detailsBuilder = new StringBuilder();
    String detailsUrl = "";
    String body;
    int checker = 0;
    HashMap<String, Float> stockData = new HashMap<>();
    LineChartView lineChartView;
    String[] mlabels;
    float[] mdata;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        String symbol=getIntent().getExtras().getString("symbol");
        ChartsInner chartsInner=new ChartsInner();
        chartsInner.execute(symbol);
    }



    class ChartsInner extends AsyncTask<String, Void, Void> {



        protected Void doInBackground(String... params) {

            OkHttpClient client;
            HttpURLConnection urlConnection = null;
            BufferedReader reader=null;
            ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                checker = 1;
                try {

                    //client = new OkHttpClient();
                    detailsBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
                    try {
                        Log.v("CHECKING PARAMS:", params[0]);
                        detailsUrl = URLEncoder.encode("select * from yahoo.finance.historicaldata where symbol = \"" + params[0] + "\" and startDate = \"2016-05-18\"" +
                                " and endDate = \"2016-06-17\"", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    detailsBuilder.append(detailsUrl);
                    detailsBuilder.append("&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.\"\n" +
                            "            + \"org%2Falltableswithkeys&callback=");

                    // Request request = new Request.Builder().url(detailsBuilder.toString()).build();
                    //Response response = client.newCall(request).execute();
                    //body = response.body().string();


                    URL stockURL = new URL(detailsBuilder.toString());
                    urlConnection = (HttpURLConnection) stockURL.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() == 0) {
                        return null;
                    }
                    body=buffer.toString();
                    try {
                        int j = 0;
                        JSONObject json = new JSONObject(body);
                        int count = Integer.parseInt(json.getJSONObject("query").getString("count"));
                        JSONObject results = json.getJSONObject("query").getJSONObject("results");
                        if (count == 1) {

                            JSONObject data = results.getJSONObject("quote");
                            String check = data.getString("Adj_Close");
                            mlabels[j] = (data.getString("Date"));
                            mdata[j] = Float.parseFloat(data.getString("Adj_Close"));
                            Log.v("In count==1", check);
                        } else if (count > 1) {
                            JSONArray data = results.getJSONArray("quote");
                            for (int i = 0; i < data.length(); i++) {
                                mlabels[j] = data.getJSONObject(i).getString("Date");
                                mdata[j] = Float.parseFloat(data.getJSONObject(i).getString("Adj_Close"));
                                j++;
                                String check = data.getJSONObject(i).getString("Adj_Close");
                                Log.v("In count>1", check);
                            }


                        }


                    } catch (JSONException e) {

                    }
                } catch (IOException e) {

                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader!= null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("MainActivityFragment", "Error closing stream", e);
                        }
                    }
                }
            }
            return null;

        }


        protected void onPostExecute(Void result) {

            if (checker != 1) {

                CharSequence charSequence = "No access to internet :(";
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(getApplicationContext(), charSequence, duration).show();

            } else {

              /*  Log.v("SIZE OF HASHMAPPP IS:",Integer.toString(stockData.size()));
                mlabels = new String[stockData.size()];
                mdata = new float[stockData.size()];
                int i = 0;
                Iterator iterator = stockData.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    mlabels[i] = (String) entry.getKey();
                    mdata[i] = (float) entry.getValue();
                    i++;
                    Log.v(Charts.class.getSimpleName(), Integer.toString(i));
                    iterator.remove();
                }

                for(int j=0;j<=mlabels.length;j++){
                    Log.v("IN CHARTS,mLabel is"+j,mlabels[j]);
                    Log.v("IN CHARTS,mdata is"+j,Float.toString(mdata[j]));

                }

                /*lineChartView = (LineChartView) findViewById(R.id.linechart);
                LineSet dataset = new LineSet(mlabels, mdata);
                dataset.setColor(Color.parseColor("#F9FBE7"))
                        .setDotsColor(Color.parseColor("#8BC34A"))
                        .setThickness(2)
                        .setDashed(new float[]{10f,10f});
                lineChartView.addData(dataset);
                lineChartView.show();


            }
        }

    }

}
*/

