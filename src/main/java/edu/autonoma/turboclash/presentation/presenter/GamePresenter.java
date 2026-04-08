package edu.autonoma.turboclash.presentation.presenter;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.presentation.view.GameWindow;

import java.util.List;

public class GamePresenter {

    private final GameWindow view;
    private final GameRulesManager rulesManager;
    private final GameResultManager resultManager;

    private boolean gameFinished = false;

    public GamePresenter(GameWindow view,
                         GameRulesManager rulesManager,
                         GameResultManager resultManager) {
        this.view = view;
        this.rulesManager = rulesManager;
        this.resultManager = resultManager;
    }

    public void onCarUpdated(Car car, List<Player> players) {
        if (car == null || players == null) return;

        checkFinish(car, players);
        checkGameEnd(players);
    }


    private void checkFinish(Car car, List<Player> players) {
        if (view == null || view.getPlayers() == null) return;

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
        if (gameFinished || players.isEmpty()) return;

        int aliveCount = 0;
        boolean someoneReachedFinish = false;

        for (Player player : players) {
            if (player != null && player.isAlive()) {
                aliveCount++;
            }

            if (player != null && player.isFinishReached()) {
                someoneReachedFinish = true;
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
}