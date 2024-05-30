package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfigEntry;
import lombok.Getter;
import lombok.Setter;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

abstract class ClothConfigEntry<T> {
    @Getter
    private final ConfigEntryBuilder builder;
    @Getter
    private final SocksProxyClientConfigEntry<T> configEntry;
    @Setter
    private AbstractConfigListEntry<T> clothConfigEntry;

    ClothConfigEntry(ConfigEntryBuilder builder, SocksProxyClientConfigEntry<T> configEntry) {
        this.builder = builder;
        this.configEntry = configEntry;
    }

    public AbstractConfigListEntry<T> getClothConfigEntry() {
        if (clothConfigEntry != null) {
            return clothConfigEntry;
        }
        clothConfigEntry = buildClothConfigEntry();
        return clothConfigEntry;
    }

    protected abstract AbstractConfigListEntry<T> buildClothConfigEntry();

    public AbstractConfigListEntry<T> rebuildClothConfigEntry() {
        clothConfigEntry = null;
        return getClothConfigEntry();
    }
}
