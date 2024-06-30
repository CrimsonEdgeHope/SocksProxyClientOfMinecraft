package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixin;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixinInner;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ClientConnectionProxySelection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public class ClientConnectionMixinInner implements IClientConnectionMixinInner {

    @Unique
    private ChannelHandler clientConnection;

    @Override
    public ChannelHandler socksProxyClient$getClientConnection() {
        return this.clientConnection;
    }

    @Override
    public void socksProxyClient$setClientConnection(ChannelHandler clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Inject(method = "initChannel", at = @At("TAIL"), remap = false)
    private void injected(Channel channel, CallbackInfo ci) {
        ((IClientConnectionMixinInner) this).socksProxyClient$setClientConnection(channel.pipeline().get("packet_handler"));
        InetSocketAddress remote = ((IClientConnectionMixin) ((IClientConnectionMixinInner) this).socksProxyClient$getClientConnection()).socksProxyClient$getInetSocketAddress();
        ClientConnectionProxySelection.fire(remote, channel.pipeline());
    }
}
