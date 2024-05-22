package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralProxyConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(targets = {"net.minecraft.network.ClientConnection", "net.minecraft.network.ClientConnection$1"})
public class ClientConnectionMixin {
    private static final Logger LOGGER = SocksProxyClient.logger();

    private static InetSocketAddress REMOTE;

    private static Proxy PROXY_SELECTION;
    private static GeneralProxyConfig.Credential PROXY_CREDENTIAL;

    @Environment(EnvType.CLIENT)
    @Mixin(ClientConnection.class)
    public static class ProxyDetermination {
        @Inject(
                method = "connect(Ljava/net/InetSocketAddress;ZLnet/minecraft/network/ClientConnection;)Lio/netty/channel/ChannelFuture;",
                at = @At("HEAD")
        )
        private static void injected(InetSocketAddress address, boolean useEpoll, ClientConnection connection, CallbackInfoReturnable<ChannelFuture> cir) {
            InetAddress inetAddress = address.getAddress();
            if (inetAddress == null) {
                PROXY_SELECTION = null;
                LOGGER.error("Remote address is null!!!");
                return;
            }
            REMOTE = address;

            if (inetAddress.isLoopbackAddress()) {
                PROXY_SELECTION = GeneralProxyConfig.getProxyForLoopback();
                PROXY_CREDENTIAL = GeneralProxyConfig.getProxyCredentialForLoopback();
            } else {
                PROXY_SELECTION = GeneralProxyConfig.getProxy();
                PROXY_CREDENTIAL = GeneralProxyConfig.getProxyCredential();
            }

            LOGGER.debug("Remote Minecraft server {}", address);

            if (PROXY_SELECTION == null) {
                LOGGER.info("No proxy on host {}", address);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(targets = "net.minecraft.network.ClientConnection$1", remap = false)
    public static class HandlerInjection {
        @Inject(
                method = "initChannel",
                at = @At("HEAD")
        )
        private void injected(Channel channel, CallbackInfo ci) {
            if (PROXY_SELECTION != null) {
                InetAddress address = REMOTE.getAddress();
                SocketAddress sa = PROXY_SELECTION.address();
                switch (GeneralProxyConfig.getSocksVersion()) {
                    case 4:
                        LOGGER.info("Using Socks4 proxy {} on {}", sa, address);
                        channel.pipeline().addFirst("socks",
                                new Socks4ProxyHandler(PROXY_SELECTION.address(), PROXY_CREDENTIAL.getUsername()));
                        break;
                    case 5:
                    default:
                        LOGGER.info("Using Socks5 proxy {} on {}", sa, address);
                        channel.pipeline().addFirst("socks",
                                new Socks5ProxyHandler(PROXY_SELECTION.address(), PROXY_CREDENTIAL.getUsername(), PROXY_CREDENTIAL.getPassword()));
                        break;
                }
            }
        }
    }
}
