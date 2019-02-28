/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Alexandru
 */
public class Pair implements java.io.Serializable{
    private Integer firstValue; 
    private Integer secondValue;

    public Pair(Integer firstValue, Integer secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public Integer getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(Integer firstValue) {
        this.firstValue = firstValue;
    }

    public Integer getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(Integer secondValue) {
        this.secondValue = secondValue;
    }

    @Override
    public String toString () {
        return "(" + firstValue + ", " + secondValue + ")";
    }
    
    
}
