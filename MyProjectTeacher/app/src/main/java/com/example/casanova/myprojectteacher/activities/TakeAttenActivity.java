package com.example.casanova.myprojectteacher.activities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casanova.myprojectteacher.dbhandlers.SubTblDbHandler;
import com.example.casanova.myprojectteacher.others.ClientInfoHolder;
import com.example.casanova.myprojectteacher.R;

public class TakeAttenActivity extends AppCompatActivity {

    TextView taip, takeAttenPort, msg;
    String message = "";
    ServerSocket serverSocket;
    EditText scode;
    Button btn;
    String clientSecurityCode, serverSecurityCode;
    int clientImei;
    private SubTblDbHandler dbHandler;
    private Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_atten);

        taip = (TextView)findViewById(R.id.takeAttenIP);
        takeAttenPort = (TextView)findViewById(R.id.takeAttenPort);
        msg = (TextView)findViewById(R.id.msg);
        scode = (EditText)findViewById(R.id.etscode);
        btn = (Button)findViewById(R.id.btn);
        spin =(Spinner) findViewById(R.id.spinner);
        dbHandler = new SubTblDbHandler(this, null, null, 1);
        setDropDownList();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Toast.makeText(TakeAttenActivity.this,"Can't close server socket",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickStart(View v){
        btn.setEnabled(false);
        serverSecurityCode = scode.getText().toString();
        taip.setText(getIpAddress());

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();

        new CountDownTimer(30000,100){
            public void onFinish(){
                Intent startActivity = new Intent(getBaseContext(),HomeActivity.class);
                startActivity(startActivity);
                finish();
            }
            public void onTick(long millisUnitlFinished){

            }
        }.start();

    }
    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 8080;

        @Override
        public void run() {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                TakeAttenActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        takeAttenPort.setText("Port: " + serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    String messageFromClient = "";
                    messageFromClient = dataInputStream.readUTF();
                    ClientInfoHolder obj = new ClientInfoHolder(messageFromClient);
                    message = messageFromClient;
                    TakeAttenActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            msg.setText(message);
                        }
                    });
                    clientSecurityCode = obj.getScode();
                    boolean flag = serverSecurityCode.equals(clientSecurityCode);
                    if(flag){
                        String msgReply = "Your Attendance is taken ";
                        dataOutputStream.writeUTF(msgReply);
                    }
                    else{
                        String msgReply = "Your Attendance can't taken ";
                        dataOutputStream.writeUTF(msgReply);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Toast.makeText(TakeAttenActivity.this,"Socket Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(TakeAttenActivity.this,"DataInputStream Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        Toast.makeText(TakeAttenActivity.this,"DataOutputStream Can't Close",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
    private String getIpAddress() {
        String ip ="";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "IP: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            Toast.makeText(TakeAttenActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }
    private void setDropDownList(){
        List<String> dbList = dbHandler.subjectInfoDatabaseToList();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dbList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
    }
}
