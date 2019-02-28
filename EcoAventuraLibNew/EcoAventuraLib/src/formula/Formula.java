/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula;

import java.util.HashMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author Alexandru
 */
public class Formula implements java.io.Serializable{
    private String formula;
    private HashMap<FormulaElement, String> variables;
    
    public Formula (HashMap<FormulaElement, String> variables, String formula) {
        this.variables = variables;
        this.formula = formula;
    }
    
    public Formula (Formula f) throws Exception {
        if (f == null) throw new Exception("Null value");
        
        this.variables = new HashMap<>(f.getVariables());
        this.formula = new String(f.getFormula());
    }

    public String getFormula() {
        return formula;
    }

    public HashMap<FormulaElement, String> getVariables() {
        return variables;
    }
    
    public double evaluate () throws Exception {
        if (formula == null || variables == null) {
            Exception e = new Exception("Formula sau variabilele nu trebuie sa fie nule");
            throw e;            
        }
        
        try {
            StringBuilder sb = new StringBuilder(formula);

            for (FormulaElement fe : variables.keySet()) {
                String s = variables.get(fe); // s este numele lui fe in formula
                int index = sb.indexOf(s);
                sb.replace(index, index + s.length(), fe.getValue()+""); //inlocuim variabila cu valuarea acesteia
            }
            
            ScriptEngineManager sem = new ScriptEngineManager();
            ScriptEngine se = sem.getEngineByName("JavaScript");
            Object result = se.eval(new String(sb)); // cream un motor de evaluare a expresiilor aritmetice
            
            if (result instanceof Integer) {
                return ((Integer) result).doubleValue();                
            } else if (result instanceof Double) {
                return (Double) result;
            } else if (result instanceof Float) {
                return ((Float) result).doubleValue();
            } else if (result instanceof Long) {
                return ((Long) result).doubleValue();
            } else if (result instanceof Short) {
                return ((Short) result).doubleValue();
            } 
            return 0;
        } catch (Exception e) {
            throw e;            
        }
    }
}
