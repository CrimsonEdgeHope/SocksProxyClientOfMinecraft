package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfigEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public abstract class CategorySubScreen<C extends SocksProxyClientConfig> {

    protected final Class<C> configClass;

    protected CategorySubScreen(final Class<C> configClass) {
        this.configClass = configClass;
    }

    @ApiStatus.Experimental
    protected static <C extends SocksProxyClientConfig> C getConfigInstance(final Class<C> clazz) throws Exception {
        Object instance = clazz.getDeclaredField("INSTANCE").get(null);
        Objects.requireNonNull(instance);
        if (!clazz.isInstance(instance)) {
            instance = null;
        }
        return (C) instance;
    }

    @ApiStatus.Experimental
    protected SocksProxyClientConfigEntry<?> entryField(final String fieldName) throws Exception {
        return getConfigInstance(this.configClass).getEntryField(fieldName);
    }

    @ApiStatus.Experimental
    protected <T> SocksProxyClientConfigEntry<T> entryField(final String fieldName, final Class<T> valueType) throws Exception {
        return getConfigInstance(this.configClass).getEntryField(fieldName, valueType);
    }

    public abstract ConfigCategory buildCategory(ClothAccess cloth);
}
