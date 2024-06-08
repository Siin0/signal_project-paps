package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.data_management.Patient;

public interface AlertStrategy {
    String checkAlert(Patient patient);
    long getTimestamp();

    AlertFactory createFactory();
}
