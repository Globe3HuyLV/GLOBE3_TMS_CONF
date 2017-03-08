package com.globe3.tno.globe3_tms_conf.ViewModel;

import android.app.Dialog;

import com.globe3.tno.globe3_tms_conf.ViewModel.Handler.AlertConfigHandler;

public class AlertConfigViewModel {
    public ConfigViewModel _configViewModel;
    public Dialog _dialog;

    public AlertConfigHandler alertConfigHandler;

    private String externalConnection;
    private int externalFontColor;

    private String internalConnection;
    private int internalFontColor;

    private String macAddressValidation;
    private int macAddressFontColor;
    private int macAddressPromptVisibility;
    private int saveVisibility;

    public AlertConfigViewModel(Dialog dialog, ConfigViewModel configViewModel){
        _configViewModel = configViewModel;
        _dialog = dialog;
        alertConfigHandler = new AlertConfigHandler(dialog, configViewModel, this);
    }
    public int getExternalFontColor() {
        return externalFontColor;
    }

    public void setExternalFontColor(int externalFontColor) {
        this.externalFontColor = externalFontColor;
    }

    public int getInternalFontColor() {
        return internalFontColor;
    }

    public void setInternalFontColor(int internalFontColor) {
        this.internalFontColor = internalFontColor;
    }

    public String getExternalConnection() {
        return externalConnection;
    }

    public void setExternalConnection(String externalConnection) {
        this.externalConnection = externalConnection;
    }

    public String getInternalConnection() {
        return internalConnection;
    }

    public void setInternalConnection(String internalConnection) {
        this.internalConnection = internalConnection;
    }

    public String getMacAddressValidation() {
        return macAddressValidation;
    }

    public int getMacAddressFontColor() {
        return macAddressFontColor;
    }

    public void setMacAddressFontColor(int macAddressFontColor) {
        this.macAddressFontColor = macAddressFontColor;
    }

    public void setMacAddressValidation(String macAddressValidation) {
        this.macAddressValidation = macAddressValidation;
    }

    public int getMacAddressPromptVisibility() {
        return macAddressPromptVisibility;
    }

    public void setMacAddressPromptVisibility(int macAddressPromptVisibility) {
        this.macAddressPromptVisibility = macAddressPromptVisibility;
    }

    public int getSaveVisibility() {
        return saveVisibility;
    }

    public void setSaveVisibility(int saveVisibility) {
        this.saveVisibility = saveVisibility;
    }
}
