package com.globe3.tno.globe3_tms_conf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.globe3.tno.globe3_tms_conf.ViewModel.ConfigViewModel;
import com.globe3.tno.globe3_tms_conf.databinding.ActivityMainBinding;

public class ConfigActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    ConfigViewModel configViewModel;

    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new InitializeConfig(this).execute();
    }

    private class InitializeConfig extends AsyncTask<Void, Void, Void> {
        Activity _activity;

        ProgressDialog progressDialog;

        public InitializeConfig(Activity activity){
            _activity = activity;
            progressDialog = ProgressDialog.show(_activity, "", _activity.getString(R.string.message_loading_configurations));
        }

        @Override
        protected Void doInBackground(Void... params) {
            configViewModel = new ConfigViewModel(_activity);
            configViewModel.configHandler.loadConfigFile();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }

            activityMainBinding = DataBindingUtil.setContentView(_activity, R.layout.activity_main);
            activityMainBinding.setConfigVM(configViewModel);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.message_storage_permission_not_granted), Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                return;
            }
        }
    }
}
