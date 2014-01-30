package grapher;

import grapher.tokens.Token;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
	private FunctionManager functionManager;
	private LinkedList<Token> expressionToken;
	private LinkedList<Token> shuntingYardForm;
	private LinkedHashMap<Double, Double> yMap;
	//private String name; //f1, f2, f3.....etc
	
	public Function(String s, FunctionManager functionManager){
		this.functionManager = functionManager;
		//edits syntax to algo readable form
		s = checkForMultiply(s);
		s = insertHash(s);
		System.out.println(s);
		this.expressionToken = this.functionManager.tokenizer(s);
		checkForNegative();
		this.shuntingYardForm = this.functionManager.shuntingYard(expressionToken);
		this.yMap = this.functionManager.completeMap(shuntingYardForm);
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
	
	public LinkedHashMap<Double, Double> getMap(){
		return this.yMap;
	}
	
}
