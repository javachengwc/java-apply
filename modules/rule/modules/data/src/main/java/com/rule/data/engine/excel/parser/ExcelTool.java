package com.rule.data.engine.excel.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

/**
 * excel antrl处理入口
 */
public class ExcelTool {

    /**
     * 返回语法树
     * @param input
     * @return
     */
    public static ExcelParser.ProgContext parse(String input) {
        if (input == null) {
            throw new NullPointerException("input");
        }
        final PlusANTLRInputStream stream = new PlusANTLRInputStream(input);
        final ExcelLexer lexer = new ExcelLexer(stream);
        final ExcelParser parser =
                new ExcelParser(new CommonTokenStream(lexer));

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            protected void reportUnwantedToken(@NotNull Parser recognizer) {
                Token t = recognizer.getCurrentToken();
                IntervalSet expecting = getExpectedTokens(recognizer);
                String msg = "无法匹配的输入 " + getTokenErrorDisplay(t) +
                        " 应该是 " + expecting.toString(recognizer.getTokenNames());

                throw new RuntimeException("语法错误, @"
                        + t.getLine() + ":"
                        + (t.getCharPositionInLine() + 1) + ", " + msg);
            }

            @Override
            protected void reportFailedPredicate(@NotNull Parser recognizer, @NotNull FailedPredicateException e) {
                String msg = e.getMessage();
                throw new RuntimeException("语法错误, @"
                        + e.getOffendingToken().getLine() + ":"
                        + (e.getOffendingToken().getCharPositionInLine() + 1) + ", " + msg);
            }

            @Override
            protected void reportInputMismatch(@NotNull Parser recognizer, @NotNull InputMismatchException e) {
                String msg = "无法匹配的输入 " + getTokenErrorDisplay(e.getOffendingToken()) +
                        " 应该是 " + e.getExpectedTokens().toString(recognizer.getTokenNames());

                throw new RuntimeException("语法错误, @"
                        + e.getOffendingToken().getLine() + ":"
                        + (e.getOffendingToken().getCharPositionInLine() + 1) + ", " + msg);
            }

            @Override
            protected void reportMissingToken(@NotNull Parser recognizer) {
                Token t = recognizer.getCurrentToken();
                IntervalSet expecting = getExpectedTokens(recognizer);
                String msg = "遗失 " + expecting.toString(recognizer.getTokenNames()) +
                        " at " + getTokenErrorDisplay(t);

                throw new RuntimeException("语法错误, @"
                        + t.getLine() + ":"
                        + (t.getCharPositionInLine() + 1) + ", " + msg);
            }

            @Override
            protected void reportNoViableAlternative(@NotNull Parser recognizer, @NotNull NoViableAltException e) {
                String input;
                if (((NoViableAltException) e).getStartToken().getType() == Token.EOF)
                    input = "<EOF>";
                else
                    input = stream.getText(Interval.of(
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
                        input = stream.getText(Interval.of(
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

        return parser.prog();
    }

    public static Map<String, String> exchange(Map<String, String> info, String colA, String colB) {
        Map<String, String> ret = new HashMap<String, String>(info.size());

        for (Map.Entry<String, String> entry : info.entrySet()) {
            if (entry.getKey().equals(colA)) {
                checkContext(parse(entry.getValue()), colB);
                ret.put(colB, entry.getValue());
            } else if (entry.getKey().equals(colB)) {
                checkContext(parse(entry.getValue()), colA);
                ret.put(colA, entry.getValue());
            } else {
                StringBuilder sb = new StringBuilder("");
                replace(parse(entry.getValue()), colA, colB, sb);
                sb.delete(sb.length() - 5, sb.length());

                ret.put(entry.getKey(), sb.toString());
            }
        }

        return ret;
    }

    private static void checkContext(ParseTree context, String colB) {
        if (context instanceof ExcelParser.CellContext) {
            ExcelParser.CellContext cell = (ExcelParser.CellContext) context;
            checkCell(cell, colB);
        } else if (context instanceof ExcelParser.RangeContext) {
            ExcelParser.RangeContext range = (ExcelParser.RangeContext) context;
            ExcelParser.CellContext left = (ExcelParser.CellContext) range.getChild(0);
            ExcelParser.CellContext right = (ExcelParser.CellContext) range.getChild(2);

            checkCell(left, colB);
            checkCell(right, colB);
        } else {
            if (context instanceof TerminalNode) {
                //
            } else {
                for (int i = 0; i < context.getChildCount(); i++) {
                    ParseTree tmp = context.getChild(i);
                    if (tmp != null) {
                        checkContext(tmp, colB);
                    }
                }
            }
        }
    }

    private static void checkCell(ExcelParser.CellContext cell, String colB) {
        final String source = cell.getText();
        String col;

        if (cell.getChildCount() == 2) {
            col = cell.NAME();
        } else {
            int numberIndex = getNumberIndex(source);

            if (numberIndex == -1) {
                col = source;
            } else {
                col = source.substring(0, numberIndex);
            }
        }

        if (col.equalsIgnoreCase(colB)) {
            throw new RuntimeException("存在互相调用, 不得上移");
        }
    }

    static String exchangeCell(ExcelParser.CellContext cell, String colA, String colB) {
        final String source = cell.getText();
        String col;
        int start = 0;

        if (cell.getChildCount() == 2) {
            col = cell.NAME();
            start = source.length() - 5;
        } else {
            int numberIndex = getNumberIndex(source);

            if (numberIndex == -1) {
                col = source;
                start = source.length();
            } else {
                col = source.substring(0, numberIndex);
                start = numberIndex;
            }
        }

        if (col.equalsIgnoreCase(colA)) {
            return colB + source.substring(start);
        } else if (col.equalsIgnoreCase(colB)) {
            return colA + source.substring(start);
        } else {
            return source;
        }
    }

    public static void replace(ParseTree context, String colA, String colB, StringBuilder sb) {
        if (context instanceof ExcelParser.CellContext) {
            ExcelParser.CellContext cell = (ExcelParser.CellContext) context;
            sb.append(exchangeCell(cell, colA, colB));
        } else if (context instanceof ExcelParser.RangeContext) {
            ExcelParser.RangeContext range = (ExcelParser.RangeContext) context;
            ExcelParser.CellContext left = (ExcelParser.CellContext) range.getChild(0);
            ExcelParser.CellContext right = (ExcelParser.CellContext) range.getChild(2);

            sb.append(exchangeCell(left, colA, colB));
            sb.append(":");
            sb.append(exchangeCell(right, colA, colB));
        } else {
            if (context instanceof TerminalNode) {
                sb.append(context.getText());
            } else {
                for (int i = 0; i < context.getChildCount(); i++) {
                    ParseTree tmp = context.getChild(i);
                    if (tmp != null) {
                        replace(tmp, colA, colB, sb);
                    }
                }
            }
        }
    }

    public static int getNumberIndex(String str) {
        final int size = str.length();
        int index = size - 1;

        for (; index >= 0; ) {
            final char c = str.charAt(index);
            if (c < '0' || c > '9') {
                break;
            }
            index--;
        }

        index++;

        if (index == 0 || index == size) {
            return -1;
        }

        return index;
    }


    public static Set<String> getServiceInvoked(String formula) {
        String[] specials = new String[]{"BYGROUP", "RANK", "PERCENTINMAX", "VLOOKUP"};

        ExcelParser.ProgContext context = parse(formula);
        final Set<String> result = new HashSet<String>();

        Queue<ParseTree> queue = new LinkedList<ParseTree>();
        queue.add(context);

        while (!queue.isEmpty()) {
            ParseTree tree = queue.poll();

            if (tree == null) {
                continue;
            }

            if (tree instanceof ExcelParser.FunctionContext) { // 函数
                final ExcelParser.FunctionContext func = (ExcelParser.FunctionContext) tree;
                final String funcName = func.funcName();

                if (funcName.startsWith("_O_")) {
                    boolean isSpecial = false;
                    for (String special : specials) {
                        if (funcName.indexOf(special) != -1) {
                            isSpecial = true;
                            break;
                        }
                    }

                    if (isSpecial) {
                        if (tree.getChildCount() > 4) {
                            String serviceName = tree.getChild(4).getText();
                            serviceName = serviceName.substring(1, serviceName.length() - 1);
                            serviceName = serviceName.replace("\"\"", "\"");
                            result.add(serviceName);
                        }
                    } else {
                        if (tree.getChildCount() > 2) {
                            String serviceName = tree.getChild(2).getText();
                            serviceName = serviceName.substring(1, serviceName.length() - 1);
                            serviceName = serviceName.replace("\"\"", "\"");
                            result.add(serviceName);
                        }
                    }
                }
            }

            for (int i = 0, size = tree.getChildCount(); i < size; i++) {
                queue.add(tree.getChild(i));
            }
        }

        return result;
    }

}
