package com.example.casanova.myprojectteacher.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.casanova.myprojectteacher.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void startAtn(View v){
        Intent i = new Intent(this,TakeAttenActivity.class);
        startActivity(i);
    }
    public void startNewCls(View v){
        Intent i = new Intent(this,NewClsActivity.class);
        startActivity(i);
    }
    public void startStatus(View v){
        Intent i = new Intent(this,StatusActivity.class);
        startActivity(i);
    }
    public void startAddstudent(View v){
        Intent i = new Intent(this,AddstudentActivity.class);
        startActivity(i);
    }
    public void startMakePoll(View v){
        Intent i = new Intent(this,MakePollActivity.class);
        startActivity(i);
    }
}
