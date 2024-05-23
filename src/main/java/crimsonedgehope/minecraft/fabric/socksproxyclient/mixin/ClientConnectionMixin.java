package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralProxyConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Unique
    private static final Logger LOGGER = SocksProxyClient.LOGGER;
    @Unique
    private static InetSocketAddress REMOTE;

    @Inject(
            method = "connect(Ljava/net/InetSocketAddress;ZLnet/minecraft/network/ClientConnection;)Lio/netty/channel/ChannelFuture;",
            at = @At("HEAD")
    )
    private static void injected(InetSocketAddress address, boolean useEpoll, ClientConnection connection, CallbackInfoReturnable<ChannelFuture> cir) {
        REMOTE = address;
        LOGGER.debug("Remote Minecraft server {}", address.getAddress());
    }

    @Inject(
            method = "addHandlers",
            at = @At("HEAD")
    )
    private static void injected(ChannelPipeline pipeline, NetworkSide side, CallbackInfo ci) {
        if (REMOTE == null) {
            return;
        }
        InetAddress address = REMOTE.getAddress();

        Proxy proxySelection;
        if (address.isLoopbackAddress()) {
            proxySelection = GeneralProxyConfig.getProxyForLoopback();
        } else {
            proxySelection = GeneralProxyConfig.getProxy();
        }

        if (proxySelection == null) {
            LOGGER.info("No proxy on host {}", address);
            return;
        }

        GeneralProxyConfig.Credential proxyCredential = GeneralProxyConfig.getProxyCredential();

        final SocketAddress sa = proxySelection.address();
        switch (GeneralProxyConfig.getSocksVersion()) {
            case 4:
                LOGGER.info("Using Socks4 proxy {} on {}", sa, address);
                pipeline.addFirst("socks",
                        new Socks4ProxyHandler(sa, proxyCredential.getUsername()));
                break;
            case 5:
                LOGGER.info("Using Socks5 proxy {} on {}", sa, address);
                pipeline.addFirst("socks",
                        new Socks5ProxyHandler(sa, proxyCredential.getUsername(), proxyCredential.getPassword()));
                break;
            default:
                LOGGER.info("No proxy on host {}", address);
                break;
        }
    }
}
