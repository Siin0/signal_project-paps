package com.data_management;
import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.generators.PatientDataGenerator;
import com.cardio_generator.outputs.OutputStrategy;
import com.cardio_generator.outputs.WebSocketOutputStrategy;

import java.io.*;
import java.io.IOException;

public class DataReaderClass implements DataReader {
    private String dir;
    public WebSocketOutputStrategy socket;

    public DataReaderClass(String dir) {
        super();
        this.dir = dir;
    }

    /**
     * Converts a file into a string format
     *
     * @return A string of the file
     * @throws IOException If file reader encounters an error (File not found)
     */
    private String readFile() throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(dir))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append(" ");
            }
        }
        return resultStringBuilder.toString();
    }

    /**
     * Returns the array of individual words from a string
     *
     * @param string The given string
     * @return A string array of words
     */
    private String[] patientData(String string) {
        return string.split(" ");
    }

    @Override
    public void readDataStream(int port) {
        socket = new WebSocketOutputStrategy(port);

    }

    public void addData(DataStorage dataStorage, String[] data) {
        for (int i = 0; i < data.length; i+= 4) {
            int patientID = Integer.parseInt(data[i]);
            double measurementValue = Double.parseDouble(data[i + 1]);
            String measurementType = data[i + 2];
            long timeStamp = Long.parseLong(data[i + 3]);
            // Add all the values to the dataStorage
            dataStorage.addPatientData(patientID, measurementValue, measurementType, timeStamp);
        }
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        String[] patient = patientData(readFile());
        addData(dataStorage, patient);
    }
}
