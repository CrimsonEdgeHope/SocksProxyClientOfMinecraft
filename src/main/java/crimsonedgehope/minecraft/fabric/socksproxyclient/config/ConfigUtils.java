package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigUtils {

    public static void loadAll() {
        GeneralConfig.INSTANCE.load();
        ServerConfig.INSTANCE.load();
    }

    public static void saveAll() {
        GeneralConfig.INSTANCE.save();
        ServerConfig.INSTANCE.save();
    }

    public static <T extends SocksProxyClientConfig> String categoryField(Class<T> clazz) {
        String category = null;
        try {
            category = (String) clazz.getDeclaredField("CATEGORY").get(null);
        } catch (Exception e) {
            SocksProxyClient.LOGGER.error("CATEGORY static field missing or inaccessible!", e);
        }
        if (Objects.isNull(category)) {
            category = clazz.getSimpleName();
        }
        return category;
    }
}
