package edu.autonoma.turboclash.presentation.presenter;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.presentation.view.GameWindow;

import java.util.List;

/**
 * Orquesta la comunicacion y actualizacion de {@code GamePresenter} en la capa de presentacion.
 */
public class GamePresenter {

    private final GameWindow view;
    private final GameRulesManager rulesManager;
    private final GameResultManager resultManager;

    private boolean gameFinished = false;
    private boolean gameStarted = false;
    private boolean movementEnabled = false;

    public GamePresenter(GameWindow view,
                         GameRulesManager rulesManager,
                         GameResultManager resultManager) {
        this.view = view;
        this.rulesManager = rulesManager;
        this.resultManager = resultManager;

        this.view.setOnCountdownFinished(() -> movementEnabled = true);
    }

    public void update(Car localCar, List<Player> players) {
        if (players == null || players.isEmpty()) return;

        if (!gameStarted) {
            if (players.size() < 2) {
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

        view.prepareRaceStart(players);
        view.showGameStarted();
        view.startCountdown();
    }

    private void checkFinish(Car car, List<Player> players) {
        if (!gameStarted || !movementEnabled) return;
        if (car == null) return;
        if (!view.isMetaVisible()) return;

        int metaX = view.getMetaX();

        for (Player player : players) {
            if (player != null && player.getCar() == car && !player.isFinishReached()) {
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
                if (player.isAlive()) {
                    aliveCount++;
                }

                if (player.isFinishReached()) {
                    someoneReachedFinish = true;
                }
            }
        }

        if (someoneReachedFinish || aliveCount <= 1) {
            finishGame(players);
        }
    }

    private void finishGame(List<Player> players) {
        gameFinished = true;
        List<Player> ranking = resultManager.calculateRanking(players);
        view.showGameResult(ranking);
    }

    public boolean isMovementEnabled() {
        return movementEnabled;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }
}