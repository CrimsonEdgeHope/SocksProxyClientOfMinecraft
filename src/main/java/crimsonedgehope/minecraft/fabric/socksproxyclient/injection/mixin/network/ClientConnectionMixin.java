package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ClientConnectionMixin implements IClientConnectionMixin {
    @Unique
    private InetSocketAddress remote;

    @Override
    public void socksProxyClient$setRemote(InetSocketAddress socketAddress) {
        this.remote = socketAddress;
    }

    @Override
    public InetSocketAddress socksProxyClient$getRemote() {
        return this.remote;
    }

    @Inject(
            method = "connect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection$1;<init>(Lnet/minecraft/network/ClientConnection;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void injected(InetAddress address, int port, boolean shouldUseNativeTransport, CallbackInfoReturnable<ClientConnection> cir, ClientConnection clientConnection) {
        ((IClientConnectionMixin) clientConnection).socksProxyClient$setRemote(new InetSocketAddress(address, port));
        SocksProxyClient.LOGGER.debug("Remote Minecraft server {}", address);
    }
}
