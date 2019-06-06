package com.cloud.pseudocode.common.util;

public class HashCode {

    private static final int SEED = 17;
    private static final long SCALE = 37;

    private int mVal;

    public HashCode() {
        mVal = SEED;
    }


    public int addValue(Object obj) {
        return foldIn((obj != null) ? obj.hashCode() : 0);
    }


    public int addValue(boolean b) {
        return foldIn(b ? 0 : 1);
    }

    public int addValue(byte i) {
        return foldIn(i);
    }

    public int addValue(char i) {
        return foldIn(i);
    }

    public int addValue(short i) {
        return foldIn(i);
    }

    public int addValue(int i) {
        return foldIn(i);
    }

    public int addValue(float f) {
        return foldIn(Float.floatToIntBits(f));
    }

    public int addValue(double f) {
        return foldIn(Double.doubleToLongBits(f));
    }

    public int addValue(Object[] array) {
        int val = hashCode();
        for (Object obj : array) {
            val = addValue(obj);
        }
        return val;
    }

    public int addValue(boolean[] array) {
        int val = hashCode();
        for (boolean b : array) {
            val = addValue(b);
        }
        return val;
    }

    public int addValue(byte[] array) {
        int val = hashCode();
        for (byte i : array) {
            val = addValue(i);
        }
        return val;
    }

    public int addValue(char[] array) {
        int val = hashCode();
        for (char i : array) {
            val = addValue(i);
        }
        return val;
    }

    public int addValue(short[] array) {
        int val = hashCode();
        for (short i : array) {
            val = addValue(i);
        }
        return val;
    }

    public int addValue(int[] array) {
        int val = hashCode();
        for (int i : array) {
            val = addValue(i);
        }
        return val;
    }

    public int addValue(float[] array) {
        int val = hashCode();
        for (float f : array) {
            val = addValue(f);
        }
        return val;
    }

    public int addValue(double[] array) {
        int val = hashCode();
        for (double f : array) {
            val = addValue(f);
        }
        return val;
    }

    public static boolean equalObjects(Object o1, Object o2) {
        if (o1 == null) {
            return (o2 == null);
        } else if (o2 == null) {
            return false;
        } else {
            return o1.equals(o2);
        }
    }

    private int foldIn(int c) {
        return setVal((SCALE * mVal) + c);
    }

    private int foldIn(long c) {
        return setVal((SCALE * mVal) + c);
    }

    private int setVal(long l) {
        mVal = (int)(l ^ (l>>>32));
        return mVal;
    }

    @Override
    public int hashCode() {
        return mVal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        HashCode h = (HashCode)obj;
        return (h.hashCode() == hashCode());
    }

    @Override
    public String toString() {
        return "{HashCode " + mVal + "}";
    }

}
