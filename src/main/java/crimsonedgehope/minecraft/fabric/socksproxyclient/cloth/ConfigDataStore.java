package crimsonedgehope.minecraft.fabric.socksproxyclient.cloth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigDataStore {

    private static final Object object = new Object();

    private static final Map<String, ConfigHolder> configs = new HashMap<>();

    private static <T extends ConfigData> void clothAutoConfigReg(final String entry, final Class<T> configClass) {
        AutoConfig.register(configClass, GsonConfigSerializer::new);
        add(entry, AutoConfig.getConfigHolder(configClass));
    }

    public static void init() {
        clothAutoConfigReg(SocksProxyClientConfigData.ENTRY, SocksProxyClientConfigData.class);
        getHolder(SocksProxyClientConfigData.ENTRY).registerSaveListener(
                (configHolder, configData) ->
                        SocksProxyClientConfigData.updateCredential((SocksProxyClientConfigData) configData)
        );
    }

    public static ConfigHolder getHolder(final String entry) {
        return configs.get(entry);
    }

    public static ConfigData get(final String entry) {
        return getHolder(entry).getConfig();
    }

    private static void add(final String entry, final ConfigHolder holder) {
        configs.put(entry, holder);
    }
}
