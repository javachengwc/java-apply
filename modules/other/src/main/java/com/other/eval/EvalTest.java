package com.other.eval;

import java.util.Collections;

import net.sourceforge.jeval.Evaluator;

import org.cheffo.jeplite.JEP;
import org.cheffo.jeplite.util.DoubleStack;

import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;

/**
 * 数字表达式计算
 */
public class EvalTest {
	
	private static final double X_VALUE = 0.0;

	public static void main(String[] args) throws Exception {		
		testParsii();
		testJeval();
		testJeplite();
	}

	private static void testParsii() throws Exception {
		String exp = "2 + (7-5) * 3.14159 * x + sin(0)";
		
		// compile
		Scope scope = Scope.create();
		Expression parsiiExpr = Parser.parse(exp);
		Variable var = scope.getVariable("x");
		var.setValue(X_VALUE);
		
		// evaluate
		double result = parsiiExpr.evaluate();

		System.out.println(result);//-> 2.0
	}

	private static void testJeval() throws Exception {
		String exp = "2 + (7-5) * 3.14159 * #{x} + sin(0)";

		// compile
		Evaluator jevalEvaluator = new Evaluator();
		jevalEvaluator.setVariables(Collections.singletonMap("x", Double.toString(X_VALUE)));

		// evaluate
		double result = Double.parseDouble(jevalEvaluator.evaluate(exp));

		System.out.println(result);//-> 2.0
	}

	private static void testJeplite() throws Exception {
		String exp = "2 + (7-5) * 3.14159 * x + sin(0)";

		JEP jep = new JEP();
		jep.addVariable("x", X_VALUE);
		jep.parseExpression(exp);
		DoubleStack jepStack = new DoubleStack();

		// evaluate
		double result = jep.getValue(jepStack);

		System.out.println(result);//-> 2.0
	}
	

}
