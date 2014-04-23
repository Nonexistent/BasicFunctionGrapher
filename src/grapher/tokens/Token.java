package grapher.tokens;

public class Token{
		private String value;
		private int precedense;
		private int associativity;
		private boolean isOperator;
		private boolean isBracket;
		private boolean isFunction;
		private Symbols symbols;
		
		public Token(String s){
			setup(s);
		}
		
		private void setup(String s){
			for(Symbols o : Symbols.values()){
				if(s.equals(o.getRepresentation())){
					this.symbols = o;
					this.value = o.toString();
					this.precedense = o.getPrecedense();
					this.associativity = o.getAssociativity();
					this.isOperator = o.isOperator();
					this.isBracket = o.isBracket();
					this.isFunction = o.isFunction();
					return;
				}
			}
			this.value = s;
			this.isOperator = false;
			this.isBracket = false;
			this.isFunction = false;
		}
		
		public Symbols getSymbol(){
			return this.symbols;
		}
		
		public int getPrecedense() {
			return this.precedense;
		}

		public int getAssociativity() {
			return this.associativity;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public boolean isOperator(){
			return this.isOperator;
		}
		
		public boolean isNumber(){
			return !this.isOperator && !this.isBracket && !isFunction;
		}
		
		public boolean isLeftBracket(){
			return this.value.equals("(");
		}
		
		public boolean isRightBracket(){
			return this.value.equals(")");
		}
		
		public boolean isVariable(){
			return this.value.matches("[a-z&&[^e]]");
		}
		
		public boolean isFunction(){
			return this.isFunction;
		}
}
