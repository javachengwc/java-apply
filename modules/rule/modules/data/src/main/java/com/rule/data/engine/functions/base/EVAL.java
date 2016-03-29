package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelEngineTool;
import com.rule.data.engine.excel.ExcelVisitor;
import com.rule.data.engine.excel.parser.ExcelParser;

public final class EVAL extends Function {

    public static final String NAME = EVAL.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            String formula = DataUtil.getStringValue(args[0]);
            ExcelParser.ProgContext progContext = ExcelEngineTool.getContext(formula);

            if (progContext == null) {
                return null;
            }

            return ExcelVisitor.visitProg(progContext, calInfo);
        }

        throw new ArgsCountException(NAME);
    }
}
