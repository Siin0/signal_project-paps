package alerts;

import com.alerts.AlertGenerator;
import com.alerts.alert_types.Alert;
import com.alerts.alert_types.BloodOxygenAlert;
import com.alerts.alert_types.BloodPressureAlert;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertGeneratorTest {

    @Test
    void testAlertDataEval(){
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        BloodPressureAlertTest.populate(storage);
        BloodOxygenAlertTest.populate(storage);
        ECGAlertTest.populate(storage);

        storage.addPatientData(200, 0, "ManualAlert", 1000L);
        storage.addPatientData(200, 0, "ManualAlert", 2000L);
        storage.addPatientData(200, 80, "SystolicPressure", 20000L);
        storage.addPatientData(200, 90, "BloodSaturation", 20000L);

        Patient healthyPatient = storage.getPatient(100);
        Patient unhealthyPatient = storage.getPatient(200);
        ArrayList<Alert> healthyPatientAlerts = alertGenerator.evaluateData(healthyPatient);
        ArrayList<Alert> unhealthyPatientAlerts = alertGenerator.evaluateData(unhealthyPatient);

        //check that the healthy patient throws no alerts

        System.out.println("Healthy patient caused "+healthyPatientAlerts.size()+" alerts.");
        HashMap<String, Integer> unhealthyAlertsCounter = new HashMap<>();
        for(Alert alert : unhealthyPatientAlerts){
            unhealthyAlertsCounter.put(alert.getCondition(), 0);
        }
        System.out.println("Unhealthy patient caused "+unhealthyPatientAlerts.size()+" alerts.");


        //check if all 15 unique alerts were triggered from the unhealthy patient
        assertEquals(15, unhealthyAlertsCounter.keySet().size());

        //check that the correct number of alerts were triggered in total
        assertEquals(0, healthyPatientAlerts.size());
        assertEquals(28, unhealthyPatientAlerts.size());
    }
}
