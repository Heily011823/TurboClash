# Documentacion tecnica clase por clase

## Como leer este documento

Cada fila resume, como minimo, los siguientes puntos pedidos por la auditoria:

1. archivo y paquete
2. responsabilidad principal
3. rol arquitectonico
4. atributos importantes
5. metodos principales
6. dependencias directas
7. entradas que recibe
8. salidas o efectos que produce
9. participacion en el flujo del juego
10. observaciones sobre SRP/SOLID
11. problemas detectados
12. recomendaciones de mejora

Las clases se organizan por paquete. Se priorizan `application`, `domain`, `infrastructure`, `presentation`, `network`, `model`, `services` y `config`.

## application

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| AuthoritativeMatchCoordinator | `src/main/java/.../application/AuthoritativeMatchCoordinator.java` `edu.autonoma.turboclash.application` | Coordina el match autoritativo; rol de orquestador del dominio de red | `match`, `engine`, `networkService`, `rulesManager`, `resultManager`, `items`, `obstacles`, `spawner`; `updateHostAuthority()`, `applySnapshot()`, `applyGameOver()` | Depende de `Match`, `GameEngine`, `GameNetworkService`, `GameRulesManager`, `GameResultManager` y `GameWindow`; recibe snapshots, start payloads y estado del loop | Agenda `GAME_START`, resuelve game over, aplica snapshots, reemplaza mundo | Alto valor arquitectonico; responsabilidad amplia pero coherente. Problema: concentra bastante coordinacion. Mejora: separar gestion de lobby y game over en servicios propios |
| GameApplication | `src/main/java/.../application/GameApplication.java` | Punto de entrada aplicativo para arrancar UI + contexto de juego; rol de inicializador | `bootstrap`, `config`; `start()`, `getPuerto()` | Depende de `GameBootstrap`, vistas Swing, config y peers cargados | Crea el frame principal, conecta peers y lanza el `GameLoop` | SRP razonable. Problema: mezcla bootstrap de UI y red. Mejora: extraer launcher de vista o configuracion de puertos |
| GameBootstrap | `src/main/java/.../application/GameBootstrap.java` | Construye el grafo principal del juego; rol de composition root | factories, `config`, `audioService`; `init()` | Depende de casi todas las factories y servicios de infraestructura/dominio | Devuelve `GameContext` totalmente armado | Parcialmente SRP: compone muchas cosas a la vez. Problema: constructor y `init()` largos. Mejora: builders internos o modulos de bootstrap |
| GameContext | `src/main/java/.../application/GameContext.java` | Contenedor de dependencias y estado compartido del runtime; rol de contexto aplicativo | `match`, `engine`, `network`, `peer`, `rulesManager`, `resultManager`, `coordinator`; getters y `addPlayer()` | Recibe todas las dependencias armadas por bootstrap | Expone acceso consistente a capas durante el loop | Util como holder. Problema: puede crecer demasiado como service locator. Mejora: dividir contextos de runtime/red/render |
| GameLoop | `src/main/java/.../application/GameLoop.java` | Ejecuta el ciclo principal; rol de loop de runtime | `frameDelay`, `networkSync`; `run()`, `shutdown()` | Usa `GameContext`, inputs y `GameWindow` | Actualiza autoridad host, UI, movimiento local y envio de red | Responsabilidad clara. Problema: aun coordina demasiadas decisiones de runtime. Mejora: separar substeps del frame en comandos/etapas |
| InputCoordinator | `src/main/java/.../application/InputCoordinator.java` | Coordina entrada de usuario; rol auxiliar aplicativo | clase ligera de coordinacion | Entradas de input | Efectos sobre autos y binding | Hoy tiene bajo peso arquitectonico. Problema: utilidad limitada frente al loop actual. Mejora: eliminar o integrar con un pipeline formal de input |
| NetworkSync | `src/main/java/.../application/NetworkSync.java` | Decide cuando emitir `MOVEMENT` y `SYNC`; rol de throttling de red | `lastX`, `lastY`, `lastScore`, timestamps de envio; `sync()` | Recibe `GameContext`, `Player` y tiempos del loop | Reduce trafico innecesario y dispara heartbeats/snapshots | Muy alineado a SRP. Problema: politicas de envio embebidas. Mejora: parametrizar umbrales desde `GameConfig` |

