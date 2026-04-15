# TurboClash

 [Repositorio en GitHub](https://github.com/Heily011823/TurboClash.git)

TurboClash es un juego de carreras multijugador desarrollado en Java Swing, con comunicación UDP entre peers.
La versión actual implementa un esquema **host-autoritativo mínimo**, donde uno de los clientes asume la autoridad lógica de la partida y sincroniza inicio, estado de jugadores, mundo, colisiones y final del match para reducir divergencias.

---

## Características principales

* Comunicación UDP real entre instancias del juego.
* Soporte para **2 a 4 jugadores**.
* Inicio sincronizado mediante `GAME_START`.
* Sincronización periódica del estado mediante `SYNC`.
* Final de partida consistente mediante `GAME_OVER`.
* Ranking final con ganador, tiempo total y estado de cada jugador.
* Sonidos automáticos por eventos relevantes.

---

## Requisitos

* Java 17 o superior.
* Maven 3.9 o superior.
* Puertos UDP `5001` a `5004` disponibles.
* Archivo `src/main/resources/peers.json` configurado con las IP/puertos de los jugadores.

---

## Ejecución

1. Configura `src/main/resources/peers.json` con las máquinas que participarán.
2. Inicia una instancia del juego por cada jugador.
3. En cada instancia indica un puerto válido entre `5001` y `5004`.
4. Ingresa el nombre del jugador cuando la interfaz lo solicite.
5. Espera al menos 2 jugadores conectados para que el host programe el inicio.

### Ejemplo con Maven

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=edu.autonoma.turboclash.main.Main -Dpuerto=5001
```

En entornos restringidos puede usarse:

```bash
mvn -s .mvn-settings.xml clean compile
```

---

## Reglas del juego

1. Recoger monedas suma puntos.
2. Chocar con obstáculos resta puntaje.
3. Si el puntaje cae a cero después de haber puntuado, el jugador pierde una vida.
4. Chocar con otro carro quita una vida a ambos jugadores (con cooldown).
5. Llegar a la meta otorga un bono y prioridad en el ranking.
6. Un jugador con `0` vidas queda eliminado.
7. El host finaliza la partida por meta, eliminación, timeout o fin del recorrido.

---

## Arquitectura del proyecto

El proyecto está organizado en capas para separar responsabilidades:

* **application**: coordinación del flujo del juego y lógica del match.
* **domain**: modelo del juego, reglas y servicios.
* **infrastructure**: red UDP, audio, entradas y fábricas.
* **presentation**: interfaz gráfica (Swing) y sincronización visual.
* **config**: configuración general (puertos, constantes, spawn points).

### Estructura del proyecto

```text
src/main/java/edu/autonoma/turboclash
├── application
├── config
├── domain
├── infrastructure
├── main
└── presentation
```

---

## Red y sincronización

El juego utiliza un modelo híbrido P2P con host autoritativo.

### Mensajes principales

* `HANDSHAKE`: descubrimiento inicial.
* `PLAYER_JOINED`: unión al match.
* `MOVEMENT`: estado/input del jugador.
* `SYNC`: snapshot autoritativo del host.
* `GAME_START`: inicio sincronizado.
* `GAME_OVER`: resultado final sincronizado.
* `PLAYER_LEFT`: salida o desconexión.

El host se determina como el cliente con el menor puerto activo conocido.

---

## Ranking final

El ranking se calcula considerando:

1. Jugadores que llegan a la meta.
2. Orden de llegada.
3. Jugadores que permanecen vivos.
4. Orden de eliminación.
5. Puntaje acumulado.

Esto garantiza consistencia entre todos los clientes.

---

## Patrones de diseño utilizados

* **Strategy**: manejo de mensajes UDP mediante `IMessageStrategy` y sus implementaciones.
* **Observer / Listener**: desacoplamiento de eventos de colisión.
* **Factory**: creación centralizada de objetos y mensajes.
* **Singleton**: gestión global del audio (`SoundManager`).

---

## Principios SOLID aplicados

* **SRP (Single Responsibility)**: separación clara en componentes como colisiones, reglas, red y sincronización visual.
* **OCP (Open/Closed)**: se pueden agregar nuevos tipos de mensajes sin modificar el sistema base.
* **DIP (Dependency Inversion)**: uso de interfaces como `IAudioService` y `CollisionListener`.

---

## Limitaciones actuales

* No existe migración de host si el host se desconecta durante la partida.
* `peers.json` sigue siendo el mecanismo inicial de descubrimiento (no hay lobby dinámico completo).
* Algunas vistas usan posicionamiento absoluto, lo que puede afectar la adaptación visual.
* **La pantalla final está diseñada visualmente para un podio de 3 jugadores; cuando participan 4, el cuarto lugar se muestra fuera del podio principal, lo que puede generar diferencias visuales aunque el ranking sea correcto.**

---

## Validación

* Compilación exitosa con Maven.
* Pruebas manuales realizadas con múltiples instancias.
* Validación de sincronización y resultados mediante ejecución distribuida.

---

## Documentación adicional

* `docs/ARCHITECTURE.md`
* `docs/CLASSES.md`
* `docs/RUBRICA.md`

---

## Autoras

* Elizabeth Meneses Muñoz
* Valerie Moreno
* María Paz Puerta Acevedo
* Heily Yohana Ríos Ayala
