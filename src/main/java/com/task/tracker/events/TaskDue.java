package com.task.tracker.events;

import com.task.tracker.models.Task;
import org.springframework.context.ApplicationEvent;

public class TaskDue extends ApplicationEvent {

    private final Task task;

    public TaskDue(Object source, Task task) {
        super(source);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "TaskDue{" + "task=" + task + '}';
    }
}
