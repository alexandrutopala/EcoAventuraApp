/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package costumSerialization;

import formula.FormulaElement;

/**
 *
 * @author Marius
 */
public class SerializableFormulaLabel implements java.io.Serializable, SerializableLabel {
    private static final long serialVersionUID = 1L;
    private String labelText;
    private FormulaElement fe;
    private String toolTip;
    
    public SerializableFormulaLabel () {}

    public SerializableFormulaLabel(String labelText, FormulaElement fe, String toolTip) {
        this.labelText = labelText;
        this.fe = fe;
        this.toolTip = toolTip;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public FormulaElement getFe() {
        return fe;
    }

    public void setFe(FormulaElement fe) {
        this.fe = fe;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
    
    @Override
    public String toString () {
        return toolTip;
    }
}
