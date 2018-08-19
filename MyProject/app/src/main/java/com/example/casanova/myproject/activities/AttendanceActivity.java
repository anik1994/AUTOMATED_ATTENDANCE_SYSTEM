package com.example.casanova.myproject.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casanova.myproject.R;

public class AttendanceActivity extends AppCompatActivity {
    TextView textResponse;
    EditText etIP, etPort;
    Button buttonConnect;
    EditText scode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        etIP = (EditText) findViewById(R.id.etIP);
        etPort = (EditText) findViewById(R.id.etPort);
        buttonConnect = (Button) findViewById(R.id.btn);
        textResponse = (TextView) findViewById(R.id.msg);

        scode = (EditText)findViewById(R.id.etscode);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

    }
    OnClickListener buttonConnectOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String tMsg = "";
            tMsg += tm.getDeviceId()+","+scode.getText().toString();

            MyClientTask myClientTask = new MyClientTask(etIP
                    .getText().toString(), Integer.parseInt(etPort
                    .getText().toString()),
                    tMsg);
            myClientTask.execute();
        }
    };
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
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if(msgToServer != null){
                    dataOutputStream.writeUTF(msgToServer);
                }

                response = dataInputStream.readUTF();

            } catch (UnknownHostException e) {
                Toast.makeText(AttendanceActivity.this,"UnknownHostException",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(AttendanceActivity.this,"IOException",Toast.LENGTH_SHORT).show();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Toast.makeText(AttendanceActivity.this,"Socket Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(AttendanceActivity.this,"DataOutputStream Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(AttendanceActivity.this,"DataInputStream Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.setText(response);
            super.onPostExecute(result);
        }

    }

}
