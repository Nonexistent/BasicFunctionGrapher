package grapher.derivative;

import grapher.tokens.Token;

public class Node {
	private Token token;
	private Node A;
	private Node B;
	private static String lB = "(";
	private static String rB = ")";

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
			return "1";
		} else if (token.isNumber()) {
			return "0";
		} else if (value.equals("+")) {
			du = A.derive();
			dv = B.derive();
			return "(" + du + value + dv + ")";
		} else if (value.equals("-")) {
			du = A.derive();
			dv = B.derive();
			return "(" + du + value + dv + ")";
		} else if (value.equals("*")) {
			u = A.getExpression();
			du = A.derive();
			v = B.getExpression();
			dv = B.derive();
			return lB + u + value + dv + rB + "+" + lB + v + value + du + rB;
		}
		return "";
	}

	public String getExpression() {
		if (A != null && B != null) {
			return lB + A.getExpression() + token.getValue()
					+ B.getExpression() + rB;
		}
		return token.getValue();
	}
}
