package alerts;

import com.alerts.strategies.AlertStrategy;
import com.alerts.strategies.BloodPressureStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

public class BloodPressureAlertTest {
    @Test
    void testBloodPressureStrategy(){
        DataStorage storage = DataStorage.getInstance();

        populate(storage);

        AlertStrategy strategy = new BloodPressureStrategy();
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


        //check if all 8 unique pressure alerts were triggered from the unhealthy patient
        assertEquals(8, alertsCounter.keySet().size());
        for(String alert : unhealthyAlerts){
            System.out.println(alert);
        }
        //check that the correct number of alerts were triggered in total
        assertEquals(0, healthyAlerts.size());
        assertEquals(10, unhealthyAlerts.size());
        storage.deleteInstance();
    }

    public static void populate(DataStorage storage){
        //normal range for healthy patient, no alert should trigger
        storage.addPatientData(100, 135, "SystolicPressure", 1000L);
        storage.addPatientData(100, 138, "SystolicPressure", 2000L);
        storage.addPatientData(100, 141, "SystolicPressure", 3000L);

        storage.addPatientData(100, 85, "DiastolicPressure", 1000L);
        storage.addPatientData(100, 89, "DiastolicPressure", 2000L);
        storage.addPatientData(100, 91, "DiastolicPressure", 3000L);

        storage.addPatientData(100, 128, "SystolicPressure", 4000L);
        storage.addPatientData(100, 132, "SystolicPressure", 5000L);
        storage.addPatientData(100, 140, "SystolicPressure", 6000L);

        storage.addPatientData(100, 83, "DiastolicPressure", 4000L);
        storage.addPatientData(100, 79, "DiastolicPressure", 5000L);
        storage.addPatientData(100, 78, "DiastolicPressure", 6000L);

        //normal upward trend, no alert should trigger
        storage.addPatientData(100, 135, "SystolicPressure", 7000L);
        storage.addPatientData(100, 146, "SystolicPressure", 8000L);
        storage.addPatientData(100, 154, "SystolicPressure", 9000L);

        storage.addPatientData(100, 81, "DiastolicPressure", 7000L);
        storage.addPatientData(100, 92, "DiastolicPressure", 8000L);
        storage.addPatientData(100, 98, "DiastolicPressure", 9000L);

        //normal downward trend, no alert should trigger
        storage.addPatientData(100, 140, "SystolicPressure", 10000L);
        storage.addPatientData(100, 130, "SystolicPressure", 11000L);
        storage.addPatientData(100, 125, "SystolicPressure", 12000L);

        storage.addPatientData(100, 98, "DiastolicPressure", 10000L);
        storage.addPatientData(100, 85, "DiastolicPressure", 11000L);
        storage.addPatientData(100, 79, "DiastolicPressure", 12000L);

        //edge case: fluctuation, no alert should trigger
        storage.addPatientData(100, 146, "SystolicPressure", 13000L);
        storage.addPatientData(100, 135, "SystolicPressure", 14000L);
        storage.addPatientData(100, 147, "SystolicPressure", 15000L);

        storage.addPatientData(100, 80, "DiastolicPressure", 13000L);
        storage.addPatientData(100, 91, "DiastolicPressure", 14000L);
        storage.addPatientData(100, 79, "DiastolicPressure", 15000L);

        //edge case: extreme but acceptable values, no alert should trigger
        storage.addPatientData(100, 179, "SystolicPressure", 16000L);
        storage.addPatientData(100, 180, "SystolicPressure", 17000L);
        storage.addPatientData(100, 90, "SystolicPressure", 18000L);

        storage.addPatientData(100, 119, "DiastolicPressure", 16000L);
        storage.addPatientData(100, 120, "DiastolicPressure", 17000L);
        storage.addPatientData(100, 60, "DiastolicPressure", 18000L);

        //normal range for unhealthy patient, no alert should trigger
        storage.addPatientData(200, 135, "SystolicPressure", 1000L);
        storage.addPatientData(200, 138, "SystolicPressure", 2000L);
        storage.addPatientData(200, 141, "SystolicPressure", 3000L);

        storage.addPatientData(200, 85, "DiastolicPressure", 1000L);
        storage.addPatientData(200, 89, "DiastolicPressure", 2000L);
        storage.addPatientData(200, 91, "DiastolicPressure", 3000L);

        storage.addPatientData(200, 128, "SystolicPressure", 4000L);
        storage.addPatientData(200, 132, "SystolicPressure", 5000L);
        storage.addPatientData(200, 135, "SystolicPressure", 6000L);

        storage.addPatientData(200, 83, "DiastolicPressure", 4000L);
        storage.addPatientData(200, 79, "DiastolicPressure", 5000L);
        storage.addPatientData(200, 61, "DiastolicPressure", 6000L);

        //large upward trend, alert should trigger
        storage.addPatientData(200, 146, "SystolicPressure", 7000L);
        storage.addPatientData(200, 157, "SystolicPressure", 8000L);
        storage.addPatientData(200, 168, "SystolicPressure", 9000L);

        storage.addPatientData(200, 72, "DiastolicPressure", 7000L);
        storage.addPatientData(200, 83, "DiastolicPressure", 8000L);
        storage.addPatientData(200, 100, "DiastolicPressure", 9000L);

        //large downward trend, alert should trigger
        storage.addPatientData(200, 145, "SystolicPressure", 10000L);
        storage.addPatientData(200, 130, "SystolicPressure", 11000L);
        storage.addPatientData(200, 110, "SystolicPressure", 12000L);

        storage.addPatientData(200, 85, "DiastolicPressure", 10000L);
        storage.addPatientData(200, 73, "DiastolicPressure", 11000L);
        storage.addPatientData(200, 61, "DiastolicPressure", 12000L);

        //edge case: fluctuation, no alert should trigger
        storage.addPatientData(200, 146, "SystolicPressure", 13000L);
        storage.addPatientData(200, 135, "SystolicPressure", 14000L);
        storage.addPatientData(200, 147, "SystolicPressure", 15000L);

        storage.addPatientData(200, 80, "DiastolicPressure", 13000L);
        storage.addPatientData(200, 91, "DiastolicPressure", 14000L);
        storage.addPatientData(200, 79, "DiastolicPressure", 15000L);

        //edge case: almost acceptable values, alert should trigger
        storage.addPatientData(200, 185, "SystolicPressure", 16000L);
        storage.addPatientData(200, 181, "SystolicPressure", 17000L);
        storage.addPatientData(200, 89, "SystolicPressure", 18000L);

        storage.addPatientData(200, 125, "DiastolicPressure", 16000L);
        storage.addPatientData(200, 121, "DiastolicPressure", 17000L);
        storage.addPatientData(200, 59, "DiastolicPressure", 18000L);
    }
}
