package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.google.common.net.InetAddresses;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.netty.ChannelInitializerAccessor;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ClientConnectionProxySelection;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.DNSUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import net.minecraft.client.network.MultiplayerServerListPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.net.InetSocketAddress;

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

    @WrapOperation(method = "ping",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/lang/String;I)Lio/netty/channel/ChannelFuture;",
                    remap = false
            )
    )
    private ChannelFuture injected(Bootstrap instance, String host, int port, Operation<ChannelFuture> original) throws Exception {
        String ip = host;
        if (!InetAddresses.isInetAddress(host)) {
            if (ServerConfig.minecraftRemoteResolve()) {
                ip = DNSUtils.resolveA(host).getHostAddress();
            }
        }
        final String ip0 = ip;
        final ChannelInitializer initializer = (ChannelInitializer) instance.config().handler();
        instance.handler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ((ChannelInitializerAccessor) initializer).invokeInitChannel(ch);
                ClientConnectionProxySelection.fire(new InetSocketAddress(InetAddresses.forString(ip0), port), ch.pipeline());
            }
        });
        return original.call(instance, ip0, port);
    }
}
