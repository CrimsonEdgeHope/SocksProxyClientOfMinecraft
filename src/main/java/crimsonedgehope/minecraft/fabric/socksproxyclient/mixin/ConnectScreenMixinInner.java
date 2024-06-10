package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.access.IConnectScreenMixinInner;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.gui.screen.ConnectScreen$1")
public class ConnectScreenMixinInner implements IConnectScreenMixinInner {
    @Unique
    private ServerAddress serverAddress;

    @Override
    public ServerAddress socksProxyClient$getServerAddress() {
        return this.serverAddress;
    }

    @Override
    public void socksProxyClient$setServerAddress(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    @ModifyArg(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AllowedAddressResolver;resolve(Lnet/minecraft/client/network/ServerAddress;)Ljava/util/Optional;"
            )
    )
    private ServerAddress modified(ServerAddress address) {
        ((IConnectScreenMixinInner) this).socksProxyClient$setServerAddress(address);
        return address;
    }

    /**
     ViaFabricPlus redirects the construction of {@code HandshakeC2SPacket}, at the method call {@code inetSocketAddress.getHostName()}
    */
    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;)V",
                    ordinal = 0
            )
    )
    private void redirected(ClientConnection instance, Packet<?> packet) {
        ((HandshakeC2SPacketAccessor) packet).setAddress(((IConnectScreenMixinInner) this).socksProxyClient$getServerAddress().getAddress());
        instance.send(packet);
    }
}
