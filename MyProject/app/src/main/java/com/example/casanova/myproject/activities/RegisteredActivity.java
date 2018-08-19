package com.example.casanova.myproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.casanova.myproject.R;

public class RegisteredActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
    }
    public void openAttendance(View view){
        Intent i = new Intent(getBaseContext(),AttendanceActivity.class);
        startActivity(i);
    }
    public void openPoll(View view){
        Intent i = new Intent(getBaseContext(),GetPollQuestionActivity.class);
        startActivity(i);
    }
    public void openTest(View view){
        Intent i = new Intent(getBaseContext(),TestActivity.class);
        startActivity(i);
    }
    public void openStatus(View view){
        Intent i = new Intent(getBaseContext(),StatusActivity.class);
        startActivity(i);
    }
}
