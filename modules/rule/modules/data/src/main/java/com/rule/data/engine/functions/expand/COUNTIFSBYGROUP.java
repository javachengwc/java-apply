package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.CriteriaUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.*;

/**
 * <hr>分组求和</hr>
 * 分组依据列的值     e.g. 1<hr/>
 * 分组依据的列             e.g. H<hr/>
 * SUM的列                       e.g. I<hr/>
 * 依据的数据源                          e.g. 2<hr/>
 */
public class COUNTIFSBYGROUP extends Function {

    public static final String NAME = COUNTIFSBYGROUP.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        final String reqKey = DataUtil.getStringValue(args[0]);
        if (!(args[1] instanceof ExcelRange) || !(args[2] instanceof ExcelRange)) {
            throw new RengineException(calInfo.getServiceName(), "输入不是数列");
        }

        final ExcelRange colBy = (ExcelRange) args[1];
        final ExcelRange colSum = (ExcelRange) args[2];

        final List<ExcelRange> criteriaRanges = new ArrayList<ExcelRange>();
        final List<String> criterias = new ArrayList<String>();

        for (int i = 3; i < args.length; i += 2) {
            if (i + 1 == args.length) {
                break;
            }

            if (!(args[i] instanceof ExcelRange)) {
                throw new RengineException(calInfo.getServiceName(), "输入不是数列");
            }

            criteriaRanges.add((ExcelRange) args[i]);
            criterias.add(DataUtil.getStringValue(args[i + 1]));
        }


        Map<Object, Object> cache = calInfo.getCache(NAME);
        __Key key = new __Key(colBy, colSum, criteriaRanges, criterias);
        Map<String, Long> result = (Map<String, Long>) cache.get(key);

        if (result == null) {
            result = new HashMap<String, Long>();

            final int size = criteriaRanges.size();
            CriteriaUtil.I_MatchPredicate[] mp = new CriteriaUtil.I_MatchPredicate[size];
            for (int i = 0; i < size; i++) {
                mp[i] = CriteriaUtil.createCriteriaPredicate((criterias.get(i)));
            }

            List<Iterator<Object>> ites = new ArrayList<Iterator<Object>>(size);
            Iterator<Object> iteSum = colSum.getIterator();
            Iterator<Object> iteBy = colBy.getIterator();

            for (int i = 0; i < size; i++) {
                ites.add(criteriaRanges.get(i).getIterator());
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

                if (iteSum.hasNext() && iteBy.hasNext()) {
                    Object sumT = iteSum.next();
                    String byT = DataUtil.getStringValue(iteBy.next());

                    if (isOK) {
                        if (sumT != null && canNumberOP(sumT)) {
                            Long tmpD = result.get(byT);
                            if (tmpD == null) {
                                result.put(byT, NumberPool.LONG_1);
                            } else {
                                result.put(byT, tmpD + NumberPool.LONG_1);
                            }
                        }
                    }
                } else {
                    break;
                }
            }

            cache.put(key, result);
        }

        Long ret = result.get(reqKey);
        if (ret == null) {
            return NumberPool.LONG_0;
        }

        return ret;
    }

    class __Key {
        Object colBy, colSum, criteriaRanges, criterias;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colBy != null ? !colBy.equals(key.colBy) : key.colBy != null)
                return false;
            if (colSum != null ? !colSum.equals(key.colSum) : key.colSum != null)
                return false;
            if (criteriaRanges != null ? !criteriaRanges.equals(key.criteriaRanges) : key.criteriaRanges != null)
                return false;
            if (criterias != null ? !criterias.equals(key.criterias) : key.criterias != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colBy != null ? colBy.hashCode() : 0;
            result = 31 * result + (colSum != null ? colSum.hashCode() : 0);
            result = 31 * result + (criteriaRanges != null ? criteriaRanges.hashCode() : 0);
            result = 31 * result + (criterias != null ? criterias.hashCode() : 0);
            return result;
        }

        __Key(Object colBy, Object colSum, Object criteriaRanges, Object criterias) {
            this.colBy = colBy;
            this.colSum = colSum;
            this.criteriaRanges = criteriaRanges;
            this.criterias = criterias;
        }
    }

}