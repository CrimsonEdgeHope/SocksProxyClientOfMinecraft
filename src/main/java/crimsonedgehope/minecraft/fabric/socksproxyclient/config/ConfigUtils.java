package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigUtils {

    public static <C extends SocksProxyClientConfig> C configInstance(final Class<C> clazz) throws Exception {
        Field field = clazz.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        Object instance = field.get(null);
        Objects.requireNonNull(instance);
        if (!clazz.isInstance(instance)) {
            instance = null;
        }
        return (C) instance;
    }

    public static void loadAll() throws Exception {
        configInstance(GeneralConfig.class).load();
        configInstance(ServerConfig.class).load();
    }

    public static void saveAll() throws Exception {
        configInstance(GeneralConfig.class).save();
        configInstance(ServerConfig.class).save();
    }

    public static <T extends SocksProxyClientConfig> String categoryField(Class<T> clazz) throws Exception {
        String category = null;
        Field field = clazz.getDeclaredField("CATEGORY");
        field.setAccessible(true);
        category = (String) field.get(null);
        if (Objects.isNull(category)) {
            category = clazz.getSimpleName();
        }
        return category;
    }
}
