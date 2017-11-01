package com.diewland.permissionviewer;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView lv ;
    private ArrayAdapter<String> listAdapter ;
    private HashMap<String, String> appHash = new HashMap<String, String>();
    private HashMap<String, String> pkgHash = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the ListView resource.
        lv = (ListView) findViewById( R.id.list_row );

        // Collect all apps
        PackageManager pm = getPackageManager();
        for (ApplicationInfo app : pm.getInstalledApplications(0)) {
            String app_name = (String)pm.getApplicationLabel(app);
            appHash.put(app.publicSourceDir, app_name);
            pkgHash.put(app.publicSourceDir, app.packageName);
        }

        // Sort by app_name
        ArrayList appNames = new ArrayList<String>(appHash.values());
        Collections.sort(appNames, String.CASE_INSENSITIVE_ORDER);

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, appNames);

        // Set the ArrayAdapter as the ListView's adapter.
        lv.setAdapter( listAdapter );

        // when click app
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int pos, long l) {
                String curAppName = ((TextView)v).getText().toString();
                for(String apkPath : appHash.keySet()){
                    if(appHash.get(apkPath).equals(curAppName)){
                        String packageName = pkgHash.get(apkPath);
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                   Uri.fromParts("package", packageName, null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        /*
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.MANAGE_APP_PERMISSIONS");
                        intent.setComponent(new ComponentName("com.google.android.packageinstaller",
                                                              "com.android.packageinstaller.permission.ui.ManagePermissionsActivity"));
                        startActivity(intent);
                        */
                    }
                }
            }
        });
    }
}
