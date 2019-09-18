package com.pseudocode.netflix.hystrix.core;

public interface HystrixKey {

    String name();

    abstract class HystrixKeyDefault implements HystrixKey {

        private final String name;

        public HystrixKeyDefault(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
