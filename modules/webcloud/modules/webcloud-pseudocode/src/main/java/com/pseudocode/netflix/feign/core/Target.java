package com.pseudocode.netflix.feign.core;

import static com.pseudocode.netflix.feign.core.Util.checkNotNull;
import static com.pseudocode.netflix.feign.core.Util.emptyToNull;

public interface Target<T> {

    Class<T> type();

    String name();

    String url();

    public Request apply(RequestTemplate input);

    public static class HardCodedTarget<T> implements Target<T> {

        private final Class<T> type;
        private final String name;
        private final String url;

        public HardCodedTarget(Class<T> type, String url) {
            this(type, url, url);
        }

        public HardCodedTarget(Class<T> type, String name, String url) {
            this.type = checkNotNull(type, "type");
            this.name = checkNotNull(emptyToNull(name), "name");
            this.url = checkNotNull(emptyToNull(url), "url");
        }

        @Override
        public Class<T> type() {
            return type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String url() {
            return url;
        }

        /* no authentication or other special activity. just insert the url. */
        @Override
        public Request apply(RequestTemplate input) {
            if (input.url().indexOf("http") != 0) {
                input.insert(0, url());
            }
            return input.request();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof HardCodedTarget) {
                HardCodedTarget<?> other = (HardCodedTarget) obj;
                return type.equals(other.type)
                        && name.equals(other.name)
                        && url.equals(other.url);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + type.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + url.hashCode();
            return result;
        }

        @Override
        public String toString() {
            if (name.equals(url)) {
                return "HardCodedTarget(type=" + type.getSimpleName() + ", url=" + url + ")";
            }
            return "HardCodedTarget(type=" + type.getSimpleName() + ", name=" + name + ", url=" + url
                    + ")";
        }
    }

    public static final class EmptyTarget<T> implements Target<T> {

        private final Class<T> type;
        private final String name;

        EmptyTarget(Class<T> type, String name) {
            this.type = checkNotNull(type, "type");
            this.name = checkNotNull(emptyToNull(name), "name");
        }

        public static <T> EmptyTarget<T> create(Class<T> type) {
            return new EmptyTarget<T>(type, "empty:" + type.getSimpleName());
        }

        public static <T> EmptyTarget<T> create(Class<T> type, String name) {
            return new EmptyTarget<T>(type, name);
        }

        @Override
        public Class<T> type() {
            return type;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String url() {
            throw new UnsupportedOperationException("Empty targets don't have URLs");
        }

        @Override
        public Request apply(RequestTemplate input) {
            if (input.url().indexOf("http") != 0) {
                throw new UnsupportedOperationException(
                        "Request with non-absolute URL not supported with empty target");
            }
            return input.request();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EmptyTarget) {
                EmptyTarget<?> other = (EmptyTarget) obj;
                return type.equals(other.type)
                        && name.equals(other.name);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + type.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }

        @Override
        public String toString() {
            if (name.equals("empty:" + type.getSimpleName())) {
                return "EmptyTarget(type=" + type.getSimpleName() + ")";
            }
            return "EmptyTarget(type=" + type.getSimpleName() + ", name=" + name + ")";
        }
    }
}

