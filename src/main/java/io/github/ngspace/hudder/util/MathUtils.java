package io.github.ngspace.hudder.util;

import java.util.HashMap;

public class MathUtils {
	private MathUtils() {}
	static HashMap<String, Double> expressionCache = new HashMap<String,Double>();
	public static double eval(final String str) {
		Double has = expressionCache.get(str);
		if (has!=null) return has;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c!='0'&&c!='1'&&c!='2'&&c!='3'&&c!='4'&&c!='5'&&c!='6'&&c!='7'&&c!='8'&&c!='9'&&c!='.'
					&&c!='+'&&c!='-'&&c!='/'&&c!='*'&&c!='^'&&c!='%'&&c!='('&&c!=')')
				throw new RuntimeException();
		}
		double res = new Object() {
	        int pos = -1;
	        int ch;
	        
	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }
	        
	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }
	        
	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }
	        
	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)` | number
	        //        | functionName `(` expression `)` | functionName factor
	        //        | factor `^` factor
	        
	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }
	        
	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else if (eat('%')) x %= parseFactor(); // division
	                else return x;
	            }
	        }
	        
	        double parseFactor() {
	            if (eat('+')) return +parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus
	            
	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                if (!eat(')')) throw new RuntimeException("Missing ')'");
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                if (eat('(')) {
	                    x = parseExpression();
	                    if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
	                } else {
	                    x = parseFactor();
	                }
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else throw new RuntimeException("Unknown function: " + func);
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }
	            
	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
	            
	            return x;
	        }
	    }.parse();
	    expressionCache.put(str, res);
	    return res;
	}

	public static double tryParse(Object text) {
		if (text instanceof Double) return (double) text;
		if (text instanceof Integer) return (int) text;
		if (text instanceof Float) return (float) text;
		if (text instanceof Long) return (long) text;
		try {
			return Double.parseDouble(String.valueOf(text));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static int tryParseInt(Object text) {
		if (text instanceof Double) return (int) (double) text;
		if (text instanceof Integer) return (int) text;
		if (text instanceof Float) return (int) (float) text;
		if (text instanceof Long) return (int) (long) text;
		try {
			return Integer.parseInt(String.valueOf(text));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
