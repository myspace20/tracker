package com.task.tracker.events;

import com.task.tracker.services.EmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TaskListener implements ApplicationListener<TaskDue> {

    private final EmailService emailService;

    public TaskListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(TaskDue taskDue) {
        String subject = taskDue.getTask().getTitle() + " is overdue";
        String body = taskDue.getTask().getDescription() + " was due on the " + taskDue.getTask().getDueDate();
        String from = "taskdue@mail.com";
        String to = "taskhandler@mail.com";
        emailService.sendPlainText(to,subject,from,body);
    }
}
