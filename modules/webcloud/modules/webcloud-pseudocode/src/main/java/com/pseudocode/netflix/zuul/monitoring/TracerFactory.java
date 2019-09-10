package com.pseudocode.netflix.zuul.monitoring;

public abstract class TracerFactory {

    private static TracerFactory INSTANCE;

    public static final void initialize(TracerFactory f) {
        INSTANCE = f;
    }

    public static final TracerFactory instance() {
        if(INSTANCE == null) throw new IllegalStateException(String.format("%s not initialized", TracerFactory.class.getSimpleName()));
        return INSTANCE;
    }

    public abstract Tracer startMicroTracer(String name);

}
