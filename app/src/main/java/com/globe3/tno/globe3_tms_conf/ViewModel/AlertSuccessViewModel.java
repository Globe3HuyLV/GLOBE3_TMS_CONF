package com.globe3.tno.globe3_tms_conf.ViewModel;

import android.app.Dialog;

import com.globe3.tno.globe3_tms_conf.ViewModel.Handler.AlertSuccessHandler;

public class AlertSuccessViewModel {
    public ConfigViewModel _configViewModel;
    public Dialog _dialog;

    public AlertSuccessHandler alertSuccessHandler;

    private String message;

    public AlertSuccessViewModel(Dialog dialog){
        _dialog = dialog;
        alertSuccessHandler = new AlertSuccessHandler(dialog, this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
