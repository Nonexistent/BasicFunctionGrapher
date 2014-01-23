package grapher.tokens;

public enum Symbols {
	// 1 for right, -1 for left
	PLUS(2, -1, "+", true), 
	MINUS(2, -1, "-", true), 
	MULTIPLY(3, -1, "*", true), 
	DIVIDE(3, -1, "/", true), 
	EXP(4, 1, "^", true), 
	L_BRACKET(1, 0, "(", false), 
	R_BRACKET(1, 0, ")", false);

	private final int precedense;
	private final int associativity;
	private final String value;
	private final boolean isOperator;

	Symbols(int precedense, int associativity, String value, boolean isOperator) {
		this.precedense = precedense;
		this.associativity = associativity;
		this.value = value;
		this.isOperator = isOperator;
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
	
	public boolean isBracket(){
		return !this.isOperator;
	}
}
