package com.alerts.strategies;

import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;

import java.util.ArrayList;

public class BloodPressureStrategy implements AlertStrategy{
    private long bloodPressureTime;
    @Override
    public String checkAlert(Patient patient) {
        ArrayList<Double> sysData = new ArrayList<>();
        ArrayList<Double> diaData = new ArrayList<>();

        for (int i = 0; i < patient.getPatientRecords().size(); i++) {
            boolean sys = false;
            boolean dia = false;
            String s = patient.getPatientRecords().get(i).getRecordType();
            if (s.equals("SystolicPressure")) {
                sys = true;
            } else if (s.equals("DiastolicPressure")) {
                dia = true;
            }
            if (sys || s.equals("BloodPressure")) {
                sysData.add(patient.getPatientRecords().get(i).getMeasurementValue());
                if (sysData.size() > 3) {
                    sysData.remove(0); //roll 3-datum window
                }
                int trendsUp = 0;
                int trendsDown = 0;
                for (int j = 0; j < sysData.size() - 1; j++) {
                    bloodPressureTime = patient.getPatientRecords().get(i).getTimestamp();
                    if (sysData.get(j + 1) - sysData.get(j) < 10) {
                        trendsUp++; //value must exceed threshold consecutively to trigger
                        if (trendsUp >= 3){return "systolicTrendIncrease";}
                    } else if (sysData.get(j + 1) - sysData.get(j) > -10) {
                        trendsDown++;
                        if (trendsDown >= 3){return "systolicTrendDecrease";}
                    } else if (sysData.get(j) > 180) { //check critical thresholds
                        return "systolicHighCritical";
                    } else if (sysData.get(j) < 90) {
                        return "systolicLowCritical";
                    } else {
                        trendsUp = 0;
                        trendsDown = 0;
                    }
                }

            } else if (dia) {
                diaData.add(patient.getPatientRecords().get(i).getMeasurementValue());
                if (diaData.size() > 3) {
                    diaData.remove(0); //roll 3-datum window
                }
                int trendsUp = 0;
                int trendsDown = 0;
                for (int j = 0; j < diaData.size() - 1; j++) {
                    bloodPressureTime = patient.getPatientRecords().get(i).getTimestamp();
                    if (diaData.get(j + 1) - diaData.get(j) < 10) {
                        trendsUp++; //value must exceed threshold consecutively to trigger
                        if (trendsUp >= 3){return "diastolicTrendIncrease";}
                    } else if (diaData.get(j + 1) - diaData.get(j) > -10) {
                        trendsDown++;
                        if (trendsDown >= 3){return "diastolicTrendDecrease";}
                    } else if (diaData.get(j) > 120) { //check critical thresholds
                        return "diastolicHighCritical";
                    } else if (diaData.get(j) < 60) {
                        return "diastolicLowCritical";
                    } else {
                        trendsUp = 0;
                        trendsDown = 0;
                    }
                }
            }
        }
        bloodPressureTime = System.currentTimeMillis();
        return "noAlert";
    }

    @Override
    public long getTimestamp() {
        return bloodPressureTime;
    }

    @Override
    public AlertFactory createFactory() {
        return new BloodPressureAlertFactory();
    }
}
