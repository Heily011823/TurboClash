package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;

import javax.swing.*;
import java.awt.*;

/**
 * Representa la clase `EndGameWindowFrame` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class EndGameWindowFrame extends JFrame {

    private final EndGameWindow view;
    private final UdpPeer peer;

    /**
     * Crea una nueva instancia de `EndGameWindowFrame`.
     * @param ganador valor del parametro `ganador`
     * @param tiempoTotal valor del parametro `tiempoTotal`
     * @param primero valor del parametro `primero`
     * @param segundo valor del parametro `segundo`
     * @param tercero valor del parametro `tercero`
     * @param cuarto valor del parametro `cuarto`
     * @param peer valor del parametro `peer`
     */
    public EndGameWindowFrame(String ganador,
                              String tiempoTotal,
                              String primero,
                              String segundo,
                              String tercero,
                              String cuarto,
                              UdpPeer peer) {

        this.peer = peer;

        SoundManager.getInstance().stopMusic();
        if (ganador == null || ganador.isBlank() || "Sin ganador".equalsIgnoreCase(ganador)) {
            SoundManager.getInstance().playEffect(SoundManager.Sound.END);
        } else {
            SoundManager.getInstance().playWinSound();
        }

        view = new EndGameWindow();
        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/podium.png");
        fondo.setLayout(new BorderLayout());
        fondo.add(view.panel1, BorderLayout.CENTER);

        setContentPane(fondo);
        setTitle("FIN");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        view.setResultados(ganador, tiempoTotal, primero, segundo, tercero, cuarto);

        view.addFinListener(e -> {
            cerrarPeer();
            System.exit(0);
        });

        view.addReiniciarListener(e -> {
            cerrarPeer();
            dispose();
            new StartWindowFrame();
        });

        setVisible(true);
    }

    private void cerrarPeer() {
        if (peer != null && peer.isActivo()) {
            peer.cerrar();
        }
    }
}