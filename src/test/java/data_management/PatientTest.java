package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PatientTest {
    @Test
    void testPatients(){
        Patient patient1 = new Patient(1);
        Patient patient2 = new Patient(2);

        patient1.addRecord(60, "Heart Rate", 10);
        patient1.addRecord(90, "Saturation", 10);
        patient1.addRecord(120, "ECG", 10);
        patient1.addRecord(61, "Heart Rate", 11);
        patient1.addRecord(91, "Saturation", 11);
        patient1.addRecord(121, "ECG", 11);

        patient2.addRecord(50, "Heart Rate", 10);
        patient2.addRecord(80, "Saturation", 10);
        patient2.addRecord(110, "ECG", 10);
        patient2.addRecord(51, "Heart Rate", 11);
        patient2.addRecord(81, "Saturation", 11);
        patient2.addRecord(111, "ECG", 11);

        List<PatientRecord> records1 = patient1.getRecords(10,10);
        List<PatientRecord> fullRecords1 = patient1.getPatientRecords();
        assertEquals(3, records1.size());
        assertEquals(60, records1.get(0).getMeasurementValue());
        assertEquals(6, fullRecords1.size());
        assertEquals(60, fullRecords1.get(0).getMeasurementValue());

        patient1.delRecord(records1.get(0).getRecordType(), records1.get(0).getTimestamp());
        List<PatientRecord> records2 = patient1.getRecords(10,10);
        assertEquals(2, records2.size());
        assertEquals(90, records2.get(0).getMeasurementValue());
        assertEquals(5, fullRecords1.size());
        assertEquals(90, fullRecords1.get(0).getMeasurementValue());

        List<PatientRecord> records3 = patient2.getRecords(10,10);
        List<PatientRecord> fullRecords2 = patient2.getPatientRecords();
        assertEquals(3, records3.size());
        assertEquals(50, records3.get(0).getMeasurementValue());
        assertEquals(6, fullRecords2.size());
        assertEquals(50, fullRecords2.get(0).getMeasurementValue());

    }

}
