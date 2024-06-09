package data_management;

import com.data_management.DataReaderClass;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataReaderTest {
    @Test
    void testDataReader() throws IOException {
        System.out.println("Testing data reader with a mix of readable and unreadable data:");
        DataReaderClass reader = new DataReaderClass();
        DataStorage dataStorage = DataStorage.getInstance();
        reader.readFile(dataStorage, "src/test/java/data_management/test.txt");
        assertEquals(29310, dataStorage.getRecords(300, 0L, 999999999L).get(0).getMeasurementValue());
        assertEquals("BloodPressure", dataStorage.getRecords(200, 0L, 999999999L).get(0).getRecordType());
        dataStorage.deleteInstance();
    }

}
