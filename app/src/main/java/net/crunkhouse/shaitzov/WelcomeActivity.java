package net.crunkhouse.shaitzov;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * An entry Activity to put in a player name and a game ID
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