## config

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameConfig | `src/main/java/.../config/GameConfig.java` | Centraliza configuracion base; rol de configuracion del sistema | puertos, `frameDelay`, vidas iniciales, `targetScore`, cooldown, spawn points; getters | No depende de capas altas | Entrega parametros a factories y bootstrap | Correcta como config. Problema: mezcla red, viewport y gameplay. Mejora: dividir por dominios de configuracion |
| SpawnPoint | `src/main/java/.../config/SpawnPoint.java` | Representa punto de generacion; rol de DTO/config | coordenadas y tipo | Datos de spawn | Facilita definicion de objetos de mundo | Clase limpia. Mejora: documentar si sigue usandose o moverla a `domain.model` si gana semantica de negocio |

## domain.events

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| CollisionListener | `src/main/java/.../domain/events/CollisionListener.java` | Contrato de reaccion a colisiones; rol de interfaz de eventos | `onItemCollision`, `onObstacleCollision`, `onPlayersCollision` | Implementada por handlers y sonido | Desacopla deteccion de consecuencias | Buena ISP/DIP. Mejora: agregar eventos de eliminacion si se necesita trazabilidad adicional |

## domain.model

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| Car | `.../domain/model/Car.java` | Modelo del vehiculo; rol de entidad de juego | posicion, velocidad previa, `active`, `lives`, debuff, `finishReached`, `carImage`; `move()`, `reduceLife()`, `applyDebuff()` | Recibe input local y reglas del dominio | Expone bounds, estado activo y movimiento | Entidad razonable. Problema: mezcla fisica simple, vida y debuff. Mejora: extraer status effects si el dominio crece |
| CarSkin | `.../domain/model/CarSkin.java` | Enum de skins; rol de valor de presentacion/dominio | valores y nombre de archivo | Config/port mapping | Define assets de carro | Correcto. Mejora: desacoplar nombre de archivo de la logica de red si cambian recursos |
| CarSkinFactory | `.../domain/model/CarSkinFactory.java` | Asigna skin por puerto/config; rol de factory de presentacion del vehiculo | `fromPort()` | `GameConfig`, puertos | Devuelve skin consistente por jugador | Practico para 4 jugadores. Problema: rigido a puertos conocidos. Mejora: soportar seleccion dinamica de skin |
| GameObject | `.../domain/model/GameObject.java` | Base de objetos del mundo; rol de superclase abstracta | `id`, posicion, ancho, alto, visibilidad; `getBounds()`, `collidesWith()` | Heredada por `Car`, `Item`, `Obstacle` | Uniforma colision y posicion | Buena reutilizacion. Problema: `visible` mezcla dominio y render. Mejora: separar estado visual si el modelo crece |
| Item | `.../domain/model/Item.java` | Moneda/item del mundo; rol de entidad recolectable | `scoreValue` | Colisiones y snapshots | Aporta puntaje | Sencilla. Mejora: permitir distintos tipos de item si se extiende el juego |
| Match | `.../domain/model/Match.java` | Agregado raiz del estado del match; rol de estado compartido | jugadores, `started`, `finished`, winner, ranking, secuencias, tombstones, host port, timers | Recibe jugadores, snapshots y decisiones del coordinador | Fuente central de estado del match | Clase critica; buena cohesion relativa. Problema: acumula varias preocupaciones de estado. Mejora: extraer `LobbyState` y `PacketTracker` |
| Obstacle | `.../domain/model/Obstacle.java` | Entidad de obstaculo; rol de objeto peligroso del mundo | `type`, `processed` | Colisiones, snapshots | Penaliza puntaje/vidas indirectamente | Correcta. Mejora: especializar comportamiento por tipo si el dominio se enriquece |
| ObstacleType | `.../domain/model/ObstacleType.java` | Enum de tipos de obstaculo; rol de catalogo | imagen/tipo | Config y render | Simplifica creacion y render | Correcto. Mejora: anadir metadata de dano/debuff por tipo |
| Player | `.../domain/model/Player.java` | Modelo del jugador; rol de entidad principal | `id`, `name`, `car`, `score`, estado de meta/eliminacion, ordenes, secuencia, puerto | Red, reglas, ranking | Expone estado completo local/remoto | Central y util. Problema: mezcla red y dominio al guardar secuencia/port. Mejora: separar metadata de red en wrapper |
| Score | `.../domain/model/Score.java` | Encapsula puntos; rol de value object mutable | puntos y operaciones de update/set | Reglas del juego | Mantiene puntaje del jugador | Simple. Mejora: validar invariantes o auditar cambios si se quiere mas trazabilidad |

