package com.alerts.alert_types;

import java.util.Date;

public class HypotensiveHypoxemiaAlert extends Alert{
    public HypotensiveHypoxemiaAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    @Override
    public void show(){ //could be changed to different behavior depending on hospital hardware (light, sound, etc.)
        String date = (new Date(getTimestamp())).toString();
        String id = this.getPatientId();
        switch(this.getCondition()){
            case "hypotensiveHypoxemia":
                System.out.println("ALERT: Patient#"+id+" | Hypotensive hypoxemia | "+date);
                break;
            default:
                super.show();

        }
    }
}
