package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.util.Type;

import java.util.*;

public final class TWO_D_SUB extends Function {
    public static final String NAME = TWO_D_SUB.class.getSimpleName();

    class __Key {
        Object o1, o2, o3, o4, o5, o6, o7, o8;

        __Key(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
            this.o1 = o1;
            this.o2 = o2;
            this.o3 = o3;
            this.o4 = o4;
            this.o5 = o5;
            this.o6 = o6;
            this.o7 = o7;
            this.o8 = o8;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (o1 != null ? !o1.equals(key.o1) : key.o1 != null) return false;
            if (o2 != null ? !o2.equals(key.o2) : key.o2 != null) return false;
            if (o3 != null ? !o3.equals(key.o3) : key.o3 != null) return false;
            if (o4 != null ? !o4.equals(key.o4) : key.o4 != null) return false;
            if (o5 != null ? !o5.equals(key.o5) : key.o5 != null) return false;
            if (o6 != null ? !o6.equals(key.o6) : key.o6 != null) return false;
            if (o7 != null ? !o7.equals(key.o7) : key.o7 != null) return false;
            if (o8 != null ? !o8.equals(key.o8) : key.o8 != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = o1 != null ? o1.hashCode() : 0;
            result = 31 * result + (o2 != null ? o2.hashCode() : 0);
            result = 31 * result + (o3 != null ? o3.hashCode() : 0);
            result = 31 * result + (o4 != null ? o4.hashCode() : 0);
            result = 31 * result + (o5 != null ? o5.hashCode() : 0);
            result = 31 * result + (o6 != null ? o6.hashCode() : 0);
            result = 31 * result + (o7 != null ? o7.hashCode() : 0);
            result = 31 * result + (o8 != null ? o8.hashCode() : 0);
            return result;
        }
    }

    class Row implements Comparable<Row> {
        long d2Count, d2Inv;
        String d2key, d1Key;
        Object sortKey1, sortKey2;
        int order1 = -1;
        int order2 = -1;


        @Override
        public int compareTo(Row o) {
            int ret = -1;

            if (order1 == 1) {
                ret = compare0(sortKey1, o.sortKey1);
            } else {
                ret = compare0(o.sortKey1, sortKey1);
            }

            if (ret == 0 && order2 != -1) {
                if (order2 == 1) {
                    return compare0(sortKey2, o.sortKey2);
                } else {
                    return compare0(o.sortKey2, sortKey2);
                }
            }

            return ret;
        }

        public int compare0(Object o1, Object o2) {
            if (o1 instanceof String && o2 instanceof String) {
                return ((String) o1).compareTo((String) o2);
            } else if (o1 instanceof Double && o2 instanceof Double) {
                return DataUtil.compare((Double) o1, (Double) o2);
            } else {
                return -1;
            }
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 7) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] instanceof ExcelRange
                && args[1] instanceof ExcelRange
                && args[2] instanceof ExcelRange
                && args[3] instanceof ExcelRange
                && args[4] instanceof ExcelRange
                ) {

        } else {
            throw new RengineException(calInfo.getServiceName(), "输入不是数列");
        }

        __Key key;
        if (args.length > 8) {
            if (!(args[7] instanceof ExcelRange)) {
                throw new RengineException(calInfo.getServiceName(), "输入不是数列");
            }

            key = new __Key(args[0], args[1], args[2], args[3], args[4], args[5], args[7], args[8]);
        } else {
            key = new __Key(args[0], args[1], args[2], args[3], args[4], args[5], null, null);
        }
        final String reqKey = DataUtil.getStringValue(args[6]);

        Map<Object, Object> cache = calInfo.getCache(NAME);
        Map<String, String> result = (Map<String, String>) cache.get(key);
        Map<String, Map<String, Long>> invOccupied = new HashMap<String, Map<String, Long>>();

