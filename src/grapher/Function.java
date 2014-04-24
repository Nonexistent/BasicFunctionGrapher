package grapher;

import grapher.derivative.Node;
import grapher.managers.ManagerBase;
import grapher.tokens.Token;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

public class Function {
	private ManagerBase manager;
	private LinkedList<Token> expressionToken;
	private LinkedList<Token> reversePolish;
	private double[][] xyValues;
	public double[] value;
	private String name; //f1, f2, f3.....etc
	private Node expressionTree;
	
	public Function(String name, String expression, Color color, ManagerBase manager, Graph graph, JTextArea statusArea){
		this.name = name;
		this.manager = manager;
		int expressionLength = expression.length();
		//edits syntax to algo readable form
		if((expressionLength - expression.replace("(", "").length()) != (expressionLength - expression.replace(")", "").length())) {
			statusArea.append("\n" + this.name + " Error; missing brackets.");
			return;
		}
		expression = checkForMultiply(expression);
		expression = checkForNegative(expression);
		expression = insertHash(expression);
		System.out.println(expression);
		expressionToken = tokenizer(expression);
		this.reversePolish = shuntingYard(expressionToken);
		Queue<Integer> variablePosition = variablePosition(reversePolish);
		try {
			this.xyValues = completeXYValues(reversePolish, variablePosition);
		} catch (EmptyStackException e) {
			statusArea.append("\n" + name + ": Syntax error.");
			return;
		}
		graph.plot(xyValues, color);
	}
	
	public Function(double min, double max, String expression){
		expression = checkForMultiply(expression);
		expression = checkForNegative(expression);
		expression = insertHash(expression);
		System.out.println(expression);
		expressionToken = tokenizer(expression);
		this.reversePolish = shuntingYard(expressionToken);
		Queue<Integer> variablePosition = variablePosition(reversePolish);
		try {
			this.value = completeParametric(reversePolish, variablePosition, min, max);
		} catch (EmptyStackException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public Function(String expression){
		expression = checkForMultiply(expression);
		expression = checkForNegative(expression);
		expression = insertHash(expression);
		System.out.println(expression);
		expressionToken = tokenizer(expression);
		this.reversePolish = shuntingYard(expressionToken);
		createTree();
	}
	
	public String getDerivative(){
		String raw = expressionTree.derive();
		return (new Function(raw)).getTree().simplify().replaceFirst("^\\(", "").replaceFirst("\\)$", "");
	}
	
	public Node getTree(){
		return expressionTree;
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
	
	private Queue<Integer> variablePosition(LinkedList<Token> s){
		Queue<Integer> positions = new ArrayDeque<Integer>(7);
		for(Token token: s){
			if(token.isVariable()){
				positions.add(s.indexOf(token));
			}
		}
		return positions;
	}
	
	private double[][] completeXYValues(LinkedList<Token> tokenList, Queue<Integer> variablePosition) throws EmptyStackException{
		//index 0 for x, index 1 for y
		double[][] xyValues = new double[2][manager.xImageLength];
		double y = 0;
		for(int i = 0; i < manager.xImageLength; i++){
			y = evaluate(tokenList, variablePosition, (i/manager.xImageIncrement) + manager.Xmin);
			xyValues[0][i] = i;
			//System.out.println("x: " + ((i/manager.xImageIncrement) + manager.Xmin) + " y: " + y);
			xyValues[1][i] = Double.POSITIVE_INFINITY == y ? Double.MIN_EXPONENT
					: Double.NEGATIVE_INFINITY == y ? Double.MAX_EXPONENT : functionToImage(y);
		}
		return xyValues;
	}
	
	public double[] completeParametric(LinkedList<Token> tokenList, Queue<Integer> variablePosition, double min, double max) throws EmptyStackException{
		double[] value = new double[(int) ((Math.abs(min) + Math.abs(max)) / 0.1) + 1];
		double y = 0;
		int counter = 0;
		for(double i = min; i <= max; i+=.1){
			y = evaluate(tokenList, variablePosition, i);
			value[counter++] = Double.POSITIVE_INFINITY == y ? Double.MIN_EXPONENT
					: Double.NEGATIVE_INFINITY == y ? Double.MAX_EXPONENT : y;
		}
		return value;
	}

	private double functionToImage(double input) {
		return input > 0 ?
			manager.yImageOrigin - (manager.yImageIncrement * input) :
		 manager.yImageOrigin + (manager.yImageIncrement * Math.abs(input));
	}
	
	private LinkedList<Token> tokenizer(String s) {
		LinkedList<Token> temp = new LinkedList<Token>();
		for (String section : s.split("#")) {
			temp.add(new Token(section));
		}
		return temp;
	}

	private LinkedList<Token> shuntingYard(LinkedList<Token> tokenList) {
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

	private Double evaluate(LinkedList<Token> s, Queue<Integer> variablePosition, double x) throws EmptyStackException{
		LinkedList<Token> temp = new LinkedList<Token>(s);
		for (Integer j : variablePosition) {
				temp.set(j, new Token(Double.toString(x)));
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
	
	public void createTree(){
		Stack<Node> treeStack = new Stack<Node>();
		LinkedList<Token> temp = new LinkedList<Token>(reversePolish);
		for(Token token : temp){
			treeStack.push(
					token.isNumber()? new Node(token)
				 :  token.isOperator()? new Node(token, treeStack.pop(), treeStack.pop())
				 :  null
					);
		}
		this.expressionTree = treeStack.pop();
	}
}
