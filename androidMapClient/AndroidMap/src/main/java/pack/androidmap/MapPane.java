package pack.androidmap;

/**
 * Created by Lefteris on 2/2/2014.
 */

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MapPane extends Fragment
    {

    private Context context;
    private ArrayList<String> choiceRoutes;
    private String ip;

    private View view;
    private GoogleMap map;
    private TextView tvDD;
    private TextView tvDDsecond;

    private GPSTracker gps;

    private Timer timer;

    private requestAsyncLivebus manipulateLivebus;


    private boolean scheduled=false;

//constructor
   public MapPane(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if(view==null && map==null)
        {
            view = inflater.inflate(R.layout.map_activity, container, false);
// Get a handle to the Map Fragment
            map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
//TextView gia provoli Apostasis kai Diarkeias me ta podia
            tvDD = (TextView) view.findViewById(R.id.tv_distance_time);
            tvDDsecond =(TextView) view.findViewById(R.id.tv_eta);
//Kento tou provalomenou xarti
            LatLng heraklion = new LatLng(35.336414, 25.124273);
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(heraklion, 13));
            //manipulateLivebus.resetBusesToIgnore();//for debuging purpuse
        }else
        {
            map.clear();
            manipulateLivebus.clearMarkersToChange();
            manipulateLivebus.setFirstTimeTrue();
        }

        ArrayList<Marker> addedMarkers = setMarkersAndPolylines(choiceRoutes);
        //Log.i("PoseiMarkerChoiceE3w-->",""+addedMarkers.size());
//Bazei marker me tin 8esi mou kai ton ananewnei
        if(gps==null) {
            gps = new GPSTracker(context, map);
            gps.setStaff(tvDD, addedMarkers);
            gps.setSecondTV(tvDDsecond);
        }
        else {
            if(gps.theLastKnownLocation!=null)
                gps.location.set(gps.theLastKnownLocation);
            gps.setRequestDirectionalPolyline();
            gps.setStaff(tvDD, addedMarkers);
        }
//Gia tis odigies einai i 8esi mou
       //LatLng myLatLng = new LatLng(myPositionObj.getLatitude(),myPositionObj.getLongitude());



//ODIGIES TELOS
        if(manipulateLivebus==null) {
            manipulateLivebus = new requestAsyncLivebus(context, map, tvDDsecond);
            //final AsyncClearMap clearMap = new AsyncClearMap(context, map);
        }
//epanalamvani ana deyterolepto tin ananewsi twn livebuses
        final int loopSec = 2; // exec time of "loop"

        if(scheduled) {
            //Log.i(""+scheduled, " eta.");
            timer.cancel();
            timer.purge();
            scheduled = false;
        }
            //Log.i(""+scheduled, " eta.");
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // run srC
                    scheduled = true;
                    manipulateLivebus.setSelectedRoutes(choiceRoutes);
                    if(gps.getTheClosestStop()!=null)
                        manipulateLivebus.setClosestStop(gps.getTheClosestStop());
                    //else
                        //    manipulateLivebus.setClosestStop(new LatLng(gps.latitude,gps.longitude));
                    manipulateLivebus.setLoopPeriod(loopSec);
                    manipulateLivebus.execute(ip);
                    //run src END
                }
            }, 0, loopSec*1000);
//epanalipsi END

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        return view;
    }

    public void setIP(String ip)    {
        this.ip =ip;
    }

    public void setSelectedRoutes(ArrayList<String> choiceRoutes){
        this.choiceRoutes = choiceRoutes;
    }

//vazei markers kai polyline ston xarti
    private ArrayList<Marker> setMarkersAndPolylines(ArrayList<String> shownRoutes){
            //Runnable placeMapStuff = new Runnable() {
            //    @Override
            //    public void run() {

//DBmanager gia prosvasi stin DB (pou ixe dimiourgi8ei stin requestAsync)
                    String table= "stops";
                    String tableRoute = "routes";
                    String query;
                    String queryRoutes;
                    ArrayList<Marker> addedMarkers = new ArrayList<Marker>();

                    SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                    database.setLocale(Locale.getDefault());

                if(!shownRoutes.isEmpty()){
                    query="SELECT * FROM "+table+" WHERE route IN ('";
                    for(int i=0; i<shownRoutes.size(); i++){
                        if(shownRoutes.size()-1!=i)
                        query+=shownRoutes.get(i)+"' , '";
                        else
                            query+=shownRoutes.get(i)+"')";
                    }
                }
                else{
                    query="SELECT * FROM "+table;
                }
                    query+=" ORDER BY lng asc";//+" WHERE route=\""+route+"\"";        //query apo db gia (stops)

                Log.i("query markers: ",query);
                    queryRoutes="SELECT * FROM "+tableRoute;                                                 //query apo db gia (routes)

                    Cursor data = database.rawQuery(query, null);                                          //klisi query gia stops
                    Cursor dataRoutes = database.rawQuery(queryRoutes, null);                            //klisi queryRoutes

                    final ArrayList<MarkerOptions> markers = new returnMapStuff().getStopsList(data);
                    final ArrayList<String> routesStrings = new returnMapStuff().getRoutesArray(dataRoutes);
//End data from db

                    //DatabaseManager.getInstance().deletedb(table);
                    //DatabaseManager.getInstance().deletedb(tableRoute);
                    DatabaseManager.getInstance().closeDatabase();           //klinei tin sindesi me tin db
                    //database.delete("stops",null,null);

//test arrays data... ;)
                    //for(int i=0; i<routesStrings.size(); i++)
                    //{
                    //    Log.i("routeArray-->",""+routesStrings.get(i));
                    //    Log.i("routeNameMarker-->", "" + markers.get(i).getTitle());
                   // }
//test end

//Add marker from db
                    //custom marker???
                    //BitmapDescriptor busStopIcon = BitmapDescriptorFactory.fromResource(R.drawable.bus);                                                 //custom marker
                    //for(int i=0; i<markers.size(); i++){(markers.get(i)).icon(busStopIcon);}
                    //for(int i=0; i<markers.size(); i++){(markers.get(i)).icon(BitmapDescriptorFactory.fromPath("icons/star.png"));}
                    //for (MarkerOptions marker1 : markers) {marker1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));};   //custom marker
                    BitmapDescriptor busStopIcon = BitmapDescriptorFactory.fromResource(R.drawable.busstop);
                    for (MarkerOptions marker1 : markers) {marker1.icon(busStopIcon);};
                    for (MarkerOptions marker : markers) {
                        Marker tempMarker = map.addMarker(marker);
                        addedMarkers.add(tempMarker);
                    }                                      //vazei markers ston xarti (stops)
// ADD marker END

//PolyLines ADD
                    ArrayList<PolylineOptions> polylines = new returnPolylines().getStopsPoly(markers,routesStrings);
                    for (PolylineOptions polyline : polylines) {
                        map.addPolyline(polyline);
                        //Log.i("HowManyPolylinesExist?????????-->", ""+polylines.size());
                    }
        return addedMarkers;
//PolyLines ADD
        }

//poios ine o pio kontinos

}