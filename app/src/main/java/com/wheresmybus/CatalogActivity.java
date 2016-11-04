package com.wheresmybus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CatalogActivity extends AppCompatActivity {

    private String[] routeNames = {"44 - Wallingford", "70 - Downtown"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_catalog, routeNames);

        ListView listView = (ListView) findViewById(R.id.routeNames);
        listView.setAdapter(adapter);
    }
}
