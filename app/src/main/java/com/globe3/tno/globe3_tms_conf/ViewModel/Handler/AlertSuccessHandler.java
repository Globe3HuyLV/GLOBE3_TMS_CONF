package com.globe3.tno.globe3_tms_conf.ViewModel.Handler;

import android.app.Dialog;
import android.view.View;

import com.globe3.tno.globe3_tms_conf.ViewModel.AlertSuccessViewModel;
import com.globe3.tno.globe3_tms_conf.ViewModel.ConfigViewModel;

public class AlertSuccessHandler {
    private Dialog _dialog;
    private AlertSuccessViewModel _alertSuccessViewModel;

    public AlertSuccessHandler(Dialog dialog, AlertSuccessViewModel alertSuccessViewModel) {
        _dialog = dialog;
        _alertSuccessViewModel = alertSuccessViewModel;
    }

    public void finish(View view) {
        if(_dialog != null){
            _dialog.dismiss();
            _dialog = null;
        }
    }
}