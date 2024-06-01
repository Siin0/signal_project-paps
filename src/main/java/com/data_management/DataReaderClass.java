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
    private String[] splitData(String string) {
        return string.split(" ");
    }

    /**
     * Reads data continuously from a websocket server
     *
     * @param data The dataStorage to write to
     * @param uri The URI of the server
     */
    @Override
    public void readDataStream(DataStorage data, URI uri)  {
        WebSocketClient webs = new WebSocketClient(uri, data);
        webs.connect();
    }

    /**
     * Adds data to the dataStorage, assumes String is standardized (int, double, string, long)
     *
     * @param dataStorage Data storage to store to
     * @param message String to store to said data storage
     */
    public void addData(DataStorage dataStorage, String message) throws IllegalArgumentException {
        String[] data = splitData(message);

        for (int i = 0; i < data.length/4; i++) {
            try {
                int patientID = Integer.parseInt(data[4 * i]);
                long timeStamp = Long.parseLong(data[4 * i + 1]);
                String measurementType = data[4 * i + 2];
                double measurementValue = Double.parseDouble(data[4 * i + 3].replace("%", ""));
                // Add all the values to the dataStorage
                dataStorage.addPatientData(patientID, measurementValue, measurementType, timeStamp);
            } catch (IllegalArgumentException e){
                System.out.println("Discarding unreadable line, continuing...");
            }
        }
    }

    /**
     * Reads a file and adds its data to a data storage
     *
     * @param dataStorage the storage where data will be stored
     * @param dir the directory of said file
     * @throws IOException If file reader encounters an error (File not found)
     */
    @Override
    public void readFile(DataStorage dataStorage, String dir) throws IOException {
        addData(dataStorage, fileToString(dir));
    }
}
