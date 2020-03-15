package pack.androidmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

/**
 * Created by Lefteris on 4/3/2014.
 */
public class DbAndroidMap extends SQLiteOpenHelper {

    static final String dbName = "livemap";


    static final String colID = "id";
    static final String colRouteID = "route_id";
    static final String colRoute = "route";
    static final String colAddress = "address";
    static final String colLat = "lat";
    static final String colLng = "lng";

    static final String coLbus_id = "bus_id";
    static final String coLroute_id= "route_id";
    static final String coLbus_name= "bus_name";
    static final String coLlivebusLat= "livebusLat";
    static final String coLlivebusLng= "livebusLng";

    static final String table= "stops";
    static final String table2 = "routes";
    static final String table3 = "livebus";
    public static final String queryLivebus="CREATE TABLE "+table3+"("+coLbus_id+" INTEGER PRIMARY KEY, "+coLroute_id+" TEXT, "+coLbus_name+" TEXT, "+coLlivebusLat+" FLOAT, "+coLlivebusLng+" FLOAT)";
    public static final String queryStops="CREATE TABLE "+table+"("+colID+" INTEGER PRIMARY KEY, "+colRoute+" TEXT, "+colAddress+" TEXT, "+colLat+" FLOAT, "+colLng+" FLOAT)";
    public static final String queryRoutes="CREATE TABLE "+table2+"("+colID+" INTEGER PRIMARY KEY, "+colRoute+" TEXT)";

    public DbAndroidMap(Context context) {
        super(context, dbName, null, 33);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.setLocale(Locale.getDefault());
        //db.execSQL("DROP TABLE IF EXISTS TABLENAME");
        db.execSQL("DROP  TABLE IF EXISTS "+table);
        db.execSQL("DROP  TABLE IF EXISTS "+table2);
        db.execSQL("DROP  TABLE IF EXISTS "+table3);

        db.execSQL(queryStops);
        db.execSQL(queryRoutes);
        db.execSQL(queryLivebus);
        //InsertDepts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    void insertStops(String route, String address, String lat, String lng){ //String id, <--stops_id from json
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(colID, Integer.valueOf(id));
        cv.put(colRoute, route);
        cv.put(colAddress, address);
        cv.put(colLat, Double.valueOf(lat));
        cv.put(colLng, Double.valueOf(lng));
        db.insert(table, null, cv);
        db.close();
    }


    public void insertRoutes(String routeRoute) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colRoute, routeRoute);
        db.insert(table2, null, cv);
        db.close();
    }

    public void insertLivebus(String route_id,String bus_name,String livebusLat,String livebusLng)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("bus_name",bus_name);
        cv.put("route_id",route_id);
        cv.put("livebusLat",livebusLat);
        cv.put("livebusLng",livebusLng);

        //db.execSQL("DROP  TABLE IF EXISTS "+table3);
        //db.execSQL(queryLivebus);
        db.insert(table3, null, cv);
        db.close();
    }

}

/*
    Cursor getStops(String route){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+table+" WHERE route=\""+route+"\"";
        Cursor data = db.rawQuery(query, null);
        db.close();
        return data;
    }

    Cursor getRoutes(){
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+table2;
        Cursor data = db.rawQuery(query, null);
        db.close();
        return data;
    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP  TABLE "+table);
        //db.execSQL("DROP  TABLE "+table2);
        //db.execSQL("DROP TRIGGER IF EXISTS dept_id_trigger");
        //db.execSQL("DROP TRIGGER IF EXISTS dept_id_trigger22");
        //db.execSQL("DROP TRIGGER IF EXISTS fk_empdept_deptid");
        //db.execSQL("DROP VIEW IF EXISTS "+viewEmps);

*/