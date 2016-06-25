/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.formula;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author progen
 */
public class FormulaEvaluator {

    public ArrayList<BigDecimal> evaluateFormulaForDataSet(Formula formula, Set<MeasureDataSet> measDataLst, int rowCount) {
        ArrayList<BigDecimal> resultArrayList = new ArrayList();
        BigDecimal result;
        HashMap expDataMap;
        for (int i = 0; i < rowCount; i++) {
            //Iterable<MeasureDataSet> filter = Iterables.filter(measDataLst, MeasureDataSet.getMeasureDataSetPredicate(""));
            expDataMap = new HashMap<String, BigDecimal>();
            for (MeasureDataSet measData : measDataLst) {
                expDataMap.put(measData.getMeasure(), measData.getData(i));
            }

            result = this.evaluateFormula(formula, expDataMap);

            resultArrayList.add(result);
        }
        return resultArrayList;
    }

    private BigDecimal evaluateFormula(Formula formula, HashMap<String, BigDecimal> dataSet) {
        BigDecimal resultBigDecimal;

        Stack<String> formulaStack = formula.getExprStack();
        Stack<String> operatorStack = new Stack<String>();
        Stack<String> operandStack = new Stack<String>();
        Stack<String> previousValue = new Stack<String>();
//        
//        

        while (!formulaStack.empty()) {
            if (!formulaStack.peek().equalsIgnoreCase("*")
                    && !formulaStack.peek().equalsIgnoreCase("/")
                    && !formulaStack.peek().equalsIgnoreCase("+")
                    && !formulaStack.peek().equalsIgnoreCase("-")) {
                if (!previousValue.empty()) {
                    if (!previousValue.peek().equalsIgnoreCase("*")
                            && !previousValue.peek().equalsIgnoreCase("/")
                            && !previousValue.peek().equalsIgnoreCase("+")
                            && !previousValue.peek().equalsIgnoreCase("-")) {
                        if (!operatorStack.empty()) {
                            if (operatorStack.peek().equalsIgnoreCase("*")) {
                                double calcVal = calculateExpressions(dataSet, formulaStack.pop().toString()) * calculateExpressions(dataSet, previousValue.pop().toString());
                                //double calcVal = Double.parseDouble(dataSet.get(previousValue.pop()).toString()) * Double.parseDouble(dataSet.get(formulaStack.pop()).toString());
                                operandStack.push(Double.toString(calcVal));
                                operatorStack.pop();
                            } else if (operatorStack.peek().equalsIgnoreCase("/")) {
                                double calcVal = calculateExpressions(dataSet, formulaStack.pop().toString()) / calculateExpressions(dataSet, previousValue.pop().toString());
                                operandStack.push(Double.toString(calcVal));
                                operatorStack.pop();
                            } else if (operatorStack.peek().equalsIgnoreCase("+")) {
                                double calcVal = calculateExpressions(dataSet, formulaStack.pop().toString()) + calculateExpressions(dataSet, previousValue.pop().toString());
                                operandStack.push(Double.toString(calcVal));
                                operatorStack.pop();
                            } else if (operatorStack.peek().equalsIgnoreCase("-")) {
                                double calcVal = calculateExpressions(dataSet, formulaStack.pop().toString()) - calculateExpressions(dataSet, previousValue.pop().toString());
                                operandStack.push(Double.toString(calcVal));
                                operatorStack.pop();
                            }
                        } else {
                            operandStack.push(previousValue.pop());
                            previousValue.push(formulaStack.pop());

                        }
                    } else {
                        operatorStack.push(previousValue.pop());
                        previousValue.push(formulaStack.pop());
                    }
                } else {
                    previousValue.push(formulaStack.pop());
                }


            } else {
                if (!previousValue.empty()) {
                    if (!previousValue.peek().equalsIgnoreCase("*")
                            && !previousValue.peek().equalsIgnoreCase("/")
                            && !previousValue.peek().equalsIgnoreCase("+")
                            && !previousValue.peek().equalsIgnoreCase("-")) {
                        operandStack.push(previousValue.pop());
                        previousValue.push(formulaStack.pop());

                    } else {
                        operatorStack.push(previousValue.pop());
                        previousValue.push(formulaStack.pop());
                    }
                } else {
                    previousValue.push(formulaStack.pop());
                }
            }

        }
        if (!previousValue.empty()) {
            if (!previousValue.peek().equalsIgnoreCase("*")
                    && !previousValue.peek().equalsIgnoreCase("/")
                    && !previousValue.peek().equalsIgnoreCase("+")
                    && !previousValue.peek().equalsIgnoreCase("-")) {
                operandStack.push(previousValue.pop());
            } else {
                operatorStack.push(previousValue.pop());
            }
        }

        while (!operatorStack.empty()) {
            if (operatorStack.peek().equalsIgnoreCase("*")) {
                double calcVal = calculateExpressions(dataSet, operandStack.pop().toString()) * calculateExpressions(dataSet, operandStack.pop().toString());
                operandStack.push(Double.toString(calcVal));
                operatorStack.pop();
            } else if (operatorStack.peek().equalsIgnoreCase("/")) {
                double calcVal = calculateExpressions(dataSet, operandStack.pop().toString()) / calculateExpressions(dataSet, operandStack.pop().toString());
                operandStack.push(Double.toString(calcVal));
                operatorStack.pop();
            } else if (operatorStack.peek().equalsIgnoreCase("+")) {
                double calcVal = calculateExpressions(dataSet, operandStack.pop().toString()) + calculateExpressions(dataSet, operandStack.pop().toString());
                operandStack.push(Double.toString(calcVal));
                operatorStack.pop();
            } else if (operatorStack.peek().equalsIgnoreCase("-")) {
                double calcVal = calculateExpressions(dataSet, operandStack.pop().toString()) - calculateExpressions(dataSet, operandStack.pop().toString());
                operandStack.push(Double.toString(calcVal));
                operatorStack.pop();
            }

        }

//        

        resultBigDecimal = new BigDecimal(operandStack.pop().toString());

        return resultBigDecimal;
    }

    public Double calculateExpressions(HashMap dataset, String valueB) {
        double result = 0;
        try {
            result = Double.parseDouble(valueB);

        } catch (NumberFormatException e) {
            result = Double.parseDouble(dataset.get(valueB).toString());
        }


        return result;
    }
}
