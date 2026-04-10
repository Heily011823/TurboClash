# TurboClash

TurboClash es un juego de carreras multijugador en Java Swing con comunicacion UDP entre peers. La version actual usa un esquema **host-autoritativo minimo**: uno de los clientes asume la autoridad logica del match y sincroniza inicio, estado de jugadores, mundo, colisiones y fin de partida para reducir divergencias.

## Estado actual del proyecto

- Comunicacion UDP real entre instancias del juego.
- Soporte para 2 a 4 jugadores.
- Inicio sincronizado mediante `GAME_START`.
- Sincronizacion periodica del match y del mundo mediante `SYNC`.
- Fin de partida consistente mediante `GAME_OVER`.
- Ranking final con ganador, tiempo total y estado de cada jugador.
- Sonidos automaticos por eventos relevantes.

## Requisitos

- Java 17 o superior.
- Maven 3.9+ para compilar el proyecto.
- Puertos UDP 5001 a 5004 disponibles.
- Archivo `src/main/resources/peers.json` configurado con las IP/puertos de los jugadores.

## Ejecucion

1. Configura `src/main/resources/peers.json` con las maquinas que participaran.
2. Inicia una instancia por jugador.
3. En cada instancia indica un puerto valido entre `5001` y `5004`.
4. Ingresa el nombre del jugador cuando la interfaz lo solicite.
5. Espera al menos 2 jugadores conectados para que el host programe el inicio.

Ejemplo con Maven:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=edu.autonoma.turboclash.main.Main -Dpuerto=5001
```

En entornos restringidos como sandbox puede usarse `-s .mvn-settings.xml` para relocalizar el repositorio local de Maven.

## Reglas del juego

1. Recoger monedas suma puntos.
2. Chocar con obstaculos resta puntaje.
3. Si el puntaje cae a cero despues de haber puntuado, el jugador pierde una vida.
4. Chocar con otro carro quita una vida a ambos jugadores, con cooldown para evitar dano por frame.
5. Llegar a la meta otorga bono y prioridad en el ranking.
6. Un jugador con `0` vidas queda eliminado y deja de participar activamente.
7. El host cierra la partida por meta, eliminacion, timeout o fin del recorrido.

## Arquitectura

Capas principales:

- `application`: coordinacion del flujo del juego, loop, contexto y autoridad del match.
- `domain`: modelo, reglas y servicios del juego.
- `infrastructure`: red UDP, audio, entradas y fabricas.
- `presentation`: ventanas Swing, sincronizacion visual y pantalla final.
- `config`: puertos, constantes de configuracion y spawn points.

Documentacion ampliada:

- `docs/ARCHITECTURE.md`
- `docs/CLASSES.md`
- `docs/RUBRICA.md`

## Red UDP / Red UDP con modelo híbrido (P2P con host autoritativo)

Mensajes principales:

- `HANDSHAKE`: descubrimiento inicial.
- `PLAYER_JOINED`: union real al match.
- `MOVEMENT`: estado/input del jugador local con secuencia.
- `SYNC`: snapshot autoritativo del host con jugadores y mundo.
- `GAME_START`: inicio real sincronizado con timestamp.
- `GAME_OVER`: resultado final sincronizado.
- `PLAYER_LEFT`: salida o desconexion real.

El host se elige por el menor puerto activo conocido. Los clientes no host no resuelven localmente el resultado final del match.

## Patrones de diseno usados

Patrones aplicados con evidencia real:

- **Strategy**: `IMessageStrategy` y sus implementaciones (`JoinStrategy`, `MoveStrategy`, `SyncStrategy`, etc.) separan el tratamiento por tipo de mensaje UDP.
- **Observer/Listener**: `CollisionListener`, `CompositeCollisionListener` y `SoundCollisionListener` desacoplan la deteccion de colisiones de sus reacciones.
- **Factory**: `GameFactory`, `WorldFactory`, `NetworkFactory` y `GameMessageFactory` centralizan creacion de objetos y mensajes.
- **Singleton**: `SoundManager` gestiona audio compartido del juego.

## SOLID

Aplicaciones reales:

- SRP: `CollisionManager`, `GameRulesManager`, `GameNetworkService`, `ViewSynchronizer` y las factories tienen responsabilidades acotadas.
- OCP: nuevas estrategias de mensajes pueden agregarse sin reescribir el handler principal.
- DIP: la capa de sonido usa `IAudioService`; colisiones dependen de `CollisionListener`.

Limitaciones actuales:

- `GameBootstrap` concentra demasiada composicion.
- `GamePresenter` quedo como componente legado y hoy no gobierna el flujo principal.
- Algunas vistas siguen mezclando logica de layout y actualizacion visual.

## Estructura del proyecto

```text
src/main/java/edu/autonoma/turboclash
├── application
├── config
├── domain
├── infrastructure
├── main
└── presentation
```

## Problemas conocidos

- No hay migracion de host si el host se desconecta durante una partida en curso.
- `peers.json` sigue siendo la fuente inicial de descubrimiento; no existe lobby dinamico completo.
- Los tests agregados son de validacion manual compilable; el entorno de sandbox presenta inestabilidad al ejecutar `mvn test` completo.

## Validacion reciente

- Compilacion Maven: exitosa con `mvn -s .mvn-settings.xml -DskipTests compile`.
- Validacion manual de clases principales y pruebas ligeras: exitosa mediante `ManualTestRunner`.

## Autoras

- Elizabeth Meneses Munoz
- Valerie Moreno
- Maria Paz Puerta Acevedo
- Heily Yohana Rios Ayala
