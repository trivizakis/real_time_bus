package pack.androidmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements DialogInterface
{
    public AlertDialog alertDialog;
    public static String serverAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        //String serverAddress= setIP();
        serverAddress= setIP();

        new requestAsync(this.getApplicationContext(), serverAddress).execute();
        //requestAsync thing=new requestAsync(this.getApplicationContext(), setIP());
        //thing.execute();


                //ProgressDialog progressDialog = ProgressDialog.show(context, "Loading Bitch...", "Loading application View, please wait...", false, false);
                // progressDialog.dismiss();
/*
        try
        {
            //Get the current thread's token
            synchronized (this)
            {                    //Wait x milliseconds
                this.wait(300);

            }
        }
        catch (InterruptedException e){e.printStackTrace();}
*/
                setContentView(R.layout.activity_main);

                WindowManager WM1 = getWindowManager();
                Display D1 = WM1.getDefaultDisplay();

                final FirstPane _firstPaneFrag = new FirstPane(this.getApplicationContext());
                focusFragment(_firstPaneFrag);


            }

            private String setIP() {
                SharedPreferences spSettings = getSharedPreferences("settings", this.MODE_PRIVATE);
                String webServerIpOrName = spSettings.getString("webServerName", null);
                if (webServerIpOrName == null) {        //IF NULL


                    webServerIpOrName=createAlertBox();
                    //SettingsPane _settingsPane = new SettingsPane(this);
                    webServerIpOrName= this.getResources().getString(R.string.web_server_ip);
                    //webServerIpOrName = spSettings.getString("webServerName", null);
                }

                //Log.i("webServerIpOrName",webServerIpOrName);
                return webServerIpOrName;
            }

            public void focusFragment(FirstPane F1) {
                // Create a new Fragment to be placed in the activity layout
                FragmentManager FM1 = getFragmentManager();
                FragmentTransaction FT1 = FM1.beginTransaction();

                try {
                    FT1.replace(R.id.container, F1);
                } catch (Exception e) {
                }
                ;
                FT1.commit();
                FT1 = null;
                FM1 = null;
            }

            public void focusRealFragment(Fragment F1) {
                // Create a new Fragment to be placed in the activity layout
                FragmentManager FM1 = getFragmentManager();
                FragmentTransaction FT1 = FM1.beginTransaction();


                try {
                    FT1.replace(R.id.container, F1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
                FT1.addToBackStack(null);
                FT1.commit();
                FT1 = null;
                FM1 = null;
            }
    public String createAlertBox(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                serverAddress =userInput.getText().toString();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        return serverAddress;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }
}