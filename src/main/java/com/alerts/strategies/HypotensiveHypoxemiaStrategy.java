package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy{
    private long hhTime;
    @Override
    public String checkAlert(Patient patient) {
        boolean sys = false;
        boolean sat = false;

        long first = patient.getPatientRecords().get(0).getTimestamp();
        long last = patient.getPatientRecords().get(patient.getPatientRecords().size() - 1).getTimestamp();

        ArrayList<PatientRecord> array;
        for (long i = first; i < last; i++) {
            array = (ArrayList<PatientRecord>) patient.getRecords(i, (i + 600000));
            for (int j = 0; j < array.size(); j++) {
                if (!(array.get(j).getRecordType().equals("BloodSaturation") ||
                        array.get(j).getRecordType().equals("SystolicPressure"))) {
                    array.remove(j);
                    j--;
                }
            }
            for (PatientRecord patientRecord : array) {
                if(patientRecord.getRecordType().equals("SystolicPressure")) {
                    if(patientRecord.getMeasurementValue() < 90) {
                        hhTime = patientRecord.getTimestamp();
                        sys = true;
                    }
                } else {
                    if(patientRecord.getMeasurementValue() < 5) {
                        hhTime = patientRecord.getTimestamp();
                        sat = true;
                    }
                }
            }
        }
        if (sat && sys){
            return "hypotensiveHypoxemia";
        } else {
            hhTime = System.currentTimeMillis();
            return "noAlert";
        }
    }

    @Override
    public long getTimestamp() {
        return hhTime;
    }

    @Override
    public AlertFactory createFactory() {
        return new BloodPressureAlertFactory();
    }
}