## domain.rules

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameStateValidator | `.../domain/rules/GameStateValidator.java` | Valida estados previos al inicio; rol de regla de validacion | `validateStart()` | entradas de UI | Lanza excepciones de validacion | Correcto. Mejora: unificar validadores bajo un paquete comun de validation |
| MovementValidator | `.../domain/rules/MovementValidator.java` | Valida movimientos; rol de regla de integridad | metodos de validacion | input y movimiento | Protege condiciones invalidas | Uso limitado en runtime actual. Mejora: integrarlo de verdad al pipeline de input |
| PlayerNameValidator | `.../domain/rules/PlayerNameValidator.java` | Valida nombre del jugador | `validate()` | string de entrada | Excepcion si el nombre es invalido | Bien para UI. Mejora: soportar mensajes localizados |
| PortValidator | `.../domain/rules/PortValidator.java` | Valida puertos de red | `validate()` | puerto | Excepcion si es invalido | Correcto. Mejora: incluir colision de puertos ocupados a nivel aplicativo |

## domain.services

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| CollisionManager | `.../domain/services/CollisionManager.java` | Detecta colisiones de dominio; rol de servicio de colisiones | listener, cooldown por pareja; `process()` y helpers | `Match`, listas de items/obstacles | Dispara eventos de colision | Muy relevante para la rubrica. Mejora: externalizar politica de cooldown |
| CompositeCollisionListener | `.../domain/services/CompositeCollisionListener.java` | Multiplexa listeners; rol de composite/observer | arreglo de listeners | Eventos de colision | Reenvia item/obstacle/player collisions | Buen patron. Mejora: usar lista inmutable si se dinamiza |
| GameCollisionHandler | `.../domain/services/GameCollisionHandler.java` | Traduce colisiones a reglas; rol de adaptador de dominio | `rules` | `CollisionListener` events | Cambia puntaje, vidas, finish | Correcto. Mejora: mover decisions de finish a un servicio aun mas explicito si crece |
| GameConstants | `.../domain/services/GameConstants.java` | Centraliza constantes de reglas | valores de coin, penalty, debuff, bonus | Reglas y servicios | Evita magic numbers | Correcto. Mejora: migrar a `config` si se vuelve configurable externamente |
| GameEngine | `.../domain/services/GameEngine.java` | Simula mundo y colisiones del host; rol de motor del dominio | `match`, `collisionManager`, `items`, `obstacles`; `update()` | match y mundo | Mueve objetos de mundo y procesa colisiones | Ahora mas coherente. Mejora: separar simulacion de mundo de simulacion de reglas |
| GameResultManager | `.../domain/services/GameResultManager.java` | Calcula ranking final; rol de servicio de resultados | `calculateRanking()` | lista de jugadores | ranking ordenado | Correcto. Mejora: extraer comparador nombrado para legibilidad |
| GameRulesManager | `.../domain/services/GameRulesManager.java` | Implementa reglas del juego; rol de servicio de negocio | targetScore, contadores de orden; `applyCoinReward()`, `applyObstaclePenalty()`, `handlePlayersCollision()`, `applyFinishBonus()` | recibe jugadores y eventos | actualiza score, vidas, finish y eliminacion | Clase central de rubrica. Mejora: descomponer reglas por estrategia si aumenta complejidad |
| GameSpawner | `.../domain/services/GameSpawner.java` | Genera items/obstaculos; rol de spawner de mundo | listas, `Random`, delay, `start()/stop()` | hilo interno y listas de mundo | inserta objetos nuevos | Funcional. Problema: usa `Random` local y sigue siendo relativamente simple. Mejora: permitir semillas o eventos de spawn persistibles |

