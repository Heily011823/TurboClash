package edu.autonoma.turboclash.infrastructure.network.message;

public enum MessageType {

    // Conexión
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