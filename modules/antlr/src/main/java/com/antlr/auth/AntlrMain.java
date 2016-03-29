package com.antlr.auth;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class AntlrMain {
	
	public static void main(String[] args) throws Exception {  
		//final PlusANTLRInputStream stm = new PlusANTLRInputStream("8-(1+4+7)+(2+3)*4+5");
        ANTLRInputStream stm = new ANTLRInputStream("8-(1+4+7)+(2+3)*4+5");
        AuthLexer lexer = new AuthLexer(stm);  
        CommonTokenStream tokens = new CommonTokenStream(lexer);  
        AuthParser parser = new AuthParser(tokens);

        ParseTree tree = parser.program(); // begin parsing at program rule
        System.out.println("-------------------------------------");
        System.out.println(tree.toStringTree(parser));
        System.out.println("-------------------------------------");

        AuthVisitor<Integer> visitor = new AuthBaseVisitor();
        int result = tree.accept(visitor);
        System.out.println("result="+result);
    }
}
