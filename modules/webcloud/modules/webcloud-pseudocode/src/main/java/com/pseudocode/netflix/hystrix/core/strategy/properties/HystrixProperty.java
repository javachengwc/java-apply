package com.pseudocode.netflix.hystrix.core.strategy.properties;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicStringProperty;

public interface HystrixProperty<T> {

    public T get();

    public static class Factory {

        public static <T> HystrixProperty<T> asProperty(final T value) {
            return new HystrixProperty<T>() {

                @Override
                public T get() {
                    return value;
                }

            };
        }

        public static HystrixProperty<Integer> asProperty(final DynamicIntegerProperty value) {
            return new HystrixProperty<Integer>() {

                @Override
                public Integer get() {
                    return value.get();
                }

            };
        }

        public static HystrixProperty<Long> asProperty(final DynamicLongProperty value) {
            return new HystrixProperty<Long>() {

                @Override
                public Long get() {
                    return value.get();
                }

            };
        }

        public static HystrixProperty<String> asProperty(final DynamicStringProperty value) {
            return new HystrixProperty<String>() {

                @Override
                public String get() {
                    return value.get();
                }

            };
        }

        public static HystrixProperty<Boolean> asProperty(final DynamicBooleanProperty value) {
            return new HystrixProperty<Boolean>() {

                @Override
                public Boolean get() {
                    return value.get();
                }

            };
        }

        public static <T> HystrixProperty<T> asProperty(final HystrixProperty<T> value, final T defaultValue) {
            return new HystrixProperty<T>() {

                @Override
                public T get() {
                    T v = value.get();
                    if (v == null) {
                        return defaultValue;
                    } else {
                        return v;
                    }
                }

            };
        }

        public static <T> HystrixProperty<T> asProperty(final HystrixProperty<T>... values) {
            return new HystrixProperty<T>() {

                @Override
                public T get() {
                    for (HystrixProperty<T> v : values) {
                        // return the first one that doesn't return null
                        if (v.get() != null) {
                            return v.get();
                        }
                    }
                    return null;
                }

            };
        }

        public static <T> HystrixProperty<T> asProperty(final HystrixPropertiesChainedArchaiusProperty.ChainLink<T> chainedProperty) {
            return new HystrixProperty<T>() {

                @Override
                public T get() {
                    return chainedProperty.get();
                }

            };
        }

        public static <T> HystrixProperty<T> nullProperty() {
            return new HystrixProperty<T>() {

                @Override
                public T get() {
                    return null;
                }

            };
        }

    }

}
