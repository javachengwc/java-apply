package com.pseudocode.netflix.feign.core;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.pseudocode.netflix.feign.core.Util.checkState;

public interface Contract {

    List<MethodMetadata> parseAndValidatateMetadata(Class<?> targetType);

    abstract class BaseContract implements Contract {

        public List<MethodMetadata> parseAndValidatateMetadata(Class<?> targetType) {
            checkState(targetType.getTypeParameters().length == 0, "Parameterized types unsupported: %s",
                    targetType.getSimpleName());
            checkState(targetType.getInterfaces().length <= 1, "Only single inheritance supported: %s",
                    targetType.getSimpleName());
            if (targetType.getInterfaces().length == 1) {
                checkState(targetType.getInterfaces()[0].getInterfaces().length == 0,
                        "Only single-level inheritance supported: %s",
                        targetType.getSimpleName());
            }
            Map<String, MethodMetadata> result = new LinkedHashMap<String, MethodMetadata>();
            for (Method method : targetType.getMethods()) {
                if (method.getDeclaringClass() == Object.class ||
                        (method.getModifiers() & Modifier.STATIC) != 0 ||
                        Util.isDefault(method)) {
                    continue;
                }
                MethodMetadata metadata = parseAndValidateMetadata(targetType, method);
                checkState(!result.containsKey(metadata.configKey()), "Overrides unsupported: %s",
                        metadata.configKey());
                result.put(metadata.configKey(), metadata);
            }
            return new ArrayList<MethodMetadata>(result.values());
        }


        @Deprecated
        public MethodMetadata parseAndValidatateMetadata(Method method) {
            return parseAndValidateMetadata(method.getDeclaringClass(), method);
        }

        protected MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
            MethodMetadata data = new MethodMetadata();
            return data;
        }

        private static void checkMapString(String name, Class<?> type, Type genericType) {
        }

        protected abstract void processAnnotationOnClass(MethodMetadata data, Class<?> clz);

        protected abstract void processAnnotationOnMethod(MethodMetadata data, Annotation annotation, Method method);

        protected abstract boolean processAnnotationsOnParameter(MethodMetadata data, Annotation[] annotations, int paramIndex);

        protected void nameParam(MethodMetadata data, String name, int i) {
            Collection<String> names = data.indexToName().containsKey(i) ? data.indexToName().get(i) : new ArrayList<String>();
            names.add(name);
            data.indexToName().put(i, names);
        }
    }

    class Default extends BaseContract {

        protected void processAnnotationOnClass(MethodMetadata data, Class<?> targetType) {

        }

        protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation, Method method) {

        }

        protected boolean processAnnotationsOnParameter(MethodMetadata data, Annotation[] annotations, int paramIndex) {
            boolean isHttpAnnotation = false;
            return isHttpAnnotation;
        }

        private static <K, V> boolean searchMapValuesContainsSubstring(Map<K, Collection<String>> map, String search) {
            return false;
        }

        private static Map<String, Collection<String>> toMap(String[] input) {
            Map<String, Collection<String>> result = new LinkedHashMap<String, Collection<String>>(input.length);
            for (String header : input) {
                int colon = header.indexOf(':');
                String name = header.substring(0, colon);
                if (!result.containsKey(name)) {
                    result.put(name, new ArrayList<String>(1));
                }
                result.get(name).add(header.substring(colon + 2));
            }
            return result;
        }
    }
}
