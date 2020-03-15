/**
 * Created by Lefteris on 3/3/2014.
 */
package pack.androidmap;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import org.apache.http.entity.ContentType;

/**
 *
 * @author joe666
 */
public class SimpleHTTPPOSTRequester{

    private String apiusername;
    private String apipassword;
    private String apiURL;
    private String theRoute;
    public JSONArray finalResult;
    public Context context;
    private DbAndroidMap db;

    public SimpleHTTPPOSTRequester(String apiusername, String apipassword, String apiURL, String theRoute) {
        this.apiURL = apiURL;
        this.apiusername = apiusername;
        this.apipassword = apipassword;
        this.theRoute = theRoute;
    }

     public void makeHTTPPOSTRequest(Context context) {
        try {
            HttpClient c = new DefaultHttpClient();
            HttpPost p = new HttpPost(this.apiURL);
            p.setEntity(new StringEntity("{\"username\":\"" + this.apiusername + "\",\"password\":\"" + this.apipassword + "\"}"));//,ContentType.create("application/json")));
            HttpResponse r = c.execute(p);
            BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent(), "UTF-8"));
            String line = rd.readLine();
            JSONTokener token = new JSONTokener(line);
            try{
                finalResult = new JSONArray(token);
            }catch(JSONException e){e.printStackTrace();}

            try{
                org.json.JSONObject jsonO;
                db = new DbAndroidMap(context);
                DatabaseManager lol =new DatabaseManager();
                lol.initializeInstance(db);
            if(theRoute == "stops"){
                for(int i=0; i<finalResult.length(); i++){
                    jsonO = finalResult.getJSONObject(i);
                    String stopsRoute = jsonO.get("route").toString();
                    String stopsAddress = jsonO.get("address").toString();
                    String stopsLat = jsonO.get("lat").toString();
                    String stopsLng = jsonO.get("lng").toString();
                    db.insertStops(stopsRoute,stopsAddress,stopsLat,stopsLng);
                }
             }else if(theRoute == "routes"){
                for(int i=0; i<finalResult.length(); i++){
                    jsonO = finalResult.getJSONObject(i);
                    String routeRoute = jsonO.get("route").toString();
                    db.insertRoutes(routeRoute);
                }
              }
            }catch(JSONException e){e.printStackTrace();}

        }catch(IOException e){System.out.println(e);}

     }
}
                /*
                org.json.JSONObject jsonO= finalResult.getJSONObject(1);
                Log.i("json object received-->", jsonO.toString());
                Log.i("AfterYeahDUO-->", (String) jsonO.get("id"));
                Log.i("AfterYeahDUO-->", (String) jsonO.get("route_id"));
                Log.i("AfterYeahDUO-->", (String) jsonO.get("route"));
                Log.i("AfterYeahDUO-->", (String) jsonO.get("address"));
                Log.i("AfterYeahDUO-->", (String) jsonO.get("lat"));
                Log.i("AfterYeahDUO-->", (String) jsonO.get("lng"));
                */
/*
                //GET dataArray From SQLite
                Cursor data = db.getStops("partali");

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

                ArrayList<Object> alpha = new ArrayList<Object>();
                alpha = dataArrays.get(0);
                Log.i("dataArrays size-->",""+dataArrays.size());
                Log.i("dataArrays first element-->",alpha.get(2).toString() );
                //GET dataArray FROM SQLite END
*/