package grapher;

import grapher.tokens.Token;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
	private FunctionManager functionManager;
	private LinkedList<Token> expressionToken;
	private LinkedList<Token> reversePolish;
	private double[][] xyValues;
	private String name; //f1, f2, f3.....etc
	
	public Function(String name, String expression, Color color, FunctionManager functionManager, Graph graph){
		this.name = name;
		this.functionManager = functionManager;
		//edits syntax to algo readable form
		expression = checkForMultiply(expression);
		expression = insertHash(expression);
		System.out.println(expression);
		this.expressionToken = tokenizer(expression);
		checkForNegative();
		this.reversePolish = shuntingYard(expressionToken);
		this.xyValues = completeXYValues(reversePolish);
		graph.plot(xyValues, color);
	}
	
	private String checkForMultiply(String s){
		s = s.toLowerCase().replace(" ", "");
		s = s.replace(")(", ")*(").replace("x(", "x*(").replace(")x", ")*x");
		Pattern pattern;
		s = checkLoop(s, pattern = Pattern.compile("[)x][0-9\\.]"), pattern.matcher(s), "*");
		s = checkLoop(s, pattern = Pattern.compile("[0-9\\.][x(]"), pattern.matcher(s), "*");
		s = checkLoop(s, pattern = Pattern.compile("xx"), pattern.matcher(s), "*");
		s = checkLoop(s, pattern = Pattern.compile("[[0-9]|x|\\)]ln"), pattern.matcher(s), "*");
		return checkLoop(s, pattern = Pattern.compile("[[0-9]|x|\\)][a-z&&[^x]][a-z&&[^x]][a-z&&[^x]]"), pattern.matcher(s), "*");
	}
	
	private String insertHash(String s){
		Pattern pattern;
		s =  checkLoop(s, pattern = Pattern.compile("[x|\\(|\\)|\\+|\\*|\\^|\\-|/][.[^#]]"), pattern.matcher(s), "#");
		return checkLoop(s, pattern = Pattern.compile("[.[^#]][x|\\(|\\)|\\+|\\*|\\^|\\-|/]"), pattern.matcher(s), "#");
	}
	
	private String checkLoop(String s, Pattern pattern, Matcher matcher, String replacement){
		while(matcher.find()){
			matcher = pattern.matcher(s = new StringBuilder(s).insert(matcher.start() + 1, replacement).toString());
		}
		return s;
	}

	private void checkForNegative() {
		int negativeCount = 0;
		for (int i = 0; i < expressionToken.size(); i++) {
			if (expressionToken.get(i).isMinusSign()) {
				if (i == 0 || expressionToken.get(i - 1).isOperator()) {
					negativeCount++;
				}
			}
		}
		for (int j = 0; j < negativeCount; j++) {
			for (int i = 0; i < expressionToken.size(); i++) {
				if (expressionToken.get(i).isMinusSign()) {
					if(i == 0 && expressionToken.get(i + 1).isLeftBracket()){
						expressionToken.add(i + 1, new Token("*"));
						expressionToken.add(i + 1, new Token(")"));
						expressionToken.add(i + 1, new Token("1"));
						expressionToken.add(i, new Token("0"));
						expressionToken.add(i, new Token("("));
						break;
					}
					else if ((i == 0 && !expressionToken.get(i + 1).isLeftBracket()) || expressionToken.get(i - 1).isOperator()) {
						expressionToken.add(i + 2, new Token(")"));
						expressionToken.add(i, new Token("0"));
						expressionToken.add(i, new Token("("));
						break;
					}
				}
			}
		}
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
		if (input > 0) {
			return functionManager.yImageOrigin - (functionManager.yImageIncrement * input);
		}
		return functionManager.yImageOrigin + (functionManager.yImageIncrement * Math.abs(input));
	}
	
	public LinkedList<Token> tokenizer(String s) {
		LinkedList<Token> done = new LinkedList<Token>();
		for (String section : s.split("#")) {
			done.add(new Token(section));
		}
		return done;
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
			int index = temp.indexOf(j);
			if (j.isVariable()) {
				temp.set(index, new Token(Double.toString(x)));
			}
		}
		double a = 0;
		double b = 0;
		while (temp.size() != 1) {
			for (Token f : temp) {
				if (f.isOperator()) {
					a = Double.parseDouble(temp.remove(temp.indexOf(f) - 2).getValue());
					b = Double.parseDouble(temp.remove(temp.indexOf(f) - 1).getValue());
					temp.set(
							temp.indexOf(f),
							new Token(Double.toString(f.getSymbol().operator(a, b))));
					break;
				} else if (f.isFunction()) {
					a = Double.parseDouble(temp.remove(temp.indexOf(f) - 1).getValue());
					temp.set(
							temp.indexOf(f),
							new Token(Double.toString(f.getSymbol().function(a))));
					break;
				}
			}
		}
		return Double.parseDouble(temp.getFirst().getValue());
	}
}
