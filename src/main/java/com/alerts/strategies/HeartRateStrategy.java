package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HeartRateStrategy implements AlertStrategy{
    private long ecgTime;
    @Override
    public HashMap<Long, ArrayList<String>> checkAlert(Patient patient) {
        HashMap<Long, ArrayList<String>> results = new HashMap<>();
        List<PatientRecord> observationWindow = new ArrayList<>();

        List<PatientRecord> ecgRecords = patient.getPatientRecords().stream()
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .filter(record -> record.getRecordType().equals("ECG"))
                .collect(Collectors.toList());

        for (PatientRecord record : ecgRecords) {
            long time = record.getTimestamp();
            results.putIfAbsent(time, new ArrayList<>());
            //create a window to look at that includes the past 10 minutes
            observationWindow = ecgRecords.stream()
                    .collect(Collectors.partitioningBy(r -> r.getTimestamp() > (time - 600000))).get(true);

            double average = observationWindow.stream()
                    .mapToDouble(PatientRecord::getMeasurementValue)
                    .average()
                    .orElse(record.getMeasurementValue());

            if (record.getMeasurementValue() > 120) {
                results.get(time).add("highHeartRate");
            } else if (record.getMeasurementValue() < 55) {
                results.get(time).add("lowHeartRate");
            }
            if (Math.abs(record.getMeasurementValue() - average) > 10) {
                results.get(time).add("abnormalECG");
            }
        }
        return results;
    }
    @Override
    public AlertFactory createFactory() {
        return new ECGAlertFactory();
    }
}
