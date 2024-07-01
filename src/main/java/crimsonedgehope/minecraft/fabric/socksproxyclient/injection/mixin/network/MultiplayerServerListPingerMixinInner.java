package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.google.common.net.InetAddresses;
import com.mojang.datafixers.util.Pair;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ClientConnectionProxySelection;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSUtils;
import io.netty.channel.Channel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Mixin(targets = "net.minecraft.client.network.MultiplayerServerListPinger$2")
@Environment(EnvType.CLIENT)
public class MultiplayerServerListPingerMixinInner {

    @Shadow(aliases = "field_3778") @Final @Mutable
    private ServerAddress serverAddress;

    /**
     * Dirty workaround that performs another DNS query.
     */
    @Inject(method = "initChannel",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/channel/ChannelPipeline;addLast([Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ), remap = false
    )
    private void injected(Channel channel, CallbackInfo ci) throws Exception {
        Pair<String, Integer> remote;
        InetAddress inet;
        final int port = serverAddress.getPort();
        if (InetAddresses.isInetAddress(serverAddress.getAddress())) {
            remote = Pair.of(serverAddress.getAddress(), port);
        } else if (ServerConfig.minecraftRemoteResolve()) {
            try {
                remote = DNSUtils.resolveSRV(serverAddress.getAddress());
            } catch (UnknownHostException e) {
                remote = Pair.of(serverAddress.getAddress(), port);
            }
        } else {
            remote = ServerAddressAccessor.invokeResolveServer(serverAddress.getAddress());
        }
        if (!InetAddresses.isInetAddress(remote.getFirst())) {
            if (ServerConfig.minecraftRemoteResolve()) {
                inet = InetAddresses.forString(DNSUtils.resolveA(remote.getFirst()).getHostAddress());
            } else {
                inet = InetAddress.getByName(remote.getFirst());
            }
        } else {
            inet = InetAddresses.forString(remote.getFirst());
        }
        ClientConnectionProxySelection.fire(new InetSocketAddress(inet, port), channel.pipeline());
        serverAddress = ServerAddressAccessor.createServerAddressObject(inet.getHostAddress(), port);
    }
}
