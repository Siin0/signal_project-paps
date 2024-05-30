package com.data_management;

import java.io.*;
import java.io.IOException;
import java.net.URI;

public class DataReaderClass implements DataReader {

    public DataReaderClass() {
        super();
    }

    /**
     * Converts a file into a string format
     *
     * @return A string of the file
     * @throws IOException If file reader encounters an error (File not found)
     */
    private String fileToString(String dir) throws IOException {
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
    public void readDataStream(DataStorage data, URI uri)  {
        WebSocketData webs = new WebSocketData(uri, data);
    }

    public void addData(DataStorage dataStorage, String[] data) {
        for (int i = 0; i < data.length/4; i++) {
            int patientID = Integer.parseInt(data[4*i]);
            double measurementValue = Double.parseDouble(data[4*i + 1]);
            String measurementType = data[4*i + 2];
            long timeStamp = Long.parseLong(data[4*i + 3]);
            // Add all the values to the dataStorage
            dataStorage.addPatientData(patientID, measurementValue, measurementType, timeStamp);
        }
    }

    @Override
    public void readFile(DataStorage dataStorage, String dir) throws IOException {
        String[] patient = patientData(fileToString(dir));
        addData(dataStorage, patient);
    }
}
