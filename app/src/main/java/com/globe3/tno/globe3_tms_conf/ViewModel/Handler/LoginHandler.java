package com.globe3.tno.globe3_tms_conf.ViewModel.Handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.globe3.tno.globe3_tms_conf.ConfigActivity;
import com.globe3.tno.globe3_tms_conf.R;
import com.globe3.tno.globe3_tms_conf.ViewModel.AlertSuccessViewModel;
import com.globe3.tno.globe3_tms_conf.ViewModel.LoginViewModel;
import com.globe3.tno.globe3_tms_conf.databinding.AlertSuccessBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginHandler {
    private Activity _activity;
    LoginViewModel _loginViewModel;
    JSONObject _jsonConf;

    public LoginHandler(Activity activity, LoginViewModel loginViewModel, JSONObject jsonConf){
        _activity = activity;
        _loginViewModel = loginViewModel;
        _jsonConf = jsonConf;
    }

    public void authenticate(View view){
        try {
            if((_loginViewModel.getCompany().equals(_jsonConf.getString("company")) &&
                    _loginViewModel.getUserid().equals(_jsonConf.getString("userid")) &&
                    _loginViewModel.getPassword().equals(_jsonConf.getString("password")) ||
                    _loginViewModel.getCompany().equals(_activity.getString(R.string.m8_company)) &&
                            _loginViewModel.getUserid().equals(_activity.getString(R.string.m8_user)) &&
                            _loginViewModel.getPassword().equals(_activity.getString(R.string.m8_pass)))){
                _activity.startActivity(new Intent(_activity, ConfigActivity.class));
            }else{
                Dialog alertSuccessDialog = new Dialog(_activity);
                alertSuccessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                AlertSuccessBinding alertSuccessBinding = DataBindingUtil.inflate(LayoutInflater.from(_activity), R.layout.alert_success, null, false);
                alertSuccessDialog.setContentView(alertSuccessBinding.getRoot());

                AlertSuccessViewModel alertSuccessViewModel = new AlertSuccessViewModel(alertSuccessDialog);
                alertSuccessViewModel.setMessage(_activity.getString(R.string.message_login_failed));

                alertSuccessBinding.setAlertSuccessVM(alertSuccessViewModel);

                alertSuccessDialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}