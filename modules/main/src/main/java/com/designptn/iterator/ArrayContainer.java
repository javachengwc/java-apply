package com.designptn.iterator;

import java.util.Arrays;

public class ArrayContainer implements Container {

    public String[] array = new String[0];

    public ArrayContainer(String[] ay ) {
        this.array= Arrays.copyOf(ay,ay.length);
    }

    public Iterator getIterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<String> {

        int index;

        @Override
        public boolean hasNext() {
            if(index < array.length){
                return true;
            }
            return false;
        }

        @Override
        public String next() {
            if(this.hasNext()){
                return array[index++];
            }
            return null;
        }
    }
}
