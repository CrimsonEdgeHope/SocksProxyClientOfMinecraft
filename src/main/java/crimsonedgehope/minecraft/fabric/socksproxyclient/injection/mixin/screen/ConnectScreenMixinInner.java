package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.screen;

import com.google.common.net.InetAddresses;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IConnectScreenMixin;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network.HandshakeC2SPacketAccessor;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.gui.screen.ConnectScreen$1")
public class ConnectScreenMixinInner {

    @Shadow(aliases = "field_2416")
    private ConnectScreen connectScreen;

    @ModifyArg(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/net/InetAddress;getByName(Ljava/lang/String;)Ljava/net/InetAddress;"
            )
    )
    private String modified(String host) throws Exception {
        ((IConnectScreenMixin) connectScreen).socksProxyClient$setHost(host);
        if (InetAddresses.isInetAddress(host)) {
            return host;
        }
        if (ServerConfig.minecraftRemoteResolve()) {
            return DNSUtils.resolveA(host).getHostAddress();
        }
        return host;
    }

    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/Packet;)V",
                    ordinal = 0
            )
    )
    private void redirected(ClientConnection instance, Packet<?> packet) {
        String host = ((IConnectScreenMixin) connectScreen).socksProxyClient$getHost();
        if (Objects.nonNull(host)) {
            ((HandshakeC2SPacketAccessor) packet).setAddress(host);
        }
        instance.send(packet);
    }
}
