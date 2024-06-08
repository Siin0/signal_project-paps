package alerts;

import com.alerts.strategies.AlertStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ECGAlertTest {
    @Test
    void testHeartRateStrategy(){
        DataStorage storage = DataStorage.getInstance();

        populate(storage);

        AlertStrategy strategy = new HeartRateStrategy();
        Patient healthyPatient = storage.getPatient(100);
        Patient unhealthyPatient = storage.getPatient(200);
        HashMap<Long, ArrayList<String>> healthyPatientAlerts = strategy.checkAlert(healthyPatient);
        HashMap<Long, ArrayList<String>> unhealthyPatientAlerts = strategy.checkAlert(unhealthyPatient);

        //check that the healthy patient throws no alerts
        ArrayList<String> healthyAlerts = new ArrayList<>();
        for(long timestamp : healthyPatientAlerts.keySet()){
            assertEquals(0, healthyPatientAlerts.get(timestamp).size());
            healthyAlerts.addAll(healthyPatientAlerts.get(timestamp));
            //System.out.println(timestamp+" | "+Arrays.toString(healthyPatientAlerts.get(timestamp).toArray()));
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


        //check if all 3 unique ECG alerts were triggered from the unhealthy patient
        assertEquals(3, alertsCounter.keySet().size());
        for(String alert : unhealthyAlerts){
            System.out.println(alert);
        }
        //check that the correct number of alerts were triggered in total
        assertEquals(0, healthyAlerts.size());
        assertEquals(7, unhealthyAlerts.size());
        storage.deleteInstance();
    }

    public static void populate(DataStorage storage){
        storage.addPatientData(100, 76, "ECG", 1000L);
        storage.addPatientData(100, 78, "ECG", 2000L);
        storage.addPatientData(100, 75, "ECG", 3000L);
        storage.addPatientData(100, 78, "ECG", 4000L);
        storage.addPatientData(100, 79, "ECG", 5000L);
        storage.addPatientData(100, 79, "ECG", 6000L);
        storage.addPatientData(100, 61, "ECG", 606000L);


        storage.addPatientData(200, 76, "ECG", 1000L);
        storage.addPatientData(200, 78, "ECG", 2000L);
        storage.addPatientData(200, 100, "ECG", 3000L);
        storage.addPatientData(200, 55, "ECG", 4000L);
        storage.addPatientData(200, 50, "ECG", 5000L);
        storage.addPatientData(200, 121, "ECG", 6000L);
        storage.addPatientData(200, 122, "ECG", 605999L);
    }
}
