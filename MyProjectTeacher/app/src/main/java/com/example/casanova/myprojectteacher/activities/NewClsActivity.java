package com.example.casanova.myprojectteacher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.casanova.myprojectteacher.R;
import com.example.casanova.myprojectteacher.others.SubjectInfo;
import com.example.casanova.myprojectteacher.dbhandlers.SubTblDbHandler;

import java.util.List;

public class NewClsActivity extends AppCompatActivity {
    private EditText etSubject,etSemester,etYear;
    private Button btnAdd,btnDel,btnSubmit;
    private Spinner spin;
    private SubTblDbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cls);

        etSubject = (EditText) findViewById(R.id.etSubject);
        etSemester = (EditText) findViewById(R.id.etSem);
        etYear = (EditText) findViewById(R.id.etYear);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDel = (Button) findViewById(R.id.btnDel);
        spin =(Spinner) findViewById(R.id.spinner);
        dbHandler = new SubTblDbHandler(this, null, null, 1);
        setDropDownList();
    }

    public void addSubInfoClicked(View v){
        String sub = etSubject.getText().toString();
        String sem = etSemester.getText().toString();
        String year = etYear.getText().toString();
        if(sub.equals("")||sem.equals("")||year.equals("")){
            Toast.makeText(this,"You can't left any field blank", Toast.LENGTH_SHORT).show();
        }
        else {
            SubjectInfo temp = new SubjectInfo(sub,sem,year);
            dbHandler.addSubjectInfo(temp);
            setDropDownList();
        }
    }

    public void delSubInfoClicked(View v){
        String sub = etSubject.getText().toString();
        String sem = etSemester.getText().toString();
        String year = etYear.getText().toString();
        if(sub.equals("")||sem.equals("")||year.equals("")){
            Toast.makeText(this,"You can't left any field blank",Toast.LENGTH_SHORT).show();
        }
        else {
            dbHandler.deleteSubjectInfo(sub,sem,year);
            setDropDownList();
        }
    }

    private void setDropDownList(){
        List<String> dbList = dbHandler.subjectInfoDatabaseToList();
        ArrayAdapter <String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dbList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
    }
}
