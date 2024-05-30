package com.data_management;

import java.io.IOException;
import java.net.URI;

public interface DataReader {

    /**
     * Reads data continuously from a web socket
     *
     * @param data The dataStorage to write to
     * @param uri The URI of the server
     */
    void readDataStream(DataStorage data, URI uri);

    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readFile(DataStorage dataStorage, String dir) throws IOException;
}
