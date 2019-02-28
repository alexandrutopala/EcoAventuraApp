/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 *
 * @author Alexandru
 */
public class PulseThread extends Thread {
    private static final int WAIT = 10;
    private static final int FadeSteps = 60;
    private JComponent component;
    private Color colour = new Color(255, 0, 0);
    private Color oldColour;
    private final Color NEW_COLOR = new Color(255, 0, 0);
    private final Color ORIGINAL_COLOR;
    private static boolean running;

    public PulseThread (JComponent component) {
        this.component = component;
        oldColour = component.getBackground();
        ORIGINAL_COLOR = new Color(oldColour.getRed(), oldColour.getGreen(), oldColour.getBlue(), oldColour.getAlpha());
    }
    
    public PulseThread (JComponent component, Color orgColor) {
        this.component = component;
        oldColour = orgColor;
        ORIGINAL_COLOR = new Color(orgColor.getRed(), orgColor.getGreen(), orgColor.getBlue(), orgColor.getAlpha());
    }

    @Override
    public void run () {
        running = true;
        while (running) {
//                colour = NEW_COLOR;
//                oldColour = ORIGINAL_COLOR;
            Color aux;
            int dRed = colour.getRed() - oldColour.getRed();
            int dGreen = colour.getGreen() - oldColour.getGreen();
            int dBlue = colour.getBlue() - oldColour.getBlue();
            // No point if no difference.
            if (dRed != 0 || dGreen != 0 || dBlue != 0) {
              // Do it in n steps.
                for (int i = 0; i <= FadeSteps; i++) {
                    final Color c = new Color(
                            oldColour.getRed() + ((dRed * i) / FadeSteps),
                            oldColour.getGreen() + ((dGreen * i) / FadeSteps),
                            oldColour.getBlue() + ((dBlue * i) / FadeSteps));
                    component.setBackground(c);
                    try {
                        Thread.sleep(WAIT);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        running = false;
                        component.setBackground(ORIGINAL_COLOR);
                        //pulse = new PulseThread(component);                            
                    }
                }
            }

            aux = colour;
            colour = oldColour;
            oldColour = aux;

            dRed = colour.getRed() - oldColour.getRed();
            dGreen = colour.getGreen() - oldColour.getGreen();
            dBlue = colour.getBlue() - oldColour.getBlue();
            // No point if no difference.
            if (dRed != 0 || dGreen != 0 || dBlue != 0) {
              // Do it in n steps.
                for (int i = 0; i <= FadeSteps; i++) {
                    final Color c = new Color(
                            oldColour.getRed() + ((dRed * i) / FadeSteps),
                            oldColour.getGreen() + ((dGreen * i) / FadeSteps),
                            oldColour.getBlue() + ((dBlue * i) / FadeSteps));
                    component.setBackground(c);
                    try {
                        Thread.sleep(WAIT);
                    } catch (InterruptedException ex) {
                        //Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        running = false;
                        component.setBackground(ORIGINAL_COLOR);
                        //pulse = new PulseThread(component);
                    }
                }
            }

            aux = colour;
            colour = oldColour;
            oldColour = aux;

        }
        component.setBackground(ORIGINAL_COLOR);
    }

    public static void stopThread () {
        running = false;
    }

}
        

