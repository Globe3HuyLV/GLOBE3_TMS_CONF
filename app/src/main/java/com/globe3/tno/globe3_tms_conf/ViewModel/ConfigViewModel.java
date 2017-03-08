package com.globe3.tno.globe3_tms_conf.ViewModel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.globe3.tno.globe3_tms_conf.BR;
import com.globe3.tno.globe3_tms_conf.ViewModel.Handler.ConfigHandler;
import com.globe3.tno.globe3_tms_conf.ViewModel.ViewObject.IPAddress;

public class ConfigViewModel extends BaseObservable {
    Activity _activity;
    public ConfigHandler configHandler;

    private String masterfn;
    private String company;
    private String userid;
    private String password;
    private String datasource;

    private boolean externalURLMode;
    private boolean externalIPMode;
    private String externalURL;
    private IPAddress externalIP;

    private boolean internalURLMode;
    private boolean internalIPMode;
    private String internalURL;
    private IPAddress internalIP;

    private boolean isChecked;

    public ConfigViewModel(Activity activity) {
        _activity = activity;
        configHandler = new ConfigHandler(activity, this);

        externalURLMode = true;
        externalIPMode = false;
        externalURL = "";

        internalURLMode = true;
        internalIPMode = false;
        internalURL = "";

        externalIP = new IPAddress();
        externalIP.setEditable(externalIPMode);

        internalIP = new IPAddress();
        internalIP.setEditable(internalIPMode);
        isChecked = false;
    }

    public String getMasterfn() {
        return masterfn;
    }

    @Bindable
    public void setMasterfn(String masterfn) {
        this.masterfn = masterfn;
        notifyPropertyChanged(BR.masterfn);
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

    @Bindable
    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
        notifyPropertyChanged(BR.datasource);
    }

    @Bindable
    public boolean getExternalURLMode() {
        return externalURLMode;
    }

    public void setExternalURLMode(boolean externalURLMode) {
        this.externalURLMode = externalURLMode;
        notifyPropertyChanged(BR.externalURLMode);
    }

    @Bindable
    public boolean getExternalIPMode() {
        return externalIPMode;
    }

    public void setExternalIPMode(boolean externalIPMode) {
        this.externalIPMode = externalIPMode;
        this.externalIP.setEditable(externalIPMode);
        notifyPropertyChanged(BR.externalIPMode);
    }

    @Bindable
    public String getExternalURL() {
        return externalURL;
    }

    public void setExternalURL(String externalURL) {
        this.externalURL = externalURL;
        notifyPropertyChanged(BR.externalURL);
    }

    @Bindable
    public IPAddress getExternalIP() {
        return externalIP;
    }

    public void setExternalIP(IPAddress externalIP) {
        this.externalIP = externalIP;
        notifyPropertyChanged(BR.externalIP);
    }

    @Bindable
    public boolean getInternalURLMode() {
        return internalURLMode;
    }

    public void setInternalURLMode(boolean internalURLMode) {
        this.internalURLMode = internalURLMode;
        notifyPropertyChanged(BR.internalURLMode);
    }

    @Bindable
    public boolean getInternalIPMode() {
        return internalIPMode;
    }

    public void setInternalIPMode(boolean internalIPMode) {
        this.internalIPMode = internalIPMode;
        this.internalIP.setEditable(internalIPMode);
        notifyPropertyChanged(BR.internalIPMode);
    }

    @Bindable
    public String getInternalURL() {
        return internalURL;
    }

    public void setInternalURL(String internalURL) {
        this.internalURL = internalURL;
        notifyPropertyChanged(BR.internalURL);
    }

    @Bindable
    public IPAddress getInternalIP() {
        return internalIP;
    }

    public void setInternalIP(IPAddress internalIP) {
        this.internalIP = internalIP;
        notifyPropertyChanged(BR.internalIP);
    }

    @Bindable
    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
        notifyPropertyChanged(BR.isChecked);
    }
}
