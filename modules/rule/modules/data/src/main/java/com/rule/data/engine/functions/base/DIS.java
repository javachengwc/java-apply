package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.ExcelRange;

public class DIS extends Function {

    public static final String NAME = DIS.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        if (args[1] instanceof ExcelRange) {
            final String reqKey = DataUtil.getStringValue(args[0]);
            ExcelRange range = (ExcelRange) args[1];

            int currentIndex = calInfo.getCurRow();
            int lastIndex = currentIndex;
            int currentColumnIndex = range.getX1();

            for (int i = lastIndex - 1; i >= 0; i--) {
                if (DataUtil.getStringValue(range.getValue(currentColumnIndex, i)).equalsIgnoreCase(reqKey)) {
                    lastIndex = i;
                    break;
                }
            }

            return new Long((currentIndex - lastIndex));
        }

        throw new RengineException(calInfo.getServiceName(), NAME+ "输入不是数列");
    }

}
