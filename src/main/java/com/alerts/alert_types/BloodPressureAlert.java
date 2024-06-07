package com.alerts.alert_types;

import java.util.Date;

public class BloodPressureAlert extends Alert{
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    @Override
    public void show(){ //could be changed to different behavior depending on hospital hardware (light, sound, etc.)
        String date = (new Date(getTimestamp())).toString();
        String id = this.getPatientId();
        switch(this.getCondition()){
            case "systolicTrendIncrease":
                System.out.println("ALERT: Patient#"+id+" | Sharp increase in systolic blood pressure | "+date);
                break;
            case "diastolicTrendIncrease":
                System.out.println("ALERT: Patient#"+id+" | Sharp increase in diastolic blood pressure | "+date);
                break;
            case "systolicTrendDecrease":
                System.out.println("ALERT: Patient#"+id+" | Sharp decrease in systolic blood pressure | "+date);
                break;
            case "diastolicTrendDecrease":
                System.out.println("ALERT: Patient#"+id+" | Sharp decrease in diastolic blood pressure | "+date);
                break;
            case "systolicHighCritical":
                System.out.println("ALERT: Patient#"+id+" | Systolic blood pressure exceeded 180mmHg | "+date);
                break;
            case "systolicLowCritical":
                System.out.println("ALERT: Patient#"+id+" | Systolic blood pressure dropped below 90mmHg | "+date);
                break;
            case "diastolicHighCritical":
                System.out.println("ALERT: Patient#"+id+" | Diastolic blood pressure exceeded 120mmHg | "+date);
                break;
            case "diastolicLowCritical":
                System.out.println("ALERT: Patient#"+id+" | Diastolic blood pressure dropped below 60mmHg | "+date);
                break;
            default:
                super.show();
        }
    }
}
