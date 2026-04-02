/*
 * Created by JFormDesigner on Thu Apr 02 09:39:45 GMT-05:00 2026
 */
package edu.autonoma.turboclash.view;

import javax.swing.*;

public class startWindow extends JFrame {
    public static void main(String[] args) {
        new startWindow().setVisible(true);
    }
    public startWindow() {
        initComponents();
    }

    private void initComponents() {

        // Obtener el panel principal
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        // Tamaño de la ventana
        setSize(800, 600);

        // Imagen de fondo
        ImageIcon imagen = new ImageIcon("src/edu/autonoma/turboclash/image/Game_Cover.png");
        JLabel lblImagen = new JLabel(imagen);
        lblImagen.setBounds(0, 0, 1280, 720);
        contentPane.add(lblImagen);

        // Botón
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setBounds(500, 580, 220, 50);
        lblImagen.add(btnContinuar);

        // Acción del botón
        btnContinuar.addActionListener(e -> {
            new introductionWindow().setVisible(true);
            dispose();
        });

        // Centrar ventana
        setLocationRelativeTo(null);
    }
}