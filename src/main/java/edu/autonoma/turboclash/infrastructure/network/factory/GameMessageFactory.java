package edu.autonoma.turboclash.infrastructure.network.factory;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;

public class GameMessageFactory {

    public GameMessageFactory() {
    }

    public GameMessage create(Player player, MessageType type, int port) {
        if (player == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }

        Car car = player.getCar();

        double posX = 0;
        double posY = 0;
        int lives = 0;
        CarSkin skin = null;

        if (car != null) {
            posX = car.getX();
            posY = car.getY();
            lives = car.getLives();
            skin = resolveSkin(car.getCarImage());
        }

        return new GameMessage(
                type,
                player.getId(),
                player.getName(),
                posX,
                posY,
                player.getCurrentPoints(),
                lives,
                System.currentTimeMillis(),
                null,
                skin,
                port
        );
    }

    public GameMessage createDiscovery(int port) {
        GameMessage msg = new GameMessage();
        msg.setType(MessageType.DISCOVERY);
        msg.setTime(System.currentTimeMillis());
        msg.setPort(port);
        return msg;
    }

    public GameMessage createEvent(Player player, MessageType type, String event, int port) {
        GameMessage msg = create(player, type, port);
        msg.setEvent(event);
        return msg;
    }

    private CarSkin resolveSkin(String carImage) {
        if (carImage == null) return CarSkin.BLUE;

        String value = carImage.trim().toLowerCase();

        if (value.contains("red")) return CarSkin.RED;
        if (value.contains("yellow")) return CarSkin.YELLOW;
        if (value.contains("brown")) return CarSkin.BROWN;
        return CarSkin.BLUE;
    }
}