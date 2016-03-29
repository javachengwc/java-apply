/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

grammar Excel;

@lexer::members{
    @Override
    public void notifyListeners(LexerNoViableAltException e) {
        String text = _input.getText(Interval.of(_tokenStartCharIndex, _input.index()));
        String msg = "\u65e0\u6cd5\u8bc6\u522b\u7684\u8f93\u5165 '" + getErrorDisplay(text) + "'";
        throw new RuntimeException(msg);
    }
}

prog
    : eval EOF
    ;

eval
    : op_join ((GE|LE|NE|GT|LT|EQ) op_join) ?
    ;

op_join
    : op_plus_minus (JOIN op_plus_minus)*
    ;

op_plus_minus
    : op_mul_div (SIGN op_mul_div)*
    ;

op_mul_div
    : op_pow ((MUL|DIV) op_pow)*
    ;

op_pow
    : signed_eval_unit (POW signed_eval_unit)*
    ;

signed_eval_unit
    : SIGN ? eval_unit
    ;

eval_unit
    : TRUE
    | FALSE
    | LONG
    | STRING
    | FLOAT
    | percent
    | cell
    | range
    | function
    | p_eval_unit
    ;

TRUE
    : 'TRUE' | 'true'
    ;

FALSE
    : 'FALSE' | 'false'
    ;

p_eval_unit
    : '(' eval ')'
    ;

percent
    : (LONG | FLOAT) '%'
    ;

function
    : NAME '(' (eval (',' eval) * ) ? ')'
    ;

cell
    : NAME '$ROW$' ?
    ;

range
    : cell ':' cell
    ;


NAME
    : ('_' | ALPHA)+ (('_' | ALPHA | DIGIT | '.')* (ALPHA | DIGIT | '_' )+)?
    ;

WS
    : ( ' ' | '\t' | '\n' | '\r' ) -> channel(HIDDEN)
    ;

STRING
    : '"' ('""' | ~('"')+ )* '"'
    ;

ALPHA
    : ('A'..'Z'|'a'..'z')
    ;

SIGN
    : ('+'|'-')+
    ;

LONG
    : DIGIT+
    ;

DIGIT
    : '0'..'9'
    ;

FLOAT
    : LONG ? (('.' DIGIT+ EXPONENT ?) | (EXPONENT))
    ;

EXPONENT
    : ('e'|'E') ('+'|'-') ? DIGIT+
    ;

POW
    : '^'
    ;

MUL
    : '*'
    ;

DIV
    : '/'
    ;

JOIN
    : '&'
    ;

GE
    : '>='
    ;

LE 
    : '<='
    ;

NE 
    : '<>'
    ;

GT
    : '>'
    ;

LT
    : '<'
    ;

EQ
    : '='
    ;