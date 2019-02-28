/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import gui.ConsoleFrame;
import gui.HelperFrame;
import gui.IstoricFrame;
import gui.StartFrame;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import javax.swing.UIManager;
import server.ServerFrame;
import service.ControllerDB;
import service.SerializeController;

/**
 *
 * @author Alexandru
 */
public class Main {
    private static boolean isClosingApp = false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
            System.out.println("UIManager Exception : "+e);
        }
        
        PrintStream printerOut = new PrintStream(new TextAreaOutputStream(ConsoleFrame.getInstance().getTextAreaOut()));
        PrintStream printerErr = new PrintStream(new TextAreaOutputStream(ConsoleFrame.getInstance().getTextAreaErr()));
        System.setOut(printerOut);
        System.setErr(printerErr);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                isClosingApp = true;
                try { StartFrame.stopXampp(); } catch (Exception e) {}
                try { ControllerDB.getInstance().disconnect(); } catch (Exception e) {}
                try { Magazin.service.MagazinController.getInstance().disconnect(); } catch (Exception e) {}
                try {
                    if (!ServerFrame.isSingletonNull()) {
                        ServerFrame.getInstance().opresteServerul();
                    }
                    HelperFrame.loseInstance();
                                 
                } catch (Exception e) {}
                
                try { if (IstoricFrame.isExistingInstance()) IstoricFrame.getInstance().saveHistory(); } catch (Exception e) {}
                try { SerializeController.getInstance().saveConfigs(); } catch (Exception e) {}
                try { if (ConsoleFrame.isExistingInstance()) ConsoleFrame.getInstance().saveReport(); } catch (Exception e) {}
                ////
                System.out.println("Firul principal de executie a fost inchis.");
            }
        }));
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            private static final long TIME_INTERVAL = 500;
            private long lastPressed = 0;
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_F && ke.isShiftDown()) {
                    if (System.currentTimeMillis() - lastPressed < TIME_INTERVAL) return false;
                    HelperFrame.getInstance().setCanReadInfo(
                            !HelperFrame.getInstance().canReadInfo()
                    );
                    lastPressed = System.currentTimeMillis();
                }
                
                if (ke.getKeyCode() == KeyEvent.VK_F2) {
                    ConsoleFrame.getInstance().setVisible(true);
                    ConsoleFrame.getInstance().toFront();
                }
                
                if (ke.getKeyCode() == KeyEvent.VK_F3) {
                    HelperFrame.getInstance().setVisible(true);
                    HelperFrame.getInstance().toFront();
                }
                return false;
            }
        });
        //new LoginFrame().setVisible(true);
        HelperFrame.getInstance();
        new StartFrame().setVisible(true);
    }
    
    public static boolean isClosingApp() {
        return isClosingApp;
    }       
}
