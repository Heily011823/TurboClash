package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.sound.IAudioService;

import javax.swing.*;
import java.awt.*;

public class IntroductionWindow {

    public JPanel panel1;
    private JButton btnStart;
    private JTextField txtName;
    private JLabel lblName;
    private JLabel infoIcon;
    private JButton btnClose;

    private final IntroductionWindowListener listener;
    private final IAudioService audioService;

    public IntroductionWindow(IntroductionWindowListener listener) {
        this(listener, null);
    }

    public IntroductionWindow(IntroductionWindowListener listener, IAudioService audioService) {
        this.listener = listener;
        this.audioService = audioService;

        setupUI();
        setupEvents();
    }

    private void setupUI() {
        panel1.setLayout(null);
        panel1.setBackground(new Color(12, 12, 12));

        btnClose = new JButton("X");
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setBackground(new Color(150, 0, 0));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 18));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel1.add(btnClose);

        infoIcon.setBounds(20, 20, 50, 50);
        infoIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/image/Informacion.png"));
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            infoIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Error loading info icon");
        }

        panel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                centerComponents();
            }
        });
    }

    private void centerComponents() {
        int width = panel1.getWidth();
        int height = panel1.getHeight();
        int centerX = width / 2;

        btnClose.setBounds(width - 70, 20, 50, 50);

        lblName.setText("INGRESA TU NOMBRE:");
        lblName.setBounds(centerX - 150, height / 2 - 100, 300, 40);
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        lblName.setForeground(new Color(220, 20, 60));

        Font gameFont = new Font("Consolas", Font.BOLD, 18);

        txtName.setBounds(centerX - 150, height / 2 - 40, 300, 50);
        txtName.setHorizontalAlignment(JTextField.CENTER);
        txtName.setBackground(new Color(20, 20, 20));
        txtName.setForeground(new Color(255, 80, 80));
        txtName.setCaretColor(new Color(255, 50, 50));
        txtName.setFont(gameFont);

        lblName.setFont(gameFont.deriveFont(22f));

        txtName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/image/Play.png"));

            int originalW = icon.getIconWidth();
            int originalH = icon.getIconHeight();

            int base = Math.min(width, height);
            int newW = (int) (base * 0.18);
            newW = Math.max(140, Math.min(newW, 200));

            int newH = (originalH * newW) / originalW;

            Image img = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

            btnStart.setIcon(new ImageIcon(img));
            btnStart.setBounds(centerX - newW / 2, height / 2 + 40, newW, newH);

        } catch (Exception e) {
            btnStart.setText("PLAY");
            btnStart.setForeground(new Color(255, 50, 50));
            btnStart.setBounds(centerX - 100, height / 2 + 40, 200, 60);
        }

        btnStart.setBorderPainted(false);
        btnStart.setContentAreaFilled(false);
        btnStart.setFocusPainted(false);
        btnStart.setOpaque(false);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEvents() {

        btnClose.addActionListener(e -> System.exit(0));

        btnStart.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnStart.setBounds(
                        btnStart.getX() - 8,
                        btnStart.getY() - 4,
                        btnStart.getWidth() + 16,
                        btnStart.getHeight() + 8
                );
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                centerComponents();
            }
        });

        infoIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                infoIcon.setBounds(18, 18, 55, 55);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                infoIcon.setBounds(20, 20, 50, 50);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showRules();
            }
        });

        btnStart.addActionListener(e -> {
            String name = txtName.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "INGRRESA TU NOMBRE");
                return;
            }

            if (audioService != null) {
                audioService.stopMusic();
            }

            listener.onContinuePressed(name);
        });
    }

    private void showRules() {
        String rules = """
                GAME RULES

                - Use arrow keys to move
                - Avoid obstacles
                - Survive as long as possible
                """;

        JOptionPane.showMessageDialog(
                null,
                rules,
                "Game Rules",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}