package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.*;
import java.util.stream.Collectors;

public class BloodPressureStrategy implements AlertStrategy{
    @Override
    public HashMap<Long, ArrayList<String>> checkAlert(Patient patient) {
        HashMap<Long, ArrayList<String>> results = new HashMap<>();


        //lists of appropriate record types sorted by timestamp
        List<PatientRecord> sysRecords = patient.getPatientRecords().stream()
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .filter(record -> (record.getRecordType().equals("BloodPressure")
                        || record.getRecordType().equals("SystolicPressure")))
                .collect(Collectors.toList());

        List<PatientRecord> diaRecords = patient.getPatientRecords().stream()
                .sorted(Comparator.comparingLong(PatientRecord::getTimestamp))
                .filter(record -> record.getRecordType().equals("DiastolicPressure"))
                .collect(Collectors.toList());

        int sysUpChanges = 0;
        int sysDownChanges = 0;
        int diaUpChanges = 0;
        int diaDownChanges = 0;
        double sysLastValue = -1;
        double diaLastValue = -1;

        //check all systolic pressure records
        for(PatientRecord record : sysRecords){
            long time = record.getTimestamp();
            results.putIfAbsent(time, new ArrayList<>());

            //check for extreme values
            double pressure = record.getMeasurementValue();
            if (pressure > 180){
                results.get(time).add("systolicHighCritical");
            } else if (pressure < 90){
                results.get(time).add("systolicLowCritical");
            }
            //count big changes in pressure
            if(pressure - sysLastValue > 10 && sysLastValue > -1){
                sysDownChanges = 0;
                sysUpChanges++;
            } else {
                sysUpChanges = 0;
            }
            if (pressure - sysLastValue < -10 && sysLastValue > -1) {
                sysUpChanges = 0;
                sysDownChanges++;
            } else {
                sysDownChanges = 0;
            }
            if(sysUpChanges >= 3){
                results.get(time).add("systolicTrendIncrease");
                sysUpChanges = 0;
            }
            if (sysDownChanges >= 3){
                results.get(time).add("systolicTrendDecrease");
                sysDownChanges = 0;
            }
            sysLastValue = pressure;
        }

        //check all diastolic pressure records
        for(PatientRecord record : diaRecords){
            long time = record.getTimestamp();
            results.putIfAbsent(time, new ArrayList<>());

            //check for extreme values
            double pressure = record.getMeasurementValue();
            if (pressure > 120){
                results.get(time).add("diastolicHighCritical");
            } else if (pressure < 60){
                results.get(time).add("diastolicLowCritical");
            }
            //count big changes in pressure
            if(pressure - diaLastValue > 10 && diaLastValue > -1){
                diaDownChanges = 0;
                diaUpChanges++;
            } else {
                diaUpChanges = 0;
            }
            if(pressure - diaLastValue < -10 && diaLastValue > -1){
                diaUpChanges = 0;
                diaDownChanges++;
            } else {
                diaDownChanges = 0;
            }
            if(diaUpChanges >= 3){
                results.get(time).add("diastolicTrendIncrease");
                diaUpChanges = 0;
            }
            if (diaDownChanges >= 3){
                results.get(time).add("diastolicTrendDecrease");
                diaDownChanges = 0;
            }
            diaLastValue = pressure;
        }


        return results;
    }

    @Override
    public AlertFactory createFactory() {
        return new BloodPressureAlertFactory();
    }
}
