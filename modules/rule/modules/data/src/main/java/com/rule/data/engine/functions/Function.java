package com.rule.data.engine.functions;

import com.rule.data.engine.functions.base.*;
import com.rule.data.engine.functions.expand.*;
import com.rule.data.engine.functions.math.*;
import com.rule.data.engine.functions.program.JSONFORMAT;
import com.rule.data.engine.functions.program.JSONOBJ;
import com.rule.data.engine.functions.program.MD5;
import com.rule.data.engine.functions.time.*;
import com.rule.data.engine.functions.type.DATE;
import com.rule.data.engine.functions.type.DATETIME;
import com.rule.data.engine.functions.type.INT;
import com.rule.data.exception.CalculateException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.functions._O.*;
import com.rule.data.util.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Function {
    /**
     * 函数实现信息
     */
    private static final HashMap<String, Function> FUNCTIONS = new HashMap<String, Function>();

    static {
        FUNCTIONS.put(AND.NAME, new AND());
        FUNCTIONS.put(NOT.NAME, new NOT());
        FUNCTIONS.put(OR.NAME, new OR());
        FUNCTIONS.put(DATE.NAME, new DATE());
        FUNCTIONS.put(ABS.NAME, new ABS());
        FUNCTIONS.put(ROUND.NAME, new ROUND());
        FUNCTIONS.put(T.NAME, new T());
        FUNCTIONS.put(TRIM.NAME, new TRIM());
        FUNCTIONS.put(FLOOR.NAME, new FLOOR());
        FUNCTIONS.put(NOW.NAME, new NOW());
        FUNCTIONS.put(ROUNDDOWN.NAME, new ROUNDDOWN());
        FUNCTIONS.put(ROUNDUP.NAME, new ROUNDUP());
        FUNCTIONS.put(CONCATENATE.NAME, new CONCATENATE());
        FUNCTIONS.put(FIND.NAME, new FIND());
        FUNCTIONS.put(EXACT.NAME, new EXACT());
        FUNCTIONS.put(CEILING.NAME, new CEILING());
        FUNCTIONS.put(COUNTA.NAME, new COUNTA());
        FUNCTIONS.put(COUNTBLANK.NAME, new COUNTBLANK());
        FUNCTIONS.put(FROM_UNIXTIME.NAME, new FROM_UNIXTIME());
        FUNCTIONS.put(DAY.NAME, new DAY());
        FUNCTIONS.put(DAYS360.NAME, new DAYS360());
        FUNCTIONS.put(INT.NAME, new INT());
        FUNCTIONS.put(HOUR.NAME, new HOUR());
        FUNCTIONS.put(MINUTE.NAME, new MINUTE());
        FUNCTIONS.put(MONTH.NAME, new MONTH());
        FUNCTIONS.put(SECOND.NAME, new SECOND());
        FUNCTIONS.put(YEAR.NAME, new YEAR());
        FUNCTIONS.put(LEN.NAME, new LEN());
        FUNCTIONS.put(MID.NAME, new MID());
        FUNCTIONS.put(ROW.NAME, new ROW());
        FUNCTIONS.put(SUBSTITUTE.NAME, new SUBSTITUTE());
        FUNCTIONS.put(MOD.NAME, new MOD());
        FUNCTIONS.put(UPPER.NAME, new UPPER());
        FUNCTIONS.put(TIME.NAME, new TIME());
        FUNCTIONS.put(DATETIME.NAME, new DATETIME());
        FUNCTIONS.put(VLOOKUP.NAME, new VLOOKUP());
        FUNCTIONS.put(RAND.NAME, new RAND());
        FUNCTIONS.put(LEFT.NAME, new LEFT());
        FUNCTIONS.put(RIGHT.NAME, new RIGHT());
        FUNCTIONS.put(TEXT.NAME, new TEXT());
        FUNCTIONS.put(SIGN.NAME, new SIGN());
        FUNCTIONS.put(LN.NAME, new LN());
        FUNCTIONS.put(ISEVEN.NAME, new ISEVEN());
        FUNCTIONS.put(EVEN.NAME, new EVEN());
        FUNCTIONS.put(ISNUMBER.NAME, new ISNUMBER());
        FUNCTIONS.put(ISTEXT.NAME, new ISTEXT());
        FUNCTIONS.put(PI.NAME, new PI());
        FUNCTIONS.put(COS.NAME, new COS());
        FUNCTIONS.put(SIN.NAME, new SIN());
        FUNCTIONS.put(SQRT.NAME, new SQRT());
        FUNCTIONS.put(TAN.NAME, new TAN());
        FUNCTIONS.put(REPLACE.NAME, new REPLACE());
        FUNCTIONS.put(TODAY.NAME, new TODAY());
        FUNCTIONS.put(SEARCH.NAME, new SEARCH());
        FUNCTIONS.put(DATEDIF.NAME, new DATEDIF());
        FUNCTIONS.put(EVAL.NAME, new EVAL());
        FUNCTIONS.put(ISEMPTY.NAME, new ISEMPTY());
        FUNCTIONS.put(TWO_D_SUB.NAME, new TWO_D_SUB());
        FUNCTIONS.put(RANKSTR.NAME, new RANKSTR());
        FUNCTIONS.put(SUMIFSBYGROUP.NAME, new SUMIFSBYGROUP());
        FUNCTIONS.put(COUNTIFSBYGROUP.NAME, new COUNTIFSBYGROUP());
        FUNCTIONS.put(STRAMOUNT.NAME, new STRAMOUNT());
        FUNCTIONS.put(WEEKDAY.NAME, new WEEKDAY());
        FUNCTIONS.put(CONCATBYGROUP.NAME, new CONCATBYGROUP());
        FUNCTIONS.put(COUNTBYGROUP.NAME, new COUNTBYGROUP());
        FUNCTIONS.put(SUMBYGROUP.NAME, new SUMBYGROUP());
        FUNCTIONS.put(JSONFORMAT.NAME, new JSONFORMAT());
        FUNCTIONS.put(RANKBYGROUP.NAME, new RANKBYGROUP());
        FUNCTIONS.put(JSONOBJ.NAME, new JSONOBJ());
        FUNCTIONS.put(SMALL.NAME, new SMALL());
        FUNCTIONS.put(LARGE.NAME, new LARGE());
        FUNCTIONS.put(WEEKNUM.NAME, new WEEKNUM());
        FUNCTIONS.put(SUBSTITUTEMORE.NAME, new SUBSTITUTEMORE());
        FUNCTIONS.put(DISTINCT.NAME,new DISTINCT());
        FUNCTIONS.put(EDATE.NAME, new EDATE());

        FUNCTIONS.put(_O_CONCAT.NAME, new _O_CONCAT());
        FUNCTIONS.put(_O_CONCATBYPARA.NAME, new _O_CONCATBYPARA());
        FUNCTIONS.put(_O_COUNT.NAME, new _O_COUNT());
        FUNCTIONS.put(_O_COUNTBYGROUP.NAME, new _O_COUNTBYGROUP());
        FUNCTIONS.put(_O_COUNTBYPARA.NAME, new _O_COUNTBYPARA());
        FUNCTIONS.put(_O_COUNTIF.NAME, new _O_COUNTIF());
        FUNCTIONS.put(_O_COUNTIFBYGROUP.NAME, new _O_COUNTIFBYGROUP());
        FUNCTIONS.put(_O_COUNTIFBYPARA.NAME, new _O_COUNTIFBYPARA());
        FUNCTIONS.put(_O_COUNTIFS.NAME, new _O_COUNTIFS());
        FUNCTIONS.put(_O_COUNTIFSBYPARA.NAME, new _O_COUNTIFSBYPARA());
        FUNCTIONS.put(_O_GET.NAME, new _O_GET());
        FUNCTIONS.put(_O_GETBYPARA.NAME, new _O_GETBYPARA());
        FUNCTIONS.put(_O_GETBYPARA2.NAME, new _O_GETBYPARA2());
        FUNCTIONS.put(_O_MAX.NAME, new _O_MAX());
        FUNCTIONS.put(_O_MAXBYGROUP.NAME, new _O_MAXBYGROUP());
        FUNCTIONS.put(_O_MAXBYPARA.NAME, new _O_MAXBYPARA());
        FUNCTIONS.put(_O_MIN.NAME, new _O_MIN());
        FUNCTIONS.put(_O_MINBYGROUP.NAME, new _O_MINBYGROUP());
        FUNCTIONS.put(_O_MINBYPARA.NAME, new _O_MINBYPARA());
        FUNCTIONS.put(_O_PERCENTINMAX.NAME, new _O_PERCENTINMAX());
        FUNCTIONS.put(_O_RANK.NAME, new _O_RANK());
        FUNCTIONS.put(_O_SUM.NAME, new _O_SUM());
        FUNCTIONS.put(_O_SUMBYGROUP.NAME, new _O_SUMBYGROUP());
        FUNCTIONS.put(_O_SUMBYPARA.NAME, new _O_SUMBYPARA());
        FUNCTIONS.put(_O_SUMIF.NAME, new _O_SUMIF());
        FUNCTIONS.put(_O_SUMIFBYGROUP.NAME, new _O_SUMIFBYGROUP());
        FUNCTIONS.put(_O_SUMIFBYPARA.NAME, new _O_SUMIFBYPARA());
        FUNCTIONS.put(_O_SUMIFS.NAME, new _O_SUMIFS());
        FUNCTIONS.put(_O_SUMIFSBYPARA.NAME, new _O_SUMIFSBYPARA());
        FUNCTIONS.put(_O_VLOOKUP.NAME, new _O_VLOOKUP());
        FUNCTIONS.put(_O_VLOOKUPBYPARA.NAME, new _O_VLOOKUPBYPARA());
        FUNCTIONS.put(_O_RANKBYPARA.NAME, new _O_RANKBYPARA());
        FUNCTIONS.put(_O_RANKSTR.NAME, new _O_RANKSTR());
        FUNCTIONS.put(_O_CONCATBYGROUP.NAME, new _O_CONCATBYGROUP());
        FUNCTIONS.put(_O_Product.NAME, new _O_Product());
        FUNCTIONS.put(_O_JSON.NAME, new _O_JSON());
        FUNCTIONS.put(_O_JSONBYPARA.NAME, new _O_JSONBYPARA());
        FUNCTIONS.put(_O_MODE.NAME, new _O_MODE());
        FUNCTIONS.put(_O_MODEBYPARA.NAME, new _O_MODEBYPARA());
        FUNCTIONS.put(_O_JSONTABLEBYPARA.NAME,new _O_JSONTABLEBYPARA());
        FUNCTIONS.put(_O_COUNTDIS.NAME, new _O_COUNTDIS());
        FUNCTIONS.put(_O_COUNTDISBYPARA.NAME, new _O_COUNTDISBYPARA());


        REG_MATCH reg_match = new REG_MATCH();

        FUNCTIONS.put(REG_MATCH.NAME, reg_match);
        FUNCTIONS.put("_S_" + REG_MATCH.NAME, reg_match);

        MD5 md5 = new MD5();
        FUNCTIONS.put(MD5.NAME, md5);
        FUNCTIONS.put("_S_" + MD5.NAME, md5);

        GET get = new GET();
        FUNCTIONS.put(GET.NAME, get);
        FUNCTIONS.put("_S_" + GET.NAME, get);

        IN in = new IN();
        FUNCTIONS.put(IN.NAME, in);
        FUNCTIONS.put("_S_" + IN.NAME, in);


        AVERAGEBYGROUP averagebygroup = new AVERAGEBYGROUP();
        FUNCTIONS.put(AVERAGEBYGROUP.NAME, averagebygroup);
        FUNCTIONS.put("_S_" + AVERAGEBYGROUP.NAME, averagebygroup);


        CONCAT concat = new CONCAT();
        FUNCTIONS.put(CONCAT.NAME, concat);
        FUNCTIONS.put("_S_" + CONCAT.NAME, concat);


        COUNT count = new COUNT();
        FUNCTIONS.put(COUNT.NAME, count);
        FUNCTIONS.put("_S_" + COUNT.NAME, count);


        COUNTIF countif = new COUNTIF();
        FUNCTIONS.put(COUNTIF.NAME, countif);
        FUNCTIONS.put("_S_" + COUNTIF.NAME, countif);


        COUNTIFS countifs = new COUNTIFS();
        FUNCTIONS.put(COUNTIFS.NAME, countifs);
        FUNCTIONS.put("_S_" + COUNTIFS.NAME, countifs);


        DIS dis = new DIS();
        FUNCTIONS.put(DIS.NAME, dis);
        FUNCTIONS.put("_S_" + DIS.NAME, dis);


        LOCAT locat = new LOCAT();
        FUNCTIONS.put(LOCAT.NAME, locat);
        FUNCTIONS.put("_S_" + LOCAT.NAME, locat);


        MAX max = new MAX();
        FUNCTIONS.put(MAX.NAME, max);
        FUNCTIONS.put("_S_" + MAX.NAME, max);


        MAXBYGROUP maxbygroup = new MAXBYGROUP();
        FUNCTIONS.put(MAXBYGROUP.NAME, maxbygroup);
        FUNCTIONS.put("_S_" + MAXBYGROUP.NAME, maxbygroup);

        MIN min = new MIN();
        FUNCTIONS.put(MIN.NAME, min);
        FUNCTIONS.put("_S_" + MIN.NAME, min);


        MINBYGROUP minbygroup = new MINBYGROUP();
        FUNCTIONS.put(MINBYGROUP.NAME, minbygroup);
        FUNCTIONS.put("_S_" + MINBYGROUP.NAME, minbygroup);


        PERCENTINMAX percentinmax = new PERCENTINMAX();
        FUNCTIONS.put(PERCENTINMAX.NAME, percentinmax);
        FUNCTIONS.put("_S_" + PERCENTINMAX.NAME, percentinmax);


        RANK rank = new RANK();
        FUNCTIONS.put(RANK.NAME, rank);
        FUNCTIONS.put("_S_" + RANK.NAME, rank);


        SUM sum = new SUM();
        FUNCTIONS.put(SUM.NAME, sum);
        FUNCTIONS.put("_S_" + SUM.NAME, sum);


        SUMIF sumif = new SUMIF();
        FUNCTIONS.put(SUMIF.NAME, sumif);
        FUNCTIONS.put("_S_" + SUMIF.NAME, sumif);


        SUMIFS sumifs = new SUMIFS();
        FUNCTIONS.put(SUMIFS.NAME, sumifs);
        FUNCTIONS.put("_S_" + SUMIFS.NAME, sumifs);


        FUNCTIONS.put(AVERAGE.NAME, new AVERAGE());
        UNIXTIME unixtime = new UNIXTIME();
        FUNCTIONS.put(UNIXTIME.NAME, unixtime);
        FUNCTIONS.put("_S_" + UNIXTIME.NAME, unixtime);
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    /**
     * calInfo里有当前数据源的名称, 参数, 缓存等
     * args是多个参数
     *
     * @param calInfo
     * @param args
     * @return
     * @throws RengineException
     */
    public abstract Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException;


    /**
     * 获取对应函数的实现
     *
     * @param name
     * @return
     */
    public static Function getFunction(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        return FUNCTIONS.get(name);
    }

    /**
     * 是否可以进行数值操作
     *
     * @param input
     * @return
     * @throws RengineException
     */
    protected static boolean canNumberOP(Object input) throws RengineException {
        final Type type = DataUtil.getType(input);

        return type == Type.DOUBLE || type == Type.LONG
                || type == Type.DATE || (type == Type.STRING && canParseDouble((String) input));
    }

    private static boolean canParseDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取对应的参数
     *
     * @param args     入参, 以.json结尾的会进行JSON解析
     * @param start    从哪个位置开始取参数
     * @param param    原始父级的参数
     * @param isBYPARA 是否默认不继承参数, 如_O_GETBYPARA等函数默认不继承, 但是_O_SUM默认继承
     * @return
     * @throws RengineException
     */
    protected static Map<String, Object> getParam(Object[] args, int start, Map<String, Object> param, boolean isBYPARA)
            throws RengineException {
        if (args.length > start) {
            List<Object> params = new ArrayList<Object>();
            Map<String, Object> currentParam = new HashMap<String, Object>();
            boolean isInherit = false;

            for (int i = start; i < args.length; i += 2) {
                if (i + 1 >= args.length) {
                    final Object tmp = args[i];
                    if (tmp instanceof Boolean) {
                        isInherit = (Boolean) tmp;
                    }
                    break;
                }

                final String value = DataUtil.getStringValue(args[i]);
                String key = DataUtil.getStringValue(args[i + 1]);

                if (key.length() == 0 || value.length() == 0) {
                    continue;
                }

                Object trueValue = value;

                if (key.endsWith(".json")) {
                    if (key.length() == 5) {
                        DataUtil.inheritParam(currentParam, DataUtil.parse(value));
                        continue;
                    } else {
                        key = key.substring(0, key.length() - 5);
                        trueValue = DataUtil.parse(value);

                    }
                }

                params.add(trueValue);
                params.add(key);
            }

            for (int i = 0; i < params.size(); i += 2) {
                currentParam.put((String) params.get(i + 1), params.get(i));
            }


            if (isInherit) {
                DataUtil.inheritParam(currentParam, param);
            }

            return currentParam;
        } else {
            if (isBYPARA) {
                return DataUtil.EMPTY;
            } else {
                return param;
            }
        }
    }

}
