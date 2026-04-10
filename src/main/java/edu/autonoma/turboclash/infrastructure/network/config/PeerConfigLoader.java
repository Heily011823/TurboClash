package edu.autonoma.turboclash.infrastructure.network.config;

import edu.autonoma.turboclash.infrastructure.network.message.MessagePayloadCodec;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PeerConfigLoader {

    public static List<PeerConfigEntry> loadFromResource(String path) {

        InputStream is = PeerConfigLoader.class.getResourceAsStream(path);

        if (is == null) {
            throw new RuntimeException("No se encontró el archivo: " + path);
        }

        try {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return MessagePayloadCodec.decodePeers(json);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo leer peers.json", e);
        }
    }
}
