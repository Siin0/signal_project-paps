package com.alerts.decorators;

public interface AlertInterface {

    public void show();

    public String getPatientId();

    public String getCondition();

    public long getTimestamp();
}
