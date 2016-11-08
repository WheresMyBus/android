package com.wheresmybus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.view.View;
import android.widget.Button;

/**
 * Created by lesli_000 on 11/8/2016.
 */

public class SubmitAlertActivity extends AppCompatActivity {
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_alert);

        submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setVisibility(View.INVISIBLE);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_bus_route:
                if (checked)
                    // Pirates are the best

                    submitButton.setVisibility(View.VISIBLE);
                    break;
            case R.id.radio_neighborhood:
                if (checked)
                    // Ninjas rule

                    submitButton.setVisibility(View.VISIBLE);
                    break;
        }
    }

    private void loadSpinnerData() {
        // get the names of bus routes or neighborhoods depending on which radio button selected
        // make sure data alphabetized
    }
}