## infrastructure (general)

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameFactory | `.../infrastructure/GameFactory.java` | Fabrica jugador/match base | `config`, `skinFactory`; `createPlayer()`, `createMatch()` | Config y puerto local | Crea jugador local con skin/posicion coherente | Buena factory. Mejora: crear tambien variantes para remotos si se quiere evitar duplicacion |
| NetworkFactory | `.../infrastructure/NetworkFactory.java` | Construye red UDP concreta | `createPeer()` | socket, handler, factory, puerto | Devuelve `UdpPeer` listo para escuchar | Correcta. Mejora: extraer builder de receiver/listener para testear mas facil |
| WorldFactory | `.../infrastructure/WorldFactory.java` | Crea mundo inicial | `createItems()`, `createObstacles()` | `GameConfig` y viewport | Entrega listas iniciales de mundo | Util. Mejora: documentar que el mundo luego pasa a ser controlado por el host |

## infrastructure.input

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameInputBinder | `.../infrastructure/input/GameInputBinder.java` | Conecta input con vista | binding de listeners | teclado/raton y paneles | Registra eventos Swing | Clase utilitaria. Mejora: consolidar con `InputCoordinator` |
| InputHandler | `.../infrastructure/input/InputHandler.java` | Contrato de input | metodos de actualizacion | autos y dispositivos | Permite polimorfismo de entrada | Buena ISP. Mejora: usarlo mas explicitamente en el loop |
| KeyboardInput | `.../infrastructure/input/KeyboardInput.java` | Maneja teclado | estado de teclas y `update()` | eventos de teclado | mueve el carro local | Correcto. Mejora: desacoplar mapeo de teclas a comandos |
| MouseInput | `.../infrastructure/input/MouseInput.java` | Maneja mouse | posicion y `update()` | eventos del mouse | mueve el carro local | Poco comun pero valido. Mejora: unificar semantica de input con teclado |

## infrastructure.network.config

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| PeerConfigEntry | `.../network/config/PeerConfigEntry.java` | DTO de peer | `nombre`, `ip`, `puerto` y getters/setters | `peers.json` | Representa peers configurados | Correcto. Mejora: hacerlo inmutable |
| PeerConfigLoader | `.../network/config/PeerConfigLoader.java` | Lee `peers.json`; rol de loader de config | `loadFromResource()` | recurso del classpath | lista de peers | Correcto. Problema: parser manual simple. Mejora: validar formato o usar parser robusto si el entorno lo permite |

