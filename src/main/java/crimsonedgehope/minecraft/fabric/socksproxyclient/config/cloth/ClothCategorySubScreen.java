package crimsonedgehope.minecraft.fabric.socksproxyclient.config.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfigEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import me.shedaniel.clothconfig2.api.ConfigCategory;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class ClothCategorySubScreen<C extends SocksProxyClientConfig> {

    protected final ClothAccess clothAccess;
    protected final Class<C> configClass;

    protected static <C extends SocksProxyClientConfig> C getConfigInstance(final Class<C> clazz) throws Exception {
        return ConfigUtils.configInstance(clazz);
    }

    protected SocksProxyClientConfigEntry<?> entryField(final String fieldName) throws Exception {
        return getConfigInstance(this.configClass).getEntryField(fieldName);
    }

    protected <T> SocksProxyClientConfigEntry<T> entryField(final String fieldName, final Class<T> valueType) throws Exception {
        return getConfigInstance(this.configClass).getEntryField(fieldName, valueType);
    }

    public abstract ConfigCategory buildClothCategory();
}
