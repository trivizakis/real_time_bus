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

/**
 * Created by Lefteris on 6/12/2014.
 */
public class SimpleHTTPPOSTRequesterLB {

    private String apiusername;
    private String apipassword;
    private String apiURL;
    private String table_name;
    public JSONArray finalResult;
    public Context context;
    private DbAndroidMap db;

    public SimpleHTTPPOSTRequesterLB(String apiusername, String apipassword, String apiURL, String table_name) {
        this.apiURL = apiURL;
        this.apiusername = apiusername;
        this.apipassword = apipassword;
        this.table_name = table_name;
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
                if(table_name == "livebus"){
                    for(int i=0; i<finalResult.length(); i++){
                        jsonO = finalResult.getJSONObject(i);
                        //String bus_id = jsonO.get("bus_id").toString();
                        String route_id = jsonO.get("route_id").toString();
                        String bus_name = jsonO.get("bus_name").toString();
                        String livebusLat = jsonO.get("lat").toString();
                        String livebusLng = jsonO.get("lng").toString();
                        db.insertLivebus(route_id,bus_name,livebusLat,livebusLng);
                    }
                }
            }catch(JSONException e){e.printStackTrace();}
        }catch(IOException e){System.out.println(e);}

    }
}
