package com.globe3.tno.globe3_tms_conf.ViewModel.Handler;

import android.app.Dialog;
import android.view.View;

import com.globe3.tno.globe3_tms_conf.ViewModel.AlertConfigViewModel;
import com.globe3.tno.globe3_tms_conf.ViewModel.ConfigViewModel;

public class AlertConfigHandler {
    public ConfigViewModel _configViewModel;
    private Dialog _dialog;
    private AlertConfigViewModel _alertConfigViewModel;

    public AlertConfigHandler(Dialog dialog, ConfigViewModel configViewModel, AlertConfigViewModel alertConfigViewModel){
        _configViewModel = configViewModel;
        _dialog = dialog;
        _alertConfigViewModel = alertConfigViewModel;
    }

    public void saveConfig(View view){
        _configViewModel.configHandler.saveConfig(_dialog);
    }

    public void dismissDialog(View view){
        if(_dialog != null){
            _dialog.dismiss();
            _dialog = null;
        }
    }
}
