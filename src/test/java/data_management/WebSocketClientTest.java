package data_management;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;
import com.data_management.WebSocketClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebSocketClientTest {
    @Test
    void testSendReceiveData() throws IOException, URISyntaxException, InterruptedException {
        //can fail if test duration is too short to process all patients
        int seconds = 10;
        System.out.println("Testing WebSocket...");
        HealthDataSimulator.main(new String[]{"--output", "websocket:8080","--patient-count","50"});
        DataStorage storage = DataStorage.getInstance();
        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:8080"), storage);
        client.connect();
        System.out.println("Sending data for "+seconds+" seconds...");
        Thread.sleep(seconds*1000);
        client.closeConnection(1000, "Test complete.");
        System.out.println(storage.getAllPatients().size()+" patients logged.");
        assertEquals(50, storage.getAllPatients().size());
        storage.deleteInstance();
    }

    @Test
    void testConnectivity() throws IOException, InterruptedException, URISyntaxException {
        int seconds = 10;
        System.out.println("Testing connectivity...");
        HealthDataSimulator.main(new String[]{"--output", "websocket:80","--patient-count","50"});
        DataStorage storage = DataStorage.getInstance();
        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:80"), storage);
        client.connect();
        System.out.println("Sending data for "+seconds+" seconds...");
        seconds *=1000;
        Thread.sleep(seconds/5);
        client.closeConnection(1006, "Simulated unexpected disconnect.");
        System.out.println("Reconnecting...");
        client.reconnect();
        Thread.sleep(seconds/5);
        client.closeConnection(1011, "Simulated server error disconnect.");
        System.out.println("Reconnecting...");
        client.reconnect();
        Thread.sleep(seconds/5);
        client.closeConnection(1012, "Simulated server restart response..");
        System.out.println("Reconnecting...");
        client.reconnect();
        Thread.sleep(seconds/5);
        client.closeConnection(1013, "Simulated server temporarily unavailable.");
        System.out.println("Reconnecting...");
        client.reconnect();
        Thread.sleep(seconds/5);
        client.closeConnection(1000,"Test complete");
        System.out.println(storage.getAllPatients().size()+" patients logged.");
        storage.deleteInstance();
    }
}
