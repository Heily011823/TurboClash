package edu.autonoma.turboclash.network.message;

public enum MessageType {

    // Conexión
    HANDSHAKE,
    PLAYER_JOINED,
    PLAYER_LEFT,

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