package com.wheresmybus;

/**
 * Created by lesli_000 on 11/1/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }
}
