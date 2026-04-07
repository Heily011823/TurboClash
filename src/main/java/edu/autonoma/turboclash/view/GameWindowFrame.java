package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.GameInputBinder;
import edu.autonoma.turboclash.input.KeyboardInput;
import edu.autonoma.turboclash.input.MouseInput;
import edu.autonoma.turboclash.model.Car;
import edu.autonoma.turboclash.model.CarSkin;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameWindowFrame extends JFrame {

    private final GameWindow view;
    private final List<Car> cars = new ArrayList<>();

    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.view = new GameWindow();

        view.getPanel().setOpaque(false);

        setTitle("TurboClash - Racing Game");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
            fondo.setLayout(new BorderLayout());
            fondo.add(view.getPanel());
            setContentPane(fondo);
        } catch (Exception e) {
            setContentPane(view.getPanel());
            System.err.println("No se pudo cargar el fondo animado: " + e.getMessage());
        }

        new GameInputBinder(keyboardInput, mouseInput).bind(view.getPanel());

        setVisible(true);
        view.requestGameFocus();

        iniciarJuego();
    }

    private void iniciarJuego() {
        cars.clear();

        cars.add(new Car("car1", 0, 0, CarSkin.BLUE));
        cars.add(new Car("car2", 0, 0, CarSkin.RED));
        cars.add(new Car("car3", 0, 0, CarSkin.YELLOW));
        cars.add(new Car("car4", 0, 0, CarSkin.BROWN));

        view.prepararInicioCarrera(cars);

        view.iniciarCuentaRegresiva(() -> {

        });
    }

    public GameWindow getView() {
        return view;
    }
}