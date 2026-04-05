package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.KeyboardInput;
import edu.autonoma.turboclash.input.MouseInput;
import javax.swing.*;
import java.awt.*;

public class GameWindowFrame extends JFrame {

    private GameWindow view;

    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput) {

        view = new GameWindow(keyboardInput, mouseInput);

        view.panel1.setOpaque(false);


        setTitle("TurboClash - Racing Game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
            fondo.setLayout(new BorderLayout());
            fondo.add(view.panel1);
            setContentPane(fondo);
        } catch (Exception e) {
            // Si falla el fondo, al menos que cargue el panel normal para que no se cierre
            setContentPane(view.panel1);
            System.err.println("No se pudo cargar el fondo animado: " + e.getMessage());
        }

        setVisible(true);

        
        view.panel1.setFocusable(true);
        view.panel1.requestFocusInWindow();
    }

    public GameWindow getView() {
        return view;
    }
}