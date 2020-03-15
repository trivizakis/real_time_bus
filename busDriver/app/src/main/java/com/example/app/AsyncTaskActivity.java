package com.example.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View.OnClickListener;

public class AsyncTaskActivity extends Activity{

    public TextView output;
    public String srvIp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ProgressBar progressBar = 	(ProgressBar) findViewById(R.id.progress);
        // because we implement OnClickListener we only have to pass "this"
        // (much easier)
        //Button btn = (Button) findViewById(R.id.button1);
        //btn.setOnClickListener(this);
        //if(findViewById(R.id.output)!= null)
        //output =(TextView)findViewById(R.id.output);
    }

/*
    @Override
    public void onClick(View view) {

        // detect the view that was "clicked"
        switch (view.getId()) {
            case R.id.button1:
            {
                output.setText("Initializing......");
                //new AsyncSendJ().execute();
                break;
            }
        }

    }*/

    public void execute(String ip) {
        srvIp = ip;
        new AsyncSendJ().execute();
    }

    // public AsyncTaskActivity(){
   // }

private class AsyncSendJ extends  AsyncTask<Void, Void, String>{

    private static final String TAG = "YEAH";
    String serverIp = "http://"+srvIp+":8080/livebus/receiveJson.php";
    private final String URL = serverIp;
    //private static final String URL = "http://79.107.7.219:8080/livebus/receiveJson.php";


    @Override
        protected String doInBackground(Void... params) {
            // JSON object to hold the information, which is sent to the server
            JSONObject jsonObjSend = new JSONObject();

            double[][] arrayLatLng = {
                    {25.124960,35.337613},
                    {25.124971,35.337556},
                    {25.124971,35.337477},
                    {25.124944,35.337398},
                    {25.124938,35.337289},
                    {25.124896,35.337236},
                    {25.124879,35.337166},
                    {25.124858,35.337109},
                    {25.124831,35.337026},
                    {25.124767,35.336965},
                    {25.124761,35.336912},
                    {25.124751,35.336864},
                    {25.124713,35.336729},
                    {25.124665,35.336676},
                    {25.124499,35.336571},
                    {25.124375,35.336527},
                    {25.124107,35.336462},
                    {25.123866,35.336449},
                    {25.123646,35.336392},
                    {25.123436,35.336400},
                    {25.123136,35.336361},
                    {25.122482,35.336278},
                    {25.122208,35.336256},
                    {25.121838,35.336212},
                    {25.121554,35.336160},
                    {25.121242,35.336138},
                    {25.121012,35.336077},
                    {25.120808,35.336050},
                    {25.120572,35.336033},
                    {25.120406,35.336020},
                    {25.120025,35.335972},
                    {25.119880,35.335941},
                    {25.119681,35.335897},
                    {25.119504,35.335858},
                    {25.119317,35.335818},
                    {25.119172,35.335775},
                    {25.119005,35.335744},
                    {25.118480,35.335621},
                    {25.118611,35.335455},
                    {25.118276,35.335595},
                    {25.118093,35.335525},
                    {25.117927,35.335525},
                    {25.117739,35.335446},
                    {25.117530,35.335407},
                    {25.117359,35.335368},
                    {25.117257,35.335280},
                    {25.117031,35.335223},
                    {25.116833,35.335061},
                    {25.116597,35.334921},
                    {25.116452,35.334829},
                    {25.116361,35.334746},
                    {25.115975,35.334523},
                    {25.115733,35.334374},
                    {25.115551,35.334230},
                    {25.115492,35.334142},
                    {25.115234,35.333888},
                    {25.115116,35.333810},
                    {25.115030,35.333692},
                    {25.114912,35.333591},
                    {25.114789,35.333481},
                    {25.114719,35.333390},
                    {25.114591,35.333315},
                    {25.114435,35.333158},
                    {25.114333,35.333066},
                    {25.114172,35.332956},
                    {25.113797,35.332654},
                    {25.113646,35.332541},
                    {25.113496,35.332427},
                    {25.113303,35.332309},
                    {25.113137,35.332212},
                    {25.113003,35.332072},
                    {25.112836,35.331954},
                    {25.112675,35.331858},
                    {25.112498,35.331757},
                    {25.112311,35.331648},
                    {25.112193,35.331560},
                    {25.111973,35.331416},
                    {25.111806,35.331333},
                    {25.111651,35.331219},
                    {25.111426,35.331044},
                    {25.111232,35.330891},
                    {25.111023,35.330759},
                    {25.110830,35.330667},
                    {25.110669,35.330580},
                    {25.110551,35.330523},
                    {25.110422,35.330453},
                    {25.110288,35.330405},
                    {25.110165,35.330365},
                    {25.109940,35.330278},
                    {25.109607,35.330169},
                    {25.109468,35.330138},
                    {25.109108,35.330059},
                    {25.108845,35.330033},
                    {25.108582,35.330024},
                    {25.108159,35.330072},
                    {25.108078,35.330072},
                    {25.107939,35.330081},
                    {25.107713,35.330077}};


        for (double[] anArrayLatLng : arrayLatLng) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                continue;
            }

            try {
                // Add key/value pairs
                //JSONObject lng= new JSONObject();
                //JSONObject lat= new JSONObject();
                //lng.put(String.valueOf(arrayLatLng[i][0]), true);
                //lat.put(String.valueOf(arrayLatLng[i][1]), true);
                // Add a nested JSONObject (e.g. for header information)
                JSONObject header = new JSONObject();
                header.put("deviceType", "Android"); // Device type
                 //json data
                jsonObjSend.put("bus_id", "1"); // Device OS version
                jsonObjSend.put("route_id", "3");    // Language of the Android client
                jsonObjSend.put("lat", String.valueOf(anArrayLatLng[1]));
                jsonObjSend.put("lng", String.valueOf(anArrayLatLng[0]));
                jsonObjSend.put("header", header);
                // Output the JSON object we're sending to Logcat:
                Log.i(TAG, jsonObjSend.toString(2));
                //onProgressUpdate();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Send the HttpPostRequest and receive a JSONObject in return
            JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL, jsonObjSend);

		/*
         *  From here on do whatever you want with your JSONObject, e.g.
		 *  1) Get the value for a key: jsonObjRecv.get("key");
		 *  2) Get a nested JSONObject: jsonObjRecv.getJSONObject("key")
		 *  3) Get a nested JSONArray: jsonObjRecv.getJSONArray("key")
		 */
        }

            return "Completed";

        }




        @Override
        protected void onPostExecute(String result) {
            Log.d("PostExecute", "On post Execute......");
           // output.setText("We are done!!!");
        }


        @Override
         protected void onProgressUpdate(Void... values) {
            Log.d("ProgressUpdate", "You are in progress update ... " );
            //output.setText("We are in progress update ... ");
        }

    @Override
        protected void onPreExecute() {
            Log.d("PreExecute","Initializing......");
            //output.setText("Initializing......");
        }

}
}