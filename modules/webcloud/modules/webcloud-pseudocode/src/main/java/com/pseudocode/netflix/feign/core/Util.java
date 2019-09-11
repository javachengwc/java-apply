package com.pseudocode.netflix.feign.core;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.lang.String.format;

public class Util {

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String RETRY_AFTER = "Retry-After";

    public static final String ENCODING_GZIP = "gzip";

    public static final String ENCODING_DEFLATE = "deflate";

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    private static final int BUF_SIZE = 0x800; // 2K chars (4K bytes)

    public static final Type MAP_STRING_WILDCARD =null;

//    public static final Type MAP_STRING_WILDCARD = new Types.ParameterizedTypeImpl(null, Map.class, String.class,
//            new Types.WildcardTypeImpl(new Type[]{Object.class}, new Type[0]));

    private Util() {
        // no instances
    }

    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static boolean isDefault(Method method) {
        final int SYNTHETIC = 0x00001000;
        return ((method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC | SYNTHETIC)) ==
                Modifier.PUBLIC) && method.getDeclaringClass().isInterface();
    }

    public static String emptyToNull(String string) {
        return string == null || string.isEmpty() ? null : string;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
        Collection<T> collection;
        if (iterable instanceof Collection) {
            collection = (Collection<T>) iterable;
        } else {
            collection = new ArrayList<T>();
            for (T element : iterable) {
                collection.add(element);
            }
        }
        T[] array = (T[]) Array.newInstance(type, collection.size());
        return collection.toArray(array);
    }

    public static <T> Collection<T> valuesOrEmpty(Map<String, Collection<T>> map, String key) {
        return map.containsKey(key) && map.get(key) != null ? map.get(key) : Collections.<T>emptyList();
    }

    public static void ensureClosed(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) { // NOPMD
            }
        }
    }

    public static Type resolveLastTypeParameter(Type genericContext, Class<?> supertype) throws IllegalStateException {
        Type resolvedSuperType =null;
        //Type resolvedSuperType = Types.getSupertype(genericContext, Types.getRawType(genericContext), supertype);
        checkState(resolvedSuperType instanceof ParameterizedType,"could not resolve %s into a parameterized type %s",genericContext, supertype);
        Type[] types = ParameterizedType.class.cast(resolvedSuperType).getActualTypeArguments();
        for (int i = 0; i < types.length; i++) {
            Type type = types[i];
            if (type instanceof WildcardType) {
                types[i] = ((WildcardType) type).getUpperBounds()[0];
            }
        }
        return types[types.length - 1];
    }

    public static Object emptyValueOf(Type type) {
        return null;
        //return EMPTIES.get(Types.getRawType(type));
    }

    private static final Map<Class<?>, Object> EMPTIES;
    static {
        Map<Class<?>, Object> empties = new LinkedHashMap<Class<?>, Object>();
        empties.put(boolean.class, false);
        empties.put(Boolean.class, false);
        empties.put(byte[].class, new byte[0]);
        empties.put(Collection.class, Collections.emptyList());
        empties.put(Iterator.class, new Iterator<Object>() { // Collections.emptyIterator is a 1.7 api
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new IllegalStateException();
            }
        });
        empties.put(List.class, Collections.emptyList());
        empties.put(Map.class, Collections.emptyMap());
        empties.put(Set.class, Collections.emptySet());
        EMPTIES = Collections.unmodifiableMap(empties);
    }

    public static String toString(Reader reader) throws IOException {
        if (reader == null) {
            return null;
        }
        try {
            StringBuilder to = new StringBuilder();
            CharBuffer buf = CharBuffer.allocate(BUF_SIZE);
            while (reader.read(buf) != -1) {
                buf.flip();
                to.append(buf);
                buf.clear();
            }
            return to.toString();
        } finally {
            ensureClosed(reader);
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        checkNotNull(in, "in");
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(in, out);
            return out.toByteArray();
        } finally {
            ensureClosed(in);
        }
    }

    private static long copy(InputStream from, OutputStream to) throws IOException {
        checkNotNull(from, "from");
        checkNotNull(to, "to");
        byte[] buf = new byte[BUF_SIZE];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    public static String decodeOrDefault(byte[] data, Charset charset, String defaultValue) {
        if (data == null) {
            return defaultValue;
        }
        checkNotNull(charset, "charset");
        try {
            return charset.newDecoder().decode(ByteBuffer.wrap(data)).toString();
        } catch (CharacterCodingException ex) {
            return defaultValue;
        }
    }
}

