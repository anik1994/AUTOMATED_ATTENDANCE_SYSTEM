package com.example.casanova.myprojectteacher.dbhandlers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;

import com.example.casanova.myprojectteacher.others.StudentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aust_anik on 7/20/2016.
 */
public class StudentInfoTblDbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentDatabase.db";
    private static final String TABLE_NAME = "studentInfoTbl";
    private static final String TABLE_NAME2 = "subjectInfoTbl";
    private static final String COLUMN_IMEI = "imei";
    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MOBILE_NO = "mbl_no";

    //for subject Info table
    private static final String COLUMN_SUBJECT_NAME = "subName";
    private static final String COLUMN_SEMESTER_NAME = "semName";
    private static final String COLUMN_YEAR = "year";
    //end here

    public StudentInfoTblDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (\"" +
                COLUMN_IMEI + "\" TEXT PRIMARY KEY, \"" +
                COLUMN_STUDENT_NAME + "\" TEXT, \"" +
                COLUMN_ID + "\" TEXT, \"" +
                COLUMN_MOBILE_NO + "\" TEXT" +
                ");";
        String query1 = "CREATE TABLE " + TABLE_NAME2 + " (\"" +
                COLUMN_ID + "\" INTEGER PRIMARY KEY AUTOINCREMENT, \"" +
                COLUMN_SUBJECT_NAME + "\" TEXT, \"" +
                COLUMN_SEMESTER_NAME + "\" TEXT, \"" +
                COLUMN_YEAR + "\" TEXT" +
                ");";
        db.execSQL(query);
        db.execSQL(query1);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    //Add a new row to the database
    public long addStudentInfo(StudentInfo s){
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMEI, s.getIMEI());
        values.put(COLUMN_STUDENT_NAME, s.getName());
        values.put(COLUMN_ID, s.getId());
        values.put(COLUMN_MOBILE_NO, s.getMblNo());
        SQLiteDatabase db = getWritableDatabase();
        long x = db.insert(TABLE_NAME, null, values);
        db.close();
        return x;
    }


    public List<String> studentInfoDatabaseToList(){
        String dbString = "";
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        //Cursor points to a location in your results
        Cursor c = db.rawQuery(query, null);
        //Move to the first row in your results
        c.moveToFirst();

        //Position after the last row means the end of the results
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(COLUMN_IMEI)) != null) {
                dbString += c.getString(c.getColumnIndex(COLUMN_STUDENT_NAME))+","+
                        c.getString(c.getColumnIndex(COLUMN_ID))+","+
                        c.getString(c.getColumnIndex(COLUMN_MOBILE_NO))+","+
                        c.getString(c.getColumnIndex(COLUMN_IMEI));
                list.add(dbString);
            }
            dbString = "";
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }
}
