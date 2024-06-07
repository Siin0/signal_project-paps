package com.alerts.factories;

import com.alerts.alert_types.Alert;
import com.alerts.alert_types.ECGAlert;
import com.alerts.alert_types.TriggeredAlert;

public class ECGAlertFactory extends AlertFactory{
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        switch(condition){
            case "abnormalECG":
                return new ECGAlert(patientID, condition, timestamp);
            case "manualTrigger":
                return new TriggeredAlert(patientID, condition, timestamp);
            default:
                return super.createAlert(patientID, condition, timestamp);
        }
    }
}