## infrastructure.network.core

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameNetworkService | `.../network/core/GameNetworkService.java` | Servicio alto nivel de red; rol de facade UDP | `peer`, `messageFactory`, `sequenceGenerator`; `sendMovement()`, `sendSnapshot()`, `sendGameOver()` | `Match`, `Player`, snapshots, peers | Emite mensajes tipados por UDP | Muy importante para rubrica. Mejora: separar emisor de eventos y emisor de snapshots |
| IMessageListener | `.../network/core/IMessageListener.java` | Contrato para recibir mensajes | `onMessage()` | mensajes y origen | desacopla receiver del handler | Correcto |
| IMessageReceiver | `.../network/core/IMessageReceiver.java` | Contrato de recepcion | `escuchar()`, `detener()`, `setListener()` | socket y listener | escucha red | Correcto |
| IMessageSender | `.../network/core/IMessageSender.java` | Contrato de envio | `enviarMensaje()` | mensaje, ip, puerto | envia datagramas | Correcto |
| NetworkConfig | `.../network/core/NetworkConfig.java` | Deriva configuracion de puertos/peers | getters de puertos | `GameConfig` | ayuda a discovery y peer bootstrap | Correcto. Mejora: unificar con `PeerConfigLoader` |
| PeerInfo | `.../network/core/PeerInfo.java` | DTO de peer activo | ip, puerto | discovery y joins | lista de peers conocidos | Correcto |
| UdpPeer | `.../network/core/UdpPeer.java` | Mantiene socket, peers y receiver thread | socket, peers, sender, receiver; `iniciar()`, `agregarPeer()`, `enviarATodos()` | mensajes, IPs y puertos | punto operativo P2P | Clase util. Mejora: agregar remove peer y healthcheck formal |
| UdpReceiver | `.../network/core/UdpReceiver.java` | Escucha datagramas | socket, listener, activo | bytes UDP entrantes | parsea `GameMessage` y delega | Se redujo ruido de logs. Mejora: telemetria opcional y manejo de errores mas fino |
| UdpSender | `.../network/core/UdpSender.java` | Envia datagramas UDP | socket; `enviarMensaje()` | `GameMessage`, ip, puerto | serializa y envia | Correcto. Mejora: retries opcionales o metricas de envio |

## infrastructure.network.factory

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameMessageFactory | `.../network/factory/GameMessageFactory.java` | Crea mensajes desde jugadores | `create()`, `createDiscovery()`, `createEvent()` | `Player`, tipo, puerto, secuencia | `GameMessage` listo para enviar | Bien aplicado. Mejora: encapsular tambien conversiones desde snapshot si aumenta el protocolo |

## infrastructure.network.handler

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameMessageHandler | `.../network/handler/GameMessageHandler.java` | Enruta mensajes entrantes; rol de dispatcher | mapa de strategies, `processedJoins`, `match`, `coordinator`; `handle()` | `GameMessage`, ip, puerto | aplica estrategia, descarta obsoletos, protege local/host | Pieza central del patron Strategy. Mejora: extraer filtros de frescura/autoria en helpers dedicados |

## infrastructure.network.message

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GameMessage | `.../network/message/GameMessage.java` | Envelope principal UDP | tipo, jugador, posicion, score, lives, sequence, flags terminales, port; `serialize()/deserialize()` | bytes/string de red | transporte de mensajes basicos | Muy importante. Problema: formato manual string delimitado. Mejora: checksum/version de protocolo |
| GameStartPayload | `.../network/message/GameStartPayload.java` | DTO de inicio | host, sequence, scheduledStartTime, jugadores | `GAME_START` | countdown sincronizado | Correcto |
| MatchSnapshot | `.../network/message/MatchSnapshot.java` | DTO de snapshot del match | host, sequence, started, finished, timer, winner, players, items, obstacles | `SYNC` y `GAME_OVER` | replica estado autoritativo | Correcto. Mejora: delta snapshots para bajar trafico |
| MessagePayloadCodec | `.../network/message/MessagePayloadCodec.java` | Codec manual de payloads | `encode/decodeGameStart`, `encode/decodeSnapshot`, `decodePeers` | payloads y strings | evita dependencia externa en runtime | Pragmatico. Problema: parser custom menos robusto. Mejora: tests mas amplios/versionado |
| MessageType | `.../network/message/MessageType.java` | Enum de tipos de mensaje | tipos UDP | handlers y services | selecciona estrategias | Correcto |
| PlayerState | `.../network/message/PlayerState.java` | DTO de jugador remoto | posicion, score, lives, estado terminal, port, sequence | snapshots | sincronizacion remota | Correcto |
| WorldObjectState | `.../network/message/WorldObjectState.java` | DTO del mundo | id, posicion, dimensiones, visibilidad, tipo | snapshots | replica items y obstaculos | Correcto |

