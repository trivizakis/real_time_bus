package pack.androidmap;

/**
 * Created by Lefteris on 3/3/2014.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

public class requestAsync  extends Activity{
    public Context context;
    private DbAndroidMap db;
    public String serverIp;
    String table= "stops";
    String tableRoute = "routes";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
    }
    public void execute() {
        new AsyncSendJ().execute();
    }

    public requestAsync(Context context,String serverIp){
        this.serverIp=serverIp;
        this.context = context;
    }


    private class AsyncSendJ extends  AsyncTask<Void, Void, String>{

       // private static final String TAG = "YEAH";
       private final String stopsURL = "http://"+serverIp+":8080/livebus/stops.php";
       private final String routesURL = "http://"+serverIp+":8080/livebus/routes.php";
       private static final String username = "lefteris";
       private static final String password = "thesis";
       // private static final String URL = "http://192.168.56.101:8080/livebus/receiveJson.php";
       //private static final String URL = "http://79.107.7.219:8080/livebus/receiveJson.php";


        @Override
        protected String doInBackground(Void... params) {
            new SimpleHTTPPOSTRequester(username, password, routesURL, "routes").makeHTTPPOSTRequest(context);
            new SimpleHTTPPOSTRequester(username, password, stopsURL, "stops").makeHTTPPOSTRequest(context);

            return "Completed!";
        }

        @Override
        protected void onPostExecute(String result){
            //Log.i("POSA Async routes", result);
            //handler.post(placeMapStuff);
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("ProgressUpdate", "You are in progress update ... " );
        }

        @Override
        protected void onPreExecute() {
            try{
                db = new DbAndroidMap(context);
                DatabaseManager lol =new DatabaseManager();
                lol.initializeInstance(db);
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                database.setLocale(Locale.getDefault());
                DatabaseManager.getInstance().deletedb(table);
                DatabaseManager.getInstance().deletedb(tableRoute);
                DatabaseManager.getInstance().closeDatabase();      //klinei tin sindesi me tin db
            }catch(Exception e){e.printStackTrace();}
        }
    }
}
/*
    Runnable placeMapStuff = new Runnable() {
        @Override
        public void run() {

//DBmanager gia prosvasi stin DB (pou ixe dimiourgi8ei stin requestAsync)
//String route = "partali";

            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            database.setLocale(Locale.getDefault());

            String query="SELECT * FROM "+table+" ORDER BY lng asc";//+" WHERE route=\""+route+"\"";        //query apo db gia (stops)
            String queryRoutes="SELECT * FROM "+tableRoute;                                                 //query apo db gia (routes)

            Cursor data = database.rawQuery(query, null);                                          //klisi query gia stops
            Cursor dataRoutes = database.rawQuery(queryRoutes, null);                            //klisi queryRoutes


            final ArrayList<MarkerOptions> markers = new returnMapStuff().getStopsList(data);
            ArrayList<String> routesStrings = new returnMapStuff().getRoutesArray(dataRoutes);
//End data from db

            DatabaseManager.getInstance().deletedb(table);
            DatabaseManager.getInstance().deletedb(tableRoute);
            DatabaseManager.getInstance().closeDatabase();      //klinei tin sindesi me tin db
            //database.delete("stops",null,null);

test arrays data... ;)
        for(int i=0; i<routesStrings.size(); i++)
        {
            Log.i("routeArray-->",""+routesStrings.get(i));
            Log.i("routeNameMarker-->", "" + markers.get(i).getTitle());
        }
//test end

//Add marker from db
            //custom marker???
            //BitmapDescriptor busStopIcon = BitmapDescriptorFactory.fromPath("icons/bus");                                                 //custom marker
            //for(int i=0; i<markers.size(); i++){(markers.get(i)).icon(busStopIcon);}
            //for(int i=0; i<markers.size(); i++){(markers.get(i)).icon(BitmapDescriptorFactory.fromPath("icons/star.png"));}
            for (MarkerOptions marker1 : markers) {marker1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));};   //custom marker
            for (MarkerOptions marker : markers) {map.addMarker(marker);}                                      //vazei markers ston xarti (stops)
// ADD marker END

//PolyLines ADD
            ArrayList<PolylineOptions> polylines = new returnPolylines().getStopsPoly(markers,routesStrings);
            for (PolylineOptions polyline : polylines) {
                map.addPolyline(polyline);
                Log.i("HowManyPolylinesExist?????????-->", ""+polylines.size());
            }
//PolyLines ADD
            markers.clear();
            polylines.clear();
        }
    };
*/