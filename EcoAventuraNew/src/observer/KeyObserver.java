/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package observer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Alexandru
 */
public class KeyObserver extends KeyAdapter{
    public static final int SHORT_LENGTH = 34;
    public static final int STANDARD_LENGHT = 45;
    public static final int HUGE_LENGTH = 140;
    private final int MAX_LENGTH;
    private final JTextComponent observedComponent; 
    private final String restrictedChars;
    
    public KeyObserver (int maxLength, JTextComponent observedComponent) {
        this.MAX_LENGTH = maxLength;
        this.observedComponent = observedComponent;
        this.restrictedChars = "";
    }
    
    public KeyObserver (int maxLength, JTextComponent observed, String restrictedChars) {
        this.MAX_LENGTH = maxLength;
        this.observedComponent = observed;
        this.restrictedChars = restrictedChars;
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
        super.keyTyped(ke); 
        if (observedComponent.getText().length() >= MAX_LENGTH) {
            try {
                observedComponent.setText(observedComponent.getText(0, MAX_LENGTH-1));
                ke.consume();
            } catch (BadLocationException ex) {
                observedComponent.setText("");
            }
            JOptionPane.showMessageDialog(observedComponent, "Limita de " + MAX_LENGTH + " caractere a fost depasita",
                    "Limita depasita", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (restrictedChars.contains("" + ke.getKeyChar())) {
            observedComponent.setText(observedComponent.getText().replace(ke.getKeyChar() + "", ""));
            ke.consume();
            JOptionPane.showMessageDialog(observedComponent, "Caracterele " + restrictedChars + " nu sunt admise", "Format gresit", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    
}
