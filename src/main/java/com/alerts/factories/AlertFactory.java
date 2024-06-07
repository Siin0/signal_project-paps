package com.alerts.factories;

import com.alerts.alert_types.Alert;

public class AlertFactory {
    public Alert createAlert(String patientID, String condition, long timestamp){
        return new Alert(patientID, condition, timestamp);
    }
}
