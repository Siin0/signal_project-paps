package com.alerts;

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
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Implementation goes here
        if (bloodPressureAlert(patient)) {
            triggerAlert(new Alert(String.valueOf(patient.getID()), "BloodPressure", bloodPressureTime));
        } else if(bloodSaturationAlert(patient)) {
            triggerAlert(new Alert(String.valueOf(patient.getID()), "BloodSaturation", bloodSaturationTime));
        } else if(comboAlert(patient)) {
            triggerAlert(new Alert(String.valueOf(patient.getID()), "HypotensiveHypoxemia", hhTime));
        } else if(ecgAlert(patient)) {
            triggerAlert(new Alert(String.valueOf(patient.getID()), "ECG", ecgTime));
        }
    }

    /**
     * Determines whether a blood pressure alert should be sent
     *
     * @param patient Target patient
     * @return true if an alert should be sent
     */
    public boolean bloodPressureAlert(Patient patient) {
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
                    sysData.remove(3);
                }
                for (int j = 0; j < sysData.size() - 1; j++) {
                    if ((Math.abs(sysData.get(j) - sysData.get(j + 1)) > 10)
                            || (sysData.get(j) > 180) || (sysData.get(j) < 90)) {
                        bloodPressureTime = patient.getPatientRecords().get(i).getTimestamp();
                        return true;
                    }
                }
            } else if (dia) {
                diaData.add(patient.getPatientRecords().get(i).getMeasurementValue());
                if (diaData.size() > 3) {
                    diaData.remove(3);
                }
                for (int j = 0; j < diaData.size() - 1; j++) {
                    if ((Math.abs(diaData.get(j) - diaData.get(j + 1)) > 10)
                            || (diaData.get(j) > 120) || (diaData.get(j) < 60)) {
                        bloodPressureTime = patient.getPatientRecords().get(i).getTimestamp();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines whether a blood saturation alert should be sent
     *
     * @param patient Target patient
     * @return true if an alert should be sent
     */
    public boolean bloodSaturationAlert(Patient patient) {
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
                    return true;
                } else if (aux > high) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    high = aux;
                } else if (aux < low) {
                    bloodSaturationTime = patientRecord.getTimestamp();
                    low = aux;
                }
            }
                if ((high - low) > 5) {
                    return true;
                }

            }
        return false;
    }

    /**
     * Determines whether a Hypotensive Hypoxemia alert should be sent
     *
     * @param patient Target patient
     * @return true if an alert should be sent
     */
    public boolean comboAlert(Patient patient) {
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
        return (sat && sys);
    }

    /**
     * Determines whether an ECG alert should be sent
     *
     * @param patient Target patient
     * @return true if an alert should be sent
     */
    public boolean ecgAlert(Patient patient) {
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
                    return true;
                }
            }

            long aux = irregularPatterns(array);
            if(aux > 0) {
                ecgTime = aux;
                return true;
            }
        }

        return false;
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
    public void healthGenAlert(Alert output) {
        triggerAlert(output);
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
    }
}
