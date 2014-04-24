package grapher.derivative;

import grapher.tokens.Symbols;
import grapher.tokens.Token;

public class Node {
	private Token token;
	private Node A;
	private Node B;
	private static final String lB = "(";
	private static final String rB = ")";
	private static final String zero = "0";
	private static final String one = "1";
	private static final String plus = Symbols.PLUS.getRepresentation();
	private static final String minus = Symbols.MINUS.getRepresentation();
	private static final String times = Symbols.MULTIPLY.getRepresentation();
	private static final String divide = Symbols.DIVIDE.getRepresentation();
	private static final String power = Symbols.EXP.getRepresentation();

	public Node(Token token) {
		this.token = token;
	}

	public Node(Token token, Node B, Node A) {
		this.token = token;
		this.B = B;
		this.A = A;
	}

	public String derive() {
		String u, du, v, dv;
		String value = token.getValue();
		if (token.isVariable()) {
			return one;
		} else if (token.isNumber()) {
			return zero;
		} else if (value.equals(plus)) {
			du = A.derive();
			dv = B.derive();
			return lB + du + value + dv + rB;
		} else if (value.equals(minus)) {
			du = A.derive();
			dv = B.derive();
			return lB + du + value + dv + rB;
		} else if (value.equals(times)) {
			u = A.getExpression();
			du = A.derive();
			v = B.getExpression();
			dv = B.derive();
			return lB + lB + u + value + dv + rB + "+" + lB + v + value + du + rB + rB;
		}else if(value.equals(divide)){
			u = A.getExpression();
			du = A.derive();
			v = B.getExpression();
			dv = B.derive();
			return lB + lB + lB +v + "*" + du + rB + "-" + lB + u + "*" + dv + rB + rB + value + lB + v + "^2" + rB + rB;
		}else if(value.equals(power)){
			//power is not a function of variable
			u = A.getExpression();
			du = A.derive();
			v = B.getExpression();
			return lB + v + "*" + u + value + lB + v + "-1" + rB + "*" + du + rB;
		}
		return "";
	}
	
	public String simplify() {
		String a, b;
		String value = token.getValue();
		if (A != null && B != null) {
			a = A.simplify();
			b = B.simplify();
			if (value.equals(plus)) {
				if (!a.equals(zero) && b.equals(zero)) {
					return a;
				} else if (a.equals(zero) && !b.equals(zero)) {
					return b;
				} else if (a.equals(zero) && b.equals(zero)) {
					return zero;
				}
			} else if (value.equals(minus)) {
				if (!a.equals(zero) && b.equals(zero)) {
					return a;
				} else if (a.equals(b)) {
					return zero;
				}
			} else if (value.equals(times)) {
				if (a.equals(zero) || b.equals(zero)) {
					return zero;
				} else if (!a.equals(one) && b.equals(one)) {
					return a;
				} else if (a.equals(one) && !b.equals(one)) {
					return b;
				}
			} else if (value.equals(divide)) {
				if (a.equals(zero) && !b.equals(zero)) {
					return zero;
				} else if ((!a.equals(zero) && !b.equals(zero)) && a.equals(b)) {
					return one;
				}
			} else if (value.equals(power)) {
				if (b.equals(zero)) {
					return one;
				} else if (b.equals(one)) {
					return b;
				}
			}
			return lB + a + value + b + rB;
		}
		return token.getValue();
	}

	private String getExpression() {
		if (A != null && B != null) {
			return lB + A.getExpression() + token.getValue() + B.getExpression() + rB;
		}
		return token.getValue();
	}
}
