package edu.autonoma.turboclash.infrastructure.input;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Representa la responsabilidad de {@code GameInputBinder} en la gestion de entrada.
 */
public class GameInputBinder {

    public enum ControlType {
        KEYBOARD,
        MOUSE,
        BOTH
    }

    private final KeyboardInput keyboardInput;
    private final MouseInput mouseInput;
    private final ControlType controlType;

    /**
     * Crea una nueva instancia de {@code GameInputBinder}.
     *
     * @param keyboardInput valor del parametro {@code keyboardInput}
     * @param mouseInput valor del parametro {@code mouseInput}
     * @param controlType tipo de control que se va a enlazar
     */
    public GameInputBinder(KeyboardInput keyboardInput, MouseInput mouseInput, ControlType controlType) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;
        this.controlType = controlType;
    }

    /**
     * Vincula la operacion principal del metodo.
     *
     * @param panel valor del parametro {@code panel}
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

    /**
     * Vincula {@code Keyboard}.
     *
     * @param panel valor del parametro {@code panel}
     */
    private void bindKeyboard(JPanel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                processKey(e.getKeyCode(), true);
            }

            @Override
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

    /**
     * Vincula {@code Mouse}.
     *
     * @param panel valor del parametro {@code panel}
     */
    private void bindMouse(JPanel panel) {
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mouseInput.setTarget(e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                mouseInput.setTarget(e.getX(), e.getY());
            }
        });
    }
}