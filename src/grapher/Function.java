package grapher;

import grapher.tokens.Token;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
	private FunctionManager functionManager;
	private LinkedList<Token> expressionToken = new LinkedList<Token>();
	private LinkedList<Token> reversePolish;
	private double[][] xyValues;
	private String name; //f1, f2, f3.....etc
	
	public Function(String name, String expression, Color color, FunctionManager functionManager, Graph graph){
		this.name = name;
		this.functionManager = functionManager;
		//edits syntax to algo readable form
		expression = checkForMultiply(expression);
		expression = checkForNegative(expression);
		expression = insertHash(expression);
		System.out.println(expression);
		tokenizer(expression);
		this.reversePolish = shuntingYard(expressionToken);
		this.xyValues = completeXYValues(reversePolish);
		graph.plot(xyValues, color);
	}
	
	private String checkForMultiply(String s){
		s = s.toLowerCase().replace(" ", "");
		s = s.replace(")(", ")*(").replace("x(", "x*(").replace(")x", ")*x");
		Pattern pattern;
		s = checkLoop(s, pattern = Pattern.compile("[0-9\\.][x(]|[x)][0-9\\.]"), pattern.matcher(s), "*");
		s = checkLoop(s, pattern = Pattern.compile("xx"), pattern.matcher(s), "*");
		return checkLoop(s, pattern = Pattern.compile("[^*&&x)[0-9]][a-z&&[^x]]+"), pattern.matcher(s), "*");
	}
	
	private String checkLoop(String s, Pattern pattern, Matcher matcher, String replacement){
		while(matcher.find()){
			matcher = pattern.matcher(s = new StringBuilder(s).insert(matcher.start() + 1, replacement).toString());
		}
		return s;
	}

	private String checkForNegative(String s) {
		Pattern pattern;
		s = negativeLoop(s, pattern = Pattern.compile("(?<=[-+*/(^]|^)-[0-9x\\.]+"), pattern.matcher(s), "(0", ")");
		return negativeLoop(s, pattern = Pattern.compile("(?<=[^0-9x)\\.])-(?=[(]|[a-z&&[^x]])"), pattern.matcher(s), "(0", "1)*");
	}
	
	private  String negativeLoop(String s, Pattern pattern, Matcher matcher, String start, String end){
		while(matcher.find()){
			matcher = pattern.matcher(s = new StringBuilder(s).insert(matcher.end(), end).insert(matcher.start(), start).toString());
		}
		return s;
	}
	
	private String insertHash(String s){
		Pattern pattern;
		return checkLoop(s, pattern = Pattern.compile("[-x+/*)(^][^#]|[^#][-x+/*)(^]"), pattern.matcher(s), "#");
	}
	
	public double[][] completeXYValues(LinkedList<Token> tokenList){
		//index 0 for x, index 1 for y
		double[][] xyValues = new double[2][functionManager.xImageLength];
		for(int i = 0; i < functionManager.xImageLength; i++){
			double y = evaluate(tokenList, (i/functionManager.xImageIncrement) + functionManager.Xmin);
			xyValues[0][i] = i;
			//System.out.println("x: " + ((i/functionManager.xImageIncrement) + functionManager.Xmin) + " y: " + y);
			xyValues[1][i] = Double.POSITIVE_INFINITY == y ? Double.MIN_EXPONENT
					: Double.NEGATIVE_INFINITY == y ? Double.MAX_EXPONENT : functionToImage(y);
			
		}
		return xyValues;
	}

	public double functionToImage(double input) {
		return input > 0 ?
			functionManager.yImageOrigin - (functionManager.yImageIncrement * input) :
		 functionManager.yImageOrigin + (functionManager.yImageIncrement * Math.abs(input));
	}
	
	public void tokenizer(String s) {
		for (String section : s.split("#")) {
			expressionToken.add(new Token(section));
		}
	}

	public LinkedList<Token> shuntingYard(LinkedList<Token> tokenList) {
		Stack<Token> stack = new Stack<Token>();
		LinkedList<Token> output = new LinkedList<Token>();
		for (Token t : tokenList) {
			if (t.isNumber()) {
				output.add(t);
			} else {
				if (t.isFunction()) {
					stack.push(t);
				} else {
					boolean isLeft = t.getAssociativity() == -1;
					if (t.isOperator()) {
						if (!stack.isEmpty()) {
							while (!stack.isEmpty() && t.getPrecedense() <= stack.peek().getPrecedense() && isLeft) {
								output.add(stack.pop());
							}
							stack.push(t);
						} else {
							stack.push(t);
						}
					} else {
						if (t.isLeftBracket()) {
							stack.push(t);
						} else {
							if (t.isRightBracket()) {
								while (!stack.peek().isLeftBracket()) {
									output.add(stack.pop());
								}
								stack.pop();
								if (!stack.isEmpty() && stack.peek().isFunction()) {
									output.add(stack.pop());
								}
							}
						}
					}
				}
			}
		}
		while (!stack.isEmpty()) {
			output.add(stack.pop());
		}
		return output;
	}

	private Double evaluate(LinkedList<Token> s, double x) {
		LinkedList<Token> temp = new LinkedList<Token>(s);
		for (Token j : temp) {
			if (j.isVariable()) {
				temp.set(temp.indexOf(j), new Token(Double.toString(x)));
			}
		}
		Stack<Token> stack = new Stack<Token>();
		for (Token token : temp) {
			stack.push(
			  token.isNumber()? token
			: token.isOperator()? new Token(Double.toString(token.getSymbol().operator(Double.parseDouble(stack.pop().getValue()),Double.parseDouble(stack.pop().getValue()))))
			: token.isFunction()? new Token(Double.toString(token.getSymbol().function(Double.parseDouble(stack.pop().getValue())))) 
			: null);
		}
		return Double.parseDouble(stack.pop().getValue());
	}
}
