package com.example.casanova.myproject.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.casanova.myproject.others.PollQueSetter;
import com.example.casanova.myproject.R;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GetPollQuestionActivity extends AppCompatActivity {

    private EditText etPollIp;
    private EditText etPollPort;
    private Button btn;
    private String question,op1,op2,op3,op4;
    private TextView recMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_poll_question);

        etPollIp = (EditText)findViewById(R.id.etPollIp);
        etPollPort = (EditText)findViewById(R.id.etPollPort);
        btn = (Button)findViewById(R.id.bGetPollQue);
        recMsg = (TextView)findViewById(R.id.recMsg);
    }

    public void getPollQuestion(View v){
        MyClientTask myClientTask = new MyClientTask(etPollIp
                .getText().toString(), Integer.parseInt(etPollPort
                .getText().toString()));
        myClientTask.execute();


        Intent i = new Intent(getBaseContext(),PollActivity.class);
        i.putExtra("ip",etPollIp.getText().toString());
        i.putExtra("port",etPollPort.getText().toString());
        i.putExtra("question",question);
        i.putExtra("op1",op1);
        i.putExtra("op2",op2);
        i.putExtra("op3",op3);
        i.putExtra("op4",op4);
        startActivity(i);

        String ob = question+","+op1+","+op2+","+op3+","+op4;
        recMsg.setText(ob);
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataInputStream = new DataInputStream(socket.getInputStream());

                response = dataInputStream.readUTF();
                PollQueSetter ob = new PollQueSetter(response);
                question = ob.getQuestion();
                op1 = ob.getOp1();
                op2 = ob.getOp2();
                op3 = ob.getOp3();
                op4 = ob.getOp4();

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
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
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
