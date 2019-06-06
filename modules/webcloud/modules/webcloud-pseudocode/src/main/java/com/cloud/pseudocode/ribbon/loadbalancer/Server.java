package com.cloud.pseudocode.ribbon.loadbalancer;

import com.cloud.pseudocode.common.util.Pair;

//服务端节点
public class Server {

    public static interface MetaInfo {

        public String getAppName();

        public String getServerGroup();

        public String getServiceIdForDiscovery();

        public String getInstanceId();
    }

    public static final String UNKNOWN_ZONE = "UNKNOWN";
    private String host;
    private int port = 80;
    private String scheme;
    private volatile String id;
    private volatile boolean isAliveFlag;
    private String zone = UNKNOWN_ZONE;
    private volatile boolean readyToServe = true;

    private MetaInfo simpleMetaInfo = new MetaInfo() {
        @Override
        public String getAppName() {
            return null;
        }

        @Override
        public String getServerGroup() {
            return null;
        }

        @Override
        public String getServiceIdForDiscovery() {
            return null;
        }

        @Override
        public String getInstanceId() {
            return id;
        }
    };

    public Server(String host, int port) {
        this(null, host, port);
    }

    public Server(String scheme, String host, int port) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.id = host + ":" + port;
        isAliveFlag = false;
    }

    public Server(String id) {
        setId(id);
        isAliveFlag = false;
    }

    public void setAlive(boolean isAliveFlag) {
        this.isAliveFlag = isAliveFlag;
    }

    public boolean isAlive() {
        return isAliveFlag;
    }

    @Deprecated
    public void setHostPort(String hostPort) {
        setId(hostPort);
    }

    static public String normalizeId(String id) {
        Pair<String, Integer> hostPort = getHostPort(id);
        if (hostPort == null) {
            return null;
        } else {
            return hostPort.first() + ":" + hostPort.second();
        }
    }

    private static String getScheme(String id) {
        if (id != null) {
            if (id.toLowerCase().startsWith("http://")) {
                return "http";
            } else if (id.toLowerCase().startsWith("https://")) {
                return "https";
            }
        }
        return null;
    }

    static Pair<String, Integer> getHostPort(String id) {
        if (id != null) {
            String host = null;
            int port = 80;

            if (id.toLowerCase().startsWith("http://")) {
                id = id.substring(7);
            } else if (id.toLowerCase().startsWith("https://")) {
                id = id.substring(8);
            }

            if (id.contains("/")) {
                int slash_idx = id.indexOf("/");
                id = id.substring(0, slash_idx);
            }

            int colon_idx = id.indexOf(':');

            if (colon_idx == -1) {
                host = id; // default
                port = 80;
            } else {
                host = id.substring(0, colon_idx);
                try {
                    port = Integer.parseInt(id.substring(colon_idx + 1));
                } catch (NumberFormatException e) {
                    throw e;
                }
            }
            return new Pair<String, Integer>(host, port);
        } else {
            return null;
        }

    }

    public void setId(String id) {
        Pair<String, Integer> hostPort = getHostPort(id);
        if (hostPort != null) {
            this.id = hostPort.first() + ":" + hostPort.second();
            this.host = hostPort.first();
            this.port = hostPort.second();
            this.scheme = getScheme(id);
        } else {
            this.id = null;
        }
    }

    public void setSchemea(String scheme) {
        this.scheme = scheme;
    }

    public void setPort(int port) {
        this.port = port;

        if (host != null) {
            id = host + ":" + port;
        }
    }

    public void setHost(String host) {
        if (host != null) {
            this.host = host;
            id = host + ":" + port;
        }
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHostPort() {
        return host + ":" + port;
    }

    public MetaInfo getMetaInfo() {
        return simpleMetaInfo;
    }

    public String toString() {
        return this.getId();
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Server))
            return false;
        Server svc = (Server) obj;
        return svc.getId().equals(this.getId());

    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == this.getId() ? 0 : this.getId().hashCode());
        return hash;
    }

    public final String getZone() {
        return zone;
    }

    public final void setZone(String zone) {
        this.zone = zone;
    }

    public final boolean isReadyToServe() {
        return readyToServe;
    }

    public final void setReadyToServe(boolean readyToServe) {
        this.readyToServe = readyToServe;
    }
}

