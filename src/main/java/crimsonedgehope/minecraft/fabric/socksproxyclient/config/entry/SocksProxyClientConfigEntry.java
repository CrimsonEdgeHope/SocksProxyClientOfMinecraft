package crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
@Getter
public class SocksProxyClientConfigEntry<T> {
    @NotNull private final Class<? extends SocksProxyClientConfig> configClass;

    @NotNull private final String jsonEntry;

    @Nullable private final T defaultValue;
    @Setter @Nullable private T value;

    @NotNull private final MutableText entryTranslateKey;
    @Nullable private final MutableText descriptionTranslateKey;

    public SocksProxyClientConfigEntry(
            @NotNull Class<? extends SocksProxyClientConfig> configClass,
            @NotNull String jsonEntry,
            @NotNull MutableText entryTranslateKey,
            @Nullable T defaultValue
    ) {
        this(configClass, jsonEntry, entryTranslateKey, null, defaultValue);
    }

    public SocksProxyClientConfigEntry(
            @NotNull Class<? extends SocksProxyClientConfig> configClass,
            @NotNull String jsonEntry,
            @NotNull MutableText entryTranslateKey,
            @Nullable MutableText descriptionTranslateKey,
            @Nullable T defaultValue
    ) {
        this.configClass = configClass;
        this.jsonEntry = jsonEntry;
        this.entryTranslateKey = entryTranslateKey;
        this.descriptionTranslateKey = descriptionTranslateKey;
        this.defaultValue = defaultValue;
        this.value = this.defaultValue;
    }
}
