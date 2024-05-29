package com.data_management;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import java.io.IOException;

public interface DataReader {

    /**
     * Reads data continuously from a web socket
     *
     * @param port Specified websocket port
     */
    void readDataStream(int port);

    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;
}
