package com.example.casanova.myproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.casanova.myproject.R;

public class HomeScreenActivity extends AppCompatActivity {
    Button bOpenAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        bOpenAbout = (Button)findViewById(R.id.bAbout);

        bOpenAbout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),AboutActivity.class);
                startActivity(i);
            }
        });

    }
    public void openNewUser(View view){
        Intent i = new Intent(getBaseContext(),NewUserActivity.class);
        startActivity(i);
    }
    public void openRegistered(View view){
        Intent i = new Intent(getBaseContext(),RegisteredActivity.class);
        startActivity(i);
    }
}
