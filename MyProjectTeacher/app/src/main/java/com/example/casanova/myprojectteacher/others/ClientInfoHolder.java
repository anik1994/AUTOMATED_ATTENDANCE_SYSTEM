package com.example.casanova.myprojectteacher.others;

import java.util.Arrays;
import java.util.List;

public class ClientInfoHolder {
    private String imei;
    private String scode;
    private String resMsg;

    public ClientInfoHolder(String resMsg) {
        this.resMsg = resMsg;
        splitMsg();
    }
    private void splitMsg(){
        List<String> AList = Arrays.asList(resMsg.split(","));
        this.imei = AList.get(0);
        this.scode = AList.get(1);
    }

    public String getImei() {
        return imei;
    }

    public String getScode() {
        return scode;
    }
}
