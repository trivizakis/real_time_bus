package pack.androidmap;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Lefteris on 4/4/2015.
 */
public class SettingsPane extends Fragment {

    private Context context;

    public SettingsPane(Context context)
    {
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View settingsPane = inflater.inflate(R.layout.settings_pane,container,false);
        final TextView tvServerAddr = (TextView) settingsPane.findViewById(R.id.settingIpTB);


        SharedPreferences spSetting = context.getSharedPreferences("settings",context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = spSetting.edit();
        tvServerAddr.setText(spSetting.getString("webServerName",null));
        Button saveBtn=(Button) settingsPane.findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("webServerName",tvServerAddr.getText().toString());
                editor.apply();

            }
        });

        return settingsPane;
    }
}
