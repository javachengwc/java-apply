package com.rule.data.engine.excel;

import com.rule.data.engine.excel.parser.ExcelParser;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.CalculateException;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.util.DataUtil;
import static com.rule.data.util.DataUtil.*;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Date;

import static com.rule.data.engine.excel.NumberPool.*;
import static com.rule.data.engine.excel.StringPool.EMPTY;

/**
 * 表达式解析
 * 表达式具体的执行
 */
public final class ExcelVisitor {

    /**
     * 1
     * 语法树的跟, 逻辑为调用Eval计算
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * prog
     *      : eval EOF
     *      ;
     */
    public static Object visitProg(ExcelParser.ProgContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        return visitEval((ExcelParser.EvalContext) ctx.getChild(0), calInfo);
    }

    /**
     * 2
     * 计算Eval语法树, 包含比较逻辑
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * eval
     *     : op_join ((GE|LE|NE|GT|LT|EQ) op_join) ?
     *     ;
     */
    private static Object visitEval(ExcelParser.EvalContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        final int childCount = ctx.getChildCount();

        if (childCount == 1) {
            return visitOp_join((ExcelParser.Op_joinContext) ctx.getChild(0), calInfo);
        } else {
            TerminalNode op = (TerminalNode) ctx.getChild(1);
            long resu = compare(ctx, calInfo);

            switch (op.getSymbol().getType()) {
                case ExcelParser.EQ:
                    return resu == LONG_0;
                case ExcelParser.GT:
                    return resu > LONG_0;
                case ExcelParser.LT:
                    return resu < LONG_0;
                case ExcelParser.NE:
                    return resu != LONG_0;
                case ExcelParser.GE:
                    return resu >= LONG_0;
                case ExcelParser.LE:
                    return resu <= LONG_0;
            }
        }

        return null;
    }

    /**
     * 3
     * 字符串拼接的逻辑
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * op_join
     *       : op_plus_minus (JOIN op_plus_minus)*
     *       ;
     */
    private static Object visitOp_join(ExcelParser.Op_joinContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        final int childCount = ctx.getChildCount();

        if (childCount == 1) {
            return visitOp_plus_minus((ExcelParser.Op_plus_minusContext) ctx.getChild(0), calInfo);
        } else {
            StringBuilder sb = new StringBuilder(EMPTY);

            for (int i = 0; i < childCount; i += 2) {
                Object str = visitOp_plus_minus((ExcelParser.Op_plus_minusContext) ctx.getChild(i), calInfo);
                if (str == null) {
                    str = "";
                }

                sb.append(getStringValue(str, getType(str)));
            }

            return sb.toString();
        }
    }

    /**
     * 4
     * 计算加减法语法树, 逻辑为左右两侧语法树的加减逻辑
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * op_plus_minus
     *        : op_mul_div (SIGN op_mul_div)*
     *        ;
     */
    private static Object visitOp_plus_minus(ExcelParser.Op_plus_minusContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        final int childCount = ctx.getChildCount();

        if (childCount == 1) {
            return visitOp_mul_div((ExcelParser.Op_mul_divContext) ctx.getChild(0), calInfo);
        } else {
            Object left = visitOp_mul_div((ExcelParser.Op_mul_divContext) ctx.getChild(0), calInfo);

            for (int i = 1; i < childCount; i += 2) {
                boolean isPositive = isPositive(ctx.getChild(i).getText());
                Object right = visitOp_mul_div((ExcelParser.Op_mul_divContext) ctx.getChild(i + 1), calInfo);

                right = right == null ? LONG_0 : right;
                Number leftN = getNumberValue(left, getType(left));
                Number rightN = getNumberValue(right, getType(right));

                if (leftN instanceof Double || rightN instanceof Double) {
                    if (isPositive) {
                        left = leftN.doubleValue() + rightN.doubleValue();
                    } else {
                        left = leftN.doubleValue() - rightN.doubleValue();
                    }
                } else {
                    if (isPositive) {
                        left = leftN.longValue() + rightN.longValue();
                    } else {
                        left = leftN.longValue() - rightN.longValue();
                    }
                }
            }

            return left;
        }
    }

