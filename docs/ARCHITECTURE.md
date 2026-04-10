# Arquitectura de TurboClash

## Resumen general del juego

TurboClash es un juego de carreras multijugador en Java Swing donde entre 2 y 4 jugadores compiten por llegar a la meta, sumar puntos y evitar ser eliminados. Cada jugador ejecuta una instancia local, pero el estado efectivo del match se coordina por UDP con una autoridad logica.

## Arquitectura del sistema

Capas principales:

- `application`: orquesta el ciclo de vida del juego, el loop principal, el contexto compartido y la coordinacion autoritativa.
- `domain`: contiene modelo de negocio, reglas de juego, colisiones, ranking y spawns.
- `infrastructure`: implementa red UDP, audio, manejo de entrada y fabricas de objetos.
- `presentation`: renderiza ventanas Swing, countdown, HUD y pantalla final.
- `config`: centraliza parametros base y spawn points.

### Componentes clave

- `AuthoritativeMatchCoordinator`: decide inicio real, estado del match, timer, game over y sincronizacion del mundo.
- `GameNetworkService`: emite mensajes UDP tipados y snapshots autoritativos.
- `GameMessageHandler`: enruta mensajes por estrategia y descarta paquetes obsoletos.
- `CollisionManager`: procesa colisiones de dominio, incluidas jugador-jugador.
- `ViewSynchronizer`: aplica el estado del match a la interfaz.

## Comunicacion UDP / P2P

### Topologia

- El juego sigue siendo peer-to-peer a nivel de transporte: cada cliente mantiene sockets UDP y conoce a los demas peers.
- A nivel logico existe un host autoritativo minimo: el peer con menor puerto activo conocido.

### Tipos de mensaje

| Mensaje | Uso |
|---|---|
| `DISCOVERY` | descubrimiento inicial |
| `HANDSHAKE` | deteccion de presencia entre peers |
| `PLAYER_JOINED` | alta real de jugador remoto |
| `MOVEMENT` | estado/input local con secuencia |
| `SYNC` | snapshot autoritativo de jugadores y mundo |
| `GAME_START` | inicio sincronizado con timestamp |
| `GAME_OVER` | cierre sincronizado con ranking y motivo |
| `PLAYER_LEFT` | salida o desconexion real |

### Flujo de lobby

1. Cada cliente abre su `UdpPeer`.
2. Se leen peers desde `peers.json`.
3. Se envian `DISCOVERY`, `HANDSHAKE` y `PLAYER_JOINED`.
4. El host se determina por el menor puerto activo.
5. Cuando hay al menos `minPlayers = 2`, el host agenda `GAME_START`.

### Flujo de actualizacion

1. Cada cliente envia `MOVEMENT` con secuencia y estado local.
2. El host procesa esas entradas, simula el dominio y actualiza el mundo.
3. El host emite `SYNC` cada pocos milisegundos.
4. Los clientes aplican snapshots y descartan paquetes viejos por secuencia.

### Flujo de colisiones y eliminacion

1. El host ejecuta `CollisionManager`.
2. Si hay colision entre jugadores, `GameRulesManager` baja vidas con cooldown por pareja.
3. Si un jugador llega a `0` vidas, queda eliminado.
4. El estado terminal se replica en snapshots y no puede ser revivido por mensajes tardios.

### Flujo de game over

1. El host detecta cierre por meta, eliminacion, timeout o fin del recorrido.
2. Calcula ranking mediante `GameResultManager`.
3. Envia `GAME_OVER` con ranking, ganador y motivo.
4. Todos los clientes muestran la misma pantalla final.

## Reglas del juego

Reglas efectivas implementadas:

1. Moneda recogida: suma puntos.
2. Obstaculo: resta puntaje.
3. Puntaje agotado despues de haber puntuado: resta una vida.
4. Colision jugador-jugador: resta una vida a ambos.
5. Llegada a meta: bonus y orden de llegada.
6. Jugador con `0` vidas: eliminado.
7. Fin del match: meta, eliminacion, timeout o fin de pista.

## Eventos del juego

Eventos relevantes soportados:

- inicio sincronizado de carrera
- recoleccion de items
- colision con obstaculos
- colision entre jugadores
- eliminacion
- salida de jugador
- fin de partida

## Sonidos automaticos

Sonidos disparados automaticamente por eventos:

- menu inicial
- countdown
- recoleccion de moneda
- colision
- victoria
- cierre sin ganador explicito

## Patrones de diseno

### Strategy

- `IMessageStrategy` abstrae el tratamiento por mensaje.
- Implementaciones: `JoinStrategy`, `MoveStrategy`, `LeaveStrategy`, `ScoreStrategy`, `GameStartStrategy`, `SyncStrategy`, `GameOverStrategy`.
- Beneficio: `GameMessageHandler` queda abierto a extension sin una cascada rigida de `if/else`.

### Observer / Listener

- `CollisionListener` define eventos de colision.
- `CompositeCollisionListener` compone listeners.
- `GameCollisionHandler` aplica reglas y `SoundCollisionListener` dispara audio.
- Beneficio: logica de colision, feedback sonoro y futuras reacciones quedan desacopladas.

### Factory

- `GameFactory`, `WorldFactory`, `NetworkFactory`, `GameMessageFactory`.
- Beneficio: centralizan construccion y reducen duplicacion.

### Singleton

- `SoundManager`.
- Beneficio: evita multiples gestores de audio compitiendo por los mismos recursos.

## Aplicacion de SOLID

### SRP

Se cumple de forma razonable en:

- `GameRulesManager`
- `CollisionManager`
- `GameNetworkService`
- `ViewSynchronizer`
- factories y estrategias de mensaje

Desviaciones:

- `GameBootstrap` concentra demasiada composicion.
- `GameWindow` mezcla widgets, layout responsive y parte de la logica del HUD.

### OCP

- La estrategia por mensajes mejora extension sin modificar el handler central.
- Las reglas podrian crecer mejor si se descompusieran aun mas en objetos de regla independientes.

### LSP

- No hay jerarquias complejas con riesgo alto, salvo listeners y estrategias que respetan bien sus contratos.

### ISP

- Interfaces pequenas y especificas: `IAudioService`, `IMessageSender`, `IMessageReceiver`, `IMessageListener`, `IMessageStrategy`.

### DIP

- El audio y colisiones dependen de abstracciones.
- Aun hay dependencia fuerte de implementaciones concretas en bootstrap y algunas vistas.

## Dificultades tecnicas, deuda y mejoras

### Deuda tecnica identificada

- No existe migracion de host si el peer autoritativo se desconecta.
- `peers.json` sigue siendo necesario como configuracion inicial.
- `GamePresenter` quedo como componente legado y no gobierna el flujo real actual.
- El codec manual de payloads es suficiente para el proyecto, pero poco ergonomico frente a formatos mas robustos.

### Mejoras aplicadas durante la auditoria

- Panel de estado de jugadores en tiempo real en la interfaz principal.
- Countdown con sonido sincronizado.
- Senal sonora diferenciada al cierre de partida.
- README y documentacion tecnica ampliados para sustentacion.

### Mejoras futuras recomendadas

- host migration o failover
- lobby dinamico con ready states reales
- reconexion tardia y sincronizacion incremental
- pruebas automatizadas con framework formal y CI
