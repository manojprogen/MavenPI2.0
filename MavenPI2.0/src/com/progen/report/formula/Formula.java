/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.formula;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author progen
 */
public class Formula {

    private String formulaExpr;
    private Stack<String> exprStack;
    private Set<String> rhsMeasures = new HashSet<String>();

    public Formula(String formulaExpr) {
        this.formulaExpr = formulaExpr;
        this.exprStack = this.convertToPostFix(formulaExpr);
        this.rhsMeasures = this.rhsMeasures;
    }

    public void updateFormula(String formulaExpr) {
        this.formulaExpr = formulaExpr;
        this.exprStack = this.convertToPostFix(formulaExpr);
    }

    private Stack<String> convertToPostFix(String formula) {
        Stack<String> tempStack = new Stack<String>();
        Stack<String> exprStack = new Stack<String>();

        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '*' || c == '/' || c == '+' || c == '-' || c == '(' || c == ')') {
                if (!tempStack.empty()) {
                    if (operatorPriority(tempStack.peek()) < operatorPriority(Character.toString(c))) {
                        tempStack.push(Character.toString(c));
                    } else if (operatorPriority(tempStack.peek()) >= operatorPriority(Character.toString(c))) {
                        if (c != ('(') && c != (')')) {
                            exprStack.push(tempStack.pop());
                            tempStack.push(Character.toString(c));
                        } else if (c == ('(')) {
                            tempStack.push(Character.toString(c));
                        } else if (c == (')')) {
                            for (int bindex = 0; bindex < tempStack.size(); bindex++) {
                                if (tempStack.peek().equalsIgnoreCase("(")) {
                                    tempStack.pop();
                                    break;
                                } else {
                                    exprStack.push(tempStack.pop());
                                }
                            }
                        }


                    }
                } else {
                    tempStack.push(Character.toString(c));
                }

            } else {
                StringBuffer singleExpression = new StringBuffer();
                for (int j = i; j < formula.length(); j++) {
                    char ck = formula.charAt(j);
                    if (ck != '*' && ck != '/' && ck != '+' && ck != '-' && ck != '(' && ck != ')') {
                        singleExpression.append(ck);
                        i++;
                    } else {
                        break;
                    }

                }
                exprStack.push(singleExpression.toString());
                rhsMeasures.add(singleExpression.toString());
                i--;
            }
        }
        while (!tempStack.empty()) {
            if (!tempStack.peek().equalsIgnoreCase("(") && !tempStack.peek().equalsIgnoreCase(")")) {
                exprStack.push(tempStack.pop());
            } else {
                tempStack.pop();
            }

        }
        return exprStack;
    }

    private int operatorPriority(String x) {
        if (x.equals("+") || x.equals("-")) {
            return 1;
        } else if (x.equals("*") || x.equals("/")) {
            return 2;
        } else if (x.equals(")")) {
            return -1;
        } else {
            return 0;
        }
    }

    public Set<String> getFormulaMeasureSet() {
        return this.rhsMeasures;
    }

    public Stack<String> getExprStack() {
        return (Stack<String>) exprStack.clone();
    }

    /**
     * @return the formulaExpr
     */
    public String getFormulaExpr() {
        return formulaExpr;
    }
}
