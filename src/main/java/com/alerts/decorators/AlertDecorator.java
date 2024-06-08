package com.alerts.decorators;

import java.util.Date;

public class AlertDecorator implements AlertInterface {
    protected AlertInterface alert;
    protected String patientID;

    protected String condition;
    protected long timeStamp;

    public AlertDecorator(AlertInterface alert, String patientID, String condition, long timeStamp) {
        this.alert = alert;
        this.patientID = patientID;
        this.condition = condition;
        this.timeStamp = timeStamp;
    }

    @Override
    public String getPatientId() {
        return patientID;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public long getTimestamp() {
        return timeStamp;
    }

    @Override
    public void show(){
        String date = (new Date(getTimestamp())).toString();
        String id = this.getPatientId();
        System.out.println("ALERT: Patient#"+id+" | "+condition+" | "+date);
    }
}
