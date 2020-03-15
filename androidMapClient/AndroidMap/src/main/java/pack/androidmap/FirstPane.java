package pack.androidmap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Lefteris on 1/3/2014.
 */
public class FirstPane extends Fragment {
    private Context context;
    public SharedPreferences spSettings;
    public int howManyCheckboxes;
    private LinearLayout ll;
    private final MapPane _mapPaneFrag;
    private final SettingsPane _settingsPane;
    private String tableRoute = "routes";

    public FirstPane(Context context) {
        this.context = context;
        //putCheckboxes = new requestAsyncForCheckBoxList(context);
        _mapPaneFrag=new MapPane(context);
        _settingsPane=new SettingsPane(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ll = (LinearLayout) view.findViewById(R.id.CheckboxLL);

        try
        {
            //Get the current thread's token
            synchronized (this)
            {                    //Wait x milliseconds
                this.wait(300);
            }
        }
        catch (InterruptedException e){e.printStackTrace();}

        howManyCheckboxes = setCheckboxes();
        view.refreshDrawableState();
        //final MapPane _mapPaneFrag = new MapPane(context);
        //putCheckboxes.setIp("192.168.10.136");
        //putCheckboxes.setLL(ll);
        //putCheckboxes.execute();
       // howManyCheckboxes = putCheckboxes.getHowManyCheckeboxes();

        spSettings =context.getSharedPreferences("settings", context.MODE_PRIVATE);
        final String webServerIpOrName= spSettings.getString("webServerName",null);

//btnNext Listener
        Button button = (Button) view.findViewById(R.id.buttonNext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _mapPaneFrag.setIP(webServerIpOrName);            //pernaei sto antikeimeno MapPane to spSettings
                _mapPaneFrag.setSelectedRoutes(getRouteSelectionFromCheckbox(ll)); //pernaei sto antikeimeno MapPane to array me ta epilegmena dromologia
                focusFragment(_mapPaneFrag);
            }
        });

        final Button exitBtn = (Button) view.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                terminateApp();
            }
        });


        Button settingsBtn=(Button) view.findViewById(R.id.btnSettings);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                focusFragment(_settingsPane);
            }
        });

        return view;
    }
//methods
    private int setCheckboxes(){                                                        //vazei ta cb's sto ll tou fragment-main
        //DBmanager gia prosvasi stin DB (pou ixe dimiourgi8ei stin requestAsync)


        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        database.setLocale(Locale.getDefault());

        String queryRoutes="SELECT * FROM "+tableRoute;                                                 //query apo db gia (routes)

        final Cursor dataRoutes = database.rawQuery(queryRoutes, null);                            //klisi queryRoutes

        final ArrayList<String> routesStrings = new returnMapStuff().getRoutesArray(dataRoutes);
//End data from db
        DatabaseManager.getInstance().closeDatabase();

        int cbCounter = routesStrings.size();

        Log.i("POSA CHECKBOXES?", ""+cbCounter);
        try{

            CheckBox cb;
            for (int i = 0; i < routesStrings.size(); i++) {
                cb = new CheckBox(context);
                cb.setText(routesStrings.get(i));
                cb.setId(i);
                ll.addView(cb);
            }

        }catch (Exception e){e.printStackTrace();}


        routesStrings.clear();

        return cbCounter;
    }

    private ArrayList<String> getRouteSelectionFromCheckbox(LinearLayout ll) { //vlepei ta tsekarismena cb kai epistrefei array

        CheckBox cb;
        ArrayList<String> routes= new ArrayList<String>();
        int j=0;

        try
       {
            for(int i=0; i<howManyCheckboxes; i++){
                cb =(CheckBox) ll.findViewById(i);
                if(cb.isChecked() && cb!=null) {
                    routes.add(cb.getText().toString());
                }
            }
        }catch (Exception e) {e.printStackTrace();}


        return routes;
    }

    private void focusFragment(Fragment F1){
        // Create a new Fragment to be placed in the activity layout
        FragmentManager FM1 = getFragmentManager();
        FragmentTransaction FT1 = FM1.beginTransaction();


        try{FT1.replace(R.id.container, F1);}catch(Exception e){e.printStackTrace();};
        FT1.addToBackStack(null);
        FT1.commit();
        FT1 = null;
        FM1 = null;
    }
    private void terminateApp()
    {

        getActivity().finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}