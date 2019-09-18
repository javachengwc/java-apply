package com.pseudocode.netflix.hystrix.core;

import com.netflix.hystrix.util.InternMap;

public interface HystrixCommandKey extends HystrixKey {
    class Factory {
        private Factory() {
        }

        private static final InternMap<String, HystrixCommandKeyDefault> intern = new InternMap<String, HystrixCommandKeyDefault>(
                new InternMap.ValueConstructor<String, HystrixCommandKeyDefault>() {
                    @Override
                    public HystrixCommandKeyDefault create(String key) {
                        return new HystrixCommandKeyDefault(key);
                    }
                });

        public static HystrixCommandKey asKey(String name) {
            return intern.interned(name);
        }

        private static class HystrixCommandKeyDefault extends HystrixKey.HystrixKeyDefault implements HystrixCommandKey {
            public HystrixCommandKeyDefault(String name) {
                super(name);
            }
        }

        static int getCommandCount() {
            return intern.size();
        }
    }

}

