package edu.autonoma.turboclash.logic;

import model.*;
import exception.GameStateException;
import exception.InvalidMovementException;

import java.util.List;

public class GameEngine {

    private Game game;
    private CollisionManager collisionManager;
    private ScoreManager scoreManager;
    private GameRulesManager rulesManager;
    private TimeManager timeManager;
    private SoundManager soundManager;

    private List<Item> items;
    private List<Obstacle> obstacles;

    private int mapWidth;
    private int mapHeight;

    public GameEngine(Game game, CollisionManager collisionManager, ScoreManager scoreManager,GameRulesManager rulesManager,
                      TimeManager timeManager, SoundManager soundManager, List<Item> items, List<Obstacle> obstacles,
                      int mapWidth, int mapHeight) {

        this.game = game;
        this.collisionManager = collisionManager;
        this.scoreManager = scoreManager;
        this.rulesManager = rulesManager;
        this.timeManager = timeManager;
        this.soundManager = soundManager;
        this.items = items;
        this.obstacles = obstacles;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    // Inicia la partida
    public void startGame() throws GameStateException {
        if (game == null) {
            throw new GameStateException("El juego no está inicializado.");
        }

        game.start();
        timeManager.start();
        soundManager.playStart();
    }

    // Actualiza el juego en tiempo real
    public void update() throws GameStateException {
        if (game == null || game.isFinished()) return;

        timeManager.update();

        processPlayer(game.getLocalPlayer());
        processPlayer(game.getRemotePlayer());

        checkGameState();
    }

    // Procesa movimiento desde el mouse
    public void processMouseMovement(Player player, double x, double y)
            throws InvalidMovementException {

        if (player == null) {
            throw new InvalidMovementException("Jugador inválido.");
        }

        // Validamos límites del mapa
        if (x < 0 || y < 0 || x > mapWidth || y > mapHeight) {
            throw new InvalidMovementException("Movimiento fuera del mapa.");
        }

        player.moveCar(x, y);
    }

    // Procesa colisiones de un jugador
    private void processPlayer(Player player) {
        if (player == null || player.getCar() == null) return;

        Item item = collisionManager.processItemCollision(player.getCar(), items);
        if (item != null) {
            scoreManager.addItemPoints(player, item);
            soundManager.playPoint();
        }

        Obstacle obstacle = collisionManager.processObstacleCollision(player.getCar(), obstacles);
        if (obstacle != null) {
            scoreManager.subtractObstaclePoints(player, obstacle);
            soundManager.playCollision();
        }
    }

    // Verifica si el juego termina
    private void checkGameState() {
        if (rulesManager.hasWinner(game, timeManager)) {
            endGame();
        }
    }

    // Finaliza la partida
    private void endGame() {
        Player winner = scoreManager.getWinner(
                game.getLocalPlayer(),
                game.getRemotePlayer()
        );

        game.setWinner(winner);
        game.setTotalTime(timeManager.getElapsedTime());
        game.finish();

        soundManager.playEnd();

        if (winner != null) {
            soundManager.playWin();
        }
    }
}