## infrastructure.network.strategy

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| IMessageStrategy | `.../network/strategy/IMessageStrategy.java` | Contrato de procesamiento por mensaje | `handle()` | `GameMessage` | estrategia pluggable | Buen OCP/ISP |
| JoinStrategy | `.../network/strategy/JoinStrategy.java` | Alta y actualizacion de remotos | `handle()`, `createRemotePlayer()` | `Match`, `GameMessage` | crea/sincroniza jugadores remotos | Corregida para usar Y real y no ignorar vidas 0 |
| LeaveStrategy | `.../network/strategy/LeaveStrategy.java` | Baja de jugador remoto | `handle()` | `Match`, `PLAYER_LEFT` | remueve y tombstonea remotos | Correcta. Mejora: expiracion de tombstones si se soporta reconexion |
| MoveStrategy | `.../network/strategy/MoveStrategy.java` | Aplica movimiento remoto | `handle()` | `Match`, `MOVEMENT` | actualiza estado remoto | Corregida para no forzar carriles por puerto/id |
| ScoreStrategy | `.../network/strategy/ScoreStrategy.java` | Sincroniza score aislado | `handle()` | `Match`, `SCORE_UPDATE` | actualiza puntos | Poco usada hoy porque el snapshot autoritativo manda mas que score aislado. Mejora: retirar si deja de usarse |
| GameStartStrategy | `.../network/strategy/GameStartStrategy.java` | Aplica inicio remoto | `handle()` | `AuthoritativeMatchCoordinator` | countdown sincronizado | Correcta |
| SyncStrategy | `.../network/strategy/SyncStrategy.java` | Aplica snapshot autoritativo | `handle()` | coordinator y payload | sincroniza jugadores y mundo | Correcta y crucial |
| GameOverStrategy | `.../network/strategy/GameOverStrategy.java` | Aplica game over remoto | `handle()` | coordinator y payload | muestra cierre consistente | Correcta |

## infrastructure.sound

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| IAudioService | `.../sound/IAudioService.java` | Contrato de audio | play menu/coin/brake/countdown/win | vistas y bootstrap | desacopla implementacion de audio | Correcto |
| SoundCollisionListener | `.../sound/SoundCollisionListener.java` | Reacciona a colisiones con sonido | `onItemCollision`, `onObstacleCollision`, `onPlayersCollision` | `CollisionListener` events | reproduce efectos de sonido | Buen desacople. Mejora: diferenciar sonido por tipo de obstaculo |
| SoundManager | `.../sound/SoundManager.java` | Gestor de audio singleton | `backgroundClip`, `effectClips`, `playBackground()`, `playEffect()`, `stopBackground()` | recursos wav y peticiones de vistas/servicios | musica y efectos | Cumple Singleton real. Problema: no es thread-safe estricto y maneja errores con stacktrace. Mejora: sincronizar acceso y logging mas limpio |

## presentation.navigation

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| IstartWindowListener | `.../presentation/navigation/IstartWindowListener.java` | Contrato del menu inicial | callbacks de UI | botones de start | navegacion | Correcto |
| IntroductionWindowListener | `.../presentation/navigation/IntroductionWindowListener.java` | Contrato de la pantalla de nombre | `onContinuePressed()` | nombre del jugador | paso a bootstrap | Correcto |

## presentation.presenter

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| GamePresenter | `.../presentation/presenter/GamePresenter.java` | Presenter legacy de UI | timer local, flags de juego, `update()`, `finishGame()` | `GameWindow`, `GameRulesManager`, `GameResultManager` | originalmente orquestaba countdown y cierre visual | Hoy quedo parcialmente desplazado por `GameLoop` + coordinator. Problema: deuda tecnica y posible confusion en sustentacion. Mejora: retirarlo o reconectarlo formalmente al flujo actual |

