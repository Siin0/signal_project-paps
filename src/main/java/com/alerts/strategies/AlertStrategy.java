package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.data_management.Patient;

import java.util.ArrayList;
import java.util.HashMap;

public interface AlertStrategy {
    HashMap<Long, ArrayList<String>> checkAlert(Patient patient);
    AlertFactory createFactory();
}
