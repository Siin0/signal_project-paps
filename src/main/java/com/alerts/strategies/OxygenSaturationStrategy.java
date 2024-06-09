package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OxygenSaturationStrategy implements AlertStrategy{
    private long bloodSaturationTime;

    @Override
    public HashMap<Long, ArrayList<String>> checkAlert(Patient patient) {
        HashMap<Long, ArrayList<String>> results = new HashMap<>();
        List<PatientRecord> observationWindow = new ArrayList<>();

        List<PatientRecord> satRecords = patient.getPatientRecords().stream()
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .filter(record -> record.getRecordType().equals("BloodSaturation"))
                .collect(Collectors.toList());

        for(PatientRecord record : satRecords){
            long time = record.getTimestamp();
            double saturation = record.getMeasurementValue();
            results.putIfAbsent(time, new ArrayList<>());

            if(saturation < 92){
                results.get(time).add("lowSaturation");
            }
            //create a window to look at that includes the past 10 minutes
            observationWindow = satRecords.stream()
                    .collect(Collectors.partitioningBy(r -> r.getTimestamp()>(time-600000))).get(true);
            for(PatientRecord pastRecord : observationWindow){
                if((pastRecord.getMeasurementValue() - record.getMeasurementValue()) > 5){
                    if(!results.get(time).contains("saturationRapidDrop")) {
                        results.get(time).add("saturationRapidDrop");
                    }
                }
            }
        }
        return results;
        /*long first = patient.getPatientRecords().get(0).getTimestamp();
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
                boolean noAlert = true;
                double aux = patientRecord.getMeasurementValue();
                if (aux < 92) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    conditions.add("lowSaturation");
                    noAlert = false;
                }
                if (aux > high) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    high = aux;
                }
                if (aux < low) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    low = aux;
                }
                if ((high - low) > 5) {
                    conditions.add("saturationRapidDrop");
                    noAlert = false;
                }
                if(noAlert){
                    conditions.add("noAlert");
                }
                results.put(bloodSaturationTime, conditions);
            }


        }
        return results; */
    }
    @Override
    public AlertFactory createFactory() {
        return new BloodOxygenAlertFactory();
    }
}
