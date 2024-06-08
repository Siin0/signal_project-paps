package com.alerts.decorators;

import com.cardio_generator.outputs.OutputStrategy;

public class PriorityAlertDecorator extends AlertDecorator {
    priority priority;

    public PriorityAlertDecorator(AlertInterface alert, String patientID,
                                  String condition, long timeStamp, priority priority) {
        super(alert, patientID, condition, timeStamp);
        this.priority = priority;
    }

    @Override
    public void show() {
        super.show();
        System.out.print(" Alert priority level: " + priority);
    }

    enum priority {
        LOW,
        MEDIUM,
        HIGH
    }
}