    /**
     * 5
     * 乘除运算的逻辑
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * op_mul_div
     *       : op_pow ((MUL|DIV) op_pow)*
     *       ;
     */
    private static Object visitOp_mul_div(ExcelParser.Op_mul_divContext ctx, CalInfo calInfo)
            throws RengineException, CalculateException {
        final int childCount = ctx.getChildCount();

        if (childCount == 1) {
            return visitOp_pow((ExcelParser.Op_powContext) ctx.getChild(0), calInfo);
        } else {
            Object left = visitOp_pow((ExcelParser.Op_powContext) ctx.getChild(0), calInfo);

            for (int i = 1; i < childCount; i += 2) {
                boolean isMulti = ((TerminalNode) ctx.getChild(i)).getSymbol().getType() == ExcelParser.MUL;

                Object right = visitOp_pow((ExcelParser.Op_powContext) ctx.getChild(i + 1), calInfo);
                if (right == null) {
                    right = LONG_0;
                }

                Number leftN = getNumberValue(left);
                Number rightN = getNumberValue(right);

                if (leftN instanceof Double || rightN instanceof Double) {
                    if (isMulti) {
                        left = leftN.doubleValue() * rightN.doubleValue();
                    } else {
                        if (DataUtil.compare(rightN.doubleValue(), DOUBLE_0) == 0) {
                            throw new CalculateException("除0错误");
                        }

                        left = leftN.doubleValue() / rightN.doubleValue();
                    }
                } else {
                    if (isMulti) {
                        left = leftN.longValue() * rightN.longValue();
                    } else {
                        if (rightN.longValue() == LONG_0) {
                            throw new CalculateException("除0错误");
                        }

                        left = leftN.doubleValue() / rightN.doubleValue();
                    }
                }
            }

            return left;
        }
    }

    /**
     * 6
     * 幂语法树的运算
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * op_pow
     *       : signed_eval_unit (POW signed_eval_unit)*
     *       ;
     */
    private static Object visitOp_pow(ExcelParser.Op_powContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        final int childCount = ctx.getChildCount();

        if (childCount == 1) {
            return visitSigned_eval_unit((ExcelParser.Signed_eval_unitContext) ctx.getChild(0), calInfo);
        } else {
            Object left = visitSigned_eval_unit((ExcelParser.Signed_eval_unitContext) ctx.getChild(0), calInfo);
            if (left == null) {
                left = DOUBLE_0;
            }

            for (int i = 1; i < childCount; i += 2) {
                Object right = visitSigned_eval_unit((ExcelParser.Signed_eval_unitContext) ctx.getChild(i + 1), calInfo);
                if (right == null) {
                    right = DOUBLE_0;
                }

                Number leftN = getNumberValue(left, getType(left));
                Number rightN = getNumberValue(right, getType(right));

                left = Math.pow(leftN.doubleValue(), rightN.doubleValue());
            }

            return left;
        }
    }

    /**
     * 7
     * 带符号的话必然是数字，有Double和Long两种
     * @param ctx
     * @param calInfo
     * @return
     * signed_eval_unit
     *       : SIGN ? eval_unit
     *       ;
     */
    private static Object visitSigned_eval_unit(ExcelParser.Signed_eval_unitContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        if (ctx.getChildCount() == 2) {  // 带符号
            Object result = visitEval_unit((ExcelParser.Eval_unitContext) ctx.getChild(1), calInfo);
            if (result == null) {
                result = LONG_0;
            }

            boolean isPositive = isPositive(ctx.getChild(0).getText());

            switch (getType(result)) {
                case LONG:
                    if (isPositive) {
                        return result;
                    }
                    //取反+1
                    return ~((Long) result) + 1;
                case DOUBLE:
                    if (isPositive) {
                        return result;
                    }
                    return DOUBLE_0 - (Double) result;
                case BOOLEAN:
                    if (isPositive) {
                        return (Boolean) result ? LONG_1 : LONG_0;
                    }
                    return (Boolean) result ? LONG_0 : LONG_1;
                case DATE:
                    if (isPositive) {
                        return ((Date) result).getTime() / LONG_1000;
                    }
                    return ~(((Date) result).getTime() / LONG_1000) + 1;
                case STRING:
                    if (isPositive) {
                        return parseDouble((String) result);
                    }
                    return DOUBLE_0 - parseDouble((String) result);
            }
        }

        return visitEval_unit((ExcelParser.Eval_unitContext) ctx.getChild(0), calInfo);
    }

