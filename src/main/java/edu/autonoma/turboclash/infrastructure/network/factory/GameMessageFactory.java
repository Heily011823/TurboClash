package edu.autonoma.turboclash.infrastructure.network.factory;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;

/**
 * Representa la clase `GameMessageFactory` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class GameMessageFactory {

    /**
     * Crea una nueva instancia de `GameMessageFactory`.
     */
    public GameMessageFactory() {
    }

    /**
     * Ejecuta la operacion publica `create`.
     * @param player valor del parametro `player`
     * @param type valor del parametro `type`
     * @param port valor del parametro `port`
     * @param sequence valor del parametro `sequence`
     * @param authority valor del parametro `authority`
     * @return resultado de la operacion documentada
     */
    public GameMessage create(Player player, MessageType type, int port, long sequence, boolean authority) {
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
                sequence,
                null,
                skin,
                port,
                player.isFinishReached(),
                player.isEliminated(),
                player.getFinishOrder(),
                player.getEliminationOrder(),
                car != null && car.isActive(),
                authority
        );
    }

    /**
     * Ejecuta la operacion publica `create`.
     * @param player valor del parametro `player`
     * @param type valor del parametro `type`
     * @param port valor del parametro `port`
     * @return resultado de la operacion documentada
     */
    public GameMessage create(Player player, MessageType type, int port) {
        return create(player, type, port, System.currentTimeMillis(), false);
    }

    /**
     * Crea el recurso necesario para create discovery.
     * @param port valor del parametro `port`
     * @return resultado de la operacion documentada
     */
    public GameMessage createDiscovery(int port) {
        GameMessage msg = new GameMessage();
        msg.setType(MessageType.DISCOVERY);
        msg.setTime(System.currentTimeMillis());
        msg.setSequence(System.currentTimeMillis());
        msg.setPort(port);
        return msg;
    }

    /**
     * Crea el recurso necesario para create event.
     * @param player valor del parametro `player`
     * @param type valor del parametro `type`
     * @param event valor del parametro `event`
     * @param port valor del parametro `port`
     * @param sequence valor del parametro `sequence`
     * @param authority valor del parametro `authority`
     * @return resultado de la operacion documentada
     */
    public GameMessage createEvent(Player player, MessageType type, String event, int port, long sequence, boolean authority) {
        GameMessage msg = create(player, type, port, sequence, authority);
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
