package edu.autonoma.turboclash.presentation.presenter;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.presentation.view.GameWindow;

import javax.swing.*;
import java.util.List;

/**
 * Representa la clase `GamePresenter` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
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

    /**
     * Crea una nueva instancia de `GamePresenter`.
     * @param view valor del parametro `view`
     * @param rulesManager valor del parametro `rulesManager`
     * @param resultManager valor del parametro `resultManager`
     */
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

    /**
     * Ejecuta la operacion publica `update`.
     * @param localCar valor del parametro `localCar`
     * @param players valor del parametro `players`
     */
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

    // Getters para sincronizaciÃ³n con la vista o red
    /**
     * Indica la condicion evaluada por `isMovementEnabled`.
     * @return resultado de la operacion documentada
     */
    public boolean isMovementEnabled() { return movementEnabled; }
    /**
     * Indica la condicion evaluada por `isGameStarted`.
     * @return resultado de la operacion documentada
     */
    public boolean isGameStarted() { return gameStarted; }
    /**
     * Indica la condicion evaluada por `isGameFinished`.
     * @return resultado de la operacion documentada
     */
    public boolean isGameFinished() { return gameFinished; }
    /**
     * Obtiene el valor asociado a `getRemainingSeconds`.
     * @return resultado de la operacion documentada
     */
    public int getRemainingSeconds() { return remainingSeconds; }
}
