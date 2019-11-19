package com.micro.apollo.pseudocode.core.apollo.core.dto;

import com.ctrip.framework.apollo.core.dto.ApolloNotificationMessages;
import lombok.Data;

@Data
public class ApolloConfigNotification {

    private String namespaceName;

    private long notificationId;

    private volatile ApolloNotificationMessages messages;

    public ApolloConfigNotification() {
    }

    public ApolloConfigNotification(String namespaceName, long notificationId) {
        this.namespaceName = namespaceName;
        this.notificationId = notificationId;
    }

    public void addMessage(String key, long notificationId) {
        if (this.messages == null) {
            synchronized (this) {
                if (this.messages == null) {
                    this.messages = new ApolloNotificationMessages();
                }
            }
        }
        this.messages.put(key, notificationId);
    }

}
