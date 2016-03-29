// Generated from Excel.g4 by ANTLR 4.0
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExcelLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, TRUE=7, FALSE=8, NAME=9, 
		WS=10, STRING=11, ALPHA=12, SIGN=13, LONG=14, DIGIT=15, FLOAT=16, EXPONENT=17, 
		POW=18, MUL=19, DIV=20, JOIN=21, GE=22, LE=23, NE=24, GT=25, LT=26, EQ=27;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'%'", "')'", "','", "'('", "'$ROW$'", "':'", "TRUE", "FALSE", "NAME", 
		"WS", "STRING", "ALPHA", "SIGN", "LONG", "DIGIT", "FLOAT", "EXPONENT", 
		"'^'", "'*'", "'/'", "'&'", "'>='", "'<='", "'<>'", "'>'", "'<'", "'='"
	};
	public static final String[] ruleNames = {
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "TRUE", "FALSE", "NAME", 
		"WS", "STRING", "ALPHA", "SIGN", "LONG", "DIGIT", "FLOAT", "EXPONENT", 
		"POW", "MUL", "DIV", "JOIN", "GE", "LE", "NE", "GT", "LT", "EQ"
	};


	    @Override
	    public void notifyListeners(LexerNoViableAltException e) {
	        String text = _input.getText(Interval.of(_tokenStartCharIndex, _input.index()));
	        String msg = "\u65e0\u6cd5\u8bc6\u522b\u7684\u8f93\u5165 '" + getErrorDisplay(text) + "'";
	        throw new RuntimeException(msg);
	    }


	public ExcelLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Excel.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 9: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: _channel = HIDDEN;  break;
		}
	}

	public static final String _serializedATN =
		"\2\4\35\u00c7\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b"+
		"\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"+
		"\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"+
		"\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\3\3"+
		"\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\5\bR\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t^\n\t"+
		"\3\n\3\n\6\nb\n\n\r\n\16\nc\3\n\3\n\3\n\3\n\7\nj\n\n\f\n\16\nm\13\n\3"+
		"\n\3\n\3\n\6\nr\n\n\r\n\16\ns\5\nv\n\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f"+
		"\3\f\6\f\u0080\n\f\r\f\16\f\u0081\7\f\u0084\n\f\f\f\16\f\u0087\13\f\3"+
		"\f\3\f\3\r\3\r\3\16\6\16\u008e\n\16\r\16\16\16\u008f\3\17\6\17\u0093\n"+
		"\17\r\17\16\17\u0094\3\20\3\20\3\21\5\21\u009a\n\21\3\21\3\21\6\21\u009e"+
		"\n\21\r\21\16\21\u009f\3\21\5\21\u00a3\n\21\3\21\5\21\u00a6\n\21\3\22"+
		"\3\22\5\22\u00aa\n\22\3\22\6\22\u00ad\n\22\r\22\16\22\u00ae\3\23\3\23"+
		"\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31"+
		"\3\31\3\32\3\32\3\33\3\33\3\34\3\34\2\35\3\3\1\5\4\1\7\5\1\t\6\1\13\7"+
		"\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\2\27\r\1\31\16\1\33\17\1\35\20\1\37"+
		"\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1"+
		"\65\34\1\67\35\1\3\2\b\5\13\f\17\17\"\"\3$$\4C\\c|\4--//\4GGgg\4--//\u00dd"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\39\3\2\2\2\5;\3"+
		"\2\2\2\7=\3\2\2\2\t?\3\2\2\2\13A\3\2\2\2\rG\3\2\2\2\17Q\3\2\2\2\21]\3"+
		"\2\2\2\23a\3\2\2\2\25w\3\2\2\2\27{\3\2\2\2\31\u008a\3\2\2\2\33\u008d\3"+
		"\2\2\2\35\u0092\3\2\2\2\37\u0096\3\2\2\2!\u0099\3\2\2\2#\u00a7\3\2\2\2"+
		"%\u00b0\3\2\2\2\'\u00b2\3\2\2\2)\u00b4\3\2\2\2+\u00b6\3\2\2\2-\u00b8\3"+
		"\2\2\2/\u00bb\3\2\2\2\61\u00be\3\2\2\2\63\u00c1\3\2\2\2\65\u00c3\3\2\2"+
		"\2\67\u00c5\3\2\2\29:\7\'\2\2:\4\3\2\2\2;<\7+\2\2<\6\3\2\2\2=>\7.\2\2"+
		">\b\3\2\2\2?@\7*\2\2@\n\3\2\2\2AB\7&\2\2BC\7T\2\2CD\7Q\2\2DE\7Y\2\2EF"+
		"\7&\2\2F\f\3\2\2\2GH\7<\2\2H\16\3\2\2\2IJ\7V\2\2JK\7T\2\2KL\7W\2\2LR\7"+
		"G\2\2MN\7v\2\2NO\7t\2\2OP\7w\2\2PR\7g\2\2QI\3\2\2\2QM\3\2\2\2R\20\3\2"+
		"\2\2ST\7H\2\2TU\7C\2\2UV\7N\2\2VW\7U\2\2W^\7G\2\2XY\7h\2\2YZ\7c\2\2Z["+
		"\7n\2\2[\\\7u\2\2\\^\7g\2\2]S\3\2\2\2]X\3\2\2\2^\22\3\2\2\2_b\7a\2\2`"+
		"b\5\31\r\2a_\3\2\2\2a`\3\2\2\2bc\3\2\2\2ca\3\2\2\2cd\3\2\2\2du\3\2\2\2"+
		"ej\7a\2\2fj\5\31\r\2gj\5\37\20\2hj\7\60\2\2ie\3\2\2\2if\3\2\2\2ig\3\2"+
		"\2\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2lq\3\2\2\2mk\3\2\2\2nr\5\31"+
		"\r\2or\5\37\20\2pr\7a\2\2qn\3\2\2\2qo\3\2\2\2qp\3\2\2\2rs\3\2\2\2sq\3"+
		"\2\2\2st\3\2\2\2tv\3\2\2\2uk\3\2\2\2uv\3\2\2\2v\24\3\2\2\2wx\t\2\2\2x"+
		"y\3\2\2\2yz\b\13\2\2z\26\3\2\2\2{\u0085\7$\2\2|}\7$\2\2}\u0084\7$\2\2"+
		"~\u0080\n\3\2\2\177~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\177\3\2\2\2\u0081"+
		"\u0082\3\2\2\2\u0082\u0084\3\2\2\2\u0083|\3\2\2\2\u0083\177\3\2\2\2\u0084"+
		"\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0088\3\2"+
		"\2\2\u0087\u0085\3\2\2\2\u0088\u0089\7$\2\2\u0089\30\3\2\2\2\u008a\u008b"+
		"\t\4\2\2\u008b\32\3\2\2\2\u008c\u008e\t\5\2\2\u008d\u008c\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\34\3\2\2"+
		"\2\u0091\u0093\5\37\20\2\u0092\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094"+
		"\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095\36\3\2\2\2\u0096\u0097\4\62;"+
		"\2\u0097 \3\2\2\2\u0098\u009a\5\35\17\2\u0099\u0098\3\2\2\2\u0099\u009a"+
		"\3\2\2\2\u009a\u00a5\3\2\2\2\u009b\u009d\7\60\2\2\u009c\u009e\5\37\20"+
		"\2\u009d\u009c\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u009d\3\2\2\2\u009f\u00a0"+
		"\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u00a3\5#\22\2\u00a2\u00a1\3\2\2\2\u00a2"+
		"\u00a3\3\2\2\2\u00a3\u00a6\3\2\2\2\u00a4\u00a6\5#\22\2\u00a5\u009b\3\2"+
		"\2\2\u00a5\u00a4\3\2\2\2\u00a6\"\3\2\2\2\u00a7\u00a9\t\6\2\2\u00a8\u00aa"+
		"\t\7\2\2\u00a9\u00a8\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ac\3\2\2\2\u00ab"+
		"\u00ad\5\37\20\2\u00ac\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00ac\3"+
		"\2\2\2\u00ae\u00af\3\2\2\2\u00af$\3\2\2\2\u00b0\u00b1\7`\2\2\u00b1&\3"+
		"\2\2\2\u00b2\u00b3\7,\2\2\u00b3(\3\2\2\2\u00b4\u00b5\7\61\2\2\u00b5*\3"+
		"\2\2\2\u00b6\u00b7\7(\2\2\u00b7,\3\2\2\2\u00b8\u00b9\7@\2\2\u00b9\u00ba"+
		"\7?\2\2\u00ba.\3\2\2\2\u00bb\u00bc\7>\2\2\u00bc\u00bd\7?\2\2\u00bd\60"+
		"\3\2\2\2\u00be\u00bf\7>\2\2\u00bf\u00c0\7@\2\2\u00c0\62\3\2\2\2\u00c1"+
		"\u00c2\7@\2\2\u00c2\64\3\2\2\2\u00c3\u00c4\7>\2\2\u00c4\66\3\2\2\2\u00c5"+
		"\u00c6\7?\2\2\u00c68\3\2\2\2\27\2Q]acikqsu\u0081\u0083\u0085\u008f\u0094"+
		"\u0099\u009f\u00a2\u00a5\u00a9\u00ae";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}