package crimsonedgehope.minecraft.fabric.socksproxyclient.mixin;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.mixin_variables.ClientConnectionMixinVariables;
import crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.ProxyCredential;
import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.network.ClientConnection$1")
public class ClientConnectionMixinInner {
    @Unique
    private static final Logger LOGGER = SocksProxyClient.LOGGER;

    @Inject(
            method = "initChannel",
            at = @At("TAIL")
    )
    private void injected(Channel channel, CallbackInfo ci) {
        InetSocketAddress remote = ClientConnectionMixinVariables.getRemote();
        if (remote == null) {
            return;
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
            return;
        }

        ProxyCredential proxyCredential = ServerConfig.getProxyCredential();

        final SocketAddress sa = proxySelection.address();
        switch (ServerConfig.getSocksVersion()) {
            case SOCKS4:
                LOGGER.info("Using Socks4 proxy {} on {}", sa, remote);
                channel.pipeline().addFirst("socks",
                        new Socks4ProxyHandler(sa, proxyCredential.getUsername()));
                break;
            case SOCKS5:
                LOGGER.info("Using Socks5 proxy {} on {}", sa, remote);
                channel.pipeline().addFirst("socks",
                        new Socks5ProxyHandler(sa, proxyCredential.getUsername(), proxyCredential.getPassword()));
                break;
            default:
                LOGGER.info("No proxy on host {}", address);
                break;
        }
    }
}
