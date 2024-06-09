package com.alerts.decorators;

import com.alerts.strategies.*;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class RepeatedAlertDecorator extends AlertDecorator {
    private final long interval; // Amount of delay between repeated alerts

    public RepeatedAlertDecorator(AlertInterface alert, long interval) {
        super(alert);
        this.interval = interval;
    }

    /**
     * Checks whether the conditions to an alert are active
     *
     * @param strategy The Alert Strategy being used
     * @return False if the conditions for an alert are active
     */
    private boolean checkAlert(AlertStrategy strategy) {
        Patient patient = DataStorage.getInstance().getPatient(Integer.parseInt(alert.getPatientId()));
        return (strategy.checkAlert(patient).equals("noAlert"));
    }

    /**
     * Checks whether for all alert strategies whether the alert must still be active
     *
     * @return False if at least one of the strategies still has an active alert
     */
    private boolean checkStrategies() {
        return checkAlert(new HeartRateStrategy()) &&
                checkAlert(new BloodPressureStrategy()) &&
                checkAlert(new HypotensiveHypoxemiaStrategy()) &&
                checkAlert(new TriggeredAlertStrategy()) &&
                checkAlert(new OxygenSaturationStrategy());
    }

    /**
     * Show the alert if it is divisible by the defined interval
     */
    @Override
    public void show() {
        do {
            alert.show();
        } while(!checkStrategies() && alert.getTimestamp() % interval == 0);
    }
}
