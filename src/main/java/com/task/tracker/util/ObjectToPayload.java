package com.task.tracker.util;

import java.util.HashMap;
import java.util.Map;

public class ObjectToPayload {
    public static Map<String, Object> createPayload(String key, Object value) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(key, value);
        return payload;
    }
}
