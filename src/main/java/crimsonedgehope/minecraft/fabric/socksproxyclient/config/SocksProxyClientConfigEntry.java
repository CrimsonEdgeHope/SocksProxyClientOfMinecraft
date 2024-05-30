package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import crimsonedgehope.minecraft.fabric.socksproxyclient.i18n.TranslateKeyUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils.categoryField;

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

    public Text getTranslatableText() {
        return Text.translatable(translateKey);
    }

    @Getter
    private final int desiredLinesOfDescription;

    public List<Text> getDescriptionTranslatableText() {
        ArrayList<Text> r = new ArrayList<>();
        if (desiredLinesOfDescription > 0) {
            String key = TranslateKeyUtil.item(translateKey, "tooltip");
            if (desiredLinesOfDescription > 1) {
                for (int i = 1; i <= desiredLinesOfDescription; ++i) {
                    r.add(TranslateKeyUtil.itemAsText(key, String.valueOf(i)));
                }
            } else {
                r.add(Text.translatable(key));
            }
        }
        return r;
    }

    public SocksProxyClientConfigEntry(
            @NotNull Class<? extends SocksProxyClientConfig> configClass,
            @NotNull String entry,
            T defaultValue
    ) {
        this(configClass, entry, defaultValue, 0);
    }

    public SocksProxyClientConfigEntry(
            @NotNull Class<? extends SocksProxyClientConfig> configClass,
            @NotNull String entry,
            T defaultValue,
            int linesOfDescription
    ) {
        this.entry = entry;
        this.defaultValue = defaultValue;
        this.value = this.defaultValue;
        this.configClass = configClass;
        try {
            this.category = categoryField(this.configClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.translateKey = TranslateKeyUtil.configItem(this.category, this.entry);
        this.desiredLinesOfDescription = linesOfDescription;
    }
}
