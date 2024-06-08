package com.alerts.decorators;

public class AlertDecorator implements AlertInterface {
    protected AlertInterface alert;

    public AlertDecorator(AlertInterface alert) {
        this.alert = alert;
    }

    @Override
    public String getPatientId() {
        return null;
    }

    @Override
    public String getCondition() {
        return null;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }

    @Override
    public void show(){

    }
}
