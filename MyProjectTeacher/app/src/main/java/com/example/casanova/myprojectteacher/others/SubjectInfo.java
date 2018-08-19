package com.example.casanova.myprojectteacher.others;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aust_anik on 7/18/2016.
 */
public class SubjectInfo {
    private String id;
    private String subject;
    private String semester;
    private String year;
    private String resMsg;

    public SubjectInfo() {
    }

    public SubjectInfo(String subject, String semester, String year) {
        this.subject = subject;
        this.semester = semester;
        this.year = year;
    }

    public SubjectInfo(String resMsg) {
        this.resMsg = resMsg;
        splitMsg();
    }

    private void splitMsg(){
        List<String> AList = Arrays.asList(resMsg.split(","));
        this.subject = AList.get(0);
        this.semester = AList.get(1);
        this.year = AList.get(2);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
