package crimsonedgehope.minecraft.fabric.socksproxyclient.injection.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixin;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ProxyCredential;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Objects;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient.LOGGER;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public class ClientConnectionMixinInner {
    @WrapOperation(method = "initChannel",
            at = @At(
                    value = "INVOKE",
                    target = "Lio/netty/channel/ChannelPipeline;addLast(Ljava/lang/String;Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;",
                    ordinal = 1
            ),
            remap = false
    )
    private ChannelPipeline wrapped(ChannelPipeline pipeline, String handlerName, ChannelHandler channelHandler, Operation<ChannelPipeline> original) {
        ChannelPipeline ret = original.call(pipeline, handlerName, channelHandler);

        InetSocketAddress remote = ((IClientConnectionMixin) (ClientConnection) channelHandler).socksProxyClient$getInetSocketAddress();

        if (Objects.isNull(remote)) {
            return ret;
        }

        InetAddress address = remote.getAddress();

        Proxy proxySelection;
        if (address.isLoopbackAddress()) {
            proxySelection = ServerConfig.getProxyForMinecraftLoopback();
        } else {
            proxySelection = ServerConfig.getProxyForMinecraft();
        }

        if (proxySelection.equals(Proxy.NO_PROXY)) {
            LOGGER.info("No proxy on host {}", address);
            return ret;
        }

        ProxyCredential proxyCredential = ServerConfig.getProxyCredential();

        final SocketAddress sa = proxySelection.address();
        switch (ServerConfig.getSocksVersion()) {
            case SOCKS4:
                LOGGER.info("Using Socks4 proxy {} on {}", sa, remote);
                pipeline.addFirst("socks", new Socks4ProxyHandler(sa, proxyCredential.getUsername()));
                break;
            case SOCKS5:
                LOGGER.info("Using Socks5 proxy {} on {}", sa, remote);
                pipeline.addFirst("socks", new Socks5ProxyHandler(sa, proxyCredential.getUsername(), proxyCredential.getPassword()));
                break;
            default:
                LOGGER.info("No proxy on host {}", address);
                break;
        }

        return ret;
    }
}
