package com.example.casanova.myprojectteacher.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casanova.myprojectteacher.R;
import com.example.casanova.myprojectteacher.others.StudentInfo;
import com.example.casanova.myprojectteacher.dbhandlers.StudentInfoTblDbHandler;

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

public class AddstudentActivity extends Activity {
    TextView ip, addStuport,addStuMsg;
    Spinner spAddStu;
    ServerSocket serverSocket;
    String message = "";
    StudentInfoTblDbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

        ip = (TextView)findViewById(R.id.tvIP);
        addStuport = (TextView)findViewById(R.id.tvPortaddStu);
        spAddStu = (Spinner)findViewById(R.id.spinnerStudent);
        addStuMsg = (TextView)findViewById(R.id.addStuMsg);
        dbHandler = new StudentInfoTblDbHandler(this,null,null,1);
        setDropDownList();

        ip.setText(getIpAddress());

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();

    }
    void clickeddel(View v){

    }
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Toast.makeText(AddstudentActivity.this,"Can't close server socket",Toast.LENGTH_SHORT).show();
            }
        }
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
                AddstudentActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        addStuport.setText("Port: " + serverSocket.getLocalPort());
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
                    message = messageFromClient;
                    StudentInfo ob = new StudentInfo(messageFromClient);
                    AddstudentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addStuMsg.setText(message);
                        }
                    });
                    long isSuccess = dbHandler.addStudentInfo(ob);
                    AddstudentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setDropDownList();
                        }
                    });
                    String msgReply;
                    if(isSuccess<0){
                        msgReply = "Unsuccessful!!";
                    }
                    else{
                        msgReply = "Success!!!";
                    }
                    dataOutputStream.writeUTF(msgReply);


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
            Toast.makeText(AddstudentActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }
    private void setDropDownList(){
        List<String> dbList = dbHandler.studentInfoDatabaseToList();
        ArrayAdapter <String> dataAdapter = new ArrayAdapter
                <String>(this,android.R.layout.simple_spinner_item,dbList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAddStu.setAdapter(dataAdapter);
    }
}
