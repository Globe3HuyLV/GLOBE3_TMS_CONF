package com.globe3.tno.globe3_tms_conf.ViewModel.ViewObject;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.globe3.tno.globe3_tms_conf.BR;

import java.util.regex.Pattern;

public class IPAddress extends BaseObservable {
    private boolean editable;
    private String num1;
    private String num2;
    private String num3;
    private String num4;
    private String numPort;

    public IPAddress(){
        this.editable = true;
        this.num1 = "";
        this.num2 = "";
        this.num3 = "";
        this.num4 = "";
        this.numPort = "";
    }

    public IPAddress(boolean editable, String num1, String num2, String num3, String num4, String numPort){
        this.editable = editable;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.numPort = numPort;
    }

    public IPAddress(boolean editable, int num1, int num2, int num3, int num4){
        this.editable = editable;
        this.num1 = String.valueOf(num1);
        this.num2 = String.valueOf(num2);
        this.num3 = String.valueOf(num3);
        this.num4 = String.valueOf(num4);
        this.numPort = "";
    }

    public IPAddress(boolean editable, String ipUrl){
        this.editable = editable;

        String ipAddress[] = ipUrl.toLowerCase().replace("http:", "").replace("/", "").split(Pattern.quote("."));

        if(ipAddress.length == 4){
            this.num1 = ipAddress[0];
            this.num2 = ipAddress[1];
            this.num3 = ipAddress[2];

            if(ipAddress[3].split(":").length == 2){
                this.num4 = ipAddress[3].split(Pattern.quote(":"))[0];
                this.numPort = ipAddress[3].split(Pattern.quote(":"))[1];
            }else{
                this.num4 = ipAddress[3];
            }
        }else{
            this.num1 = "";
            this.num2 = "";
            this.num3 = "";
            this.num4 = "";
            this.numPort = "";
        }
    }

    public IPAddress(boolean editable, int num1, int num2, int num3, int num4, int numPort){
        this.editable = editable;
        this.num1 = String.valueOf(num1);
        this.num2 = String.valueOf(num2);
        this.num3 = String.valueOf(num3);
        this.num4 = String.valueOf(num4);
        this.numPort = String.valueOf(numPort);
    }

    @Bindable
    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyPropertyChanged(BR.editable);
    }

    @Bindable
    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
        notifyPropertyChanged(BR.num1);
    }

    @Bindable
    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
        notifyPropertyChanged(BR.num2);
    }

    @Bindable
    public String getNum3() {
        return num3;
    }

    public void setNum3(String num3) {
        this.num3 = num3;
        notifyPropertyChanged(BR.num3);
    }

    @Bindable
    public String getNum4() {
        return num4;
    }

    public void setNum4(String num4) {
        this.num4 = num4.replace(":", "");
        notifyPropertyChanged(BR.num4);
    }

    @Bindable
    public String getNumPort() {
        return numPort;
    }

    public void setNumPort(String numPort) {
        this.numPort = numPort;
        notifyPropertyChanged(BR.numPort);
    }

    public String toURL(){
        return "http://" + this.num1 + "." + this.num2 + "." + this.num3 + "." + this.num4 + (this.numPort!=null & this.numPort!="" ? ":" + this.numPort : "") + "/";
    }
}
