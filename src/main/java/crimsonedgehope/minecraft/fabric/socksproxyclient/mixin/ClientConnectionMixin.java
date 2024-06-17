package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.access.IClientConnectionMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.InetSocketAddress;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient.LOGGER;

@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ClientConnectionMixin implements IClientConnectionMixin {
    @Unique
    private InetSocketAddress remote;

    @Override
    public InetSocketAddress socksProxyClient$getInetSocketAddress() {
        return remote;
    }

    @Override
    public void socksProxyClient$setInetSocketAddress(InetSocketAddress inetSocketAddress) {
        this.remote = inetSocketAddress;
    }

    @Inject(
            method = "connect",
            at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;<init>()V", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false
    )
    private static void injected(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> cir, ClientConnection clientConnection) {
        ((IClientConnectionMixin) clientConnection).socksProxyClient$setInetSocketAddress(address);
        LOGGER.debug("Remote Minecraft server {}", address);
    }
}
