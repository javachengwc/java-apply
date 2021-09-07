package com.pseudocode.lang;

import java.lang.annotation.Native;

public final class Integer1 {
    // extends Number implements Comparable<Integer>

    @Native public static final int   MIN_VALUE = 0x80000000;

    @Native public static final int   MAX_VALUE = 0x7fffffff;

//    public static final Class<Integer>  TYPE = (Class<Integer>) Class.getPrimitiveClass("int");

    @Native public static final int SIZE = 32;

    public static final int BYTES = SIZE / Byte.SIZE;

    public static int sum(int a, int b) {
        return a + b;
    }

    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    public static int min(int a, int b) {
        return Math.min(a, b);
    }
}
