package com.data_management;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;
import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.outputs.WebSocketOutputStrategy;

import javax.xml.crypto.Data;

/**
 * Manages storage and retrieval of patient data within a healthcare monitoring
 * system.
 * This class serves as a repository for all patient records, organized by
 * patient IDs.
 */
public class DataStorage {
    private static DataStorage instance = null;
    private Map<Integer, Patient> patientMap; // Stores patient objects indexed by their unique patient ID.

    /**
     * Constructs a new instance of DataStorage, initializing the underlying storage
     * structure.
     */
    private DataStorage() {
        this.patientMap = new HashMap<>();
    }
    
    public static DataStorage getInstance() {
        if(instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    /**
     * Adds or updates patient data in the storage.
     * If the patient does not exist, a new Patient object is created and added to
     * the storage.
     * Otherwise, the new data is added to the existing patient's records.
     *
     * @param patientId        the unique identifier of the patient
     * @param measurementValue the value of the health metric being recorded
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since the Unix epoch
     */
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        //check to overwrite matching records
        List<PatientRecord> possibleDuplicates = patient.getRecords(timestamp,timestamp);
        for (PatientRecord record : patient.getPatientRecords()) {
            if(record.getRecordType().equals(recordType)){
                patient.delRecord(recordType, timestamp);
            }
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    /**
     * Returns a patient matching the given ID
     * @param patientID the ID to check
     * @return the patient desired
     */
    public Patient getPatient(int patientID){
        return patientMap.get(patientID);
    }
    /**
     * Retrieves a list of PatientRecord objects for a specific patient, filtered by
     * a time range.
     *
     * @param patientId the unique identifier of the patient whose records are to be
     *                  retrieved
     * @param startTime the start of the time range, in milliseconds since the Unix
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since the Unix
     *                  epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>(); // return an empty list if no patient is found
    }

    /**
     * Retrieves a collection of all patients stored in the data storage.
     *
     * @return a list of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    /**
     * The main method for the DataStorage class.
     * Initializes the system, reads data into storage, and continuously monitors
     * and evaluates patient data.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        // DataReader is not defined in this scope, should be initialized appropriately.
        DataReader reader = new DataReaderClass();
        DataStorage storage = DataStorage.getInstance();
        
        WebSocketOutputStrategy test = new WebSocketOutputStrategy(8080);
        BloodPressureDataGenerator gen = new BloodPressureDataGenerator(100);
        for (int i = 0; i < 2; i++) {
            gen.generate(i, test);
        }

        // Assuming the reader has been properly initialized and can read data into the storage
        reader.readFile(storage, "src/test/java/data_management/test.txt");

        // Testing for whether the data has been parsed correctly
        for(Patient patient : storage.getAllPatients()) {
            for(PatientRecord patientRecord : patient.getPatientRecords()) {
                System.out.println("Record for Patient ID: " + patientRecord.getPatientId() +
                        ", Type: " + patientRecord.getRecordType() +
                        ", Data: " + patientRecord.getMeasurementValue() +
                        ", Timestamp: " + patientRecord.getTimestamp());
            }
        }

        // Example of using DataStorage to retrieve and print records for a patient
        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L);
        for (PatientRecord record : records) {
            System.out.println("Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }

        // Initialize the AlertGenerator with the storage
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        // Evaluate all patients' data to check for conditions that may trigger alerts
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }
}
