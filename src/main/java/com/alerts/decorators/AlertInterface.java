package com.alerts.decorators;

import com.cardio_generator.outputs.OutputStrategy;

public interface AlertInterface {

    public String getPatientId();

    public String getCondition();

    public long getTimestamp();

    public void show();

}
