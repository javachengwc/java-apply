/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

grammar Auth;
program : statement + ;
statement: expression;
expression : multExpr (('+' |'-' ) multExpr)*;
multExpr : atom ('*' atom)*;
atom : INT | '(' expression ')';
VAR : ('a'..'z' |'A'..'Z' )+ ;
INT : '0'..'9' + ;
WS : (' ' |'\t' |'\n' |'\r' )+ {skip();} ;
