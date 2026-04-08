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

    /**
     * Crea una nueva instancia de {@code GamePresenter}.
     *
     * @param view valor del parametro {@code view}
     * @param rulesManager valor del parametro {@code rulesManager}
     * @param resultManager valor del parametro {@code resultManager}
     */
    public GamePresenter(GameWindow view,
                         GameRulesManager rulesManager,
                         GameResultManager resultManager) {
        this.view = view;
        this.rulesManager = rulesManager;
        this.resultManager = resultManager;

        this.view.setOnCountdownFinished(() -> movementEnabled = true);
    }

    /**
     * Actualiza la operacion principal del metodo.
     *
     * @param localCar valor del parametro {@code localCar}
     * @param players valor del parametro {@code players}
     */
    public void update(Car localCar, List<Player> players) {
        if (players == null || players.isEmpty()) return;

        view.updateCars(players);

        if (!gameStarted) {
            if (players.size() < 2) {
                view.showWaitingPlayers();
                return;
            }

            startGame(players);
            return;
        }

        checkFinish(localCar, players);
        checkGameEnd(players);
    }

    /**
     * Inicia {@code Game}.
     *
     * @param players valor del parametro {@code players}
     */
    private void startGame(List<Player> players) {
        if (gameStarted) return;

        gameStarted = true;
        movementEnabled = false;

        view.showGameStarted();
        view.startCountdown();
    }

    /**
     * Ejecuta la operacion {@code checkFinish}.
     *
     * @param car valor del parametro {@code car}
     * @param players valor del parametro {@code players}
     */
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

    /**
     * Ejecuta la operacion {@code checkGameEnd}.
     *
     * @param players valor del parametro {@code players}
     */
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

    /**
     * Ejecuta la operacion {@code finishGame}.
     *
     * @param players valor del parametro {@code players}
     */
    private void finishGame(List<Player> players) {
        gameFinished = true;

        List<Player> ranking = resultManager.calculateRanking(players);
        view.showGameResult(ranking);
    }

    /**
     * Indica si {@code MovementEnabled}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isMovementEnabled() {
        return movementEnabled;
    }

    /**
     * Indica si {@code GameStarted}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Indica si {@code GameFinished}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isGameFinished() {
        return gameFinished;
    }
}
