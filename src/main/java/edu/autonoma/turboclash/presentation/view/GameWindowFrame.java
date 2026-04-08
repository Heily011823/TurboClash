package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.infrastructure.input.GameInputBinder;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;

import javax.swing.*;
import java.awt.*;

/**
 * Representa y organiza la vista {@code GameWindowFrame} en la capa de presentacion.
 */
public class GameWindowFrame extends JFrame {

    private final GameWindow view;
    private Runnable countdownAction;

    /**
     * Crea una nueva instancia de {@code GameWindowFrame}.
     *
     * @param keyboardInput valor del parametro {@code keyboardInput}
     * @param mouseInput valor del parametro {@code mouseInput}
     */
    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.view = new GameWindow();

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

    /**
     * Configura {@code FrameProperties}.
     */
    private void setupFrameProperties() {
        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
    }

    /**
     * Configura {@code ContentLayout}.
     *
     * @param keyboardInput valor del parametro {@code keyboardInput}
     * @param mouseInput valor del parametro {@code mouseInput}
     */
    private void setupContentLayout(KeyboardInput keyboardInput, MouseInput mouseInput) {
        JPanel gamePanel = view.getPanel();
        gamePanel.setOpaque(false);
        gamePanel.setFocusable(true);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        gamePanel.setPreferredSize(screen);
        gamePanel.setMinimumSize(screen);
        gamePanel.setMaximumSize(screen);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
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
            System.err.println("No se pudo cargar el fondo animado: " + e.getMessage());
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
     * Actualiza el valor de {@code CountdownAction}.
     *
     * @param countdownAction valor del parametro {@code countdownAction}
     */
    public void setCountdownAction(Runnable countdownAction) {
        this.countdownAction = countdownAction;
    }

    /**
     * Obtiene el valor de {@code View}.
     *
     * @return valor de {@code View}
     */
    public GameWindow getView() {
        return view;
    }

    /**
     * Obtiene el valor de {@code GameView}.
     *
     * @return valor de {@code GameView}
     */
    public GameWindow getGameView() {
        return view;
    }
}