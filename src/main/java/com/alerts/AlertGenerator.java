package com.alerts;

import com.alerts.alert_types.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.alerts.strategies.*;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private long bloodPressureTime;
    private long bloodSaturationTime;
    private long hhTime;
    private long ecgTime;
    private long manualTime;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine what, if any, alert conditions
     * are met. If a condition is met, an alert created and triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public ArrayList<Alert> evaluateData(Patient patient) {
        ArrayList<Alert> alerts = new ArrayList<Alert>();
        AlertStrategy strategy;

        strategy = new BloodPressureStrategy();
        evaluateStrategy(patient, alerts, strategy);
        strategy = new OxygenSaturationStrategy();
        evaluateStrategy(patient, alerts, strategy);
        strategy = new HeartRateStrategy();
        evaluateStrategy(patient, alerts, strategy);
        strategy = new HypotensiveHypoxemiaStrategy();
        evaluateStrategy(patient, alerts, strategy);
        strategy = new TriggeredAlertStrategy();
        evaluateStrategy(patient, alerts, strategy);

        return alerts;
    }

    private void evaluateStrategy(Patient patient, ArrayList<Alert> alerts, AlertStrategy strategy){
        String condition = strategy.checkAlert(patient);
        if (!condition.equals("noAlert")){
            triggerAlert(alerts, strategy.createFactory().createAlert(String.valueOf(patient.getID()),
                    condition, strategy.getTimestamp()));
        }
    }

    /**
     * Determines whether a blood pressure alert should be sent and what kind
     *
     * @param patient Target patient
     * @return the alert condition as string
     */
    public String bloodPressureAlert(Patient patient) {
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

        return "noAlert";
    }

    /**
     * Determines whether a blood saturation alert should be sent and what kind
     *
     * @param patient Target patient
     * @return the alert condition as string
     */
    public String bloodSaturationAlert(Patient patient) {
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
        return "noAlert";
    }

    /**
     * Determines whether a Hypotensive Hypoxemia alert should be sent
     *
     * @param patient Target patient
     * @return the alert condition as string
     */
    public String comboAlert(Patient patient) {
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
            return "noAlert";
        }
    }

    /**
     * Determines whether an ECG alert should be sent
     *
     * @param patient Target patient
     * @return the alert condition as string
     */
    public String ecgAlert(Patient patient) {
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

        return "noAlert";
    }

    /**
     *
     * @param patient Target patient
     * @return the alert condition as string
     */
    private String manualAlert(Patient patient){
        //example, possibly connected to physical button in hospital
        boolean buttonPressed = false;
        if(buttonPressed){
            return "manualAlert";
        } else {
            return "noAlert";
        }
    }
    /**
     * Determines if there appear any patterns that may suggest irregularities in heartbeat
     *
     * @param array Array of patientRecords which store the patient's information
     * @return -1 if no irregularities, otherwise the time at which it happened
     */
    private long irregularPatterns(ArrayList<PatientRecord> array) {

        for (int i = 0; i < array.size() - 1; i++) {
            if(Math.abs(array.get(i).getMeasurementValue() - array.get(i + 1).getMeasurementValue()) > 5) {
                return array.get(i).getTimestamp();
            }
        }

        return -1;
    }

    /**
     * Determines whether an alert should be sent according to the HealthGenerator.
     *
     * @param output Alert from HealthGenerator
     */
    public void healthGenAlert(ArrayList<Alert> alerts, Alert output) {
        triggerAlert(alerts,output);
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(ArrayList<Alert> alerts, Alert alert) {
        alerts.add(alert);
        alert.show();
        // Implementation might involve logging the alert or notifying staff
    }
}
