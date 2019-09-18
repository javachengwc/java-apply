package com.pseudocode.netflix.hystrix.core;

import com.netflix.hystrix.util.InternMap;

public interface HystrixThreadPoolKey extends HystrixKey {
    class Factory {
        private Factory() {
        }

        // used to intern instances so we don't keep re-creating them millions of times for the same key
        private static final InternMap<String, HystrixThreadPoolKey> intern = new InternMap<String, HystrixThreadPoolKey>(
                new InternMap.ValueConstructor<String, HystrixThreadPoolKey>() {
                    @Override
                    public HystrixThreadPoolKey create(String key) {
                        return new HystrixThreadPoolKeyDefault(key);
                    }
                });

        public static HystrixThreadPoolKey asKey(String name) {
            return intern.interned(name);
        }

        private static class HystrixThreadPoolKeyDefault extends HystrixKeyDefault implements HystrixThreadPoolKey {
            public HystrixThreadPoolKeyDefault(String name) {
                super(name);
            }
        }

        static int getThreadPoolCount() {
            return intern.size();
        }
    }
}
