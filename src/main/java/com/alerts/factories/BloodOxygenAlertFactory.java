package com.alerts.factories;

import com.alerts.alert_types.Alert;
import com.alerts.alert_types.BloodOxygenAlert;
import com.alerts.alert_types.HypotensiveHypoxemiaAlert;
import com.alerts.alert_types.TriggeredAlert;

public class BloodOxygenAlertFactory extends AlertFactory{
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        switch(condition){
            case "lowSaturation":
            case "saturationRapidDrop":
                return new BloodOxygenAlert(patientID, condition, timestamp);
            case "hypotensiveHypoxemia":
                return new HypotensiveHypoxemiaAlert(patientID, condition, timestamp);
            case "manualTrigger":
                return new TriggeredAlert(patientID, condition, timestamp);
            default:
                return super.createAlert(patientID, condition, timestamp);
        }
    }
}
