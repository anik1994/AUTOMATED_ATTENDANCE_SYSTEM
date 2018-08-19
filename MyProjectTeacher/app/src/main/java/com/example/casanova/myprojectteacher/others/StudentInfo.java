package com.example.casanova.myprojectteacher.others;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aust_anik on 7/20/2016.
 */
public class StudentInfo {
    private String name;
    private String id;
    private String mblNo;
    private String imei;
    private String resMsg;

    public StudentInfo(){

    }
    public StudentInfo(String name, String id, String mblNo, String imei) {
        this.name = name;
        this.id = id;
        this.mblNo = mblNo;
        this.imei = imei;
    }
    public StudentInfo(String s){
        this.resMsg = s;
        splitMsg();
    }
    private void splitMsg(){
        List<String> AList = Arrays.asList(resMsg.split(","));
        this.name = AList.get(0);
        this.id = AList.get(1);
        this.mblNo = AList.get(2);
        this.imei = AList.get(3);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMblNo() {
        return mblNo;
    }

    public void setMblNo(String mblNo) {
        this.mblNo = mblNo;
    }

    public String getIMEI() {
        return imei;
    }

    public void setIMEI(String IMEI) {
        this.imei = imei;
    }
}
