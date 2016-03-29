// Generated from D:\tmp\Excel\Auth.g4 by ANTLR 4.0
package com.antlr.auth;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface AuthVisitor<T> extends ParseTreeVisitor<T> {
	T visitMultExpr(AuthParser.MultExprContext ctx);

	T visitExpression(AuthParser.ExpressionContext ctx);

	T visitStatement(AuthParser.StatementContext ctx);

	T visitAtom(AuthParser.AtomContext ctx);

	T visitProgram(AuthParser.ProgramContext ctx);
}