package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {
  String class_name="Class:StockIntentService";
  public StockIntentService(){
    super(StockIntentService.class.getName());
  }

  public StockIntentService(String name) {
    super(name);
  }

  @Override protected void onHandleIntent(Intent intent) {
    Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
    Log.v(class_name, "Method:onHandleIntent");

    Bundle args = new Bundle();
    if (intent.getStringExtra("tag").equals("init")){
      StockTaskService stockTaskService = new StockTaskService(this);
      stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));

    }
    if (intent.getStringExtra("tag").equals("add")){
      args.putString("symbol", intent.getStringExtra("symbol"));
      StockTaskService stockTaskService = new StockTaskService(this);
      stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
    }
    if(intent.getStringExtra("tag").equals("details")) {
      args.putString("symbol", intent.getStringExtra("symbol"));
      args.putString("endDate",intent.getStringExtra("endDate"));
      args.putString("startDate",intent.getStringExtra("startDate"));
      StockDetailTaskService stockDetailTaskService = new StockDetailTaskService(this);
      stockDetailTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
    }
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.

  }

 /* public static class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context,Intent intent){
      Log.v("Class:AlarmReceiver","Method:onReceive");
      Intent mServiceIntent = new Intent(context, StockIntentService.class);
      mServiceIntent.putExtra("tag", "init");
      context.startService(mServiceIntent);
    }

  }
  */
}
