package pack.androidmap;

/**
 * Created by Lefteris on 6/12/2014.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class requestAsyncLivebus extends Activity{
    private TextView tvDD;
    private Context context;
    //private ProgressDialog progressDialog;
    private final GoogleMap map;
    private ArrayList<Marker> markersToChange = new ArrayList<>();
    private final Handler handler = new Handler();
    private String serverIp;
    private boolean firstTime = true, initial=true;
    private ArrayList<String> choiceRoutes;
    private LatLng closestStop,first;
    private int loopTime;
    private int counter=1;
    private ArrayList<Integer> speeds= new ArrayList<>();
    private ArrayList<String> busesToIgnore = new ArrayList<>();
    private float oldDistance=0;
    private float firstD=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        //progressDialog = new ProgressDialog(context);
    }
    public void execute(String ip) {
        serverIp=ip;
        new AsyncSendJ().execute();
    }

    public requestAsyncLivebus(Context context, GoogleMap map, TextView tvDD){
        this.context = context;
        this.map =map;
        this.tvDD = tvDD;
    }

    final Runnable updateMarker = new Runnable() {
        @Override
        public void run() {

            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            database.setLocale(Locale.getDefault());

            //String queryLive="SELECT * FROM "+tableLivebus+" INNER JOIN routes ON livebus.route_id=routes.id";                          //query apo db gia (livebus)

            String queryLive;
            String tableLivebus = "livebus";
            if(!choiceRoutes.isEmpty()){
                    queryLive="SELECT * FROM "+ tableLivebus +" INNER JOIN routes ON livebus.route_id=routes.id WHERE route IN ('";
                    for(int i=0; i<choiceRoutes.size(); i++){
                        if(choiceRoutes.size()-1!=i)
                            queryLive+=choiceRoutes.get(i)+"' , '";
                        else
                            queryLive+=choiceRoutes.get(i)+"')";
                    }
                }
                else{
                    queryLive="SELECT * FROM "+ tableLivebus +" INNER JOIN routes ON livebus.route_id=routes.id";
                }
                //query apo db gia (livebus)

            try
            {
            if(database.rawQuery(queryLive, null)!=null) {
                final Cursor liveData = database.rawQuery(queryLive, null);             //klisi query gia livebus

                ArrayList<MarkerOptions> livebusMarkers = new returnMapStuff().getLivebusList(liveData);

                DatabaseManager.getInstance().deletedb(tableLivebus);
                DatabaseManager.getInstance().closeDatabase();



                 if(livebusMarkers!=null && closestStop!=null) {
                     movingBus kontinoteroLeoforio = findClosestBus(closestStop, livebusMarkers);

                     if(kontinoteroLeoforio!=null) {
                         for(int i=0; i<livebusMarkers.size(); i++) {
                             if (livebusMarkers.get(i).getSnippet().equals(kontinoteroLeoforio.getId())) {
                                 calculateETA(kontinoteroLeoforio);
                                 break;
                             }
                         }
                     }
                     else
                         tvDD.setText("Μη διαθέσιμο όχημα");

                 }
                else{
                     if(livebusMarkers!=null)
                         tvDD.setText("Υπολογίζω!");
                     else
                         tvDD.setText("Απενεργοποιημένο GPS ή Internet");
                 }


                BitmapDescriptor liveBusIcon = BitmapDescriptorFactory.fromResource(R.drawable.bus);

                Marker datMarker;
                if (firstTime) {
                    for (int i = 0; i < livebusMarkers.size(); i++) {
                        datMarker = map.addMarker(livebusMarkers.get(i).icon(liveBusIcon));
                        markersToChange.add(datMarker);
                    }
                    firstTime = false;
                } else {
                    for (int i = 0; i < livebusMarkers.size(); i++) {
                        for (int j = 0; j < markersToChange.size(); j++) {

                            if (markersToChange.get(j).getSnippet().equals(livebusMarkers.get(i).getSnippet())) {
                                //Log.i("Fresh changed: ", "" + markersToChange.size());
                                markersToChange.get(i).setPosition(livebusMarkers.get(i).getPosition());
                                livebusMarkers.get(i).visible(false);
                            }
                        }
                        if(livebusMarkers.get(i).isVisible()) {
                            //Log.i("Fresh real: "+ markersToChange.get(i).getSnippet()+" "+livebusMarkers.get(i).getSnippet(), "markers " + markersToChange.size());
                            datMarker = map.addMarker(livebusMarkers.get(i).icon(liveBusIcon));
                            markersToChange.add(datMarker);
                            livebusMarkers.get(i).visible(false);
                        }
                    }
                    //handler.post(this);//postDelayed(this, MARKER_UPDATE_INTERVAL); ayto prokalei exception kai exei amfivoli xrisimotita
                }
            }
            } catch(Exception e)
            {
                System.out.println(e.getMessage()); }
    }};

    private class AsyncSendJ extends AsyncTask<Void, Void, String> {

        private final String livebusURL = "http://"+serverIp+":8080/livebus/livebus.php";
        private static final String username = "lefteris";
        private static final String password = "thesis";

        @Override
        protected String doInBackground(Void... params) {
            new SimpleHTTPPOSTRequesterLB(username, password, livebusURL, "livebus").makeHTTPPOSTRequest(context);

            return "NTA3!!";
        }

        @Override
        protected void onPostExecute(String result){
            //for(int i=0; i< markersToChange.size(); i++){
            //    markersToChange.get(i).remove();}
            handler.post(updateMarker);
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("ProgressUpdate LiveBus", "You are in progress update ... " );
        }

        @Override
        protected void onPreExecute() {
            //Log.d("PreExecute","Initializing LiveBus......");
        }

    }

    public void setClosestStop(LatLng closestStop) {
        this.closestStop = closestStop;
    }

    public void setSelectedRoutes(ArrayList<String> choiceRoutes){
        this.choiceRoutes = choiceRoutes;
    }

    private movingBus findClosestBus(LatLng closestStop, ArrayList<MarkerOptions> liveBuses){
        ArrayList<movingBus> busQueue = new ArrayList<>();
        float[] distanceArray = new float[1];

        for (int i = 0; i < liveBuses.size(); i++) {
            movingBus toAddMovingBus = new movingBus();
            Location.distanceBetween(closestStop.latitude, closestStop.longitude, liveBuses.get(i).getPosition().latitude, liveBuses.get(i).getPosition().longitude, distanceArray);
            //toAddMovingBus.setIndex(i);
            toAddMovingBus.setDistance(distanceArray[0]);
            toAddMovingBus.setId(liveBuses.get(i).getSnippet());
            Log.i("findClosestBus","isItComing ID:"+toAddMovingBus.getId()+"DISTANCE:"+toAddMovingBus.getDistance());
            if(toAddMovingBus.getDistance()>20 && !busesToIgnore.contains(toAddMovingBus.getId())) {
                busQueue.add(toAddMovingBus);
            }
            else if(toAddMovingBus.getDistance()<20 && !busesToIgnore.contains(toAddMovingBus.getId())){
                busesToIgnore.add(toAddMovingBus.getId());
                isItComing(-1);
            }
        }

        int minIndex = 0;
        for (int i = 1; i < busQueue.size(); i++){
            float newNumber = busQueue.get(i).getDistance();
            if ((newNumber < busQueue.get(minIndex).getDistance())){
                minIndex = i;
            }
        }

        if(busQueue.isEmpty())
            return (movingBus) null;
        else
            return busQueue.get(minIndex);
    }

    public void setLoopPeriod(int loopTime){
        this.loopTime=loopTime;
    }

    private void calculateETA(movingBus kontinoteroLeoforio){
        int eta,temp;
        int speedBus;
        //float[] distanceArray = new float[1];
try {
    //Log.i("count(eta): ", "" + counter);
    if (counter == 1) {
        Log.i("counter1","isItComing ID: "+kontinoteroLeoforio.getId());
        firstD=kontinoteroLeoforio.getDistance();
        isItComing(kontinoteroLeoforio.getDistance());
        counter++;
    } else if (counter == 3) {

        Log.i("counter3","isItComing ID: "+kontinoteroLeoforio.getId());
        //Location.distanceBetween(first.latitude, first.longitude, livebus.getPosition().latitude, livebus.getPosition().longitude, distanceArray);
        if(isItComing(kontinoteroLeoforio.getDistance())) {
            //speedBus = ((int) (kontinoteroLeoforio.getDistance())) / (loopTime * counter);
            speedBus = ((int) (firstD - kontinoteroLeoforio.getDistance())) / (loopTime * counter);
            //speedBus = ((int) distanceArray[0]) / (loopTime * counter);
            speeds.add(speedBus);

            temp = 0;
            for (int i = 0; i < speeds.size(); i++) {
                temp += speeds.get(i);
            }

            eta = (((int) kontinoteroLeoforio.getDistance())) / (temp / speeds.size()); //(me statistiki dior8osi)ektimomenos xronos se deyterolepta
            tvDD.setText("Επόμενο όχημα σε " + eta + " δ. Id: " + kontinoteroLeoforio.getId());
            isItComing(-1);
        }
        else{
            busesToIgnore.add(kontinoteroLeoforio.getId());
            isItComing(-1);
            //tvDD.setText("Μη διαθέσιμο όχημα");
        }
        counter = 1;
    } else {
        counter++;
    }
} catch (Exception e){Log.e("Exception eta ",e.getMessage());}
    }

    public void clearMarkersToChange(){
        markersToChange.clear();
        //markersToChange.removeAll(markersToChange);
    }

    public void setFirstTimeTrue(){firstTime=true;}

    private boolean isItComing(float distance){
        if(initial && distance>0) {
            oldDistance = distance;
            initial=false;
            Log.i("LOLLOL", "isItComing first:"+oldDistance);
            return true;
        }
        else {
            Log.i("LOLLOL", "isItComing second:" + distance);
            if (oldDistance < distance && distance > 0) {
                //oldDistance = 0;
                tvDD.setText("Υπολογίζω...");
                initial = true;
                Log.i("LOLLOL", "isItComing BAMMM distance:" + distance);
                return false;
            } else if (distance == -1) {
                initial = true;
                return true;
            } else {
                oldDistance = distance;
                return true;
            }
        }
    }

    public void resetBusesToIgnore(){busesToIgnore.clear();}
}