package com.example.casanova.myproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.casanova.myproject.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PollActivity extends AppCompatActivity {
    private String question,op1,op2,op3,op4;
    private String ip,port;
    private TextView tvQue,recMsg;
    private RadioGroup rbgrp;
    private RadioButton rb1,rb2,rb3,rb4;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        tvQue = (TextView)findViewById(R.id.tvPollQue);
        rbgrp = (RadioGroup)findViewById(R.id.rbGroup1);
        rb1 = (RadioButton)findViewById(R.id.rbOne);
        rb2 = (RadioButton)findViewById(R.id.rbTwo);
        rb3 = (RadioButton)findViewById(R.id.rbThree);
        rb4 = (RadioButton)findViewById(R.id.rbFour);
        recMsg = (TextView)findViewById(R.id.recMsg);
        btn = (Button)findViewById(R.id.bPollSubmit);

        Intent i = getIntent();
        ip = i.getStringExtra("ip");
        port = i.getStringExtra("port");
        question = i.getStringExtra("question");
        op1 = i.getStringExtra("op1");
        op2 = i.getStringExtra("op2");
        op3 = i.getStringExtra("op3");
        op4 = i.getStringExtra("op4");
        setQuestionView();
    }

    private void setQuestionView(){
        tvQue.setText(question);
        rb1.setText(op1);
        rb2.setText(op2);
        rb3.setText(op3);
        rb4.setText(op4);
        String ob = question+","+op1+","+op2+","+op3+","+op4;
        recMsg.setText(ob);
    }


    public void submitPoll(View v){
        btn.setEnabled(false);
        RadioButton answer = (RadioButton)findViewById(rbgrp.getCheckedRadioButtonId());
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String tMsg = "";
        tMsg += tm.getDeviceId()+","+answer.getText().toString();

        MyClientTask myClientTask = new MyClientTask(ip, Integer.parseInt(port),tMsg);
        myClientTask.execute();
    }
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());

                if(msgToServer != null){
                    dataOutputStream.writeUTF(msgToServer);
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}
