package crimsonedgehope.minecraft.fabric.socksproxyclient.config.yacl;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ConfigUtils;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.SocksProxyClientConfigEntry;
import dev.isxander.yacl.api.ConfigCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class YACLCategory<C extends SocksProxyClientConfig> {
    protected final YACLAccess yaclAccess;
    protected final Class<C> configClass;

    protected SocksProxyClientConfigEntry<?> entryField(final String fieldName) throws Exception {
        return ConfigUtils.getConfigInstance(this.configClass).getEntryField(fieldName);
    }

    protected <T> SocksProxyClientConfigEntry<T> entryField(final String fieldName, final Class<T> valueType) throws Exception {
        return ConfigUtils.getConfigInstance(this.configClass).getEntryField(fieldName, valueType);
    }

    public abstract ConfigCategory buildConfigCategory() throws Exception;
}
