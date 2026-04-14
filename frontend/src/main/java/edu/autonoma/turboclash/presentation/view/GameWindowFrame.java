package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.infrastructure.input.GameInputBinder;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;

import javax.swing.*;
import java.awt.*;

/**
 * Representa la clase `GameWindowFrame` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class GameWindowFrame extends JFrame {

    private final GameWindow view;
    private final UdpPeer peer;
    private Runnable countdownAction;

    /**
     * Crea una nueva instancia de `GameWindowFrame`.
     * @param keyboardInput valor del parametro `keyboardInput`
     * @param mouseInput valor del parametro `mouseInput`
     * @param peer valor del parametro `peer`
     */
    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput, UdpPeer peer) {
        this.view = new GameWindow();
        this.peer = peer;

        setupFrameProperties();
        setupContentLayout(keyboardInput, mouseInput);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        SwingUtilities.invokeLater(() -> {
            view.getPanel().setFocusable(true);
            view.getPanel().requestFocusInWindow();

            if (countdownAction != null) {
                countdownAction.run();
            }
        });
    }

    private void setupFrameProperties() {
        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
    }

    private void setupContentLayout(KeyboardInput keyboardInput, MouseInput mouseInput) {
        JPanel gamePanel = view.getPanel();
        gamePanel.setOpaque(false);
        gamePanel.setFocusable(true);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        gamePanel.setPreferredSize(screen);
        gamePanel.setMinimumSize(screen);
        gamePanel.setMaximumSize(screen);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png", peer);
            fondo.setLayout(new BorderLayout());
            fondo.setPreferredSize(screen);
            fondo.setMinimumSize(screen);
            fondo.setMaximumSize(screen);

            fondo.add(gamePanel, BorderLayout.CENTER);

            view.setBackgroundPanel(fondo);
            setContentPane(fondo);

        } catch (Exception e) {
            gamePanel.setOpaque(true);
            gamePanel.setBackground(Color.GRAY);
            setContentPane(gamePanel);
        }

        int puerto = Integer.parseInt(System.getProperty("puerto", "5001"));

        GameInputBinder.ControlType controlType;
        if (puerto == 5001 || puerto == 5002) {
            controlType = GameInputBinder.ControlType.KEYBOARD;
        } else {
            controlType = GameInputBinder.ControlType.MOUSE;
        }

        new GameInputBinder(keyboardInput, mouseInput, controlType).bind(gamePanel);
    }

    /**
     * Actualiza el valor asociado a `setCountdownAction`.
     * @param countdownAction valor del parametro `countdownAction`
     */
    public void setCountdownAction(Runnable countdownAction) {
        this.countdownAction = countdownAction;
    }

    /**
     * Obtiene el valor asociado a `getView`.
     * @return resultado de la operacion documentada
     */
    public GameWindow getView() {
        return view;
    }

    /**
     * Obtiene el valor asociado a `getGameView`.
     * @return resultado de la operacion documentada
     */
    public GameWindow getGameView() {
        return view;
    }
}