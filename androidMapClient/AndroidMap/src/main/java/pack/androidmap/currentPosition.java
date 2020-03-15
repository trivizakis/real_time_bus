package pack.androidmap;

import android.app.Service;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Lefteris on 22/3/2015.
 */
public class currentPosition implements LocationListener, LocationSource {

    private LatLng myPosition;
    private LocationManager locationManager;
    private Context context ;
    private GoogleMap map;

    //Constructor
    private currentPosition(Context applicationContext, GoogleMap map) {
        this.context = applicationContext;
        this.map = map;
    }

    public LatLng onCreate()
    {
    // Enabling MyLocation Layer of Google Map
    map.setMyLocationEnabled(true);

    // Getting LocationManager object from System Service LOCATION_SERVICE
   //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    // Creating a criteria object to retrieve provider
    Criteria criteria = new Criteria();

    // Getting the name of the best provider
    String provider = locationManager.getBestProvider(criteria, true);

    // Getting Current Location
    Location location = locationManager.getLastKnownLocation(provider);

    if(location!=null){
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        myPosition = new LatLng(latitude, longitude);
    }

        return myPosition;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

}
