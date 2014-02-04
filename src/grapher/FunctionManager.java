package grapher;

import grapher.tokens.Token;

import java.util.LinkedList;
import java.util.Stack;

public class FunctionManager {
	private Graph graph;
	private double xImageIncrement;
	private double Xmin;
	private int xImageLength;

	public FunctionManager(Graph graph) {
		this.graph = graph;
		this.xImageIncrement = graph.getxImageIncrement();
		this.Xmin = graph.getXmin();
		this.xImageLength = graph.getxImageLength();
	}

	public double[][] completeXYValues(LinkedList<Token> tokenList){
		//index 0 for x, index 1 for y
		double[][] xyValues = new double[2][xImageLength];
		for(int i = 0; i < xImageLength; i++){
			double y = evaluate(tokenList, (i/xImageIncrement) + Xmin);
			xyValues[0][i] = i;
			//System.out.println("x: " + ((i/xImageIncrement) + Xmin) + " y: " + y);
			xyValues[1][i] = Double.POSITIVE_INFINITY == y ? Double.MIN_EXPONENT
					: Double.NEGATIVE_INFINITY == y ? Double.MAX_EXPONENT : graph.functionToImage(y);
		}
		return xyValues;
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
	
	public void updateValues(){
		this.Xmin = graph.getXmin();
		this.xImageIncrement = graph.getxImageIncrement();
	}
}
