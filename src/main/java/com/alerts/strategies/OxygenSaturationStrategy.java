package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;

public class OxygenSaturationStrategy implements AlertStrategy{
    private long bloodSaturationTime;

    @Override
    public String checkAlert(Patient patient) {
        long first = patient.getPatientRecords().get(0).getTimestamp();
        long last = patient.getPatientRecords().get(patient.getPatientRecords().size() - 1).getTimestamp();

        ArrayList<PatientRecord> array;
        for (long i = first; i < last; i++) {
            double high = 0;
            double low = Double.MAX_VALUE;
            array = (ArrayList<PatientRecord>) patient.getRecords(i, (i + 600000));
            for (int j = 0; j < array.size(); j++) {
                if (!array.get(j).getRecordType().equals("BloodSaturation")) {
                    array.remove(j);
                    j--;
                }
            }

            for (PatientRecord patientRecord : array) {
                double aux = patientRecord.getMeasurementValue();
                if (aux < 92) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    return "lowSaturation";
                } else if (aux > high) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    high = aux;
                } else if (aux < low) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    low = aux;
                }
            }
            if ((high - low) > 5) {
                return "saturationRapidDrop";
            }

        }
        bloodSaturationTime = System.currentTimeMillis();
        return "noAlert";
    }

    @Override
    public long getTimestamp() {
        return bloodSaturationTime;
    }

    @Override
    public AlertFactory createFactory() {
        return new BloodOxygenAlertFactory();
    }
}
