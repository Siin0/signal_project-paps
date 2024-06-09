package com.alerts.alert_types;

import com.alerts.decorators.AlertInterface;
import java.util.Date;

// Represents an alert
public class Alert implements AlertInterface {
    private String patientId;
    private String condition;
    private long timestamp;

    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void show(){
        String date = (new Date(getTimestamp())).toString();
        String id = this.getPatientId();
        //System.out.println("ALERT: Patient#"+id+" | "+condition+" | "+date);
    }
}