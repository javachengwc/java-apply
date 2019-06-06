package com.cloud.pseudocode.ribbon;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ConfigurationProperties("ribbon")
public class ServerIntrospectorProperties {

    private List<Integer> securePorts = Arrays.asList(443,8443);

    public List<Integer> getSecurePorts() {
        return securePorts;
    }

    public void setSecurePorts(List<Integer> securePorts) {
        this.securePorts = securePorts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerIntrospectorProperties that = (ServerIntrospectorProperties) o;
        return Objects.equals(securePorts, that.securePorts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securePorts);
    }

    @Override
    public String toString() {
        return new StringBuilder("ServerIntrospectorProperties{")
                .append("securePorts=").append(securePorts)
                .append("}").toString();
    }

}