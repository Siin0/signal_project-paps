package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;

public class HeartRateStrategy implements AlertStrategy{
    private long ecgTime;
    @Override
    public String checkAlert(Patient patient) {
        long first = patient.getPatientRecords().get(0).getTimestamp();
        long last = patient.getPatientRecords().get(patient.getPatientRecords().size() - 1).getTimestamp();

        ArrayList<PatientRecord> array;
        for (long i = first; i < last; i++) { // This is assuming the time diff is 1 every time, likely incorrect
            array = (ArrayList<PatientRecord>) patient.getRecords(i, (i + 600000)); // 10 minutes in ms
            for (int j = 0; j < array.size(); j++) {
                if (!array.get(j).getRecordType().equals("ECG")) {
                    array.remove(j);
                    j--;
                }
            }

            for(PatientRecord patientRecord : array) {
                if(patientRecord.getMeasurementValue() < 50 ||
                        patientRecord.getMeasurementValue() > 100) {
                    ecgTime = patientRecord.getTimestamp();
                    return "abnormalECG";
                }
            }

            long aux = irregularPatterns(array);
            if(aux > 0) {
                ecgTime = aux;
                return "abnormalECG";
            }
        }
        ecgTime = System.currentTimeMillis();
        return "noAlert";
    }

    private long irregularPatterns(ArrayList<PatientRecord> array) {

        for (int i = 0; i < array.size() - 1; i++) {
            if(Math.abs(array.get(i).getMeasurementValue() - array.get(i + 1).getMeasurementValue()) > 5) {
                return array.get(i).getTimestamp();
            }
        }

        return -1;
    }

    @Override
    public long getTimestamp() {
        return ecgTime;
    }

    @Override
    public AlertFactory createFactory() {
        return new ECGAlertFactory();
    }
}
