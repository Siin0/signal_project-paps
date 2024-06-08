package com.alerts.decorators;

import com.cardio_generator.outputs.OutputStrategy;

public class PriorityAlertDecorator extends AlertDecorator {
    public PriorityAlertDecorator(AlertInterface alert) {
        super(alert);
    }

    @Override
    public void show() {

    }
}
