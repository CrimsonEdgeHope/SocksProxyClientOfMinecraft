package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import com.google.common.net.InetAddresses;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.MultiplayerServerListPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(MultiplayerServerListPinger.class)
public class MultiplayerServerListPingerMixin {
    @ModifyArg(
            method = "add",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/net/InetAddress;getByName(Ljava/lang/String;)Ljava/net/InetAddress;"
            )
    )
    private String modified(String host) throws Exception {
        if (InetAddresses.isInetAddress(host)) {
            return host;
        }
        if (ServerConfig.minecraftRemoteResolve()) {
            return DNSUtils.resolveA(host).getHostAddress();
        }
        return host;
    }
}
