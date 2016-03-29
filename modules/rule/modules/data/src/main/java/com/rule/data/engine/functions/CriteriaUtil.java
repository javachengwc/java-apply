package com.rule.data.engine.functions;

import com.rule.data.util.DataUtil;

import java.util.regex.Pattern;

public final class CriteriaUtil {
    public interface I_MatchPredicate {
        boolean matches(Object x);
    }

    public static I_MatchPredicate createCriteriaPredicate(String arg) {
        return createGeneralMatchPredicate(arg);
    }


    private static abstract class MatcherBase implements I_MatchPredicate {
        private final CmpOp _operator;

        MatcherBase(CmpOp operator) {
            _operator = operator;
        }

        protected final int getCode() {
            return _operator.getCode();
        }

        protected final boolean evaluate(int cmpResult) {
            return _operator.evaluate(cmpResult);
        }

        protected final boolean evaluate(boolean cmpResult) {
            return _operator.evaluate(cmpResult);
        }

        @Override
        public final String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName()).append(" [");
            sb.append(_operator.getRepresentation());
            sb.append(getValueText());
            sb.append("]");
            return sb.toString();
        }

        protected abstract String getValueText();
    }

    private static final class NumberMatcher extends MatcherBase {

        private final double _value;

        public NumberMatcher(double value, CmpOp operator) {
            super(operator);
            _value = value;
        }

        @Override
        protected String getValueText() {
            return String.valueOf(_value);
        }

        public boolean matches(Object x) {
            double testValue;
            if (x instanceof String) {
                // if the target(x) is a string, but parses as a number
                // it may still count as a match, only for the equality operator
                switch (getCode()) {
                    case CmpOp.EQ:
                    case CmpOp.NONE:
                        break;
                    case CmpOp.NE:
                        // Always matches (inconsistent with above two cases).
                        // for example '<>123' matches '123', '4', 'abc', etc
                        return true;
                    default:
                        // never matches (also inconsistent with above three cases).
                        // for example '>5' does not match '6',
                        return false;
                }
                String se = (String) x;
                Double val = parseDouble(se);
                if (val == null) {
                    // x is text that is not a number
                    return false;
                }
                return _value == val.doubleValue();
            } else if ((x instanceof Number)) {
                testValue = ((Number) x).doubleValue();
            } else if ((x == null)) {
                switch (getCode()) {
                    case CmpOp.NE:
                        // Excel counts blank values in range as not equal to any value. See Bugzilla 51498
                        return true;
                    default:
                        return false;
                }
            } else {
                return false;
            }
            return evaluate(DataUtil.compare(testValue, _value));
        }

    }

    private static Double parseDouble(String se) {
        try {
            return Double.parseDouble(se);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static final class BooleanMatcher extends MatcherBase {

        private final int _value;

        public BooleanMatcher(boolean value, CmpOp operator) {
            super(operator);
            _value = boolToInt(value);
        }

        @Override
        protected String getValueText() {
            return _value == 1 ? "TRUE" : "FALSE";
        }

        private static int boolToInt(boolean value) {
            return value ? 1 : 0;
        }

        public boolean matches(Object x) {
            int testValue;
            if (x instanceof String) {
                if (true) { // change to false to observe more intuitive behaviour
                    // Note - Unlike with numbers, it seems that COUNTIF never matches
                    // boolean values when the target(x) is a string
                    return false;
                }
                String se = (String) x;
                Boolean val = parseBoolean(se);
                if (val == null) {
                    // x is text that is not a boolean
                    return false;
                }
                testValue = boolToInt(val.booleanValue());
            } else if ((x instanceof Boolean)) {
                testValue = boolToInt((Boolean) x);
            } else if (x == null) {
                switch (getCode()) {
                    case CmpOp.NE:
                        // Excel counts blank values in range as not equal to any value. See Bugzilla 51498
                        return true;
                    default:
                        return false;
                }
            } else if ((x instanceof Number)) {
                switch (getCode()) {
                    case CmpOp.NE:
                        // not-equals comparison of a number to boolean always returnes false
                        return true;
                    default:
                        return false;
                }
            } else {
                return false;
            }
            return evaluate(testValue - _value);
        }
    }


    private static final class StringMatcher extends MatcherBase {

        private final String _value;
        private final Pattern _pattern;

        public StringMatcher(String value, CmpOp operator) {
            super(operator);
            _value = value;
            switch (operator.getCode()) {
                case CmpOp.NONE:
                case CmpOp.EQ:
                case CmpOp.NE:
                    _pattern = getWildCardPattern(value);
                    break;
                default:
                    // pattern matching is never used for < > <= =>
                    _pattern = null;
            }
        }

        @Override
        protected String getValueText() {
            if (_pattern == null) {
                return _value;
            }
            return _pattern.pattern();
        }

        public boolean matches(Object x) {
            if (x == null) {
                switch (getCode()) {
                    case CmpOp.NONE:
                    case CmpOp.EQ:
                        return _value.length() == 0;
                    case CmpOp.NE:
                        // pred '<>' matches empty string but not blank cell
                        // pred '<>ABC'  matches blank and 'not ABC'
                        return _value.length() != 0;
                }
                // no other criteria matches a blank cell
                return false;
            }
            if (!(x instanceof String)) {
                // must always be string
                // even if match str is wild, but contains only digits
                // e.g. '4*7', NumberEval(4567) does not match
                return false;
            }
            String testedValue = ((String) x);
            if (testedValue.length() < 1 && _value.length() < 1) {
                // odd case: criteria '=' behaves differently to criteria ''

                switch (getCode()) {
                    case CmpOp.NONE:
                        return true;
                    case CmpOp.EQ:
                        return false;
                    case CmpOp.NE:
                        return true;
                }
                return false;
            }
            if (_pattern != null) {
                return evaluate(_pattern.matcher(testedValue).matches());
            }
            // String criteria in COUNTIF are case insensitive:
            // for example, the string "apples" and the string "APPLES" will match the same cells.
            return evaluate(testedValue.compareToIgnoreCase(_value));
        }

        /**
         * Translates Excel countif wildcard strings into java regex strings
         *
         * @return <code>null</code> if the specified value contains no special wildcard characters.
         */
        private static Pattern getWildCardPattern(String value) {
            int len = value.length();
            StringBuffer sb = new StringBuffer(len);
            boolean hasWildCard = false;
            for (int i = 0; i < len; i++) {
                char ch = value.charAt(i);
                switch (ch) {
                    case '?':
                        hasWildCard = true;
                        // match exactly one character
                        sb.append('.');
                        continue;
                    case '*':
                        hasWildCard = true;
                        // match one or more occurrences of any character
                        sb.append(".*");
                        continue;
                    case '~':
                        if (i + 1 < len) {
                            ch = value.charAt(i + 1);
                            switch (ch) {
                                case '?':
                                case '*':
                                    hasWildCard = true;
                                    sb.append('[').append(ch).append(']');
                                    i++; // Note - incrementing loop variable here
                                    continue;
                            }
                        }
                        // else not '~?' or '~*'
                        sb.append('~'); // just plain '~'
                        continue;
                    case '.':
                    case '$':
                    case '^':
                    case '[':
                    case ']':
                    case '(':
                    case ')':
                        // escape literal characters that would have special meaning in regex
                        sb.append("\\").append(ch);
                        continue;
                }
                sb.append(ch);
            }
            if (hasWildCard) {
                return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
            }
            return null;
        }
    }


    private static final class CmpOp {
        public static final int NONE = 0;
        public static final int EQ = 1;
        public static final int NE = 2;
        public static final int LE = 3;
        public static final int LT = 4;
        public static final int GT = 5;
        public static final int GE = 6;

        public static final CmpOp OP_NONE = op("", NONE);
        public static final CmpOp OP_EQ = op("=", EQ);
        public static final CmpOp OP_NE = op("<>", NE);
        public static final CmpOp OP_LE = op("<=", LE);
        public static final CmpOp OP_LT = op("<", LT);
        public static final CmpOp OP_GT = op(">", GT);
        public static final CmpOp OP_GE = op(">=", GE);
        private final String _representation;
        private final int _code;

        private static CmpOp op(String rep, int code) {
            return new CmpOp(rep, code);
        }

        private CmpOp(String representation, int code) {
            _representation = representation;
            _code = code;
        }

        /**
         * @return number of characters used to represent this operator
         */
        public int getLength() {
            return _representation.length();
        }

        public int getCode() {
            return _code;
        }

        public static CmpOp getOperator(String value) {
            int len = value.length();
            if (len < 1) {
                return OP_NONE;
            }

            char firstChar = value.charAt(0);

            switch (firstChar) {
                case '=':
                    return OP_EQ;
                case '>':
                    if (len > 1) {
                        switch (value.charAt(1)) {
                            case '=':
                                return OP_GE;
                        }
                    }
                    return OP_GT;
                case '<':
                    if (len > 1) {
                        switch (value.charAt(1)) {
                            case '=':
                                return OP_LE;
                            case '>':
                                return OP_NE;
                        }
                    }
                    return OP_LT;
            }
            return OP_NONE;
        }

        public boolean evaluate(boolean cmpResult) {
            switch (_code) {
                case NONE:
                case EQ:
                    return cmpResult;
                case NE:
                    return !cmpResult;
            }
            throw new RuntimeException("Cannot call boolean evaluate on non-equality operator '"
                    + _representation + "'");
        }

        public boolean evaluate(int cmpResult) {
            switch (_code) {
                case NONE:
                case EQ:
                    return cmpResult == 0;
                case NE:
                    return cmpResult != 0;
                case LT:
                    return cmpResult < 0;
                case LE:
                    return cmpResult <= 0;
                case GT:
                    return cmpResult > 0;
                case GE:
                    return cmpResult >= 0;
            }
            throw new RuntimeException("Cannot call boolean evaluate on non-equality operator '"
                    + _representation + "'");
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append(" [").append(_representation).append("]");
            return sb.toString();
        }

        public String getRepresentation() {
            return _representation;
        }
    }

    static Boolean parseBoolean(String strRep) {
        if (strRep.length() < 1) {
            return null;
        }
        switch (strRep.charAt(0)) {
            case 't':
            case 'T':
                if ("TRUE".equalsIgnoreCase(strRep)) {
                    return Boolean.TRUE;
                }
                break;
            case 'f':
            case 'F':
                if ("FALSE".equalsIgnoreCase(strRep)) {
                    return Boolean.FALSE;
                }
                break;
        }
        return null;
    }

    private static I_MatchPredicate createGeneralMatchPredicate(String value) {
        CmpOp operator = CmpOp.getOperator(value);
        value = value.substring(operator.getLength());

        Boolean booleanVal = parseBoolean(value);
        if (booleanVal != null) {
            return new BooleanMatcher(booleanVal.booleanValue(), operator);
        }

        Double doubleVal = parseDouble(value);
        if (doubleVal != null) {
            return new NumberMatcher(doubleVal.doubleValue(), operator);
        }

        //else - just a plain string with no interpretation.
        return new StringMatcher(value, operator);
    }

}
