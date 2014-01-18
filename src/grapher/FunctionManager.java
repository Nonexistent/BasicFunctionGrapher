package grapher;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Stack;

public class FunctionManager {
	private Graph graph;
	private double topIncre;
	private double bottomIncre;
	private double rightIncre;
	private double leftIncre;
	private int Xmax;
	private int Xmin;
	private LinkedHashMap<Double, Double> yMap;
	
		public FunctionManager(Graph graph){
			this.graph = graph;
			this.topIncre = graph.getTop();
			this.bottomIncre = graph.getBottom();
			this.leftIncre = graph.getLeft();
			this.rightIncre = graph.getRight();
			this.Xmax = graph.getXmax();
			this.Xmin = graph.getXmin();
			this.yMap = graph.getMap();
		}
		
		private enum operators{
			//1 for right, -1 for left
			PLUS(2, -1, "+"),
			MINUS(2, -1, "-"),
			MULTIPLY(3, -1, "*"),
			DIVIDE(3, -1, "/"),
			EXP(4, 1, "^"),
			L_BRACKET(1, 0, "("),
			R_BRACKET(1, 0, ")");
			
			private final int prec;
			private final int asso;
			private final String ch;
			
			operators(int precedense, int associativity, String c){
				this.prec = precedense;
				this.asso = associativity;
				this.ch = c;
			}
			
			public int getPrecedense(){
				return this.prec;
			}
			public int getAssociativity(){
				return this.asso;
			}
			public String getCharacter(){
				return this.ch;
			}
		}
		
		public void calculate(String s){
			if(s.equals("")){
				return;
			}
			final LinkedList<String> h = shuntingYard(tokenizer(s));
			double i = 0;
			for(int x = Xmin; x < Xmax + 1; x++){
				double y = evaluate(h, x);
				System.out.println("x: " +x + " y: "+ y);
				if(y != Double.POSITIVE_INFINITY && y != Double.NEGATIVE_INFINITY){
				this.yMap.put(i, graph.functionToImage(y));
				}else{
					verticalAsymptote(i, x, h);
				}
				i = i + rightIncre;
			}
		}
		
		private void verticalAsymptote(double i, double x, LinkedList<String> h){
			Stack<Double> xTemp = new Stack<Double>(), yTemp = new Stack<Double>();
			double increment = 0.2 * rightIncre, fromLeft = i - rightIncre, fromRight = i + rightIncre;
			for(double j = x - 1; j < x; j = j + 0.2){
				double y = evaluate(h, j);
				System.out.println("from left: x: "+j+" y: "+y);
				this.yMap.put(fromLeft, graph.functionToImage(y));
				fromLeft = fromLeft + increment;
			}
			for(double j = x + 1; j > x; j = j - 0.2){
				double y = evaluate(h, j);
				System.out.println("from right: x: "+j+" y: "+y);
				xTemp.push(fromRight); yTemp.push(graph.functionToImage(y));
				fromRight = fromRight - increment;
			}
			this.yMap.put(i, Double.MAX_VALUE);
			for(int k = 0; k < xTemp.size(); k++){
				this.yMap.put(xTemp.pop(), yTemp.pop());
			}
		}
		
		private LinkedList<String> tokenizer(String s){
			LinkedList<String> done = new LinkedList<String>();
			LinkedList<Character> temp = new LinkedList<Character>();
			for(Character c : s.toCharArray()){
				if(Character.isDigit(c) || c == 'x'){
					temp.add(c);
				}else{
					if(!temp.isEmpty()){
						char[] t = new char[temp.size()];
						for(int i = 0; i < temp.size(); i++){
							t[i] = temp.get(i);
						}
						done.add(new String(t));
						temp.clear();
						done.add(Character.toString(c));
					}else{
						done.add(Character.toString(c));
					}
				}
			}
			if(!temp.isEmpty()){
				char[] t = new char[temp.size()];
				for(int i = 0; i < temp.size(); i++){
					t[i] = temp.get(i);
				}
				done.add(new String(t));
			}
			return done;
		}
		
		private LinkedList<String> shuntingYard(LinkedList<String> s){
			Stack<operators> stack = new Stack<operators>();
			LinkedList<String> output = new LinkedList<String>();
			for(String c : s){
				if(Character.isDigit(c.charAt(0)) || c.equals("x")){
					output.add(c);
				}else{
					operators temp = convert(c.charAt(0));
					boolean isLeft = temp.getAssociativity() == -1;
					if(temp.getPrecedense() == 2 || temp.getPrecedense() == 3 || temp.getPrecedense() == 4){
						if(!stack.isEmpty()){
							while(temp.getPrecedense() <= stack.peek().getPrecedense() && isLeft){
								output.add(stack.pop().getCharacter());
							}
							stack.push(temp);
						}else{
							stack.push(temp);
						}
					}else{
						if(temp.equals(operators.L_BRACKET)){
							stack.push(temp);
						}else{
							if(temp.equals(operators.R_BRACKET)){
								while(stack.peek() != operators.L_BRACKET){
									output.add(stack.pop().getCharacter());
								}
								stack.pop();
							}
						}
					}
				}
			}
			while(!stack.isEmpty()){
				output.add(stack.pop().getCharacter());
			}
			return output;
		}

		private operators convert(Character c){
			if(c == '+'){
				return operators.PLUS;
			}else if(c == '-'){
				return operators.MINUS;
			}else if(c == '*'){
				return operators.MULTIPLY;
			}else if(c == '/'){
				return operators.DIVIDE;
			}else if(c == ')'){
				return operators.R_BRACKET;
			}else if(c == '('){
				return operators.L_BRACKET;
			}else if(c == '^'){
				return operators.EXP;
			}else return null;
		}
		
		private Double evaluate(LinkedList<String> s, double x){
			LinkedList<String> temp = new LinkedList<String>(s);
			for(String j : temp){
				int index = temp.indexOf(j);
				if(j.equals("x")){
					temp.set(index, Double.toString(x));
				}
			}
			double a = 0;
			double b = 0;
			while(temp.size() != 1){
			for(String f : temp){
				if(!f.matches(".*\\d.*")){
					a = Double.parseDouble(temp.remove(temp.indexOf(f) - 2));
					b = Double.parseDouble(temp.remove(temp.indexOf(f) - 1));
					temp.set(temp.indexOf(f), Double.toString(determine(a, b, f)));
					break;
				}
			}
			}
			return Double.parseDouble(temp.getFirst());
		}
		
		private double determine(double a, double b, String s){
			if(s.equals("+")){
				return a + b;
			}else if(s.equals("-")){
				return a - b;
			}else if(s.equals("*")){
				return a * b;
			}else if(s.equals("/")){
				return a / b;
			}else if(s.equals("^")){
				return Math.pow(a, b);
			}else return -1;
		}
		
}
