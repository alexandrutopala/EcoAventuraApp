/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumSerialization;

/**
 *
 * @author Marius
 */
public class SerializableArithmeticLabel implements java.io.Serializable, SerializableLabel{
    private static final long serialVersionUID = 1L;
    private String operator;
    
    public SerializableArithmeticLabel () {}
    
    public SerializableArithmeticLabel (String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    @Override
    public String toString () {
        return operator;
    }
}
