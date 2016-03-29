// Generated from D:\tmp\Excel\Auth.g4 by ANTLR 4.0
package com.antlr.auth;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface AuthListener extends ParseTreeListener {
	void enterMultExpr(AuthParser.MultExprContext ctx);
	void exitMultExpr(AuthParser.MultExprContext ctx);

	void enterExpression(AuthParser.ExpressionContext ctx);
	void exitExpression(AuthParser.ExpressionContext ctx);

	void enterStatement(AuthParser.StatementContext ctx);
	void exitStatement(AuthParser.StatementContext ctx);

	void enterAtom(AuthParser.AtomContext ctx);
	void exitAtom(AuthParser.AtomContext ctx);

	void enterProgram(AuthParser.ProgramContext ctx);
	void exitProgram(AuthParser.ProgramContext ctx);
}