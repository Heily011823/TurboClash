package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.KeyboardInput;
import edu.autonoma.turboclash.input.MouseInput;

import javax.swing.*;

public class GameWindowFrame extends JFrame {

    private GameWindow view;

    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput) {

        view = new GameWindow(keyboardInput, mouseInput);

        view.panel1.setOpaque(false);

        FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
        fondo.setLayout(new java.awt.BorderLayout());
        fondo.add(view.panel1);

        setContentPane(fondo);

        setTitle("Juego");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);


        view.panel1.requestFocusInWindow();
    }


    public GameWindow getView() {
        return view;
    }
}