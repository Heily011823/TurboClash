package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.net.URL;

public class MainWindow {
    public JPanel panel1;
    private JLabel fondoLabel;

    public MainWindow() {
        // El diseñador de IntelliJ llama a un método invisible llamado $$$setupUI$$$
        // antes de entrar aquí. Por eso, fondoLabel ya existe en este punto.

        cargarImagenFondo();
    }

    private void cargarImagenFondo() {
        URL imgUrl = getClass().getResource("/image/Game_Cover.png");
        if (imgUrl != null) {
            fondoLabel.setIcon(new ImageIcon(imgUrl));
            fondoLabel.setText(""); // Borra el texto "Label"
        } else {
            System.err.println("No se encontró la imagen en: /image/Game_Cover.png");
        }
    }

    public JPanel getPanel1() {
        return panel1;
    }
}