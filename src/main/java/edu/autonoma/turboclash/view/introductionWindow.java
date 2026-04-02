package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;

public class introductionWindow extends JFrame {

    private JTextField txtJugador1;
    private JTextField txtJugador2;
    private JTextField txtJugador3;
    private JTextField txtJugador4;

    public introductionWindow() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Turbo Clash - Jugadores");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Imagen de fondo
        ImageIcon iconoFondo = new ImageIcon("src/edu/autonoma/turboclash/image/start.png");
        Image fondoEscalado = iconoFondo.getImage().getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
        JLabel fondo = new JLabel(new ImageIcon(fondoEscalado));
        fondo.setLayout(null);
        setContentPane(fondo);

        // Título
        JLabel lblTitulo = new JLabel("INGRESA LOS JUGADORES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.black);
        lblTitulo.setBounds(400, 60, 500, 40);
        fondo.add(lblTitulo);

        // Jugador 1
        JLabel lblJugador1 = new JLabel("Jugador 1:");
        lblJugador1.setForeground(Color.black);
        lblJugador1.setFont(new Font("Arial", Font.BOLD, 16));
        lblJugador1.setBounds(400, 150, 100, 30);
        fondo.add(lblJugador1);

        txtJugador1 = new JTextField();
        txtJugador1.setBounds(520, 150, 200, 30);
        fondo.add(txtJugador1);

        // Jugador 2
        JLabel lblJugador2 = new JLabel("Jugador 2:");
        lblJugador2.setForeground(Color.black);
        lblJugador2.setFont(new Font("Arial", Font.BOLD, 16));
        lblJugador2.setBounds(400, 200, 100, 30);
        fondo.add(lblJugador2);

        txtJugador2 = new JTextField();
        txtJugador2.setBounds(520, 200, 200, 30);
        fondo.add(txtJugador2);

        // Jugador 3
        JLabel lblJugador3 = new JLabel("Jugador 3:");
        lblJugador3.setForeground(Color.black);
        lblJugador3.setFont(new Font("Arial", Font.BOLD, 16));
        lblJugador3.setBounds(400, 250, 100, 30);
        fondo.add(lblJugador3);

        txtJugador3 = new JTextField();
        txtJugador3.setBounds(520, 250, 200, 30);
        fondo.add(txtJugador3);

        // Jugador 4
        JLabel lblJugador4 = new JLabel("Jugador 4:");
        lblJugador4.setForeground(Color.black);
        lblJugador4.setFont(new Font("Arial", Font.BOLD, 16));
        lblJugador4.setBounds(400, 300, 100, 30);
        fondo.add(lblJugador4);

        txtJugador4 = new JTextField();
        txtJugador4.setBounds(520, 300, 200, 30);
        fondo.add(txtJugador4);

        // Botón información
        ImageIcon infoIcono = new ImageIcon("src/edu/autonoma/turboclash/image/informacion.png");
        Image infoEscalada = infoIcono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JButton btnInfo = new JButton(new ImageIcon(infoEscalada));
        btnInfo.setBounds(1180, 20, 50, 50);
        btnInfo.setBorderPainted(false);
        btnInfo.setContentAreaFilled(false);
        btnInfo.setFocusPainted(false);
        btnInfo.setOpaque(false);
        fondo.add(btnInfo);

        // Botón iniciar
        JButton btnIniciar = new JButton("Iniciar ");
        btnIniciar.setBounds(560, 400, 160, 40);
        fondo.add(btnIniciar);

        // Eventos
        btnInfo.addActionListener(e -> mostrarInformacion());
        btnIniciar.addActionListener(e -> iniciarJuego());
    }

    private void mostrarInformacion() {
        String mensaje = """
                Bienvenido a Turbo Clash.".
                """;

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Información del juego",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void iniciarJuego() {
        String jugador1 = txtJugador1.getText().trim();
        String jugador2 = txtJugador2.getText().trim();
        String jugador3 = txtJugador3.getText().trim();
        String jugador4 = txtJugador4.getText().trim();

        if (jugador1.isEmpty() || jugador2.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar al menos el nombre del Jugador 1 y Jugador 2.",
                    "Campos obligatorios",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String mensaje = "Jugadores registrados:\n"
                + "Jugador 1: " + jugador1 + "\n"
                + "Jugador 2: " + jugador2 + "\n"
                + "Jugador 3: " + (jugador3.isEmpty() ? "No registrado" : jugador3) + "\n"
                + "Jugador 4: " + (jugador4.isEmpty() ? "No registrado" : jugador4);

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Juego iniciado",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Aquí puedes abrir la siguiente ventana
        // new GameWindow().setVisible(true);
        // dispose();
    }


}