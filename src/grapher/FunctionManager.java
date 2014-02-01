package grapher;

import grapher.tokens.Token;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Stack;

public class FunctionManager {
	private Graph graph;
	private double rightIncre;
	private double Xmax;
	private double Xmin;
	private DecimalFormat rounding = new DecimalFormat("#.#");

	public FunctionManager(Graph graph) {
		this.graph = graph;
		this.rightIncre = graph.getRight();
		this.Xmax = graph.getXmax();
		this.Xmin = graph.getXmin();
	}

	public LinkedHashMap<Double, Double> completeMap(LinkedList<Token> h) {
		LinkedHashMap<Double, Double> map = new LinkedHashMap<Double, Double>();
		double i = 0;
		double precision = 0.1;
		for (double x = Xmin; x < Xmax + 1; x = x + precision) {
			x = Double.parseDouble(rounding.format(x));
			double y = evaluate(h, x);
			if (y != Double.POSITIVE_INFINITY && y != Double.NEGATIVE_INFINITY) {
				map.put(i, graph.functionToImage(y));
				System.out.println("x: "+ x + " " + "y: " + y);
			} else {
				verticalAsymptote(i, x, h, map);
			}
			i = i + rightIncre * precision;
		}
		LinkedList<Double> temp = new LinkedList<Double>(map.keySet());
		LinkedHashMap<Double, Double> newMap = new LinkedHashMap<Double, Double>();
		Collections.sort(temp);
		for (Double j : temp) {
			newMap.put(j, map.get(j));
		}
		return newMap;
	}

	private void verticalAsymptote(double i, double x, LinkedList<Token> h, LinkedHashMap<Double, Double> map) {
		DecimalFormat r = new DecimalFormat("#.##");
		Stack<Double> xTemp = new Stack<Double>(), yTemp = new Stack<Double>();
		double rate = 0.05;
		double increment = rate * rightIncre, fromLeft = i - rightIncre, fromRight = i + rightIncre;
		for (double j = x - 1; j < x; j = j + rate) {
			double y = evaluate(h, Double.parseDouble(r.format(j)));
			map.put(fromLeft, graph.functionToImage(y));
			System.out.println("From Left - x: " + j + " " + "y: " + y);
			fromLeft = fromLeft + increment;
		}
		for (double j = x + 1; j > x; j = j - rate) {
			double y = evaluate(h, Double.parseDouble(r.format(j)));
			xTemp.push(fromRight);
			yTemp.push(graph.functionToImage(y));
			System.out.println("From Right - x: " + j + " " + "y: " + y);
			fromRight = fromRight - increment;
		}
		map.put(i, Double.MAX_VALUE);
		for (int k = 0; k < xTemp.size(); k++) {
			map.put(xTemp.pop(), yTemp.pop());
		}
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
