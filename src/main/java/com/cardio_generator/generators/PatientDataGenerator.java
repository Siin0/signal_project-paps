package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Calls generate method to ideally generate simulated data
 */
public interface PatientDataGenerator {

    /**
     * Generate data for a patient by a certain method
     *
     * @param patientId Patient to generate data for, identified by ID
     * @param outputStrategy Method to generate data from
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
