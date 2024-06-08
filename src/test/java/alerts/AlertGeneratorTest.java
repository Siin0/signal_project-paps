package alerts;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertGeneratorTest {

    @Test
    void testECGTriggers(){
        DataStorage storage = DataStorage.getInstance();
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        storage.addPatientData(100, 66, "ECG", 100000L);
        storage.addPatientData(100, 67, "ECG", 200000L);
        storage.addPatientData(100, 68, "ECG", 300000L);

        storage.addPatientData(200, 55, "ECG", 100000L);
        storage.addPatientData(200, 54, "ECG", 200000L);
        storage.addPatientData(200, 53, "ECG", 300000L);

        storage.addPatientData(300, 95, "ECG", 100000L);
        storage.addPatientData(300, 96, "ECG", 200000L);
        storage.addPatientData(300, 97, "ECG", 300000L);

        Patient patient100 = storage.getAllPatients().get(0);
        Patient patient200 = storage.getAllPatients().get(1);
        Patient patient300 = storage.getAllPatients().get(2);
        assertEquals("noAlert", alertGenerator.ecgAlert(patient100));
        assertEquals("noAlert", alertGenerator.ecgAlert(patient200));
        assertEquals("noAlert", alertGenerator.ecgAlert(patient300));

        storage.addPatientData(100, 90, "ECG", 400000L);
        assertEquals("abnormalECG", alertGenerator.ecgAlert(patient100));
        storage.addPatientData(200, 49, "ECG", 400000L);
        assertEquals("abnormalECG", alertGenerator.ecgAlert(patient200));
        storage.addPatientData(300, 101, "ECG", 400000L);
        assertEquals("abnormalECG", alertGenerator.ecgAlert(patient300));
    }

    @Test
    void testAlertDataEval(){
        DataStorage storage = DataStorage.getInstance();
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        storage.addPatientData(100, 135, "SystolicPressure", 100000L);
        storage.addPatientData(100, 138, "SystolicPressure", 200000L);
        storage.addPatientData(100, 141, "ECG", 300000L);


        Patient healthyPatient = storage.getPatient(100);
        Patient unhealthyPatient = storage.getPatient(200);
    }

}
