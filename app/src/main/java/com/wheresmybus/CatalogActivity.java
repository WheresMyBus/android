package com.wheresmybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

public class CatalogActivity extends AppCompatActivity {

    private String[] routeNames = {"44 - Wallingford", "70 - Downtown"};
    private ArrayAdapter<String> adapter;
    // = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, routeNames);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //createTabs();

        //Intent intent = getIntent();

        //adapter = new ArrayAdapter<String>(this, R.layout.activity_catalog, routeNames);

        ListView listView = (ListView) findViewById(R.id.routeNames);
        //listView.setAdapter(adapter);
    }

    private void createTabs() {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");

        tab1.setIndicator("routes");
        //tab1.setContent(new Intent(this,TabActivity1.class));

        tab2.setIndicator("neighborhoods");
        //tab2.setContent(new Intent(this,TabActivity2.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }
}
