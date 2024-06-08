package com.alerts.decorators;

import com.alerts.alert_types.TriggeredAlert;
import com.alerts.strategies.*;
import com.data_management.DataStorage;

public class RepeatedAlertDecorator extends AlertDecorator {
    private long interval; // Amount of delay between repeated alerts

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
        String res = strategy.checkAlert(DataStorage.getInstance().getPatient(Integer.parseInt(super.getPatientId())));
        return (res.equals("noAlert"));
    }

    /**
     * Checks whether for all alert strategies whether the alert must still be active
     *
     * @return False if one of the strategies still has an active alert
     */
    private boolean checkStrategies() {
        return checkAlert(new HeartRateStrategy()) &&
                checkAlert(new BloodPressureStrategy()) &&
                checkAlert(new HypotensiveHypoxemiaStrategy()) &&
                checkAlert(new TriggeredAlertStrategy()) &&
                checkAlert(new OxygenSaturationStrategy());
    }

    /**
     * Show the alert if it is divisible by the defined interval of time
     */
    @Override
    public void show() {
        while(!checkStrategies()) {
            super.show();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
