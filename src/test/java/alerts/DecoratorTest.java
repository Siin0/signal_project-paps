package alerts;

import com.alerts.alert_types.Alert;
import com.alerts.decorators.RepeatedAlertDecorator;
import org.junit.jupiter.api.Test;

public class DecoratorTest {

    @Test
    public void testRepeatDecorator() {
        Alert alert = new Alert("1", "BloodSaturation", 100);
        //Alert test = new RepeatedAlertDecorator()
    }

    @Test
    public void testPriorityDecorator() {

    }
}
