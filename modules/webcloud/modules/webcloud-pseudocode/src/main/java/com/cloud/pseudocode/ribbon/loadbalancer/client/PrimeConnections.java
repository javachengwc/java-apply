package com.cloud.pseudocode.ribbon.loadbalancer.client;

import com.cloud.pseudocode.ribbon.loadbalancer.Server;

public class PrimeConnections {

    public static abstract interface PrimeConnectionListener
    {
        public abstract void primeCompleted(Server paramServer, Throwable paramThrowable);
    }
}
