package com.cardio_generator.outputs;

public interface OutputStrategy {

    /**
     * Outputs the information of a patient
     *
     * @param patientId The ID of a patient
     * @param timestamp The time period of registry
     * @param label The category of registry
     * @param data The data corresponding to the label
     */
    void output(int patientId, long timestamp, String label, String data);
}
