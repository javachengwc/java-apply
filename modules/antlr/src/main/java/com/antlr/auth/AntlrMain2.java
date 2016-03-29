package com.antlr.auth;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

public class AntlrMain2 {

    public static void main(String[] args) throws Exception {
        final PlusANTLRInputStream stm = new PlusANTLRInputStream("8-(1+4+7)+(2+3)*4+5");
        AuthLexer lexer = new AuthLexer(stm);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AuthParser parser = new AuthParser(tokens);
        parser.setErrorHandler(new DefaultErrorStrategy() {

            public void reportUnwantedToken(@NotNull Parser recognizer) {
                Token t = recognizer.getCurrentToken();
                IntervalSet expecting = getExpectedTokens(recognizer);
                String msg = "无法匹配的输入 " + getTokenErrorDisplay(t) +
                        " 应该是 " + expecting.toString(recognizer.getTokenNames());

                throw new RuntimeException("语法错误, @"
                        + t.getLine() + ":"
                        + (t.getCharPositionInLine() + 1) + ", " + msg);
            }

            public void reportFailedPredicate(@NotNull Parser recognizer, @NotNull FailedPredicateException e) {
                String msg = e.getMessage();
                throw new RuntimeException("语法错误, @"
                        + e.getOffendingToken().getLine() + ":"
                        + (e.getOffendingToken().getCharPositionInLine() + 1) + ", " + msg);
            }

            public void reportInputMismatch(@NotNull Parser recognizer, @NotNull InputMismatchException e) {
                String msg = "无法匹配的输入 " + getTokenErrorDisplay(e.getOffendingToken()) +
                        " 应该是 " + e.getExpectedTokens().toString(recognizer.getTokenNames());

                throw new RuntimeException("语法错误, @"
                        + e.getOffendingToken().getLine() + ":"
                        + (e.getOffendingToken().getCharPositionInLine() + 1) + ", " + msg);
            }

            public void reportMissingToken(@NotNull Parser recognizer) {
                Token t = recognizer.getCurrentToken();
                IntervalSet expecting = getExpectedTokens(recognizer);
                String msg = "遗失 " + expecting.toString(recognizer.getTokenNames()) +
                        " at " + getTokenErrorDisplay(t);

                throw new RuntimeException("语法错误, @"
                        + t.getLine() + ":"
                        + (t.getCharPositionInLine() + 1) + ", " + msg);
            }

            public void reportNoViableAlternative(@NotNull Parser recognizer, @NotNull NoViableAltException e) {
                String input=null;
                if (((NoViableAltException) e).getStartToken().getType() == Token.EOF)
                    input = "<EOF>";
                else
                    input = stm.getText(Interval.of(
                            ((NoViableAltException) e).getStartToken().getStartIndex()
                            , e.getOffendingToken().getStopIndex()));

                String msg = "输入存在歧义 '" + input + "'";

                throw new RuntimeException("语法错误, @"
                        + e.getOffendingToken().getLine() + ":"
                        + (e.getOffendingToken().getCharPositionInLine() + 1) + ", " + msg);
            }

            @Override
            public void reportError(Parser recognizer, RecognitionException e) {
                String msg;
                if (e instanceof NoViableAltException) {
                    String input;
                    if (((NoViableAltException) e).getStartToken().getType() == Token.EOF)
                        input = "<EOF>";
                    else
                        input = stm.getText(Interval.of(
                                ((NoViableAltException) e).getStartToken().getStartIndex()
                                , e.getOffendingToken().getStopIndex()));

                    msg = "输入存在歧义 '" + input + "'";
                } else if (e instanceof InputMismatchException) {
                    msg = "无法匹配的输入 " + getTokenErrorDisplay(e.getOffendingToken()) +
                            " 应该是 " + e.getExpectedTokens().toString(recognizer.getTokenNames());
                } else {
                    msg = e.getMessage();
                }

                throw new RuntimeException("语法错误, @"
                        + e.getOffendingToken().getLine() + ":"
                        + (e.getOffendingToken().getCharPositionInLine() + 1) + ", " + msg);
            }
        });

        AuthVisitor<Integer> visitor = new AuthBaseVisitor();
        int result = parser.program().accept(visitor);
        System.out.println("result="+result);



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

    public static int calcNode(ParseTree tree) {
        if (tree.getChildCount() == 0) {
            return Integer.parseInt(tree.getText());
        }
        if (tree.getChildCount() != 2) {
            throw new RuntimeException("node " + tree.getText() + "illegal expression"
                    + "tree.getChildCount() = " + tree.getChildCount());
        }

        int leftChildValue = calcNode(tree.getChild(0));
        int rightChildVale = calcNode(tree.getChild(1));
        String sign = tree.getText().trim();

        return doCalc(sign, leftChildValue, rightChildVale);
    }
}
