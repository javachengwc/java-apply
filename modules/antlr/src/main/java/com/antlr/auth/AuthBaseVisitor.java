// Generated from D:\tmp\Excel\Auth.g4 by ANTLR 4.0
package com.antlr.auth;

import com.util.NumberUtil;
import org.antlr.v4.runtime.tree.*;

public class AuthBaseVisitor extends AbstractParseTreeVisitor<Integer> implements
		AuthVisitor<Integer> {
	
	private static String OPSSTR="+-*"; 
	private static String BRACKET="()";
	
	@Override
	public Integer visitMultExpr(AuthParser.MultExprContext ctx) {
		
		int count =ctx.getChildCount();
		System.out.println("mult childs count ="+count +",text="+ctx.getText());
		
		Integer p1=null;
		Integer p2=null;
		String op=null;
		boolean doP1=false;
		boolean doP2=false;
		
		for(int i=0;i<count;i++)
		{
			System.out.println(i+" "+ctx.getChild(i).getText());
			String tmp=ctx.getChild(i).getText();
			if(NumberUtil.isNumeric(tmp))
			{   
				if(!doP1)
				{
				   p1=Integer.parseInt(tmp);
				   doP1=true;
				}else
				{
					p2=Integer.parseInt(tmp);
					doP2=true;
				}	
				
			}else if(OPSSTR.indexOf(tmp)>=0)
			{
				op=tmp;
			}else
			{
				if(!doP1)
				{
				   p1=visitAtom((AuthParser.AtomContext) ctx.getChild(i));
				   doP1=true;
				}else
				{
					p2=visitAtom((AuthParser.AtomContext) ctx.getChild(i));
					doP2=true;
				}	
			}
			if(doP1 && doP2 && op!=null)
			{
				p1=doCalc(op,p1,p2);
				doP2=false;
			}	
		}
		System.out.println("mult rt="+p1);
		return p1;
	}

	@Override
	public Integer visitExpression(AuthParser.ExpressionContext ctx) {
		int count =ctx.getChildCount();
		System.out.println("expression childs count ="+count +",text="+ctx.getText());
		
		Integer p1=null;
		Integer p2=null;
		String op=null;
		boolean doP1=false;
		boolean doP2=false;
		
		for(int i=0;i<count;i++)
		{
			System.out.println(i+" "+ctx.getChild(i).getText());
			String tmp=ctx.getChild(i).getText();
			if(NumberUtil.isNumeric(tmp))
			{   
				if(!doP1)
				{
				   p1=Integer.parseInt(tmp);
				   doP1=true;
				}else
				{
					p2=Integer.parseInt(tmp);
					doP2=true;
				}	
				
			}else if(OPSSTR.indexOf(tmp)>=0)
			{
				op=tmp;
			}else
			{
				if(!doP1)
				{
				   p1=visitMultExpr((AuthParser.MultExprContext) ctx.getChild(i));
				   doP1=true;
				}else
				{
					p2=visitMultExpr((AuthParser.MultExprContext) ctx.getChild(i));
					doP2=true;
				}	
			}
			if(doP1 && doP2 && op!=null)
			{
				p1=doCalc(op,p1,p2);
				doP2=false;
			}	
		}
		System.out.println("expression rt="+p1);
		return p1;
	}

	@Override
	public Integer visitStatement(AuthParser.StatementContext ctx) {
		System.out.println("statement ctx="+ctx.getText());
		return visitExpression((AuthParser.ExpressionContext) ctx.getChild(0));
	}

	@Override
	public Integer visitAtom(AuthParser.AtomContext ctx) {
		int count =ctx.getChildCount();
		System.out.println("atom childs count ="+count +",text="+ctx.getText());
		String tmp = ctx.getText();
		Integer rt =null;
		if(NumberUtil.isNumeric(tmp))
		{
			rt= Integer.parseInt(tmp);
		}
		else
		{
			System.out.println("atom invoke expression"+ ctx.getChild(1).getText());
			rt= visitExpression((AuthParser.ExpressionContext) ctx.getChild(1));
		}
		System.out.println("atom rt="+rt);
		return rt;
	}

	@Override
	public Integer visitProgram(AuthParser.ProgramContext ctx) {
		System.out.println("program ctx="+ctx.getText());
		 return visitStatement((AuthParser.StatementContext) ctx.getChild(0));
	}
	
	public static int doCalc(String sign, int leftChildValue, int rightChildVale) {  
        if (sign.equals("+")) {  
            return leftChildValue + rightChildVale;  
        } else if (sign.equals("-")) {  
            return leftChildValue - rightChildVale;  
        } else if (sign.equals("*")) {  
            return leftChildValue * rightChildVale;  
        } else if (sign.equals("/")) {  
            return leftChildValue / rightChildVale;  
        }  
        throw new RuntimeException("unknown sign " + sign);  
    }
}