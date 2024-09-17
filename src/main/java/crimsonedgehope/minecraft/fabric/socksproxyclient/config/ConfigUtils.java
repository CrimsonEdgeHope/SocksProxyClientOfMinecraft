package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigUtils {

    public static <C extends SocksProxyClientConfig> C getConfigInstance(final Class<C> clazz) throws Exception {
        Field field = clazz.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        Object instance = field.get(null);
        Objects.requireNonNull(instance);
        if (!clazz.isInstance(instance)) {
            instance = null;
        }
        return (C) instance;
    }

    public static void loadAllConfig() throws Exception {
        getConfigInstance(GeneralConfig.class).load();
        getConfigInstance(ServerConfig.class).load();
    }

    public static void saveAllConfig() throws Exception {
        getConfigInstance(GeneralConfig.class).save();
        getConfigInstance(ServerConfig.class).save();
    }

    public static <T extends SocksProxyClientConfig> String getCategoryField(Class<T> clazz) throws Exception {
        String category = null;
        Field field = clazz.getDeclaredField("CATEGORY");
        field.setAccessible(true);
        category = (String) field.get(null);
        if (Objects.isNull(category)) {
            category = clazz.getSimpleName();
        }
        return category;
    }

    public static SocksProxyClientConfigEntry<?> getEntryField(
            final Class<? extends SocksProxyClientConfig> configClass, final String fieldName) throws Exception {
        return getConfigInstance(configClass).getEntryField(fieldName);
    }

    public static <T> SocksProxyClientConfigEntry<T> getEntryField(
            final Class<? extends SocksProxyClientConfig> configClass, final String fieldName, final Class<T> valueType) throws Exception {
        return getConfigInstance(configClass).getEntryField(fieldName, valueType);
    }
}
