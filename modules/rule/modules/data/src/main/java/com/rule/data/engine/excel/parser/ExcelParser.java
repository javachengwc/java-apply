// Generated from Excel.g4 by ANTLR 4.1
package com.rule.data.engine.excel.parser;

import com.rule.data.engine.excel.ExcelVisitor;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.engine.excel.StringPool;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.util.DataUtil;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNSimulator;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import static com.rule.data.engine.excel.NumberPool.getLong;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExcelParser extends Parser {
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__5 = 1, T__4 = 2, T__3 = 3, T__2 = 4, T__1 = 5, T__0 = 6, TRUE = 7, FALSE = 8, NAME = 9,
            WS = 10, STRING = 11, ALPHA = 12, SIGN = 13, LONG = 14, DIGIT = 15, FLOAT = 16, EXPONENT = 17,
            POW = 18, MUL = 19, DIV = 20, JOIN = 21, GE = 22, LE = 23, NE = 24, GT = 25, LT = 26, EQ = 27;
    public static final String[] tokenNames = {
            "<INVALID>", "'%'", "')'", "','", "'('", "'$ROW$'", "':'", "TRUE", "FALSE",
            "NAME", "WS", "STRING", "ALPHA", "SIGN", "LONG", "DIGIT", "FLOAT", "EXPONENT",
            "'^'", "'*'", "'/'", "'&'", "'>='", "'<='", "'<>'", "'>'", "'<'", "'='"
    };
    public static final int
            RULE_prog = 0, RULE_eval = 1, RULE_op_join = 2, RULE_op_plus_minus = 3,
            RULE_op_mul_div = 4, RULE_op_pow = 5, RULE_signed_eval_unit = 6, RULE_eval_unit = 7,
            RULE_p_eval_unit = 8, RULE_percent = 9, RULE_function = 10, RULE_cell = 11,
            RULE_range = 12;
    public static final String[] ruleNames = {
            "prog", "eval", "op_join", "op_plus_minus", "op_mul_div", "op_pow", "signed_eval_unit",
            "eval_unit", "p_eval_unit", "percent", "function", "cell", "range"
    };

    @Override
    public String getGrammarFileName() {
        return "Excel.g4";
    }

    @Override
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public ExcelParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class ProgContext extends ParserRuleContext {
        public TerminalNode EOF() {
            return getToken(ExcelParser.EOF, 0);
        }

        public EvalContext eval() {
            return getRuleContext(EvalContext.class, 0);
        }

        public ProgContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_prog;
        }
    }

    public final ProgContext prog() throws RecognitionException {
        ProgContext _localctx = new ProgContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_prog);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(26);
                eval();
                setState(27);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class EvalContext extends ParserRuleContext {
        public Op_joinContext op_join(int i) {
            return getRuleContext(Op_joinContext.class, i);
        }

        public TerminalNode LT() {
            return getToken(ExcelParser.LT, 0);
        }

        public List<Op_joinContext> op_join() {
            return getRuleContexts(Op_joinContext.class);
        }

        public TerminalNode NE() {
            return getToken(ExcelParser.NE, 0);
        }

        public TerminalNode LE() {
            return getToken(ExcelParser.LE, 0);
        }

        public TerminalNode GT() {
            return getToken(ExcelParser.GT, 0);
        }

        public TerminalNode EQ() {
            return getToken(ExcelParser.EQ, 0);
        }

        public TerminalNode GE() {
            return getToken(ExcelParser.GE, 0);
        }

        public EvalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_eval;
        }
    }

    public final EvalContext eval() throws RecognitionException {
        EvalContext _localctx = new EvalContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_eval);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(29);
                op_join();
                setState(32);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GE) | (1L << LE) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << EQ))) != 0)) {
                    {
                        setState(30);
                        _la = _input.LA(1);
                        if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GE) | (1L << LE) | (1L << NE) | (1L << GT) | (1L << LT) | (1L << EQ))) != 0))) {
                            _errHandler.recoverInline(this);
                        }
                        consume();
                        setState(31);
                        op_join();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Op_joinContext extends ParserRuleContext {
        public List<TerminalNode> JOIN() {
            return getTokens(ExcelParser.JOIN);
        }

        public Op_plus_minusContext op_plus_minus(int i) {
            return getRuleContext(Op_plus_minusContext.class, i);
        }

        public TerminalNode JOIN(int i) {
            return getToken(ExcelParser.JOIN, i);
        }

        public List<Op_plus_minusContext> op_plus_minus() {
            return getRuleContexts(Op_plus_minusContext.class);
        }

        public Op_joinContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_op_join;
        }
    }

    public final Op_joinContext op_join() throws RecognitionException {
        Op_joinContext _localctx = new Op_joinContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_op_join);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(34);
                op_plus_minus();
                setState(39);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == JOIN) {
                    {
                        {
                            setState(35);
                            match(JOIN);
                            setState(36);
                            op_plus_minus();
                        }
                    }
                    setState(41);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Op_plus_minusContext extends ParserRuleContext {
        public List<TerminalNode> SIGN() {
            return getTokens(ExcelParser.SIGN);
        }

        public Op_mul_divContext op_mul_div(int i) {
            return getRuleContext(Op_mul_divContext.class, i);
        }

        public TerminalNode SIGN(int i) {
            return getToken(ExcelParser.SIGN, i);
        }

        public List<Op_mul_divContext> op_mul_div() {
            return getRuleContexts(Op_mul_divContext.class);
        }

        public Op_plus_minusContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_op_plus_minus;
        }
    }

    public final Op_plus_minusContext op_plus_minus() throws RecognitionException {
        Op_plus_minusContext _localctx = new Op_plus_minusContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_op_plus_minus);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(42);
                op_mul_div();
                setState(47);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == SIGN) {
                    {
                        {
                            setState(43);
                            match(SIGN);
                            setState(44);
                            op_mul_div();
                        }
                    }
                    setState(49);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Op_mul_divContext extends ParserRuleContext {
        public List<Op_powContext> op_pow() {
            return getRuleContexts(Op_powContext.class);
        }

        public List<TerminalNode> MUL() {
            return getTokens(ExcelParser.MUL);
        }

        public List<TerminalNode> DIV() {
            return getTokens(ExcelParser.DIV);
        }

        public TerminalNode DIV(int i) {
            return getToken(ExcelParser.DIV, i);
        }

        public Op_powContext op_pow(int i) {
            return getRuleContext(Op_powContext.class, i);
        }

        public TerminalNode MUL(int i) {
            return getToken(ExcelParser.MUL, i);
        }

        public Op_mul_divContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_op_mul_div;
        }
    }

    public final Op_mul_divContext op_mul_div() throws RecognitionException {
        Op_mul_divContext _localctx = new Op_mul_divContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_op_mul_div);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(50);
                op_pow();
                setState(55);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == MUL || _la == DIV) {
                    {
                        {
                            setState(51);
                            _la = _input.LA(1);
                            if (!(_la == MUL || _la == DIV)) {
                                _errHandler.recoverInline(this);
                            }
                            consume();
                            setState(52);
                            op_pow();
                        }
                    }
                    setState(57);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Op_powContext extends ParserRuleContext {
        public Signed_eval_unitContext signed_eval_unit(int i) {
            return getRuleContext(Signed_eval_unitContext.class, i);
        }

        public List<Signed_eval_unitContext> signed_eval_unit() {
            return getRuleContexts(Signed_eval_unitContext.class);
        }

        public TerminalNode POW(int i) {
            return getToken(ExcelParser.POW, i);
        }

        public List<TerminalNode> POW() {
            return getTokens(ExcelParser.POW);
        }

        public Op_powContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_op_pow;
        }
    }

    public final Op_powContext op_pow() throws RecognitionException {
        Op_powContext _localctx = new Op_powContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_op_pow);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(58);
                signed_eval_unit();
                setState(63);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == POW) {
                    {
                        {
                            setState(59);
                            match(POW);
                            setState(60);
                            signed_eval_unit();
                        }
                    }
                    setState(65);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Signed_eval_unitContext extends ParserRuleContext {
        public TerminalNode SIGN() {
            return getToken(ExcelParser.SIGN, 0);
        }

        public Eval_unitContext eval_unit() {
            return getRuleContext(Eval_unitContext.class, 0);
        }

        public Signed_eval_unitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_signed_eval_unit;
        }
    }

    public final Signed_eval_unitContext signed_eval_unit() throws RecognitionException {
        Signed_eval_unitContext _localctx = new Signed_eval_unitContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_signed_eval_unit);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(67);
                _la = _input.LA(1);
                if (_la == SIGN) {
                    {
                        setState(66);
                        match(SIGN);
                    }
                }

                setState(69);
                eval_unit();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Eval_unitContext extends ParserRuleContext {
        private String string;
        private Long _long;
        private Double _double;

        public FunctionContext function() {
            return getRuleContext(FunctionContext.class, 0);
        }

        public Double FLOAT(String symbol) {
            if (_double == null) {
                _double = NumberPool.getDouble(symbol);
            }

            return _double;
        }


        public Long LONG(String symbol) {
            if (_long == null) {
                _long = NumberPool.getLong(symbol);
            }

            return _long;
        }

        public RangeContext range() {
            return getRuleContext(RangeContext.class, 0);
        }

        public P_eval_unitContext p_eval_unit() {
            return getRuleContext(P_eval_unitContext.class, 0);
        }

        public CellContext cell() {
            return getRuleContext(CellContext.class, 0);
        }

        public String STRING(String symbol) {
            if (string == null) {
                string = StringPool.getString(symbol);
            }

            return string;
        }

        public PercentContext percent() {
            return getRuleContext(PercentContext.class, 0);
        }

        public Eval_unitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_eval_unit;
        }
    }

    public final Eval_unitContext eval_unit() throws RecognitionException {
        Eval_unitContext _localctx = new Eval_unitContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_eval_unit);
        try {
            setState(81);
            switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(71);
                    match(TRUE);
                }
                break;

                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(72);
                    match(FALSE);
                }
                break;

                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(73);
                    match(LONG);
                }
                break;

                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(74);
                    match(STRING);
                }
                break;

                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(75);
                    match(FLOAT);
                }
                break;

                case 6:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(76);
                    percent();
                }
                break;

                case 7:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(77);
                    cell();
                }
                break;

                case 8:
                    enterOuterAlt(_localctx, 8);
                {
                    setState(78);
                    range();
                }
                break;

                case 9:
                    enterOuterAlt(_localctx, 9);
                {
                    setState(79);
                    function();
                }
                break;

                case 10:
                    enterOuterAlt(_localctx, 10);
                {
                    setState(80);
                    p_eval_unit();
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class P_eval_unitContext extends ParserRuleContext {
        public EvalContext eval() {
            return getRuleContext(EvalContext.class, 0);
        }

        public P_eval_unitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_p_eval_unit;
        }
    }

    public final P_eval_unitContext p_eval_unit() throws RecognitionException {
        P_eval_unitContext _localctx = new P_eval_unitContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_p_eval_unit);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(83);
                match(4);
                setState(84);
                eval();
                setState(85);
                match(2);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PercentContext extends ParserRuleContext {
        private Double _double;
        private Long _long;

        public Double FLOAT(String input) {
            if (_double == null) {
                _double = NumberPool.getDouble(input) / 100D;
            }

            return _double;
        }

        public Double LONG(String input) {
            if (_double == null) {
                _double = NumberPool.getLong(input) / 100D;
            }

            return _double;
        }

        public PercentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_percent;
        }
    }

    public final PercentContext percent() throws RecognitionException {
        PercentContext _localctx = new PercentContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_percent);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(87);
                _la = _input.LA(1);
                if (!(_la == LONG || _la == FLOAT)) {
                    _errHandler.recoverInline(this);
                }
                consume();
                setState(88);
                match(1);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FunctionContext extends ParserRuleContext {
        private String funcName = null;
        private Function function;

        public Function getFunction() {
            if (function == null) {
                function = Function.getFunction(funcName());
            }

            return function;
        }

        public EvalContext eval(int i) {
            return getRuleContext(EvalContext.class, i);
        }

        public List<EvalContext> eval() {
            return getRuleContexts(EvalContext.class);
        }

        public String funcName() {
            if (funcName == null) {
                funcName = getChild(0).getText().toUpperCase();
            }

            return funcName;
        }

        public FunctionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_function;
        }
    }

    public final FunctionContext function() throws RecognitionException {
        FunctionContext _localctx = new FunctionContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_function);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(90);
                match(NAME);
                setState(91);
                match(4);
                setState(100);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 4) | (1L << TRUE) | (1L << FALSE) | (1L << NAME) | (1L << STRING) | (1L << SIGN) | (1L << LONG) | (1L << FLOAT))) != 0)) {
                    {
                        setState(92);
                        eval();
                        setState(97);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == 3) {
                            {
                                {
                                    setState(93);
                                    match(3);
                                    setState(94);
                                    eval();
                                }
                            }
                            setState(99);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(102);
                match(2);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CellContext extends ParserRuleContext {
        private int row = -2;
        private int numberIndex = -2;
        private int column = -1;
        private String name;

        public void checkNumberIndex() {
            if (numberIndex == -2) {
                numberIndex = ExcelVisitor.getNumberIndex(NAME());
            }
        }

        public int getNumberIndex() {
            checkNumberIndex();
            return numberIndex;
        }

        public int getColumn() throws RengineException {
            if (column == -1) {
                if (this.getChildCount() == 2) {
                    column = DataUtil.countIndex(NAME());
                } else {
                    checkNumberIndex();

                    if (numberIndex == -1) {
                        column = DataUtil.countIndex(NAME());
                    } else {
                        column = DataUtil.countIndex(NAME().substring(0, numberIndex));
                    }

                }
            }

            return column;
        }

        public int getRow(CalInfo calInfo) throws RengineException {
            if (this.getChildCount() == 2) {
                return calInfo.getCurRow();
            } else {
                if (row == -2) {
                    checkNumberIndex();

                    if (numberIndex == -1) {
                        row = -1;
                    } else {
                        row = getLong(NAME().substring(numberIndex)).intValue() - 1;
                    }
                }

                return row;
            }
        }


        public String NAME() {
            if (name == null) {
                name = getChild(0).getText();
            }

            return name;
        }

        public CellContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cell;
        }
    }

    public final CellContext cell() throws RecognitionException {
        CellContext _localctx = new CellContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_cell);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(104);
                match(NAME);
                setState(106);
                _la = _input.LA(1);
                if (_la == 5) {
                    {
                        setState(105);
                        match(5);
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class RangeContext extends ParserRuleContext {
        public CellContext cell(int i) {
            return getRuleContext(CellContext.class, i);
        }

        public List<CellContext> cell() {
            return getRuleContexts(CellContext.class);
        }

        public RangeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_range;
        }
    }

    public final RangeContext range() throws RecognitionException {
        RangeContext _localctx = new RangeContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_range);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(108);
                cell();
                setState(109);
                match(6);
                setState(110);
                cell();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN =
            "\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\35s\4\2\t\2\4\3\t" +
                    "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4" +
                    "\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\3\3\3\3\5\3#\n\3\3\4\3\4\3\4" +
                    "\7\4(\n\4\f\4\16\4+\13\4\3\5\3\5\3\5\7\5\60\n\5\f\5\16\5\63\13\5\3\6\3" +
                    "\6\3\6\7\68\n\6\f\6\16\6;\13\6\3\7\3\7\3\7\7\7@\n\7\f\7\16\7C\13\7\3\b" +
                    "\5\bF\n\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tT\n\t\3\n" +
                    "\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\7\fb\n\f\f\f\16\fe\13" +
                    "\f\5\fg\n\f\3\f\3\f\3\r\3\r\5\rm\n\r\3\16\3\16\3\16\3\16\3\16\2\17\2\4" +
                    "\6\b\n\f\16\20\22\24\26\30\32\2\5\3\2\30\35\3\2\25\26\4\2\20\20\22\22" +
                    "w\2\34\3\2\2\2\4\37\3\2\2\2\6$\3\2\2\2\b,\3\2\2\2\n\64\3\2\2\2\f<\3\2" +
                    "\2\2\16E\3\2\2\2\20S\3\2\2\2\22U\3\2\2\2\24Y\3\2\2\2\26\\\3\2\2\2\30j" +
                    "\3\2\2\2\32n\3\2\2\2\34\35\5\4\3\2\35\36\7\2\2\3\36\3\3\2\2\2\37\"\5\6" +
                    "\4\2 !\t\2\2\2!#\5\6\4\2\" \3\2\2\2\"#\3\2\2\2#\5\3\2\2\2$)\5\b\5\2%&" +
                    "\7\27\2\2&(\5\b\5\2\'%\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*\7\3\2\2" +
                    "\2+)\3\2\2\2,\61\5\n\6\2-.\7\17\2\2.\60\5\n\6\2/-\3\2\2\2\60\63\3\2\2" +
                    "\2\61/\3\2\2\2\61\62\3\2\2\2\62\t\3\2\2\2\63\61\3\2\2\2\649\5\f\7\2\65" +
                    "\66\t\3\2\2\668\5\f\7\2\67\65\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2" +
                    ":\13\3\2\2\2;9\3\2\2\2<A\5\16\b\2=>\7\24\2\2>@\5\16\b\2?=\3\2\2\2@C\3" +
                    "\2\2\2A?\3\2\2\2AB\3\2\2\2B\r\3\2\2\2CA\3\2\2\2DF\7\17\2\2ED\3\2\2\2E" +
                    "F\3\2\2\2FG\3\2\2\2GH\5\20\t\2H\17\3\2\2\2IT\7\t\2\2JT\7\n\2\2KT\7\20" +
                    "\2\2LT\7\r\2\2MT\7\22\2\2NT\5\24\13\2OT\5\30\r\2PT\5\32\16\2QT\5\26\f" +
                    "\2RT\5\22\n\2SI\3\2\2\2SJ\3\2\2\2SK\3\2\2\2SL\3\2\2\2SM\3\2\2\2SN\3\2" +
                    "\2\2SO\3\2\2\2SP\3\2\2\2SQ\3\2\2\2SR\3\2\2\2T\21\3\2\2\2UV\7\6\2\2VW\5" +
                    "\4\3\2WX\7\4\2\2X\23\3\2\2\2YZ\t\4\2\2Z[\7\3\2\2[\25\3\2\2\2\\]\7\13\2" +
                    "\2]f\7\6\2\2^c\5\4\3\2_`\7\5\2\2`b\5\4\3\2a_\3\2\2\2be\3\2\2\2ca\3\2\2" +
                    "\2cd\3\2\2\2dg\3\2\2\2ec\3\2\2\2f^\3\2\2\2fg\3\2\2\2gh\3\2\2\2hi\7\4\2" +
                    "\2i\27\3\2\2\2jl\7\13\2\2km\7\7\2\2lk\3\2\2\2lm\3\2\2\2m\31\3\2\2\2no" +
                    "\5\30\r\2op\7\b\2\2pq\5\30\r\2q\33\3\2\2\2\f\")\619AEScfl";
    public static final ATN _ATN =
            ATNSimulator.deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}