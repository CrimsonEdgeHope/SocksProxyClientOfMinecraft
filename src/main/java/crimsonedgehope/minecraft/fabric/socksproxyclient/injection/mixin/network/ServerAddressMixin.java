package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.google.common.net.InetAddresses;
import com.mojang.datafixers.util.Pair;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.UnknownHostException;

@Environment(EnvType.CLIENT)
@Mixin(ServerAddress.class)
public class ServerAddressMixin {
    @Redirect(method = "parse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ServerAddress;resolveServer(Ljava/lang/String;)Lcom/mojang/datafixers/util/Pair;"))
    private static Pair<String, Integer> redirected(String address) throws Exception {
        if (InetAddresses.isInetAddress(address)) {
            return Pair.of(address, 25565);
        }
        if (ServerConfig.minecraftRemoteResolve()) {
            try {
                return DNSUtils.resolveSRV(address);
            } catch (UnknownHostException e) {
                return Pair.of(address, 25565);
            }
        }
        return ServerAddressAccessor.invokeResolveServer(address);
    }
}
