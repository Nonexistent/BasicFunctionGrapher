package grapher;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Function {
	private FunctionManager functionManager;
	private LinkedList<String> expressionToken;
	private LinkedHashMap<Double, Double> yMap;
	//private String name; //f1, f2, f3.....etc
	
	public Function(String s, FunctionManager functionManager){
		this.functionManager = functionManager;
		this.expressionToken = this.functionManager.tokenizer(s);
		checkForNegative();
		this.yMap = this.functionManager.completeMap(expressionToken);
	}
	
	private boolean isOperator(String c){
		return c.equals("+") || c.equals("-") || c.equals("*")|| c.equals("/");
	}

	private void checkForNegative() {
		int negativeCount = 0;
		for (int i = 0; i < expressionToken.size(); i++) {
			if (expressionToken.get(i).equals("-")) {
				if (i == 0 || isOperator(expressionToken.get(i - 1))) {
					negativeCount++;
				}
			}
		}
		for (int j = 0; j < negativeCount; j++) {
			for (int i = 0; i < expressionToken.size(); i++) {
				if (expressionToken.get(i).equals("-")) {
					if(i == 0 && expressionToken.get(i + 1).equals("(")){
						expressionToken.add(i + 1, "*");
						expressionToken.add(i + 1, ")");
						expressionToken.add(i + 1, "1");
						expressionToken.add(i, "0");
						expressionToken.add(i, "(");
						break;
					}
					else if ((i == 0 && !expressionToken.get(i + 1).equals("(")) || isOperator(expressionToken.get(i - 1))) {
						expressionToken.add(i + 2, ")");
						expressionToken.add(i, "0");
						expressionToken.add(i, "(");
						break;
					}
				}
			}
		}
	}
	
	public LinkedHashMap<Double, Double> getMap(){
		return this.yMap;
	}
	
}
