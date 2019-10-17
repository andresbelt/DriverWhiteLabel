package co.com.autolagos.rtaxi.local.driver.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Boolean login = Preferences.getSavedLogin();
        if (login) {

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }

    }
}