    /**
     * 8
     * EvalUnit语法树的计算, 包含每种子类型的运算, 如基本类型,函数等
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * eval_unit
     *       : TRUE
     *       | FALSE
     *       | LONG
     *       | STRING
     *       | FLOAT
     *       | percent
     *       | cell
     *       | range
     *       | function
     *       | p_eval_unit
     *       ;
     */
    private static Object visitEval_unit(ExcelParser.Eval_unitContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        ParseTree child = ctx.getChild(0);

        if (child instanceof TerminalNode) {
            final Token token = ((TerminalNode) child).getSymbol();
            final String symbol = token.getText();

            switch (token.getType()) {
                case ExcelParser.LONG:
                    return ctx.LONG(symbol);
                case ExcelParser.STRING:
                    return ctx.STRING(symbol);
                case ExcelParser.FLOAT:
                    return ctx.FLOAT(symbol);
                case ExcelParser.TRUE:
                    return true;
                case ExcelParser.FALSE:
                    return false;
            }
        } else {
            RuleContext childContext = (RuleContext) child;

            switch (childContext.getRuleIndex()) {
                case ExcelParser.RULE_cell:
                    return visitCell((ExcelParser.CellContext) childContext, calInfo);
                case ExcelParser.RULE_range:
                    return visitRange((ExcelParser.RangeContext) childContext, calInfo);
                case ExcelParser.RULE_function:
                    return visitFunction((ExcelParser.FunctionContext) childContext, calInfo);
                case ExcelParser.RULE_percent:
                    return visitPercent((ExcelParser.PercentContext) childContext, calInfo);
                case ExcelParser.RULE_p_eval_unit:
                    return visitP_eval_unit((ExcelParser.P_eval_unitContext) childContext, calInfo);
            }
        }

        return null;
    }

    /**
     * 9.1
     * 获取当前二维表中指定单元格的值
     *
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * cell
     *       : NAME '$ROW$' ?
     *       ;
     */
    private static Object visitCell(ExcelParser.CellContext ctx, CalInfo calInfo) throws RengineException {
        return calInfo.curD2data.getValue(ctx.getColumn(), ctx.getRow(calInfo));
    }

    /**
     *  9.2
     * 数列逻辑, 返回ExcelRange
     *
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * range
     *       : cell ':' cell
     *       ;
     */
    private static Object visitRange(ExcelParser.RangeContext ctx, CalInfo calInfo) throws RengineException {
        ExcelParser.CellContext left = (ExcelParser.CellContext) ctx.getChild(0);
        ExcelParser.CellContext right = (ExcelParser.CellContext) ctx.getChild(2);

        int x1 = left.getColumn();
        int y1 = left.getRow(calInfo);
        int x2 = right.getColumn();
        int y2 = right.getRow(calInfo);

        if (left.getNumberIndex() == -1) {
            y1 = 0;
        }

        if (right.getNumberIndex() == -1) {
            y2 = calInfo.maxRow - 1;
        }

        x1 = x1 < 0 ? 0 : x1;
        x2 = x2 < 0 ? 0 : x2;
        y1 = y1 < 0 ? 0 : y1;
        y2 = y2 < 0 ? 0 : y2;

        if (x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }

        if (y1 > y2) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        return new ExcelRange(x1, y1, x2, y2, calInfo.curD2data);
    }

