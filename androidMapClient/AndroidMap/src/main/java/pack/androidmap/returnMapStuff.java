package pack.androidmap;

import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Lefteris on 7/3/2014.
 */
class returnMapStuff {

    ArrayList<MarkerOptions> getStopsList(Cursor data){
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        // move the cursors pointer to position zero.
        data.moveToFirst();
        // if there is data after the current cursor position, add it
        // to the ArrayList.
        if (!data.isAfterLast())
        {
            do
            {
                ArrayList<Object> dataList = new ArrayList<Object>();

                dataList.add(data.getInt(data.getColumnIndex("id")));
                dataList.add(data.getString(data.getColumnIndex("route")));
                dataList.add(data.getString(data.getColumnIndex("address")));
                dataList.add(data.getDouble(data.getColumnIndex("lat")));
                dataList.add(data.getDouble(data.getColumnIndex("lng")));

                dataArrays.add(dataList);
            }
            // move the cursor's pointer up one position.
            while (data.moveToNext());
        }
/*
        ArrayList<Object> alpha = new ArrayList<Object>();
        alpha = dataArrays.get(0);
        Log.i("dataArrays size-->", "" + dataArrays.size());
        Log.i("dataArrays first element-->",alpha.get(2).toString() );
        //}catch(Exception e){e.printStackTrace();}
        //GET dataArray FROM SQLite END
*/
       return getMarker(dataArrays);
    }
    //apo DB array se MarkerOptions array
    public ArrayList<MarkerOptions> getMarker (ArrayList<ArrayList<Object>> data)
    {
        ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        ArrayList<Object> alpha = new ArrayList<Object>();

        for(int i=0; i<data.size(); i++)
        {
            alpha=data.get(i);
            //int id = Integer.parseInt(alpha.get(0).toString());
            String route =alpha.get(1).toString();
            String address =alpha.get(2).toString();
            double lat =Double.parseDouble(alpha.get(3).toString());
            double lng =Double.parseDouble(alpha.get(4).toString());
        /*Log.i("dataArrays ID-->",""+id);
        Log.i("dataArrays ROUTE-->",route);
        Log.i("dataArrays ADDRESS-->",address);
        Log.i("dataArrays LAT-->",""+lat);
        Log.i("dataArrays LNG-->",""+lng);*/
            //Log.i("HowManyMarkersDoICreate?-->",""+i+"/"+data.size());
            LatLng oneMark = new LatLng(lat, lng);      //create LatLng for MarkerOptions
            markers.add(new MarkerOptions()            //add MarkerOptions to array
                    .title(route)
                    .snippet(address)
                    .position(oneMark)
                    .visible(true));
        }
        return markers;
    }

    public ArrayList<MarkerOptions> getLivebusList (Cursor data)
    {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        // move the cursors pointer to position zero.
        data.moveToFirst();
        // if there is data after the current cursor position, add it
        // to the ArrayList.
        if (!data.isAfterLast())
        {
            do
            {
                ArrayList<Object> dataList = new ArrayList<Object>();

                dataList.add(data.getInt(data.getColumnIndex("bus_id")));
                dataList.add(data.getString(data.getColumnIndex("route")));
                dataList.add(data.getString(data.getColumnIndex("bus_name")));
                dataList.add(data.getDouble(data.getColumnIndex("livebusLat")));
                dataList.add(data.getDouble(data.getColumnIndex("livebusLng")));

                dataArrays.add(dataList);
            }
            // move the cursor's pointer up one position.
            while (data.moveToNext());
        }
/*
        ArrayList<Object> alpha = new ArrayList<Object>();
        alpha = dataArrays.get(0);
        Log.i("dataArrays size-->", "" + dataArrays.size());
        Log.i("dataArrays first element-->",alpha.get(2).toString() );
        //}catch(Exception e){e.printStackTrace();}
        //GET dataArray FROM SQLite END
*/
        return getMarker(dataArrays);
    }

    public ArrayList<String> getRoutesArray(Cursor data) {
        ArrayList<String> routesArray = new ArrayList<String>();

        data.moveToFirst();
        // if there is data after the current cursor position, add it
        // to the ArrayList.
        if (!data.isAfterLast())
        {
            do
            {
                routesArray.add(data.getString(data.getColumnIndex("route")));
            }
            // move the cursor's pointer up one position.
            while (data.moveToNext());
        }

        return routesArray;
    }
}