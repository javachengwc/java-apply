// Generated from Excel.g4 by ANTLR 4.0
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface ExcelListener extends ParseTreeListener {
	void enterEval_unit(ExcelParser.Eval_unitContext ctx);
	void exitEval_unit(ExcelParser.Eval_unitContext ctx);

	void enterSigned_eval_unit(ExcelParser.Signed_eval_unitContext ctx);
	void exitSigned_eval_unit(ExcelParser.Signed_eval_unitContext ctx);

	void enterPercent(ExcelParser.PercentContext ctx);
	void exitPercent(ExcelParser.PercentContext ctx);

	void enterRange(ExcelParser.RangeContext ctx);
	void exitRange(ExcelParser.RangeContext ctx);

	void enterP_eval_unit(ExcelParser.P_eval_unitContext ctx);
	void exitP_eval_unit(ExcelParser.P_eval_unitContext ctx);

	void enterEval(ExcelParser.EvalContext ctx);
	void exitEval(ExcelParser.EvalContext ctx);

	void enterCell(ExcelParser.CellContext ctx);
	void exitCell(ExcelParser.CellContext ctx);

	void enterOp_plus_minus(ExcelParser.Op_plus_minusContext ctx);
	void exitOp_plus_minus(ExcelParser.Op_plus_minusContext ctx);

	void enterFunction(ExcelParser.FunctionContext ctx);
	void exitFunction(ExcelParser.FunctionContext ctx);

	void enterProg(ExcelParser.ProgContext ctx);
	void exitProg(ExcelParser.ProgContext ctx);

	void enterOp_pow(ExcelParser.Op_powContext ctx);
	void exitOp_pow(ExcelParser.Op_powContext ctx);

	void enterOp_mul_div(ExcelParser.Op_mul_divContext ctx);
	void exitOp_mul_div(ExcelParser.Op_mul_divContext ctx);

	void enterOp_join(ExcelParser.Op_joinContext ctx);
	void exitOp_join(ExcelParser.Op_joinContext ctx);
}