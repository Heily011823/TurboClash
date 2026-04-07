package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.*;
import javax.swing.*;
import java.awt.*;

/**
 * SOLID: Esta clase cumple con el principio de Responsabilidad Única (SRP)
 * al encargarse exclusivamente de la configuración de la ventana principal (JFrame).
 */
public class GameWindowFrame extends JFrame {

    private final GameWindow view;

    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.view = new GameWindow();

        setupFrameProperties();
        setupContentLayout(keyboardInput, mouseInput);

        // Finalizar configuración de ventana
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Solicitar foco para los inputs y arrancar cuenta regresiva
        view.requestGameFocus();
    }

    private void setupFrameProperties() {
        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void setupContentLayout(KeyboardInput keyboardInput, MouseInput mouseInput) {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setPreferredSize(GameViewport.size());

        try {
            // Intentar cargar el fondo animado (Open/Closed Principle)
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
            fondo.setLayout(new BorderLayout());
            fondo.setPreferredSize(GameViewport.size());

            // Agregar la capa de juego transparente sobre el fondo
            fondo.add(view.getPanel(), BorderLayout.CENTER);
            rootPanel.add(fondo, BorderLayout.CENTER);

        } catch (Exception e) {
            // Fallback: Si el fondo falla, mostrar al menos el panel de juego
            System.err.println("Error cargando fondo animado, usando fallback.");
            rootPanel.add(view.getPanel(), BorderLayout.CENTER);
        }

        setContentPane(rootPanel);

        // Vincular periféricos
        new GameInputBinder(keyboardInput, mouseInput).bind(view.getPanel());
    }

    public GameWindow getView() {
        return view;
    }

    // Alias para compatibilidad con GameApplication
    public GameWindow getGameView() {
        return view;
    }
}