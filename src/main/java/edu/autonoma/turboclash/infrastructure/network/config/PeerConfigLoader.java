package edu.autonoma.turboclash.infrastructure.network.config;

import edu.autonoma.turboclash.infrastructure.network.message.MessagePayloadCodec;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Representa la clase `PeerConfigLoader` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class PeerConfigLoader {

    /**
     * Carga la informacion requerida para load from resource.
     * @param path valor del parametro `path`
     * @return resultado de la operacion documentada
     */
    public static List<PeerConfigEntry> loadFromResource(String path) {

        InputStream is = PeerConfigLoader.class.getResourceAsStream(path);

        if (is == null) {
            throw new RuntimeException("No se encontrÃ³ el archivo: " + path);
        }

        try {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return MessagePayloadCodec.decodePeers(json);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo leer peers.json", e);
        }
    }
}
