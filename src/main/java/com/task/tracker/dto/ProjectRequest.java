package com.task.tracker.dto;

import java.sql.Date;

public record ProjectRequest(String name, String description, Date deadline) {
}
