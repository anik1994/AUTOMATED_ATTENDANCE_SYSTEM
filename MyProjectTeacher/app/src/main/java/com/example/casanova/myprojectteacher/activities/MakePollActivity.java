package com.example.casanova.myprojectteacher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casanova.myprojectteacher.R;
import com.example.casanova.myprojectteacher.others.ClientInfoHolder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MakePollActivity extends AppCompatActivity {

    private TextView tvPollIp,tvPollPort,msg,msgSend;
    private EditText etPollQue,etPollOp1,etPollOp2,etPollOp3,etPollOp4;

    String message = "";
    String msgReply = "";
    ServerSocket serverSocket;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_poll);

        tvPollIp = (TextView)findViewById(R.id.tvPollIP);
        tvPollPort = (TextView)findViewById(R.id.tvPollPort);
        msg = (TextView)findViewById(R.id.tvPollMsg);
        msgSend = (TextView)findViewById(R.id.tvPollMsgSend);

        etPollQue = (EditText)findViewById(R.id.etPollQue);
        etPollOp1 = (EditText)findViewById(R.id.etPollOp1);
        etPollOp2 = (EditText)findViewById(R.id.etPollOp2);
        etPollOp3 = (EditText)findViewById(R.id.etPollOp3);
        etPollOp4 = (EditText)findViewById(R.id.etPollOp4);

        btn = (Button)findViewById(R.id.bPollSubmit);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickPollStart(View v){
        btn.setEnabled(false);
        tvPollIp.setText(getIpAddress());
         msgReply = etPollQue.getText().toString()+","+
                etPollOp1.getText().toString()+","+
                etPollOp2.getText().toString()+","+
                etPollOp3.getText().toString()+","+
                etPollOp4.getText().toString();

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
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
                MakePollActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tvPollPort.setText("Port: " + serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    dataOutputStream.writeUTF(msgReply);
                    MakePollActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            msgSend.setText(msgReply);
                        }
                    });
                    String messageFromClient = "";
                    messageFromClient = dataInputStream.readUTF();
                    message = messageFromClient;
                    MakePollActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            msg.setText(message);
                        }
                    });
                }
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

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
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
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }
}
