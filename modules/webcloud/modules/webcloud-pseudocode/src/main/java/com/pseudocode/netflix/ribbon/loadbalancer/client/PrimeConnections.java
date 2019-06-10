package com.pseudocode.netflix.ribbon.loadbalancer.client;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

public class PrimeConnections {

    public static abstract interface PrimeConnectionListener
    {
        public abstract void primeCompleted(Server paramServer, Throwable paramThrowable);
    }
}
