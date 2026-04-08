package edu.autonoma.turboclash.infrastructure.network.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PeerConfigLoader {

    public static List<PeerConfigEntry> loadFromResource(String path) {

        InputStream is = PeerConfigLoader.class.getResourceAsStream(path);

        if (is == null) {
            throw new RuntimeException("No se encontró el archivo: " + path);
        }

        Type listType = new TypeToken<List<PeerConfigEntry>>(){}.getType();

        return new Gson().fromJson(
                new InputStreamReader(is, StandardCharsets.UTF_8),
                listType
        );
    }
}