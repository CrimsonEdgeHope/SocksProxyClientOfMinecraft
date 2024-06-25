package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixin;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.ClientConnection;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientConnectionProxySelection {
    private static final Logger LOGGER = SocksProxyClient.logger("Connect");

    public static void fire(ClientConnection instance, ChannelPipeline pipeline) {
        InetSocketAddress remote = ((IClientConnectionMixin) instance).socksProxyClient$getInetSocketAddress();
        fire(remote, pipeline);
    }

    public static void fire(InetSocketAddress remote, ChannelPipeline pipeline) {
        if (Objects.isNull(remote)) {
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
                pipeline.addFirst("socks",
                        new Socks4ProxyHandler(sa, proxyCredential.getUsername()));
                break;
            case SOCKS5:
                LOGGER.info("Using Socks5 proxy {} on {}", sa, remote);
                pipeline.addFirst("socks",
                        new Socks5ProxyHandler(sa, proxyCredential.getUsername(), proxyCredential.getPassword()));
                break;
            default:
                LOGGER.info("No proxy on host {}", address);
                break;
        }
    }
}
