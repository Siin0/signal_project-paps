package alerts;

import com.alerts.AlertGenerator;
import com.alerts.strategies.AlertStrategy;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BloodOxygenAlertTest {
    @Test
    void testBloodSaturationStrategy(){
        DataStorage storage = new DataStorage();
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        populate(storage);

        AlertStrategy strategy = new OxygenSaturationStrategy();
        Patient healthyPatient = storage.getPatient(100);
        Patient unhealthyPatient = storage.getPatient(200);
        HashMap<Long, ArrayList<String>> healthyPatientAlerts = strategy.checkAlert(healthyPatient);
        HashMap<Long, ArrayList<String>> unhealthyPatientAlerts = strategy.checkAlert(unhealthyPatient);

        //check that the healthy patient throws no alerts
        ArrayList<String> healthyAlerts = new ArrayList<>();
        for(long timestamp : healthyPatientAlerts.keySet()){
            assertEquals(0, healthyPatientAlerts.get(timestamp).size());
            healthyAlerts.addAll(healthyPatientAlerts.get(timestamp));
            //System.out.println(timestamp+" | "+ Arrays.toString(healthyPatientAlerts.get(timestamp).toArray()));
        }

        System.out.println("Healthy patient caused "+healthyAlerts.size()+" alerts.");

        ArrayList<String> unhealthyAlerts = new ArrayList<>();
        HashMap<String, Integer> alertsCounter = new HashMap<>();
        for(long timestamp : unhealthyPatientAlerts.keySet()){
            for(String alert : unhealthyPatientAlerts.get(timestamp)){
                alertsCounter.put(alert, 0);
            }
            unhealthyAlerts.addAll(unhealthyPatientAlerts.get(timestamp));
        }
        System.out.println("Unhealthy patient caused "+unhealthyAlerts.size()+" alerts.");
        System.out.println("Alerts caused:");


        //check if both alerts were triggered from the unhealthy patient
        assertEquals(2, alertsCounter.keySet().size());
        for(String alert : unhealthyAlerts){
            System.out.println(alert);
        }
        //check that the correct number of alerts were triggered in total
        assertEquals(0, healthyAlerts.size());
        assertEquals(5, unhealthyAlerts.size());
    }

    public static void populate(DataStorage storage){
        storage.addPatientData(100, 96, "BloodSaturation", 1000L);
        storage.addPatientData(100, 98, "BloodSaturation", 2000L);
        storage.addPatientData(100, 95, "BloodSaturation", 3000L);
        storage.addPatientData(100, 98, "BloodSaturation", 4000L);
        storage.addPatientData(100, 99, "BloodSaturation", 5000L);
        storage.addPatientData(100, 99, "BloodSaturation", 6000L);
        storage.addPatientData(100, 93, "BloodSaturation", 606000L);


        storage.addPatientData(200, 96, "BloodSaturation", 1000L);
        storage.addPatientData(200, 98, "BloodSaturation", 2000L);
        storage.addPatientData(200, 95, "BloodSaturation", 3000L);
        storage.addPatientData(200, 93, "BloodSaturation", 4000L);
        storage.addPatientData(200, 92, "BloodSaturation", 5000L);
        storage.addPatientData(200, 91, "BloodSaturation", 6000L);
        storage.addPatientData(200, 80, "BloodSaturation", 605999L);
    }
}
