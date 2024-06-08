package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TriggeredAlertStrategy implements AlertStrategy{
    private long manualTime;
    @Override
    public HashMap<Long, ArrayList<String>> checkAlert(Patient patient) {
        HashMap<Long, ArrayList<String>> results = new HashMap<>();

        List<PatientRecord> manualRecords = patient.getPatientRecords().stream()
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .filter(record -> record.getRecordType().equals("ManualAlert"))
                .collect(Collectors.toList());

        for(PatientRecord record : manualRecords){
            long time = record.getTimestamp();
            results.putIfAbsent(time, new ArrayList<>());
            results.get(time).add("manualAlert");
        }
        return results;
    }


    @Override
    public AlertFactory createFactory() {
        return new BloodPressureAlertFactory();
    }
}
