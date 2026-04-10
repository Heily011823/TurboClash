package edu.autonoma.turboclash.infrastructure.input;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Representa la clase `GameInputBinder` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-090
 */
public class GameInputBinder {

    /**
     * Enumera las opciones disponibles para `ControlType` dentro del sistema.
     *
     * @author 
     * @version 1.0
     */
    public enum ControlType {
        KEYBOARD,
        MOUSE,
        BOTH
    }

    private final KeyboardInput keyboardInput;
    private final MouseInput mouseInput;
    private final ControlType controlType;

    /**
     * Crea una nueva instancia de `GameInputBinder`.
     * @param keyboardInput valor del parametro `keyboardInput`
     * @param mouseInput valor del parametro `mouseInput`
     * @param controlType valor del parametro `controlType`
     */
    public GameInputBinder(KeyboardInput keyboardInput, MouseInput mouseInput, ControlType controlType) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;
        this.controlType = controlType;
    }

    /**
     * Ejecuta la operacion publica `bind`.
     * @param panel valor del parametro `panel`
     */
    public void bind(JPanel panel) {
        panel.setFocusable(true);

        SwingUtilities.invokeLater(panel::requestFocusInWindow);

        switch (controlType) {
            case KEYBOARD -> bindKeyboard(panel);
            case MOUSE -> bindMouse(panel);
            case BOTH -> {
                bindKeyboard(panel);
                bindMouse(panel);
            }
        }
    }

    private void bindKeyboard(JPanel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            /**
             * Ejecuta la operacion publica `keyPressed`.
             * @param e valor del parametro `e`
             */
            public void keyPressed(KeyEvent e) {
                processKey(e.getKeyCode(), true);
            }

            @Override
            /**
             * Ejecuta la operacion publica `keyReleased`.
             * @param e valor del parametro `e`
             */
            public void keyReleased(KeyEvent e) {
                processKey(e.getKeyCode(), false);
            }

            private void processKey(int code, boolean pressed) {
                switch (code) {
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> keyboardInput.setUp(pressed);
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> keyboardInput.setDown(pressed);
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT -> keyboardInput.setLeft(pressed);
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> keyboardInput.setRight(pressed);
                }
            }
        });
    }

    private void bindMouse(JPanel panel) {
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            /**
             * Ejecuta la operacion publica `mouseMoved`.
             * @param e valor del parametro `e`
             */
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mouseInput.setTarget(e.getX(), e.getY());
            }

            @Override
            /**
             * Ejecuta la operacion publica `mouseDragged`.
             * @param e valor del parametro `e`
             */
            public void mouseDragged(java.awt.event.MouseEvent e) {
                mouseInput.setTarget(e.getX(), e.getY());
            }
        });
    }
}
