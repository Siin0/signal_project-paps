package com.alerts.factories;

import com.alerts.alert_types.Alert;
import com.alerts.alert_types.BloodPressureAlert;
import com.alerts.alert_types.HypotensiveHypoxemiaAlert;
import com.alerts.alert_types.TriggeredAlert;

public class BloodPressureAlertFactory extends AlertFactory{
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        switch(condition){
            case "systolicTrendIncrease":
            case "systolicTrendDecrease":
            case "diastolicTrendIncrease":
            case "diastolicTrendDecrease":
            case "systolicHighCritical":
            case "systolicLowCritical":
            case "diastolicHighCritical":
            case "diastolicLowCritical":
                return new BloodPressureAlert(patientID, condition, timestamp);
            case "hypotensiveHypoxemia":
                return new HypotensiveHypoxemiaAlert(patientID, condition, timestamp);
            case "manualAlert":
                return new TriggeredAlert(patientID, condition, timestamp);
            default:
                return super.createAlert(patientID, condition, timestamp);
        }
    }
}
