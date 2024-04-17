package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ProxyConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.util.Lazy;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    private static final Logger LOGGER = SocksProxyClient.logger();

    @Inject(
        method = "connect(Ljava/net/InetSocketAddress;ZLnet/minecraft/network/ClientConnection;)Lio/netty/channel/ChannelFuture;",
        at = @At(
            target = "Lio/netty/bootstrap/Bootstrap;<init>()V",
            value = "INVOKE",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true
    )
    private static void injected(InetSocketAddress address, boolean useEpoll, final ClientConnection connection, CallbackInfoReturnable<ChannelFuture> cir, Class class_, Lazy lazy) {
        InetAddress inetAddress = address.getAddress();
        if (inetAddress == null) {
            LOGGER.error("Remote address is null!!!");
            return;
        }

        final String hostnameIP = inetAddress.toString();
        final String ip = inetAddress.getHostAddress();
        final int port = address.getPort();
        final String ipPort = String.format("%s:%s", ip, port);

        LOGGER.debug("Remote IP {}", ipPort);

        Proxy proxy;
        ProxyConfig.Credential credential;

        if (inetAddress.isLoopbackAddress()) {
            proxy = ProxyConfig.getProxyForLoopback();
            credential = ProxyConfig.getProxyCredentialForLoopback();
        } else {
            proxy = ProxyConfig.getProxy();
            credential = ProxyConfig.getProxyCredential();
        }

        if (proxy == null) {
            LOGGER.info("No proxy for IP {}", ipPort);
            LOGGER.info("No proxy for host {}:{}", hostnameIP, port);
            return;
        }

        final Proxy proxy0 = proxy;
        final ProxyConfig.Credential credential0 = credential;

        cir.cancel();
        cir.setReturnValue((new Bootstrap()).group((EventLoopGroup)lazy.get()).handler(new ChannelInitializer<>() {
            protected void initChannel(Channel channel) {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (ChannelException ex) {
                    // NO-OP
                }

                ChannelPipeline channelPipeline = channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
                switch (ProxyConfig.getSocksVersion()) {
                    case 4:
                        LOGGER.info("Using Socks4 proxy for {}:{}", hostnameIP, port);
                        channelPipeline.addFirst("socks",
                                new Socks4ProxyHandler(proxy0.address(), credential0.getUsername()));
                        break;
                    case 5:
                    default:
                        LOGGER.info("Using Socks5 proxy for {}:{}", hostnameIP, port);
                        channelPipeline.addFirst("socks",
                                new Socks5ProxyHandler(proxy0.address(), credential0.getUsername(), credential0.getPassword()));
                        break;
                }
                ClientConnection.addHandlers(channelPipeline, NetworkSide.CLIENTBOUND);
                channelPipeline.addLast("packet_handler", connection);
            }
        }).channel(class_).connect(address.getAddress(), address.getPort()));
    }
}
