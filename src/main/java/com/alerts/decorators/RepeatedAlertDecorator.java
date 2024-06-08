package com.alerts.decorators;

import com.alerts.strategies.*;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class RepeatedAlertDecorator extends AlertDecorator {
    private long interval; // Amount of delay between repeated alerts

    public RepeatedAlertDecorator(AlertInterface alert, String patientID,
                                  String condition, long timeStamp, long interval) {
        super(alert, patientID, condition, timeStamp);
        this.interval = interval;
    }

    /**
     * Checks whether the conditions to an alert are active
     *
     * @param strategy The Alert Strategy being used
     * @return False if the conditions for an alert are active
     */
    private boolean checkAlert(AlertStrategy strategy) {
        Patient patient = DataStorage.getInstance().getPatient(Integer.parseInt(patientID));
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
        if(timeStamp % interval == 0 && !checkStrategies()) {
            super.show();
        }
    }
}
