package pack.androidmap;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Lefteris on 9/3/2014.
 */
public class returnPolylines {
    ArrayList<PolylineOptions> getStopsPoly(ArrayList<MarkerOptions> markers, ArrayList<String> routesNamesDB)
    {
        ArrayList<PolylineOptions> polyOptArray = new ArrayList<PolylineOptions>();
        //ArrayList<ArrayList<LatLng>> listLL = new ArrayList<ArrayList<LatLng>>();
        int[] color = {Color.BLACK,Color.CYAN,Color.GRAY,Color.GREEN,Color.YELLOW,Color.LTGRAY,Color.MAGENTA,Color.DKGRAY,Color.RED};
        ArrayList<LatLng> nodeLL = new ArrayList<LatLng>();
        PolylineOptions polyopt = new PolylineOptions()
            .color(Color.BLUE)
            .width(2);
            polyopt=polyopt.geodesic(true);
        for(int j=0; j<routesNamesDB.size(); j++)
        {
            for(int i=0; i<markers.size(); i++)
            {
                    if((routesNamesDB.get(j)).equals(markers.get(i).getTitle()))
                    {
                            nodeLL.add(markers.get(i).getPosition());
                    }
            }

            polyopt.addAll(nodeLL);
            polyOptArray.add(polyopt);
            nodeLL.clear();
            polyopt = new PolylineOptions()
                    .color(color[j])
                    .width(3);
        }


        nodeLL.clear();

        return polyOptArray;
    }
}
/*              --Demo polyline options
        poly.add(new PolylineOptions()
                .add(markers.get(i).getPosition())
                .width(5)
                .color(Color.RED))
            */