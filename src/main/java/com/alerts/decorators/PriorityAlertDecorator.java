package com.alerts.decorators;

import com.cardio_generator.outputs.OutputStrategy;

public class PriorityAlertDecorator extends AlertDecorator {
    priority priority;

    public PriorityAlertDecorator(AlertInterface alert, int num) {
        super(alert);
        switch (num) {
            case 1: priority = com.alerts.decorators.priority.LOW; break;
            case 2: priority = com.alerts.decorators.priority.MEDIUM; break;
            case 3: priority = com.alerts.decorators.priority.HIGH; break;
            default: System.out.println("Invalid number");
        }
    }

    @Override
    public void show() {
        alert.show();
        System.out.print("Alert priority level: " + priority + "\n");
    }
}