    /**
     * 9.3
     * 函数运算的逻辑, 找到对应的函数进行计算, 其中对于IF和IFERROR特殊处理
     *
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * function
     *        : NAME '(' (eval (',' eval) * ) ? ')'
     *        ;
     */
    private static Object visitFunction(ExcelParser.FunctionContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        final String functionName = ctx.funcName();
        final int childCount = ctx.getChildCount();
        final int evalSize = (childCount - 2) / 2;
        final int startEval = 2;

        if (functionName.equalsIgnoreCase("IF")) {
            if(evalSize<2){        //与excel语法保持一致   if函数内参数为0个或者1个，则报参数个数不匹配的异常
                throw new ArgsCountException("IF");
            }

            if(evalSize>3){
                throw new ArgsCountException("IF");
            }

            if (evalSize < 1) {
                return false;
            }

            Object logical = visitEval((ExcelParser.EvalContext) ctx.getChild(startEval), calInfo);

            if (DataUtil.compare(DataUtil.getNumberValue(logical).doubleValue(), NumberPool.DOUBLE_0)!= 0) { // true
                if (evalSize < 2) {
                    return true;
                } else {
                    return visitEval((ExcelParser.EvalContext) ctx.getChild(startEval + 2), calInfo);
                }
            } else { // false
                return false;
            }
        } else if (functionName.equalsIgnoreCase("IFERROR")) {

            //待调整

            if (evalSize < 2) {
                throw new ArgsCountException("IFERROR");
            }

            try {
                Object ret = visitEval((ExcelParser.EvalContext) ctx.getChild(startEval), calInfo);
                if (ret != null) {
                    return ret;
                }
            } catch (CalculateException e) {
                //
            }
            return visitEval((ExcelParser.EvalContext) ctx.getChild(startEval + 2), calInfo);


        } else {
            
            final Function function = ctx.getFunction();

            if (function == null) {
                throw new RengineException(null, "未实现的函数, " + functionName);
            }

            Object[] args = new Object[evalSize];

            for (int i = 0; i < evalSize; i++) {
                args[i] = visitEval((ExcelParser.EvalContext) ctx.getChild(startEval + 2 * i), calInfo);
            }

            return function.eval(calInfo, args);
        }
    }

    /**
     * 9.4
     * 百分比语法树的逻辑
     *
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * percent
     *      : (LONG | FLOAT) '%'
     *      ;
     */
    private static Object visitPercent(ExcelParser.PercentContext ctx, CalInfo calInfo) throws RengineException {
        final TerminalNode num = (TerminalNode) ctx.getChild(0);

        if (num.getSymbol().getType() == ExcelParser.LONG) {
            return ctx.LONG(num.getText()).doubleValue();
        } else {
            return ctx.FLOAT(num.getText());
        }
    }

    /**
     * 9.5
     * 加括号的处理逻辑, 很简单
     *
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     * @throws CalculateException
     * p_eval_unit
     *       : '(' eval ')'
     *       ;
     */
    private static Object visitP_eval_unit(ExcelParser.P_eval_unitContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        return visitEval((ExcelParser.EvalContext) ctx.getChild(1), calInfo);
    }

    /**
     * 字符-字符，字符序
     * 字符-日期，日期将转换为字符来做对比
     * 其他 转为数字进行对比
     * @param ctx
     * @param calInfo
     * @return
     * @throws RengineException
     */
    private static long compare(ExcelParser.EvalContext ctx, CalInfo calInfo) throws RengineException, CalculateException {
        Object left = visitOp_join((ExcelParser.Op_joinContext) ctx.getChild(0), calInfo);
        Object right = visitOp_join((ExcelParser.Op_joinContext) ctx.getChild(2), calInfo);

        return compareO(left, right);
    }

    /**
     * 获取字符中第一个数字的文职, 比如A3中3的位置
     *
     * @param str
     * @return
     */
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

}
