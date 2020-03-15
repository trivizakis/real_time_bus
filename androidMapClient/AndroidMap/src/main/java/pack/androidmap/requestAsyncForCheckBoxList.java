package pack.androidmap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Lefteris on 26/3/2015.
 */
public class requestAsyncForCheckBoxList extends Activity {

    private Context context;
    private String ip;
    private Handler handler = new Handler();
    private LinearLayout ll;
    public int routesStringsLength;
    String tableRoute = "routes";

    public void execute() {
        new AsyncGetNames().execute();
    }

    public requestAsyncForCheckBoxList(Context context){
        this.context = context;
            }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
    }

    Runnable getRouteNamesTable = new Runnable() {
        @Override
            public void run() {

//DBmanager gia prosvasi stin DB (pou ixe dimiourgi8ei stin requestAsync)
//String route = "partali";

            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            database.setLocale(Locale.getDefault());

            String queryRoutes="SELECT * FROM "+tableRoute;                                                 //query apo db gia (routes)

            Cursor dataRoutes = database.rawQuery(queryRoutes, null);                            //klisi queryRoutes

            ArrayList<String> routesStrings = new returnMapStuff().getRoutesArray(dataRoutes);
//End data from db
            DatabaseManager.getInstance().closeDatabase();

            routesStringsLength = routesStrings.size();
            try{

                   CheckBox cb;
                   for (int i = 0; i < routesStrings.size(); i++) {
                       cb = new CheckBox(context);
                       cb.setTextColor(Color.BLACK);
                       cb.setText(routesStrings.get(i));
                       cb.setId(i);
                       ll.addView(cb);
                    }

            }catch (Exception e){e.printStackTrace();}

            dataRoutes.close();
            routesStrings.clear();
        }
    };

    private class AsyncGetNames  extends AsyncTask<Void, Void, String> {
        private final String routesURL = "http://"+ ip +":8080/livebus/routes.php";
        private static final String username = "lefteris";
        private static final String password = "thesis";

        @Override
        protected String doInBackground(Void... voids) {

            new SimpleHTTPPOSTRequester(username, password, routesURL, "routes").makeHTTPPOSTRequest(context);

            return "Completed!";
        }

        @Override
        protected void onPostExecute(String result){
            handler.post(getRouteNamesTable);
        }
    }

    public int getHowManyCheckeboxes(){
        //Log.i("I AM THE (R) NEO",""+routesStringsLength);
        return routesStringsLength;
    }
    public void setIp(String ip){
        this.ip=ip;
    }
    public void setLL(LinearLayout ll){
        this.ll =ll;
    }
}
