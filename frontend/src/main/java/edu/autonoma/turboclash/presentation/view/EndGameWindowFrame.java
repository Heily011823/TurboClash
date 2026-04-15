package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana contenedora de la pantalla final del juego.
 *
 * <p>Su responsabilidad es:</p>
 * <ul>
 *   <li>Mostrar el resumen final de la partida.</li>
 *   <li>Configurar el fondo visual del podio.</li>
 *   <li>Reproducir el audio correspondiente al final del juego.</li>
 *   <li>Liberar correctamente el recurso de red ({@link UdpPeer})
 *       al salir o reiniciar.</li>
 * </ul>
 *
 * <p>Esta clase no calcula ganadores ni ranking; únicamente presenta
 * resultados ya resueltos por la lógica autoritativa del juego.</p>
 *
 * @author Valerie Moreno Castaño
 * @version 1.1
 * @since 2025-04-09
 */
public class EndGameWindowFrame extends JFrame {

    private final EndGameWindow view;
    private final UdpPeer peer;

    /**
     * Construye la ventana final de resultados.
     *
     * @param ganador nombre del jugador ganador
     * @param tiempoTotal tiempo total de la partida
     * @param primero texto del primer lugar
     * @param segundo texto del segundo lugar
     * @param tercero texto del tercer lugar
     * @param cuarto texto del cuarto lugar
     * @param peer conexión UDP activa de la instancia local
     */
    public EndGameWindowFrame(String ganador,
                              String tiempoTotal,
                              String primero,
                              String segundo,
                              String tercero,
                              String cuarto,
                              UdpPeer peer) {

        this.peer = peer;

        reproducirAudioFinal(ganador);

        view = new EndGameWindow();
        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/podium.png");
        fondo.setLayout(new BorderLayout());
        fondo.add(view.panel1, BorderLayout.CENTER);

        setContentPane(fondo);
        setTitle("Fin de la partida");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        view.setResultados(ganador, tiempoTotal, primero, segundo, tercero, cuarto);

        view.addFinListener(e -> cerrarAplicacion());
        view.addReiniciarListener(e -> reiniciarJuego());

        /*
         * Garantiza cierre correcto del peer incluso si el usuario
         * cierra la ventana desde el botón X del sistema operativo.
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarPeer();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                cerrarPeer();
            }
        });

        setVisible(true);
    }

    /**
     * Reproduce el sonido correspondiente al final del juego.
     *
     * @param ganador nombre del ganador
     */
    private void reproducirAudioFinal(String ganador) {
        SoundManager.getInstance().stopMusic();

        if (ganador == null || ganador.isBlank() || "Sin ganador".equalsIgnoreCase(ganador)) {
            SoundManager.getInstance().playEffect(SoundManager.Sound.END);
        } else {
            SoundManager.getInstance().playWinSound();
        }
    }

    /**
     * Cierra la conexión UDP si sigue activa.
     */
    private void cerrarPeer() {
        if (peer != null && peer.isActivo()) {
            peer.cerrar();
        }
    }

    /**
     * Cierra la aplicación liberando primero los recursos de red.
     */
    private void cerrarAplicacion() {
        cerrarPeer();
        dispose();
        System.exit(0);
    }

    /**
     * Reinicia el flujo del juego cerrando la conexión actual
     * y abriendo nuevamente la ventana de inicio.
     */
    private void reiniciarJuego() {
        cerrarPeer();
        dispose();
        new StartWindowFrame();
    }
}