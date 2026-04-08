package edu.autonoma.turboclash.presentation.presenter;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.presentation.view.GameWindow;

import javax.swing.*;
import java.util.List;

/**
 * Orquesta la comunicación y actualización de GamePresenter en la capa de presentación.
 */
public class GamePresenter {

    private final GameWindow view;
    private final GameRulesManager rulesManager;
    private final GameResultManager resultManager;

    private boolean gameFinished = false;
    private boolean gameStarted = false;
    private boolean movementEnabled = false; // Empieza en false hasta que termine la cuenta regresiva
    private boolean timerStarted = false;

    private Timer gameTimer;
    private int remainingSeconds = 180; // 3 minutos

    public GamePresenter(GameWindow view,
                         GameRulesManager rulesManager,
                         GameResultManager resultManager) {
        this.view = view;
        this.rulesManager = rulesManager;
        this.resultManager = resultManager;

        // Listener para habilitar movimiento cuando la vista termine el "3, 2, 1..."
        this.view.setOnCountdownFinished(() -> {
            movementEnabled = true;
            startMatchTimer();
        });
    }

    public void update(Car localCar, List<Player> players) {
        if (players == null || players.isEmpty()) {
            return;
        }

        if (!gameStarted) {
            if (players.size() < 4) {
                view.showWaitingPlayers();
                view.updateCars(players);
                return;
            }
            startGame(players);
            return;
        }

        view.updateCars(players);
        checkFinish(localCar, players);
        checkGameEnd(players);
    }

    private void startGame(List<Player> players) {
        if (gameStarted) return;

        gameStarted = true;
        movementEnabled = false;
        timerStarted = false;
        remainingSeconds = 180;

        view.prepareRaceStart(players);
        view.showGameStarted();
        view.startCountdown();
    }

    private void startMatchTimer() {
        if (timerStarted || gameFinished) return;

        timerStarted = true;
        gameTimer = new Timer(1000, e -> {
            if (gameFinished) {
                stopMatchTimer();
                return;
            }

            remainingSeconds--;

            if (remainingSeconds <= 0) {
                stopMatchTimer();
                gameFinished = true;
            }
        });
        gameTimer.start();
    }

    private void stopMatchTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    private void checkFinish(Car car, List<Player> players) {
        if (!gameStarted || !movementEnabled || car == null || !view.isMetaVisible()) {
            return;
        }

        int metaX = view.getMetaX();

        for (Player player : players) {
            if (player != null && player.getCar() == car && !player.isFinishReached()) {
                // Si el frente del carro (x + ancho) cruza la meta
                if ((int) car.getX() + 100 >= metaX) {
                    rulesManager.applyFinishBonus(player);
                }
                break;
            }
        }
    }

    private void checkGameEnd(List<Player> players) {
        if (gameFinished) return;
        if (!gameStarted || !movementEnabled) return;
        if (players.isEmpty() || view.isBackgroundFinished()) return;

        int aliveCount = 0;
        boolean someoneReachedFinish = false;

        for (Player player : players) {
            if (player != null) {
                if (player.isAlive()) aliveCount++;
                if (player.isFinishReached()) someoneReachedFinish = true;
            }
        }

        // El juego termina si alguien llega, si queda uno solo vivo, si se acaba el tiempo o el mapa
        if (someoneReachedFinish || aliveCount <= 1 || remainingSeconds <= 0 || view.isBackgroundFinished()) {
            finishGame(players);
        }
    }

    private void finishGame(List<Player> players) {
        if (gameFinished && !timerStarted) return;

        gameFinished = true;
        movementEnabled = false;
        stopMatchTimer();

        List<Player> ranking = resultManager.calculateRanking(players);
        view.showGameResult(ranking);
    }

    // Getters para sincronización con la vista o red
    public boolean isMovementEnabled() { return movementEnabled; }
    public boolean isGameStarted() { return gameStarted; }
    public boolean isGameFinished() { return gameFinished; }
    public int getRemainingSeconds() { return remainingSeconds; }
}