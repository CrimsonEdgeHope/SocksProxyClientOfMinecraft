package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ProxyConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(
        method = "connect(Ljava/net/InetSocketAddress;ZLnet/minecraft/network/ClientConnection;)Lio/netty/channel/ChannelFuture;",
        at = @At("RETURN"))
    private static void injected(InetSocketAddress address, boolean useEpoll, final ClientConnection connection, CallbackInfoReturnable<ChannelFuture> cir) {
        Proxy proxy = MinecraftClient.getInstance().getNetworkProxy();
        if (proxy.address() == null && proxy.type() != Proxy.Type.SOCKS) {
            LOGGER.info("No SOCKS proxy configured.");
            return;
        }

        InetAddress inetAddress = address.getAddress();
        if (inetAddress == null) {
            LOGGER.error("Remote address is null");
            return;
        }
        String hostnameIP = inetAddress.toString();
        String ip = inetAddress.getHostAddress();
        int port = address.getPort();

        LOGGER.info(String.format("Remote address %s:%s", ip, port));
        if (inetAddress.isLoopbackAddress()) {
            LOGGER.info(String.format("Remote host %s on port is loopback, not imposing proxy.", hostnameIP));
            return;
        }
        ChannelFuture channelFuture = cir.getReturnValue();
        Channel channel = channelFuture.channel();

        String s1 = ProxyConfig.getUsername();
        String s2 = ProxyConfig.getPassword();
        if (proxy.address() != null) {
            switch (ProxyConfig.getSocksVersion()) {
                case 4:
                    LOGGER.info(String.format("Using Socks4 on %s", hostnameIP));
                    channel.pipeline().addFirst("socks", new Socks4ProxyHandler(proxy.address(), s1));
                    break;
                case 5:
                default:
                    LOGGER.info(String.format("Using Socks5 on %s", hostnameIP));
                    channel.pipeline().addFirst("socks", new Socks5ProxyHandler(proxy.address(), s1, s2));
                    break;
            }
        }
    }
}
