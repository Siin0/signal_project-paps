package com.data_management;
import java.io.*;
import java.io.IOException;

public class DataReaderClass implements DataReader {
    private final String dir;

    public DataReaderClass(String dir) {
        super();
        this.dir = dir;
    }

    /**
     * Converts a file into a string format
     *
     * @param directory The file directory
     * @return A string of the file
     * @throws IOException If file reader encounters an error (File not found)
     */
    private String readFromInputStream(String directory) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(directory))) {
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
    public void readData(DataStorage dataStorage) throws IOException {
        String[] patient = patientData(readFromInputStream(dir));
        for (int i = 0; i < patient.length; i += 4) {
            int patientID = Integer.parseInt(patient[i]);
            double measurementValue = Double.parseDouble(patient[i + 1]);
            String measurementType = patient[i + 2];
            long timeStamp = Long.parseLong(patient[i + 3]);
            // Add all the values to the dataStorage
            dataStorage.addPatientData(patientID, measurementValue, measurementType, timeStamp);
        }
    }
}