## presentation.view

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| EndGameWindow | `.../presentation/view/EndGameWindow.java` | Vista de resultados finales | labels de ganador, tiempo y ranking; `setResultados()` | strings de ranking | renderiza cierre del juego | Correcta. Mejora: recibir DTO de resultados en vez de seis strings |
| EndGameWindowFrame | `.../presentation/view/EndGameWindowFrame.java` | Frame de fin de juego | `view`; constructor arma fondo y listeners | datos finales y `SoundManager` | muestra ventana final y reproduce audio | Correcta. Mejora: desacoplar reinicio de `StartWindowFrame` |
| FondoAnimadoPanel | `.../presentation/view/FondoAnimadoPanel.java` | Fondo animado, timer visual y transicion a pantalla final | timers, metaX, segundosRestantes; `startGame()`, `terminarJuego()` | ranking y render de pista | anima fondo y calcula tiempo mostrado | Clase util pero mezcla animacion y decision visual de fin. Mejora: extraer calculo de tiempo y adaptacion a end screen |
| FondoPanel | `.../presentation/view/FondoPanel.java` | Panel de fondo estatico | imagen de fondo | ruta de imagen | soporte visual para frames | Correcto |
| GameViewport | `.../presentation/view/GameViewport.java` | Constantes y clamps de viewport | dimensiones, lanes, spawn X, clamp helpers | mundo y vehiculos | centraliza coordenadas base | Correcto. Mejora: mover a `config` si deja de ser puramente visual |
| GameWindow | `.../presentation/view/GameWindow.java` | Vista principal del juego | labels de score, countdown, HUD de jugadores, mapas de sprites, cache de iconos; `updateCars()`, `updateItems()`, `updateObstacles()`, `startSynchronizedCountdown()` | estado del match, listas de mundo, audio | renderiza jugadores, HUD y countdown | Critica para rubrica visual. Problema: mezcla layout, HUD responsive y render state. Mejora: dividir HUD/sprites/scoreboard |
| GameWindowFrame | `.../presentation/view/GameWindowFrame.java` | Frame principal de juego | configura `GameWindow`, panel de fondo y bindings | keyboard/mouse | ventana de partida | Correcto. Mejora: documentar mejor el wiring con background panel |
| IntroductionWindow | `.../presentation/view/IntroductionWindow.java` | Vista de ingreso de nombre y reglas | `txtName`, botones, icono info; `showRules()` | input de usuario, audio service | pide nombre y muestra reglas | Cumple rubrica. Mejora: externalizar reglas a texto centralizado del dominio |
| IntroductionWindowFrame | `.../presentation/view/IntroductionWindowFrame.java` | Frame que valida nombre y arranca juego | `view`, `startGame()` | validadores, bootstrap, config | inicia `GameApplication` | Correcta. Mejora: mover composicion de bootstrap fuera de la vista |
| StartWindow | `.../presentation/view/StartWindow.java` | Vista del menu inicial | botones/menu principal | listener y audio | abre introduccion | Correcta |
| StartWindowFrame | `.../presentation/view/StartWindowFrame.java` | Frame de portada | listeners de navegacion | `StartWindow`, `SoundManager` | primera pantalla del juego | Correcta. Mejora: centralizar navegacion en un router de UI |
| ViewSynchronizer | `.../presentation/view/ViewSynchronizer.java` | Sincroniza UI con estado del match | `sync()` | `GameWindow`, `Match`, items, obstacles | actualiza score, scoreboard, carros, mundo | Muy buena SRP. Mejora: aceptar DTO visuales si se quiere desacoplar del dominio |

## main

| Clase | Archivo y paquete | Responsabilidad / rol | Atributos y metodos clave | Dependencias / entradas | Salidas / flujo | SOLID, problemas y mejora |
|---|---|---|---|---|---|---|
| Main | `src/main/java/edu/autonoma/turboclash/main/Main.java` | Punto de entrada Java | `main()` | Swing | abre `StartWindowFrame` | Correcto y simple |

## Observaciones globales por clases

- Las clases de `application` y `network` sostienen hoy el cumplimiento funcional principal de la rubrica.
- `GamePresenter` es la mayor deuda de claridad arquitectonica porque ya no domina el flujo principal.
- `GameWindow` y `FondoAnimadoPanel` concentran demasiado comportamiento visual.
- El paquete `exception` no se documenta clase por clase aqui porque contiene excepciones simples de validacion/infraestructura con bajo peso arquitectonico.


