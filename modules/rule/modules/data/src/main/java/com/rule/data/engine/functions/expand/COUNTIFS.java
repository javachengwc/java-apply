package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.CriteriaUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class COUNTIFS extends Function {

    public static final String NAME = COUNTIFS.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2 || args.length % 2 != 0) {
            throw new ArgsCountException(NAME);
        }

        final List<ExcelRange> areas = new ArrayList<ExcelRange>();
        final List<String> criterias = new ArrayList<String>();

        for (int i = 0; i < args.length; i += 2) {
            if (args[i] instanceof ExcelRange) {
                areas.add((ExcelRange) args[i]);
                criterias.add(DataUtil.getStringValue(args[i + 1]));
            } else {
                throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
            }
        }

        Map<Object, Object> cache = calInfo.getCache(NAME);
        __Key key = new __Key(areas, criterias);
        Long areaValue = (Long) cache.get(key);

        if (areaValue == null) {
            areaValue = NumberPool.LONG_0;

            CriteriaUtil.I_MatchPredicate[] mp = new CriteriaUtil.I_MatchPredicate[criterias.size()];
            for (int i = 0; i < criterias.size(); i++) {
                mp[i] = CriteriaUtil.createCriteriaPredicate((criterias.get(i)));
            }

            List<Iterator<Object>> ites = new ArrayList<Iterator<Object>>(areas.size());

            for (int i = 0; i < areas.size(); i++) {
                ites.add(areas.get(i).getIterator());
            }

            while (true) {
                boolean isOK = true;
                boolean isDone = false;

                for (int i = 0; i < ites.size(); i++) {
                    final Iterator<Object> ite = ites.get(i);

                    if (ite.hasNext()) {
                        Object tmp = ite.next();

                        if (isOK) {
                            if (!mp[i].matches(DataUtil.getValueEval(tmp))) {
                                isOK = false;
                            }
                        }
                    } else {
                        isDone = true;
                        break;
                    }
                }

                if (isDone) {
                    break;
                }

                if (isOK) {
                    areaValue++;
                }
            }

            cache.put(key, areaValue);
        }

        return areaValue;
    }

    class __Key {
        Object areas, criterias;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (areas != null ? !areas.equals(key.areas) : key.areas != null)
                return false;
            if (criterias != null ? !criterias.equals(key.criterias) : key.criterias != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = areas != null ? areas.hashCode() : 0;
            result = 31 * result + (criterias != null ? criterias.hashCode() : 0);
            return result;
        }

        __Key(Object areas, Object criterias) {
            this.areas = areas;
            this.criterias = criterias;
        }
    }
}
