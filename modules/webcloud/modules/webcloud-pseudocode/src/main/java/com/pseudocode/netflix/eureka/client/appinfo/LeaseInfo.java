package com.pseudocode.netflix.eureka.client.appinfo;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

//租约信息
@JsonRootName("leaseInfo")
public class LeaseInfo {

    public static final int DEFAULT_LEASE_RENEWAL_INTERVAL = 30;
    public static final int DEFAULT_LEASE_DURATION = 90;

    // Client settings
    //表示eureka client发送心跳给server端的频率。默认30秒
    private int renewalIntervalInSecs = DEFAULT_LEASE_RENEWAL_INTERVAL;
    //Eureka server至上一次收到client的心跳之后，等待下一次心跳的超时时间，也就是服务失效时间,默认为90秒
    private int durationInSecs = DEFAULT_LEASE_DURATION;

    // Server populated
    private long registrationTimestamp;
    private long lastRenewalTimestamp;
    private long evictionTimestamp;
    private long serviceUpTimestamp;

    public static final class Builder {

        private LeaseInfo result;

        private Builder() {
            result = new LeaseInfo();
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setRegistrationTimestamp(long ts) {
            result.registrationTimestamp = ts;
            return this;
        }

        public Builder setRenewalTimestamp(long ts) {
            result.lastRenewalTimestamp = ts;
            return this;
        }

        public Builder setEvictionTimestamp(long ts) {
            result.evictionTimestamp = ts;
            return this;
        }

        public Builder setServiceUpTimestamp(long ts) {
            result.serviceUpTimestamp = ts;
            return this;
        }

        public Builder setDurationInSecs(int d) {
            if (d <= 0) {
                result.durationInSecs = DEFAULT_LEASE_DURATION;
            } else {
                result.durationInSecs = d;
            }
            return this;
        }

        public Builder setRenewalIntervalInSecs(int i) {
            if (i <= 0) {
                result.renewalIntervalInSecs = DEFAULT_LEASE_RENEWAL_INTERVAL;
            } else {
                result.renewalIntervalInSecs = i;
            }
            return this;
        }

        public LeaseInfo build() {
            return result;
        }
    }

    private LeaseInfo() {
    }

    @JsonCreator
    public LeaseInfo(@JsonProperty("renewalIntervalInSecs") int renewalIntervalInSecs,
                     @JsonProperty("durationInSecs") int durationInSecs,
                     @JsonProperty("registrationTimestamp") long registrationTimestamp,
                     @JsonProperty("lastRenewalTimestamp") Long lastRenewalTimestamp,
                     @JsonProperty("renewalTimestamp") long lastRenewalTimestampLegacy,  // for legacy
                     @JsonProperty("evictionTimestamp") long evictionTimestamp,
                     @JsonProperty("serviceUpTimestamp") long serviceUpTimestamp) {
        this.renewalIntervalInSecs = renewalIntervalInSecs;
        this.durationInSecs = durationInSecs;
        this.registrationTimestamp = registrationTimestamp;
        this.evictionTimestamp = evictionTimestamp;
        this.serviceUpTimestamp = serviceUpTimestamp;

        if (lastRenewalTimestamp == null) {
            this.lastRenewalTimestamp = lastRenewalTimestampLegacy;
        } else {
            this.lastRenewalTimestamp = lastRenewalTimestamp;
        }
    }

    public long getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    @JsonProperty("lastRenewalTimestamp")
    public long getRenewalTimestamp() {
        return lastRenewalTimestamp;
    }

    public long getEvictionTimestamp() {
        return evictionTimestamp;
    }

    public long getServiceUpTimestamp() {
        return serviceUpTimestamp;
    }

    public int getRenewalIntervalInSecs() {
        return renewalIntervalInSecs;
    }

    public int getDurationInSecs() {
        return durationInSecs;
    }

}

