package com.globe3.tno.globe3_tms_conf.ViewModel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.globe3.tno.globe3_tms_conf.BR;
import com.globe3.tno.globe3_tms_conf.ViewModel.Handler.ConfigHandler;
import com.globe3.tno.globe3_tms_conf.ViewModel.Handler.LoginHandler;
import com.globe3.tno.globe3_tms_conf.ViewModel.ViewObject.IPAddress;

import org.json.JSONObject;

public class LoginViewModel extends BaseObservable {
    Activity _activity;
    public LoginHandler loginHandler;

    private String company;
    private String userid;
    private String password;

    public LoginViewModel(Activity activity, JSONObject jsonConf){
        _activity = activity;
        this.setCompany("");
        this.setUserid("");
        this.setPassword("");
        loginHandler = new LoginHandler(_activity, this, jsonConf);
    }

    @Bindable
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
        notifyPropertyChanged(BR.company);
    }

    @Bindable
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
        notifyPropertyChanged(BR.userid);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
