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
        view.getPanel().setPreferredSize(GameViewport.size());
        view.getPanel().setMinimumSize(GameViewport.size());

        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));

        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(Color.BLACK);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
            fondo.setLayout(new BorderLayout());
            fondo.setPreferredSize(GameViewport.size());
            fondo.setMinimumSize(GameViewport.size());
            fondo.add(view.getPanel(), BorderLayout.CENTER);
            rootPanel.add(fondo);
        } catch (Exception e) {
            JPanel fallbackPanel = new JPanel(new BorderLayout());
            fallbackPanel.setPreferredSize(GameViewport.size());
            fallbackPanel.setMinimumSize(GameViewport.size());
            fallbackPanel.add(view.getPanel(), BorderLayout.CENTER);
            rootPanel.add(fallbackPanel);
            System.err.println("No se pudo cargar el fondo animado: " + e.getMessage());
        }

        setContentPane(rootPanel);

        new GameInputBinder(keyboardInput, mouseInput).bind(view.getPanel());

        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
