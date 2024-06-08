package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;

public class TriggeredAlertStrategy implements AlertStrategy{
    private long manualTime;
    @Override
    public String checkAlert(Patient patient) {
        //example manually triggered alert, possibly connected to physical button in hospital
        boolean buttonPressed = false;
        if(buttonPressed){
            manualTime = System.currentTimeMillis();
            return "manualAlert";
        } else {
            manualTime = System.currentTimeMillis();
            return "noAlert";
        }
    }

    @Override
    public long getTimestamp() {
        return manualTime;
    }

    @Override
    public AlertFactory createFactory() {
        return new BloodPressureAlertFactory();
    }
}
