package edu.autonoma.turboclash.presentation.navigation;

/**
 * Define el contrato de escucha y coordinacion para {@code IntroductionWindowListener} para la navegacion de la interfaz.
 */
public interface IntroductionWindowListener {

    /**
     * Atiende {@code ContinuePressed}.
     *
     * @param playerName valor del parametro {@code playerName}
     */
    void onContinuePressed(String playerName);
}
