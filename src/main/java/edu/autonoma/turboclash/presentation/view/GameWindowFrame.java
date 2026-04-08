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

        setVisible(true);

        view.requestGameFocus();

        if (countdownAction != null) {
            view.startCountdown();
        }
    }

    /**
     * Configura {@code FrameProperties}.
     */
    private void setupFrameProperties() {
        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        gamePanel.setPreferredSize(screen);
        gamePanel.setMinimumSize(screen);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
            fondo.setLayout(new BorderLayout());
            fondo.setPreferredSize(screen);
            fondo.add(gamePanel, BorderLayout.CENTER);

            view.setBackgroundPanel(fondo);

            setContentPane(fondo);

        } catch (Exception e) {
            gamePanel.setOpaque(true);
            gamePanel.setBackground(Color.GRAY);
            setContentPane(gamePanel);
            System.err.println("No se pudo cargar el fondo animado: " + e.getMessage());
        }

        new GameInputBinder(keyboardInput, mouseInput).bind(gamePanel);
    }

    /**
     * Actualiza el valor de {@code CountdownAction}.
     *
     * @param countdownAction valor del parametro {@code countdownAction}
     */
    public void setCountdownAction(Runnable countdownAction) {
        this.countdownAction = countdownAction;
        view.startCountdown();
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
