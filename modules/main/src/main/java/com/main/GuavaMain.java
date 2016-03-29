package com.main;
import java.util.Arrays;
import java.util.Iterator;

import com.entity.Entity;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;


public class GuavaMain {
	
	public static void main(String args [])
	{
		String str ="";
		String rt=Preconditions.checkNotNull(str);
		System.out.println("rt="+rt);
		int i=-1;
		//Preconditions.checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
		System.out.println(Objects.equal("a", "a"));
		
		Entity et = new Entity();
		
		et.setId(10);
		et.setName("do what");
		
		System.out.println(Objects.toStringHelper(et).toString());
		
		System.out.println(Objects.toStringHelper("MyObject").add("x", 1).toString());
		
		Joiner joiner = Joiner.on(";").skipNulls();
	    System.out.println(joiner.join("Harry", null, "Ron", "Hermione"));
	    
	    System.out.println(Joiner.on(",").join(Arrays.asList(1, 5, 7)));
	    
	    Iterable<String> it=Splitter.on(',').trimResults().omitEmptyStrings().split("foo,b2 ar,,   qux");
	    Iterator<String> itr = it.iterator();
	    while(itr.hasNext())
	    {
	    	System.out.println(itr.next());
	    }
	    
	}

}
