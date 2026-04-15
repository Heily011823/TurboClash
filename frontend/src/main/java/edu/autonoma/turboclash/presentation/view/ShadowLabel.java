package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;

class ShadowLabel extends JLabel {

    public ShadowLabel(String text) {
        super(text);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();


        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String text = getText();

        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();


        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 2, y + 2);


        g2.setColor(getForeground());
        g2.drawString(text, x, y);

        g2.dispose();
    }
}
