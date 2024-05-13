package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataReaderClass;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        // DataReader reader
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
    }

    @Test
    void testDataReader() {
        DataReaderClass reader = new DataReaderClass("src/test/java/data_management/test.txt");
    }

    @Test
    void testAlertTriggers(){}

    @Test
    void testAlertDataEval(){}

    @Test
    void testPatientGetRecords(){
        Patient patient = new Patient(1);
        patient.addRecord(150, "ECG", 1714376789050L);
        patient.addRecord(131, "ECG", 1714376791754L);
        patient.addRecord(112, "BloodPressure", 1714376789131L);
        patient.addRecord(80, "HeartRate", 1714376789988L);

        List<PatientRecord> records = patient.getRecords(1714376789000L, 1714376790000L);
        assertEquals(records.get(0).getMeasurementValue(), 150);
    }
}
