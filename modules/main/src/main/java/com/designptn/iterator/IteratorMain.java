package com.designptn.iterator;

public class IteratorMain {

    public static void main(String [] args ) {
        String [] array={"a","b","c","d","e","f","g"};
        Container container = new ArrayContainer(array);
        //array[0]="h";
        Iterator<String> i= container.getIterator();
        while(i.hasNext()) {
           String e= i.next();
           System.out.println(e);
        }
    }
}
