package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public abstract class SocksProxyClientConfig {

    protected static final Logger LOGGER = SocksProxyClient.LOGGER;

    protected static Path configPathDir() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("socksproxyclient");
        File file = path.toFile();
        if (!path.toFile().exists()) {
            file.mkdirs();
        }
        return path;
    }

    @Getter
    private File configFile;

    public abstract JsonObject defaultEntries();
    public abstract JsonObject toJsonObject();
    public abstract void fromJsonObject(JsonObject object);

    protected SocksProxyClientConfig(String filename) {
        this(configPathDir().resolve(filename).toFile());
    }

    protected SocksProxyClientConfig(File configFile) {
        this.configFile = configFile;
    }

    public void load() {
        LOGGER.info("Reading config file " + this.configFile.getName());
        if (!this.configFile.exists()) {
            writeFile(defaultEntries());
        }
        try {
            readFile();
        } catch (Exception e) {
            LOGGER.info("Error reading config file " + this.configFile.getName());
        }
    }

    public void save() {
        writeFile(toJsonObject());
    }

    private void readFile() throws IOException {
        FileReader reader = new FileReader(this.configFile, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(new JsonReader(reader), JsonObject.class);
        readJson(object);
    }

    private void readJson(JsonObject object) {
        JsonObject defaults = defaultEntries();
        if (object == null || object.size() == 0) {
            writeFile(defaults);
            load();
            return;
        }
        LOGGER.info("Reading config json " + this.configFile.getName());
        try {
            for (String key : defaults.keySet()) {
                if (!object.has(key)) {
                    writeFile(defaults);
                    load();
                    return;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error reading config json " + this.configFile.getName(), e);
        }
        fromJsonObject(object);
    }

    private void writeFile(JsonObject entries) {
        try (FileWriter writer = new FileWriter(this.configFile, false)) {
            LOGGER.info("Writing config to file " + this.configFile.getName());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(entries));
        } catch (Exception e) {
            LOGGER.error("Error writing config to file " + this.configFile.getName(), e);
        }
    }
}
