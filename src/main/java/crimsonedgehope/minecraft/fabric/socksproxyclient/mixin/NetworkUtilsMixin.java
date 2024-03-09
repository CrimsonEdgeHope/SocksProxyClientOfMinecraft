package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ProxyConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.NetworkUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(NetworkUtils.class)
public class NetworkUtilsMixin {
    @ModifyVariable(method = "downloadResourcePack", at = @At("HEAD"), argsOnly = true)
    private static Proxy injected(Proxy proxy) {
        if (ProxyConfig.shouldProxyResourcePackDownloading()) {
            SocksProxyClient.logger().info("Proxy will be used to download resource pack");
            return proxy;
        }
        SocksProxyClient.logger().debug("Proxy will NOT be used to download resource pack");
        return null;
    }
}
