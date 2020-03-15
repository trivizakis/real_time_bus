package pack.androidmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Lefteris on 22/12/2014.
 */

public class AsyncClearMap  extends Activity {

    public Context context;
    private String route;
    public ProgressDialog progressDialog;
    private GoogleMap map;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        progressDialog = new ProgressDialog(context);
    }
    public void execute(String route) {
        this.route=route;
        new AsyncSendJ().execute();
    }
    public AsyncClearMap(Context context, GoogleMap map){
        this.context = context;
        this.map =map;
    }

    private class AsyncSendJ extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            return "destroy that world!!";
        }

        @Override
        protected void onPostExecute(String result){
            map.clear();
            Log.i("<---------------------------------OnPostExecute LiveBus!-->", result);
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d("ProgressUpdate LiveBus", "You are in progress update ... " );
        }

        @Override
        protected void onPreExecute() {
            Log.d("PreExecute","Initializing LiveBus......");
        }

    }
}
