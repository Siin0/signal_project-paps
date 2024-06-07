package com.alerts.alert_types;

import java.util.Date;

public class BloodOxygenAlert extends Alert{
    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    @Override
    public void show(){ //could be changed to different behavior depending on hospital hardware (light, sound, etc.)
        String date = (new Date(getTimestamp())).toString();
        String id = this.getPatientId();
        switch(this.getCondition()) {
            case "lowSaturation":
                System.out.println("ALERT: Patient#"+id+" | Blood oxygen saturation has fallen below 92% | "+date);
                break;
            case "saturationRapidDrop":
                System.out.println("ALERT: Patient#"+id+" | Blood oxygen saturation has fallen rapidly | "+date);
                break;
            default:
                super.show();
        }
    }
}
