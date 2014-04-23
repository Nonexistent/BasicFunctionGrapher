package grapher.tokens;

public enum Symbols {
	// 1 for right, -1 for left
	PLUS(2, -1, "+", true, false) {
		@Override
		public double operator(double b, double a) {
			return a + b;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	}, 
	MINUS(2, -1, "-", true, false) {
		@Override
		public double operator(double b, double a) {
			return a - b;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	}, 
	MULTIPLY(3, -1, "*", true, false) {
		@Override
		public double operator(double b, double a) {
			return a * b;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	}, 
	DIVIDE(3, -1, "/", true, false) {
		@Override
		public double operator(double b, double a) {
			return a / b;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	}, 
	EXP(4, 1, "^", true, false) {
		@Override
		public double operator(double b, double a) {
			return Math.pow(a, b);
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	}, 
	L_BRACKET(1, 0, "(", false, false) {
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	}, 
	R_BRACKET(1, 0, ")", false, false) {
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	},
	ABSOLUTE(0, -1, "abs", false, true) {
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return Math.abs(a);
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	},
	LOG(0, -1, "log", false, true){
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return Math.log10(a);
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	},
	LN(0, -1, "ln", false, true){
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return Math.log(a);
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	},
	SINE(0, -1, "sin", false, true){
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return Math.abs(Math.sin(a)) < 0.05 ? 0.0 : Math.sin(a);
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	},
	COSINE(0, -1, "cos", false, true){
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return Math.abs(Math.cos(a)) < 0.05 ? 0.0 : Math.cos(a);
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	},
	TAN(0 , -1, "tan", false, true){
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			System.out.println("cos: " + Math.cos(a));
			return Math.abs(Math.cos(a)) < 0.01 ? Double.NEGATIVE_INFINITY : Math.abs(Math.sin(a)) < 0.01 ? 0.0 : Math.tan(a);
		}
		@Override
		protected String getValue() {
			return getRepresentation();
		}
	},
	PI(0, 0, "pi", false, false){
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return pi;
		}
	},
	E(0, 0, "e", false, false){
		@Override
		public double operator(double b, double a) {
			return 0;
		}
		@Override
		public double function(double a) {
			return 0;
		}
		@Override
		protected String getValue() {
			return e;
		}
	};

	private final int precedense;
	private final int associativity;
	private final String value;
	private final boolean isOperator;
	private final boolean isFunction;
	private final static String pi = Double.toString(Math.PI);
	private final static String e = Double.toString(Math.E);

	Symbols(int precedense, int associativity, String value, boolean isOperator, boolean isFunction) {
		this.precedense = precedense;
		this.associativity = associativity;
		this.value = value;
		this.isOperator = isOperator;
		this.isFunction = isFunction;
	}

	public int getPrecedense() {
		return this.precedense;
	}

	public int getAssociativity() {
		return this.associativity;
	}
	
	public String getRepresentation() {
		return this.value;
	}
	
	public boolean isOperator(){
		return this.isOperator;
	}
	
	public boolean isBracket(){
		return value.equals("(") || value.equals(")");
	}
	
	public boolean isFunction(){
		return this.isFunction;
	}
	
	public abstract double operator(double b, double a);
	
	public abstract double function(double a);
	
	protected abstract String getValue();
	
	@Override
	public String toString(){
		return getValue();
	}
}
