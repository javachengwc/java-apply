package com.pseudocode.netflix.ribbon.ribbon;

public final class Ribbon {

    private static final RibbonResourceFactory factory = new DefaultResourceFactory(ClientConfigFactory.DEFAULT,
            RibbonTransportFactory.DEFAULT, AnnotationProcessorsProvider.DEFAULT);

    private Ribbon() {
    }

    public static Builder createHttpResourceGroupBuilder(String name) {
        return factory.createHttpResourceGroupBuilder(name);
    }

    public static HttpResourceGroup createHttpResourceGroup(String name) {
        return factory.createHttpResourceGroup(name);
    }

    public static HttpResourceGroup createHttpResourceGroup(String name, ClientOptions options) {
        return factory.createHttpResourceGroup(name, options);
    }

    public static <T> T from(Class<T> contract) {
        return factory.from(contract);
    }
}

