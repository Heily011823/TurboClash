package edu.autonoma.turboclash.network.message;

public enum MessageType {

    // Conexión
    HANDSHAKE,      // primer contacto
    PLAYER_JOINED,  // jugador confirmado en el juego
    PLAYER_LEFT,    // jugador salió

    // Juego
    MOVEMENT,
    COLLISION,
    ITEM_COLLECTED,

    // Estado
    SCORE_UPDATE,
    SYNC,           // sincronización general

    // Control
    GAME_START,
    GAME_OVER
}