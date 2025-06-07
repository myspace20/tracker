package com.task.tracker.events;

import com.task.tracker.models.Task;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
public class TaskDuePublisher {

    private final ApplicationEventPublisher publisher;

    public TaskDuePublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(Task task) {
        TaskDue taskDue = new TaskDue(this,task);
        publisher.publishEvent(taskDue);
    }
}
