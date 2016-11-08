package com.wheresmybus;

import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.view.View;

/**
 * Created by lesli_000 on 11/8/2016.
 */

public class SubmitAlertActivity extends AppCompatActivity {

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_bus_route:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_neighborhood:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }
}
