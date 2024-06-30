package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.screen;

import com.google.common.net.InetAddresses;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network.HandshakeC2SPacketAccessor;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.gui.screen.ConnectScreen$1")
public class ConnectScreenMixinInner {
    @Unique
    private static String HOST;

    @ModifyArg(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/net/InetAddress;getByName(Ljava/lang/String;)Ljava/net/InetAddress;"
            )
    )
    private String modified(String host) throws Exception {
        HOST = host;
        if (InetAddresses.isInetAddress(host)) {
            return host;
        }
        if (ServerConfig.minecraftRemoteResolve()) {
            return DNSUtils.resolveA(host).getHostAddress();
        }
        return HOST;
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
        if (Objects.nonNull(HOST)) {
            ((HandshakeC2SPacketAccessor) packet).setAddress(HOST);
        }
        instance.send(packet);
    }
}
