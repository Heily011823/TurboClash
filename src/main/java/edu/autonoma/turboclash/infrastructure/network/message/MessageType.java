package edu.autonoma.turboclash.infrastructure.network.message;

/**
 * Enumera las opciones disponibles para `MessageType` dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public enum MessageType {

    // ConexiÃ³n
    HANDSHAKE,
    PLAYER_JOINED,
    PLAYER_LEFT,
    DISCOVERY,

    // Juego
    MOVEMENT,
    COLLISION,
    ITEM_COLLECTED,

    // Estado
    SCORE_UPDATE,
    SYNC,

    // Control
    GAME_START,
    GAME_OVER
}
