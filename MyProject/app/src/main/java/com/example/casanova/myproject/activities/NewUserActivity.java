package com.example.casanova.myproject.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casanova.myproject.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NewUserActivity extends AppCompatActivity {
    EditText name;
    EditText id;
    EditText mblno;
    EditText ip,port;
    Button btn;
    TextView textResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        name = (EditText)findViewById(R.id.etName);
        id = (EditText)findViewById(R.id.etID);
        mblno = (EditText)findViewById(R.id.etMblNo);
        ip = (EditText)findViewById(R.id.etip);
        port = (EditText)findViewById(R.id.etport);
        btn = (Button)findViewById(R.id.bsubmit);
        textResponse = (TextView)findViewById(R.id.tvRes);

        btn.setOnClickListener(buttonConnectOnClickListener);
    }
   OnClickListener buttonConnectOnClickListener = new View.OnClickListener() {

       @Override
       public void onClick(View v) {
           TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
           String tMsg = "";
           tMsg += name.getText().toString()+","+
                   id.getText().toString()+","+
                   mblno.getText().toString()+","+
                   tm.getDeviceId();

           MyClientTask myClientTask = new MyClientTask(ip
                   .getText().toString(), Integer.parseInt(port
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
                Toast.makeText(NewUserActivity.this,"UnknownHostException",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(NewUserActivity.this,"IOException",Toast.LENGTH_SHORT).show();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Toast.makeText(NewUserActivity.this,"Socket Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(NewUserActivity.this,"DataOutputStream Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(NewUserActivity.this,"DataInputStream Can't Close",Toast.LENGTH_SHORT).show();
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
