package edu.autonoma.turboclash.infrastructure.network.factory;

import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;

/**
 * Crea y configura instancias relacionadas con {@code GameMessageFactory} en la fabrica de mensajes de red.
 */
public class GameMessageFactory {

    /**
     * Crea la operacion principal del metodo.
     *
     * @param player valor del parametro {@code player}
     * @param type valor del parametro {@code type}
     * @return resultado de la operacion {@code create}
     */
    public GameMessage create(Player player, MessageType type) {
        if (player == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }

        GameMessage msg = new GameMessage();

        msg.setType(type);
        msg.setPlayerId(player.getId());
        msg.setPlayerName(player.getName());

        if (player.getCar() != null) {
            msg.setPosX(player.getCar().getX());
            msg.setPosY(player.getCar().getY());
            msg.setCarSkin(resolveSkin(player));
        } else {
            msg.setPosX(80);
            msg.setPosY(80);
            msg.setCarSkin(CarSkin.BLUE);
        }

        msg.setScore(player.getCurrentPoints());
        msg.setTime(System.currentTimeMillis());
        msg.setEvent(null);

        return msg;
    }

    public GameMessage createDiscovery() {
        GameMessage msg = new GameMessage();

        msg.setType(MessageType.DISCOVERY);
        msg.setTime(System.currentTimeMillis());

        return msg;
    }

    private CarSkin resolveSkin(Player player) {
        if (player == null || player.getCar() == null || player.getCar().getCarImage() == null) {
            return CarSkin.BLUE;
        }

        String image = player.getCar().getCarImage().trim();

        if (image.startsWith("/image/")) {
            image = image.substring("/image/".length());
        }

        for (CarSkin skin : CarSkin.values()) {
            if (skin.getFileName().equalsIgnoreCase(image)) {
                return skin;
            }
        }

        return CarSkin.BLUE;
    }
}