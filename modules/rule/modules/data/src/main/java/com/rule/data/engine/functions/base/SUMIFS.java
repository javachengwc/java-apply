package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.CriteriaUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SUMIFS extends Function {

    public static final String NAME = SUMIFS.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] instanceof ExcelRange) {
            final ExcelRange rangeSum = (ExcelRange) args[0];
            final List<ExcelRange> rangeCriterias = new ArrayList<ExcelRange>();
            final List<String> criterias = new ArrayList<String>();

            for (int i = 1; i < args.length; i += 2) {
                if (args[i] instanceof ExcelRange) {
                    rangeCriterias.add((ExcelRange) args[i]);
                    criterias.add(DataUtil.getStringValue(args[i + 1]));
                } else {
                    throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
                }
            }

            Map<Object, Object> cache = calInfo.getCache(NAME);
            __Key key = new __Key(rangeSum, rangeCriterias, criterias);
            Double result = (Double) cache.get(key);

            if (result == null) {
                result = NumberPool.DOUBLE_0;
                final int size = rangeCriterias.size();
                CriteriaUtil.I_MatchPredicate[] mp = new CriteriaUtil.I_MatchPredicate[size];
                for (int i = 0; i < size; i++) {
                    mp[i] = CriteriaUtil.createCriteriaPredicate((criterias.get(i)));
                }

                List<Iterator<Object>> ites = new ArrayList<Iterator<Object>>(size);
                Iterator<Object> iteSum = rangeSum.getIterator();

                for (int i = 0; i < size; i++) {
                    ites.add(rangeCriterias.get(i).getIterator());
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

                    if (iteSum.hasNext()) {
                        Object tmp = iteSum.next();

                        if (isOK) {
                            if (tmp != null && canNumberOP(tmp)) {
                                result += DataUtil.getNumberValue(tmp).doubleValue();
                            }
                        }
                    } else {
                        break;
                    }
                }

                cache.put(key, result);
            }

            return result;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }


    class __Key {
        Object colSum, colCriterias, criterias;

        __Key(Object colSum, Object colCriterias, Object criterias) {
            this.colCriterias = colCriterias;
            this.colSum = colSum;
            this.criterias = criterias;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCriterias != null ? !colCriterias.equals(key.colCriterias) : key.colCriterias != null)
                return false;
            if (colSum != null ? !colSum.equals(key.colSum) : key.colSum != null)
                return false;
            if (criterias != null ? !criterias.equals(key.criterias) : key.criterias != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colSum != null ? colSum.hashCode() : 0;
            result = 31 * result + (colCriterias != null ? colCriterias.hashCode() : 0);
            result = 31 * result + (criterias != null ? criterias.hashCode() : 0);
            return result;
        }
    }

}
