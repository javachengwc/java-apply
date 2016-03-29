package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;

public class LOCAT extends Function {

    public static final String NAME = LOCAT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] instanceof ExcelRange) {
            ExcelRange range = (ExcelRange) args[0];
            int offset = DataUtil.getNumberValue(args[1]).intValue();
            Object ret = null;

            if (args.length > 2) {
                ret = args[2];
            }

            Object rr = range.getValue(range.getX1(), calInfo.getCurRow() + offset);

            if (rr != null) {
                return rr;
            } else {
                return ret;
            }
        }

        throw new RengineException(calInfo.getServiceName(), NAME+ "输入不是数列");
    }
}
