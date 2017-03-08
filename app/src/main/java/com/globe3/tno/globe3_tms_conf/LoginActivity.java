package com.globe3.tno.globe3_tms_conf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.globe3.tno.globe3_tms_conf.Constant.AppSetting;
import com.globe3.tno.globe3_tms_conf.Utils.G3Encryption;
import com.globe3.tno.globe3_tms_conf.ViewModel.LoginViewModel;
import com.globe3.tno.globe3_tms_conf.databinding.ActivityLoginBinding;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    LoginViewModel loginViewModel;

    JSONObject jsonConf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new InitializeLogin(this).execute();
    }

    private class InitializeLogin extends AsyncTask<Void, Integer, Boolean> {
        Activity _activity;

        ProgressDialog progressDialog;

        public InitializeLogin(Activity activity){
            _activity = activity;
            progressDialog = ProgressDialog.show(_activity, "", _activity.getString(R.string.message_loading_configurations));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                File fileConf = new File(Environment.getExternalStorageDirectory(), AppSetting.CONF_PATH);

                StringBuilder textConf = new StringBuilder();

                BufferedReader br = new BufferedReader(new FileReader(fileConf));
                String line;

                while ((line = br.readLine()) != null) {
                    textConf.append(line);
                    textConf.append('\n');
                }
                br.close();

                G3Encryption g3encryption = G3Encryption.getDefault(G3Encryption.ENC_KEY, G3Encryption.ENC_SALT, new byte[16]);
                String confDecrypted = g3encryption.decryptOrNull(textConf.toString());

                jsonConf = new JSONObject(confDecrypted);

                loginViewModel = new LoginViewModel(_activity, jsonConf);

                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isConfigLoaded) {
            if(progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }

            if(isConfigLoaded) {
                activityLoginBinding = DataBindingUtil.setContentView(_activity, R.layout.activity_login);
                activityLoginBinding.setLoginVM(loginViewModel);
            }else{
                _activity.startActivity(new Intent(_activity, ConfigActivity.class));
            }
            return;
        }
    }
}
