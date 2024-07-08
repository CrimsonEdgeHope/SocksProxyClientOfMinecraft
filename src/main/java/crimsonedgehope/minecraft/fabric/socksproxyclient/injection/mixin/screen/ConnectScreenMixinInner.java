package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.screen;

import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IConnectScreenMixinInner;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network.HandshakeC2SPacketAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
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

    @Redirect(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/Packet;)V",
                    ordinal = 0
            )
    )
    private void redirected(ClientConnection instance, Packet<?> packet) {
        ((HandshakeC2SPacketAccessor) packet).setAddress(((IConnectScreenMixinInner) this).socksProxyClient$getServerAddress().getAddress());
        instance.send(packet);
    }
}
