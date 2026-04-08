package edu.autonoma.turboclash.infrastructure.input;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;

public class GameInputBinder {

    private final KeyboardInput keyboardInput;
    private final MouseInput mouseInput;

    public GameInputBinder(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;
    }

    public void bind(JPanel panel) {
        bindKeyboard(panel);
        bindMouse(panel);
    }

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

    private void bindMouse(JPanel panel) {
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mouseInput.setTarget(e.getX(), e.getY());
            }
        });
    }
}