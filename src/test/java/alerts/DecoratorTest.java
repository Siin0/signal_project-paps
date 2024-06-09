package alerts;

import com.alerts.alert_types.Alert;
import com.alerts.decorators.PriorityAlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

public class DecoratorTest {

    @Test
    public void testDecorators() {
        // Initialize the alert
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100000, "BloodSaturation", 100);
        Alert alert = new Alert("1", "BloodSaturation", 100);

        // Test the repeat decorator
        RepeatedAlertDecorator repeat = new RepeatedAlertDecorator(alert, 40);
        repeat.show();

        // Test the priority decorator
        PriorityAlertDecorator priority = new PriorityAlertDecorator(alert, 2);
        priority.show();

        // Test both decorators at the same time
        PriorityAlertDecorator bothDecorators = new PriorityAlertDecorator(repeat, 1);
        bothDecorators.show();

        storage.deleteInstance();
    }
}
