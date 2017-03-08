package com.globe3.tno.globe3_tms_conf.ViewModel.Handler;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.globe3.tno.globe3_tms_conf.ConfigActivity;
import com.globe3.tno.globe3_tms_conf.Constant.AppSetting;
import com.globe3.tno.globe3_tms_conf.Constant.ConnectionStatus;
import com.globe3.tno.globe3_tms_conf.R;
import com.globe3.tno.globe3_tms_conf.Utils.G3Encryption;
import com.globe3.tno.globe3_tms_conf.ViewModel.AlertConfigViewModel;
import com.globe3.tno.globe3_tms_conf.ViewModel.AlertSuccessViewModel;
import com.globe3.tno.globe3_tms_conf.ViewModel.ConfigViewModel;
import com.globe3.tno.globe3_tms_conf.ViewModel.ViewObject.IPAddress;
import com.globe3.tno.globe3_tms_conf.databinding.AlertConfigBinding;
import com.globe3.tno.globe3_tms_conf.databinding.AlertSuccessBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {
    private Activity _activity;
    ConfigViewModel _configViewModel;

    ProgressDialog progressDialog;

    Map<String, Integer> connectionStatus = new HashMap<String, Integer>();
    Map<String, String> macAddress = new HashMap<String, String>();

    public ConfigHandler(Activity activity, ConfigViewModel configViewModel) {
        _activity = activity;
        _configViewModel = configViewModel;
        connectionStatus.put("external", ConnectionStatus.NOT_TESTED);
        connectionStatus.put("internal", ConnectionStatus.NOT_TESTED);

        macAddress.put("external", "");
        macAddress.put("internal", "");
    }

    public void enableExtUrl(View view) {
        _configViewModel.setExternalIPMode(false);
    }

    public void enableExtIp(View view) {
        _configViewModel.setExternalURLMode(false);
    }

    public void enableIntUrl(View view) {
        _configViewModel.setInternalIPMode(false);
    }

    public void enableIntIp(View view) {
        _configViewModel.setInternalURLMode(false);
    }

    public void checkDeletion(View view) {
        _configViewModel.setIsChecked(!_configViewModel.getIsChecked());
    }

    public void loadConfigFile() {
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

            JSONObject jsonConf = new JSONObject(confDecrypted);

            _configViewModel.setMasterfn(jsonConf.getString("masterfn"));
            _configViewModel.setCompany(jsonConf.getString("company"));
            _configViewModel.setUserid(jsonConf.getString("userid"));
            _configViewModel.setPassword(jsonConf.getString("password"));
            _configViewModel.setDatasource(jsonConf.getString("datasource"));
            if (jsonConf.getBoolean("external_mode_url")) {
                _configViewModel.setExternalURLMode(true);
                _configViewModel.setExternalIPMode(false);
                _configViewModel.setExternalURL(jsonConf.getString("external"));
            } else {
                _configViewModel.setExternalURLMode(false);
                _configViewModel.setExternalIPMode(true);
                _configViewModel.setExternalIP(new IPAddress(true, jsonConf.getString("external")));
            }

            if (jsonConf.getBoolean("internal_mode_url")) {
                _configViewModel.setInternalURLMode(true);
                _configViewModel.setInternalIPMode(false);
                _configViewModel.setInternalURL(jsonConf.getString("internal"));
            } else {
                _configViewModel.setInternalURLMode(false);
                _configViewModel.setInternalIPMode(true);
                _configViewModel.setInternalIP(new IPAddress(true, jsonConf.getString("internal")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig(Dialog alertConfigDialog) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(_activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new SaveConfig(_activity, alertConfigDialog).execute();
            } else {
                requestPermission();
            }

        }
    }

    public void validateConfig(View view) {
        if (_configViewModel.getIsChecked()) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);
            dialogBuilder.setMessage(_activity.getString(R.string.message_confirm_delete_data_folder))
                    .setCancelable(false)
                    .setPositiveButton(_activity.getString(R.string.label_proceed), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            testConnectivity();
                        }
                    }).setNegativeButton(_activity.getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } else {
            testConnectivity();
        }
    }

    private void testConnectivity() {
        progressDialog = ProgressDialog.show(_activity, "", _activity.getString(R.string.message_testing_connectivity));

        if (_configViewModel.getExternalURLMode() && !_configViewModel.getExternalURL().equals("")) {
            _configViewModel.setExternalURL(_configViewModel.getExternalURL().substring(_configViewModel.getExternalURL().length() - 1).equals("/") ? _configViewModel.getExternalURL() : _configViewModel.getExternalURL() + "/");
        }

        if (_configViewModel.getInternalURLMode() && !_configViewModel.getInternalURL().equals("")) {
            _configViewModel.setInternalURL(_configViewModel.getInternalURL().substring(_configViewModel.getInternalURL().length() - 1).equals("/") ? _configViewModel.getInternalURL() : _configViewModel.getInternalURL() + "/");
        }

        connectionStatus.put("external", ConnectionStatus.NOT_TESTED);
        connectionStatus.put("internal", ConnectionStatus.NOT_TESTED);

        new TestConnection("external").execute((_configViewModel.getExternalURLMode() ? _configViewModel.getExternalURL()
                : _configViewModel.getExternalIP().toURL()) + "rest/mobile/login", "cfsqlfilename="
                + _configViewModel.getDatasource() + "&masterfn=" + _configViewModel.getMasterfn()
                + "&company=" + _configViewModel.getCompany() + "&userloginid=" + _configViewModel.getUserid()
                + "&password=" + _configViewModel.getPassword());
        new TestConnection("internal").execute((_configViewModel.getInternalURLMode() ? _configViewModel.getInternalURL()
                : _configViewModel.getInternalIP().toURL()) + "rest/mobile/login", "cfsqlfilename="
                + _configViewModel.getDatasource() + "&masterfn=" + _configViewModel.getMasterfn()
                + "&company=" + _configViewModel.getCompany() + "&userloginid=" + _configViewModel.getUserid()
                + "&password=" + _configViewModel.getPassword());
    }


    private void validateConnections(String connection) {
        if (connectionStatus.get("external") != ConnectionStatus.NOT_TESTED && connectionStatus.get("internal") != ConnectionStatus.NOT_TESTED) {

            Dialog alertConfigDialog = new Dialog(_activity);
            alertConfigDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            AlertConfigBinding alertConfigBinding = DataBindingUtil.inflate(LayoutInflater.from(_activity), R.layout.alert_config, null, false);
            alertConfigDialog.setContentView(alertConfigBinding.getRoot());

            AlertConfigViewModel alertConfigViewModel = new AlertConfigViewModel(alertConfigDialog, _configViewModel);

            alertConfigViewModel.setExternalConnection(getConnectionMessage(connectionStatus.get("external")));
            alertConfigViewModel.setExternalFontColor(ContextCompat.getColor(_activity, connectionStatus.get("external") == ConnectionStatus.CONNECTION_SUCCESS ? R.color.colorSuccess : R.color.colorFailed));
            alertConfigViewModel.setInternalConnection(getConnectionMessage(connectionStatus.get("internal")));
            alertConfigViewModel.setInternalFontColor(ContextCompat.getColor(_activity, connectionStatus.get("internal") == ConnectionStatus.CONNECTION_SUCCESS ? R.color.colorSuccess : R.color.colorFailed));

            if (connectionStatus.get("external") == ConnectionStatus.CONNECTION_SUCCESS && connectionStatus.get("internal") == ConnectionStatus.CONNECTION_SUCCESS) {
                alertConfigViewModel.setMacAddressPromptVisibility(View.VISIBLE);
                if (macAddress.get("external").equals(macAddress.get("internal"))) {
                    alertConfigViewModel.setMacAddressFontColor(ContextCompat.getColor(_activity, R.color.colorSuccess));
                    alertConfigViewModel.setMacAddressValidation(_activity.getString(R.string.message_matched));
                } else {
                    alertConfigViewModel.setSaveVisibility(View.GONE);
                    alertConfigViewModel.setMacAddressFontColor(ContextCompat.getColor(_activity, R.color.colorFailed));
                    alertConfigViewModel.setMacAddressValidation(_activity.getString(R.string.message_did_not_match));
                }
            } else {
                alertConfigViewModel.setMacAddressPromptVisibility(View.GONE);
            }

            alertConfigBinding.setAlertConfigVM(alertConfigViewModel);

            alertConfigDialog.show();

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private String getConnectionMessage(int connectionStatus) {
        switch (connectionStatus) {
            case ConnectionStatus.CONNECTION_FAILED:
                return _activity.getString(R.string.message_connection_failed);
            case ConnectionStatus.CONNECTION_SUCCESS:
                return _activity.getString(R.string.message_connection_success);
            case ConnectionStatus.AUTHENTICATION_FAILED:
                return _activity.getString(R.string.message_authentication_failed);
            case ConnectionStatus.WEBSERVICE_ERROR:
                return _activity.getString(R.string.message_webservice_error);
            default:
                return _activity.getString(R.string.message_not_tested);
        }
    }

    private class TestConnection extends AsyncTask<String, Void, Integer> {
        String _connection;

        public TestConnection(String connection) {
            _connection = connection;
        }

        @Override
        protected Integer doInBackground(String... params) {
            int httpResponse = ConnectionStatus.NOT_TESTED;

            try {
                URL url = new URL(params[0]);
                String message = params[1];

                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setReadTimeout(10000);
                httpConnection.setConnectTimeout(15000);
                httpConnection.setRequestMethod("POST");
                httpConnection.setDoInput(true);
                httpConnection.setDoOutput(true);

                OutputStream outStream = httpConnection.getOutputStream();
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
                bufferWriter.write(message);
                bufferWriter.flush();
                bufferWriter.close();
                outStream.close();

                httpConnection.connect();

                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String resultLine = null;
                while ((resultLine = bufferReader.readLine()) != null) {
                    stringBuilder.append(resultLine + "\n");
                }
                bufferReader.close();

                JSONObject resultJSON = new JSONObject(stringBuilder.toString());

                if (resultJSON.getBoolean("login_valid")) {
                    httpResponse = ConnectionStatus.CONNECTION_SUCCESS;
                    macAddress.put(_connection, resultJSON.getString("mac_address"));
                } else {
                    httpResponse = ConnectionStatus.AUTHENTICATION_FAILED;
                }
                //Thread.sleep(1000, 0);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                httpResponse = ConnectionStatus.CONNECTION_FAILED;
            } catch (IOException e) {
                e.printStackTrace();
                httpResponse = ConnectionStatus.CONNECTION_FAILED;
            } catch (JSONException e) {
                e.printStackTrace();
                httpResponse = ConnectionStatus.CONNECTION_FAILED;
            }/*catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            return httpResponse;
        }

        @Override
        protected void onPostExecute(Integer result) {
            connectionStatus.put(_connection, result);
            validateConnections(_connection);
            return;
        }
    }

    private class SaveConfig extends AsyncTask<Void, Void, Void> {
        Activity _activity;
        Dialog _alertConfigDialog;

        ProgressDialog progressDialog;

        public SaveConfig(Activity activity, Dialog alertConfigDialog) {
            _activity = activity;
            _alertConfigDialog = alertConfigDialog;

            progressDialog = ProgressDialog.show(_activity, "", _activity.getString(R.string.message_saving_configurations));
        }

        @Override
        protected Void doInBackground(Void... params) {
            _alertConfigDialog.dismiss();

            if (_configViewModel.getIsChecked()) {
                String[] filePaths = {"globe3/db", "globe3/timelog", "globe3/project_photos", "globe3/images", "globe3/data"};
                for (String path : filePaths) {
                    File file = new File(Environment.getExternalStorageDirectory(), path);
                    deleteDirectory(file);
                }
            }

            File directory = new File(Environment.getExternalStorageDirectory(), AppSetting.CONF_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try {

                FileOutputStream fOut = new FileOutputStream(new File(directory, AppSetting.CONF_FILE));

                OutputStreamWriter osw = new OutputStreamWriter(fOut);

                G3Encryption g3encryption = G3Encryption.getDefault(G3Encryption.ENC_KEY, G3Encryption.ENC_SALT, new byte[16]);
                String confEncrypted = g3encryption.encryptOrNull("{\"masterfn\": \"" + _configViewModel.getMasterfn() + "\", \"datasource\": \"" + _configViewModel.getDatasource() + "\", \"external_mode_url\":" + String.valueOf(_configViewModel.getExternalURLMode()) + ", \"internal_mode_url\":" + String.valueOf(_configViewModel.getInternalURLMode()) + ", \"external\": \"" + (_configViewModel.getExternalURLMode() ? _configViewModel.getExternalURL() : _configViewModel.getExternalIP().toURL()) + "\",\"internal\": \"" + (_configViewModel.getInternalURLMode() ? _configViewModel.getInternalURL() : _configViewModel.getInternalIP().toURL()) + "\", \"company\":\"" + _configViewModel.getCompany() + "\" , \"userid\":\"" + _configViewModel.getUserid() + "\", \"password\":\"" + _configViewModel.getPassword() + "\"}");
                osw.write(confEncrypted);
                osw.flush();
                osw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            Dialog alertSuccessDialog = new Dialog(_activity);
            alertSuccessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            AlertSuccessBinding alertSuccessBinding = DataBindingUtil.inflate(LayoutInflater.from(_activity), R.layout.alert_success, null, false);
            alertSuccessDialog.setContentView(alertSuccessBinding.getRoot());

            AlertSuccessViewModel alertSuccessViewModel = new AlertSuccessViewModel(alertSuccessDialog);
            alertSuccessViewModel.setMessage(_activity.getString(R.string.message_configurations_saved));

            alertSuccessBinding.setAlertSuccessVM(alertSuccessViewModel);

            alertSuccessDialog.show();
            return;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(_activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(_activity)
                    .setMessage(_activity.getString(R.string.message_app_requires_storage_permission))
                    .setPositiveButton(_activity.getString(R.string.label_request), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConfigActivity.REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }).show();

        } else {
            ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConfigActivity.REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private boolean deleteDirectory(File path) {
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                deleteDirectory(f);
            }
        }
        return (path.delete());
    }
}