        if (result == null) {
            final Iterator<Object> d2Key = ((ExcelRange) args[0]).getIterator();
            final Iterator<Object> d2Count = ((ExcelRange) args[1]).getIterator();
            final Iterator<Object> d2Inv = ((ExcelRange) args[2]).getIterator();
            final Iterator<Object> d1Key = ((ExcelRange) args[3]).getIterator();
            final Iterator<Object> sortKey1 = ((ExcelRange) args[4]).getIterator();
            final int order1 = DataUtil.getNumberValue(args[5]).intValue();
            final Iterator<Object> sortKey2 = args.length > 8 ? ((ExcelRange) args[7]).getIterator() : null;
            final int order2 = args.length > 8 ? DataUtil.getNumberValue(args[8]).intValue() : -1;

            result = new HashMap<String, String>();
            Map<String, Long> inv = new HashMap<String, Long>();
            List<Row> rows = new ArrayList<Row>();

            while (true) {
                Row row = new Row();

                if (d2Key.hasNext()) {
                    row.d2key = DataUtil.getStringValue(d2Key.next());
                } else {
                    break;
                }

                if (d2Count.hasNext()) {
                    row.d2Count = DataUtil.getNumberValue(d2Count.next()).longValue();
                } else {
                    break;
                }

                if (d2Inv.hasNext()) {
                    row.d2Inv = DataUtil.getNumberValue(d2Inv.next()).longValue();
                } else {
                    break;
                }

                if (d1Key.hasNext()) {
                    row.d1Key = DataUtil.getStringValue(d1Key.next());
                } else {
                    break;
                }

                if (sortKey1.hasNext()) {
                    Object sortKey1Obj = sortKey1.next();
                    Type type = DataUtil.getType(sortKey1Obj);
                    switch (type) {
                        case LONG:
                        case DOUBLE:
                            row.sortKey1 = ((Number) sortKey1Obj).doubleValue();
                            break;
                        default:
                            row.sortKey1 = DataUtil.getStringValue(sortKey1Obj, type);
                    }
                } else {
                    break;
                }

                if (sortKey2 != null) {
                    if (sortKey2.hasNext()) {
                        Object sortKey2Obj = sortKey2.next();
                        Type type = DataUtil.getType(sortKey2Obj);
                        switch (type) {
                            case LONG:
                            case DOUBLE:
                                row.sortKey2 = ((Number) sortKey2Obj).doubleValue();
                                break;
                            default:
                                row.sortKey2 = DataUtil.getStringValue(sortKey2Obj, type);
                        }
                    } else {
                        break;
                    }
                }

                result.put(row.d1Key, "Y");
                row.order1 = order1;
                row.order2 = order2;
                rows.add(row);
            }

            Collections.sort(rows);

            for (Row row : rows) {
                if ("Y".equals(result.get(row.d1Key))) {
                    Long curInv = inv.get(row.d2key);
                    if (curInv == null) {
                        curInv = row.d2Inv;
                        inv.put(row.d2key, curInv);
                    }


                    Map<String, Long> invOccupiedCur = invOccupied.get(row.d1Key);
                    if (invOccupiedCur == null) {
                        invOccupiedCur = new HashMap<String, Long>();
                        invOccupied.put(row.d1Key, invOccupiedCur);
                    }


                    if (curInv >= row.d2Count) {
                        curInv -= row.d2Count;
                        invOccupiedCur.put(row.d2key, row.d2Count); // 占用
                        inv.put(row.d2key, curInv);
                    } else {
                        result.put(row.d1Key, "N");

                        for (Map.Entry<String, Long> entry : invOccupiedCur.entrySet()) { // 释放
                            inv.put(entry.getKey(), inv.get(entry.getKey()) + entry.getValue());
                        }

                        invOccupiedCur.clear();
                    }
                }
            }

            cache.put(key, result);
        }


        String ret = result.get(reqKey);
        if (ret == null) {
            return "N";
        }

        return ret;
    }
}
