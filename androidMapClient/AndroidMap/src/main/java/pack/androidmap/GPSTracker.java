package pack.androidmap;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;
    private GoogleMap map;
    private Marker positionMarker;
    private boolean calledJustOnce = false;
    private TextView textVDD;
    private ArrayList<Marker> addedMarkers;

	private Polyline polylineToChange;
	private boolean requestDirectionalPolyline =true;
	private int indexOfClosestAddedMarker,previousIndexOfClosestAddedMarker=-2;
	private static int  distanceCounter;
	private float lastDistance;

	private DirectionHandler myDirect;
	private DirectionHandler.DownloadTask downloadTask;
	public Location theLastKnownLocation;
	private LatLng theClosestStop;
	private TextView secondTV;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // millisec * sec

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public GPSTracker(Context context, GoogleMap map) {
		this.mContext = context;
        this.map =map;
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}		
	}
	
	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		
		// return latitude
		return latitude;
	}
	
	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		
		// return longitude
		return longitude;
	}
	
	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	mContext.startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
	@Override
	public void onProviderEnabled(String provider) {
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
        try{
			theLastKnownLocation=location;
            if(calledJustOnce){
                positionMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                setDirectionsPolyline(location);
            }
            else{
                setMyLocationMarker(location);
            }
        }catch (NullPointerException e){e.printStackTrace();}
    }

	public int getTheClosestMarker(Location myLocation,ArrayList<Marker> addedMarkers){


		float[] suma= new float[addedMarkers.size()];
		float[] distanceArray = new float[1];
		for(int i=0; i<addedMarkers.size(); i++)
		{
			Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),addedMarkers.get(i).getPosition().latitude,addedMarkers.get(i).getPosition().longitude,distanceArray);
			suma[i]=distanceArray[0];
		}
		//for(int i=0; i<suma.length; i++)
		//	Log.i("Apostaseis se m? :",""+suma[i]);

		int minIndex = 0;
		for (int i = 1; i < suma.length; i++){
			float newnumber = suma[i];
			if ((newnumber < suma[minIndex])){
				minIndex = i;
			}
		}
		//Log.i("Apostaseis minIndex--> ",""+suma[minIndex]);

		if(suma[minIndex]>50)
			return minIndex;
		else
			return -1;
	}

	public void setStaff(TextView tvDD, ArrayList<Marker> addedMarkers) {
		boolean firstTime = calledJustOnce;
		this.textVDD=tvDD;
		this.addedMarkers= addedMarkers;
		setMyLocationMarker(this.getLocation());

		if(firstTime)
			setDirectionsPolyline(this.getLocation());
	}

	private void setDirectionsPolyline(Location location) {
		if(myDirect==null)
			myDirect = new DirectionHandler(map, textVDD);
		if(!addedMarkers.isEmpty()) {
			indexOfClosestAddedMarker = getTheClosestMarker(location, addedMarkers);

			if(previousIndexOfClosestAddedMarker==-2)	//proti kai teleytaia fora
				previousIndexOfClosestAddedMarker=indexOfClosestAddedMarker;
			else if(previousIndexOfClosestAddedMarker!=indexOfClosestAddedMarker){	//poly makria apo tin arxika ypologismeni polyline
				removeDirectionsPolyline();
				polylineToChange = myDirect.getFinalPolyline();
				previousIndexOfClosestAddedMarker=indexOfClosestAddedMarker;
				setRequestDirectionalPolyline();
				//Log.d("PouEimai ","katestrepses polylineToChange lol");
			}

			//ODIGIES ARXI myLatlng/heraklion
			if(indexOfClosestAddedMarker>-1 && requestDirectionalPolyline) { //ekteleitai tin proti fora alla kai otan apomakryn8i poly apo tin arxika ypologismeni stasi													// δεν είναι κοντά στην στάση

				downloadTask = new DirectionHandler.DownloadTask();
				theClosestStop=addedMarkers.get(indexOfClosestAddedMarker).getPosition();
				String urlForDirections = myDirect.getDirectionsUrl(new LatLng(location.getLatitude(), location.getLongitude()), addedMarkers.get(indexOfClosestAddedMarker).getPosition());
				downloadTask.execute(urlForDirections);
				requestDirectionalPolyline =false; //gia na min mpainei edw mesa askopa alla spania
				removeDirectionsPolyline(); //an yparxei 3emparki polyline
				resetDistanceCounter(); //gia na apofeyxoyn klisis sto Directions API(an erxomai apo recalculate)
			}
			else if(indexOfClosestAddedMarker>-1 && !requestDirectionalPolyline){
				if(polylineToChange==null && myDirect!=null) {
					polylineToChange = myDirect.getFinalPolyline();
					//Log.d("PouEimai ","katestrepses polylineToChange lol2");
				}
				resetDirectionPolyline(location);
			}
			else {
				removeDirectionsPolyline();
				setRequestDirectionalPolyline();
				resetDistanceCounter();
				textVDD.setText("Φτάσατε στην στάση!");        //είναι κοντά σε στάση
			}
		}
		else
			textVDD.setText("Προσωρινά μη διαθέσιμή διαδρομή!");
	}

	public void setMyLocationMarker(Location datLocation)
	{
		try
		{
			if(isGPSEnabled) {
				BitmapDescriptor myPosIcon = BitmapDescriptorFactory.fromResource(R.drawable.star);
				positionMarker = map.addMarker(new MarkerOptions()
								.title("ΕΔΩ ΕΙΜΑΙ!")
								.icon(myPosIcon)
								.position(new LatLng(datLocation.getLatitude(), datLocation.getLongitude()))
				);
				calledJustOnce = true;
			}
			else
				secondTV.setText("Ενεργοποίησε το GPS!");

		}catch (NullPointerException e){e.printStackTrace();}
/*
        int index = getTheClosestMarker(location, addedMarkers);
        //ODIGIES ARXI myLatlng/heraklion
        DirectionHandler myDirect = new DirectionHandler(map,textVDD);
        DirectionHandler.DownloadTask downloadTask = new DirectionHandler.DownloadTask();

        String urlForDirections = myDirect.getDirectionsUrl(new LatLng(location.getLatitude(), location.getLongitude()),addedMarkers.get(index).getPosition());
        downloadTask.execute(urlForDirections);

        polylineToChange =myDirect.getFinalPolyline();
*/
	}

	public LatLng getTheClosestStop() {
		return theClosestStop;
	}

	private void removeDirectionsPolyline(){
		if(polylineToChange !=null) {
			polylineToChange.remove();
		}
		polylineToChange=null;
	}

	private void resetDirectionPolyline(Location location){
		List<LatLng> remainingPoints = polylineToChange.getPoints();
		float[] suma= new float[remainingPoints.size()];
		float[] distanceArray = new float[1];

		for(int i=0; i<remainingPoints.size(); i++){
			Location.distanceBetween(location.getLatitude(),location.getLongitude(),remainingPoints.get(i).latitude,remainingPoints.get(i).longitude,distanceArray);
			suma[i]=distanceArray[0];
		}

		int minIndex = 0;
		for (int i = 1; i < suma.length; i++){
			float newnumber = suma[i];
			if ((newnumber < suma[minIndex])){
				minIndex = i;
			}
		}

		//Log.d("PouEimai"," dCounter "+getDistanceCounter());
		if(minIndex==0 && getDistanceCounter()<10){
			//Log.d("PouEimai", " add");
			addPointsToPolyline(remainingPoints, location);
		}
		else if(minIndex==0 && getDistanceCounter()==10){
			requestDirectionalPolyline=true;
			//removeDirectionsPolyline();
			resetDistanceCounter();
		}
		else{
			//Log.d("PouEimai"," pop");
			resetDistanceCounter();
			popPointsFromPolyline(remainingPoints,location,minIndex);
		}


	}

	private void addPointsToPolyline(List<LatLng> initialPoints, Location location){
		float totalDistance = 0;
		float[] distanceArray = new float[1];
		LatLng lastpoint = new LatLng(location.getLatitude(), location.getLongitude());

		PolylineOptions polylineOptions = new PolylineOptions()
			.width(2)
			.color(Color.RED)
			.geodesic(true);

		polylineOptions.add(lastpoint);
		//Log.d("PouEimai", " " + lastpoint.toString());
		for (int i = 0; i < initialPoints.size(); i++) {

			LatLng point = initialPoints.get(i);
			polylineOptions.add(point);

			Location.distanceBetween(lastpoint.latitude, lastpoint.longitude, point.latitude, point.longitude, distanceArray);
			totalDistance += distanceArray[0];
			lastpoint = point;
		}
		//Log.d("PouEimai", " " + polylineOptions.getPoints().size());

	/*
		LatLng point = initialPoints.get(initialPoints.size()-1);
		Location.distanceBetween(lastpoint.latitude, lastpoint.longitude, point.latitude, point.longitude, distanceArray);
		float myLocationToStopDistance = distanceArray[0];
		totalDistance>(myLocationToStopDistance+myLocationToStopDistance*0.3)
	*/
		if(lastDistance<totalDistance) //prepei na mpei kai kritirio apostaseis gia pragmatiko kosmo isws to -- lastDistance<totalDistance && totalDistance>(myLocationToStopDistance+myLocationToStopDistance*0.3) --
			distanceCounterPlusOne();		// opws an i polyline e;inai megalyteri apo thn ey8eia me tin stasi
		lastDistance=totalDistance;

		//Log.d("PouEimai"," dCounterMESA "+getDistanceCounter());
		//Log.d("PouEimai"," lastDMESA "+lastDistance);

		//for(int i=0; i<polylineOptions.getPoints().size(); i++) {
		//	Log.d("simiaPoly", "" + polylineOptions.getPoints().get(i));
		//}
		float time = ((totalDistance / 1000) / 4) * 60;
		totalDistance = (int) totalDistance / 100;
		textVDD.setText("Απόσταση: " + totalDistance / 10 + " χλμ, Διάρκεια: " + (int) time + " λεπτά.");

		removeDirectionsPolyline();

		polylineToChange = map.addPolyline(polylineOptions);



	}

	private void popPointsFromPolyline(List<LatLng> initialPoints, Location location, int index){
		float totalDistance=0;
		float[] distanceArray = new float[1];
		LatLng lastpoint=new LatLng(location.getLatitude(), location.getLongitude());

		PolylineOptions polylineOptions =new PolylineOptions()
				.width(2)
				.color(Color.RED)
				.geodesic(true);

		polylineOptions.add(lastpoint);

		for(int i=index; i<initialPoints.size(); i++) {

			LatLng point = initialPoints.get(i);
			polylineOptions.add(point);

			Location.distanceBetween(lastpoint.latitude, lastpoint.longitude,point.latitude,point.longitude, distanceArray);
			totalDistance += distanceArray[0];
			lastpoint=point;
		}

		//Log.d("PouEimai", " " + polylineOptions.getPoints().size());

		float time=((totalDistance/1000)/4)*60;
		totalDistance=(int)totalDistance/100;
		textVDD.setText("Απόσταση: "+totalDistance/10+" χλμ, Διάρκεια: "+(int)time+" λεπτά.");

		removeDirectionsPolyline();

		polylineToChange= map.addPolyline(polylineOptions);
	}

	public void setRequestDirectionalPolyline(){
		this.requestDirectionalPolyline=true;
	}

	private void distanceCounterPlusOne(){
		distanceCounter+=1;
	}
	private int getDistanceCounter(){
		return distanceCounter;
	}
	private void resetDistanceCounter(){
		distanceCounter=0;
	}
	public void setSecondTV(TextView secondTV){ this.secondTV=secondTV;}
}
