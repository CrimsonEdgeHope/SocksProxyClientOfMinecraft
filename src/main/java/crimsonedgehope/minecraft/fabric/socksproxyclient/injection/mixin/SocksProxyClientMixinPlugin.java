package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.version.VersionComparisonOperator;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class SocksProxyClientMixinPlugin implements IMixinConfigPlugin {

    private static final Logger LOGGER = SocksProxyClient.logger("Mixin");

    private boolean viaFabricPlusLive;

    @Override
    public void onLoad(String mixinPackage) {
        final Optional<ModContainer> viafabricplus = FabricLoader.getInstance().getModContainer("viafabricplus");
        if (viafabricplus.isEmpty()) {
            viaFabricPlusLive = false;
            return;
        }

        final ModMetadata viafabricplusMetadata = viafabricplus.get().getMetadata();
        try {
            viaFabricPlusLive = VersionComparisonOperator.GREATER_EQUAL.test(viafabricplusMetadata.getVersion(), Version.parse("3.0.0"));
        } catch (VersionParsingException e) {
            throw new Error(e);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        final String pac = this.getClass().getPackageName();
        boolean ret = true;

        if (viaFabricPlusLive) {
            if (mixinClassName.equals(pac + ".network.MultiplayerServerListPingerMixin")) {
                ret = false;
            }
            if (!ret) {
                LOGGER.warn("Dismiss some of our Mixins because of ViaFabricPlus to prevent crash!");
            }
        }
        return ret;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
