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
        Proxy proxy = ProxyConfig.getProxy();
        if (proxy == null) {
            LOGGER.debug("No SOCKS proxy configured.");
            return;
        }

        InetAddress inetAddress = address.getAddress();
        if (inetAddress == null) {
            LOGGER.debug("Remote address is null");
            return;
        }
        String hostnameIP = inetAddress.toString();
        String ip = inetAddress.getHostAddress();
        int port = address.getPort();
        String ipPort = String.format("%s:%s", ip, port);

        LOGGER.debug(String.format("Remote address %s", ipPort));

        if (inetAddress.isLoopbackAddress() && !ProxyConfig.shouldProxyLoopback()) {
            LOGGER.info(String.format("Remote host %s on port %d is loopback, not imposing proxy.", hostnameIP, port));
            return;
        }

        String s1 = ProxyConfig.getUsername();
        String s2 = ProxyConfig.getPassword();

        cir.cancel();
        cir.setReturnValue((new Bootstrap()).group((EventLoopGroup)lazy.get()).handler(new ChannelInitializer<>() {
            protected void initChannel(Channel channel) {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (ChannelException ex) {

                }

                ChannelPipeline channelPipeline = channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30));
                switch (ProxyConfig.getSocksVersion()) {
                    case 4:
                        LOGGER.debug(String.format("Using Socks4 on %s", hostnameIP));
                        channelPipeline.addFirst("socks", new Socks4ProxyHandler(proxy.address(), s1));
                        break;
                    case 5:
                    default:
                        LOGGER.debug(String.format("Using Socks5 on %s", hostnameIP));
                        channelPipeline.addFirst("socks", new Socks5ProxyHandler(proxy.address(), s1, s2));
                        break;
                }
                ClientConnection.addHandlers(channelPipeline, NetworkSide.CLIENTBOUND);
                channelPipeline.addLast("packet_handler", connection);
            }
        }).channel(class_).connect(address.getAddress(), address.getPort()));
    }
}
