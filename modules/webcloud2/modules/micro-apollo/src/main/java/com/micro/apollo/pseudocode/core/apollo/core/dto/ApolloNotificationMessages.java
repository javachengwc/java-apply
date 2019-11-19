package com.micro.apollo.pseudocode.core.apollo.core.dto;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class ApolloNotificationMessages {

    private Map<String, Long> details;

    public ApolloNotificationMessages() {
        this(Maps.<String, Long>newHashMap());
    }

    private ApolloNotificationMessages(Map<String, Long> details) {
        this.details = details;
    }

    public void put(String key, long notificationId) {
        details.put(key, notificationId);
    }

    public Long get(String key) {
        return this.details.get(key);
    }

    public boolean has(String key) {
        return this.details.containsKey(key);
    }

    public boolean isEmpty() {
        return this.details.isEmpty();
    }

    public void mergeFrom(ApolloNotificationMessages source) {
        if (source == null) {
            return;
        }
        for (Map.Entry<String, Long> entry : source.getDetails().entrySet()) {
            if (this.has(entry.getKey()) &&
                    this.get(entry.getKey()) >= entry.getValue()) {
                continue;
            }
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public ApolloNotificationMessages clone() {
        return new ApolloNotificationMessages(ImmutableMap.copyOf(this.details));
    }
}
