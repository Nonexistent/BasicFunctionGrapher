package grapher.tokens;

public class Token{
		private String value;
		private int precedense;
		private int associativity;
		private boolean isOperator;
		private boolean isBracket;
		
		public Token(String s){
			setup(s);
		}
		
		private void setup(String s){
			for(Symbols o : Symbols.values()){
				if(s.equals(o.getValue())){
					this.value = o.getValue();
					this.precedense = o.getPrecedense();
					this.associativity = o.getAssociativity();
					this.isOperator = o.isOperator();
					this.isBracket = o.isBracket();
					return;
				}
			}
			this.value = s;
			this.isOperator = false;
			this.isBracket = false;
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
		
		public void setValue(String s){
			this.value = s;
		}
		
		public boolean isOperator(){
			return this.isOperator;
		}
		
		public boolean isNumber(){
			return !this.isOperator && !this.isBracket;
		}
		
		public boolean isLeftBracket(){
			return this.value.equals("(");
		}
		
		public boolean isRightBracket(){
			return this.value.equals(")");
		}
		
		public boolean isMinusSign(){
			return this.value.equals("-");
		}
		
		public boolean isVariable(){
			return this.value.equals("x");
		}
}
