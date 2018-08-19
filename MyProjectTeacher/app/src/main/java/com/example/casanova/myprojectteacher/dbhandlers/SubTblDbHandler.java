package com.example.casanova.myprojectteacher.dbhandlers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;

import com.example.casanova.myprojectteacher.others.SubjectInfo;

import java.util.ArrayList;
import java.util.List;

public class SubTblDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentDatabase.db";
    private static final String TABLE_NAME = "subjectInfoTbl";
    private static final String TABLE_NAME2 = "studentInfoTbl";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SUBJECT_NAME = "subName";
    private static final String COLUMN_SEMESTER_NAME = "semName";
    private static final String COLUMN_YEAR = "year";

    //for student info table
    private static final String COLUMN_IMEI = "imei";
    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_MOBILE_NO = "mbl_no";
    //end here

    public SubTblDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (\"" +
                COLUMN_ID + "\" INTEGER PRIMARY KEY AUTOINCREMENT, \"" +
                COLUMN_SUBJECT_NAME + "\" TEXT, \"" +
                COLUMN_SEMESTER_NAME + "\" TEXT, \"" +
                COLUMN_YEAR + "\" TEXT" +
                ");";
        String query1 = "CREATE TABLE " + TABLE_NAME2 + " (\"" +
                COLUMN_IMEI + "\" TEXT PRIMARY KEY, \"" +
                COLUMN_STUDENT_NAME + "\" TEXT, \"" +
                COLUMN_ID + "\" TEXT, \"" +
                COLUMN_MOBILE_NO + "\" TEXT" +
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
    public void addSubjectInfo(SubjectInfo s){
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT_NAME, s.getSubject());
        values.put(COLUMN_SEMESTER_NAME, s.getSemester());
        values.put(COLUMN_YEAR, s.getYear());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //Delete a row from the database
    public void deleteSubjectInfo(String subName, String semName, String year){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " +
                COLUMN_SUBJECT_NAME + "=\"" + subName + "\" AND " +
                COLUMN_SEMESTER_NAME + "=\"" + semName + "\" AND " +
                COLUMN_YEAR + "=\"" + year+
                "\";");
    }

    public List<String> subjectInfoDatabaseToList(){
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
            if (c.getString(c.getColumnIndex(COLUMN_SUBJECT_NAME)) != null) {
                dbString += c.getString(c.getColumnIndex(COLUMN_SUBJECT_NAME))+","+
                                c.getString(c.getColumnIndex(COLUMN_SEMESTER_NAME))+","+
                                c.getString(c.getColumnIndex(COLUMN_YEAR));
                list.add(dbString);
            }
            dbString = "";
            c.moveToNext();
        }
        db.close();
        return list;
    }
}
