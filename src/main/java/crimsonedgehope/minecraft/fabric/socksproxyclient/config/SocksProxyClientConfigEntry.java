package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SocksProxyClientConfigEntry<T> {
    @NotNull @Getter
    private final Class<? extends SocksProxyClientConfig> configClass;
    @NotNull @Getter
    private String category;
    @NotNull @Getter
    private String entry;

    @Getter
    private final T defaultValue;
    @Getter @Setter
    private T value;

    @NotNull @Getter
    private final String translateKey;
    @Nullable @Getter
    private final String description;

    public SocksProxyClientConfigEntry(
            @NotNull Class<? extends SocksProxyClientConfig> configClass,
            @NotNull String entry,
            T defaultValue
    ) {
        this(configClass, entry, defaultValue, null);
    }

    public SocksProxyClientConfigEntry(
            @NotNull Class<? extends SocksProxyClientConfig> configClass,
            @NotNull String entry,
            T defaultValue,
            @Nullable String description
    ) {
        this.entry = entry;
        this.defaultValue = defaultValue;
        this.value = this.defaultValue;
        this.description = description;
        this.configClass = configClass;
        try {
            this.category = (String) this.configClass.getDeclaredField("CATEGORY").get(null);
        } catch (Exception e) {
            SocksProxyClient.logger().error("", e);
        }
        if (this.category == null) {
            this.category = this.configClass.getSimpleName();
        }
        this.translateKey = TranslateKeyUtil.configItem(this.category, this.entry);
    }
}
