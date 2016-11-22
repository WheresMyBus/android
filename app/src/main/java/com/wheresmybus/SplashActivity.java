package com.wheresmybus;

/**
 * Created by lesli_000 on 11/1/2016.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

public class SplashActivity extends AppCompatActivity {

    /**
     * Displays the logo on the splash screen, then goes to the main screen
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}