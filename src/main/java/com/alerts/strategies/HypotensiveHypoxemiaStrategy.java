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

public class HypotensiveHypoxemiaStrategy implements AlertStrategy{
    private long hhTime;
    @Override
    public HashMap<Long, ArrayList<String>> checkAlert(Patient patient) {
        HashMap<Long, ArrayList<String>> results = new HashMap<>();

        List<PatientRecord> sysRecords = patient.getPatientRecords().stream()
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .filter(record -> (record.getRecordType().equals("BloodPressure")
                        || record.getRecordType().equals("SystolicPressure")))
                .collect(Collectors.toList());

        List<PatientRecord> satRecords = patient.getPatientRecords().stream()
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .filter(record -> record.getRecordType().equals("BloodSaturation"))
                .collect(Collectors.toList());

        for(PatientRecord sysRecord : sysRecords){
            for(PatientRecord satRecord : satRecords){
                if(sysRecord.getTimestamp() == satRecord.getTimestamp()){
                    long time = sysRecord.getTimestamp();
                    results.putIfAbsent(time, new ArrayList<>());
                    if(sysRecord.getMeasurementValue() < 90 && satRecord.getMeasurementValue() < 92){
                        results.get(time).add("hypotensiveHypoxemia");
                    }
                }
            }
        }
        return results;
    }

    @Override
    public AlertFactory createFactory() {
        return new BloodPressureAlertFactory();
    }
}
