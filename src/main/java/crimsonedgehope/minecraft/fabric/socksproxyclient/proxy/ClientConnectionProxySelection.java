package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.ProxyEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixin;
import io.netty.channel.ChannelPipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.ClientConnection;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
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

        List<ProxyEntry> proxies;
        if (address.isLoopbackAddress()) {
            proxies = ServerConfig.getProxyEntryForMinecraftLoopback();
        } else {
            proxies = ServerConfig.getProxyEntryForMinecraft();
        }

        if (proxies.isEmpty()) {
            LOGGER.info("[Direct] -> [Remote] {}", remote);
            return;
        }

        for (ProxyEntry entry : proxies) {
            final Credential proxyCredential = entry.getCredential();
            final InetSocketAddress sa = (InetSocketAddress) entry.getProxy().address();
            switch (entry.getVersion()) {
                case SOCKS4 -> {
                    SocksUtils.applySocks4ProxyHandler(pipeline, sa, proxyCredential);
                    LOGGER.info("[Socks 4] {} -> [Remote] {}", sa, remote);
                }
                case SOCKS5 -> {
                    SocksUtils.applySocks5ProxyHandler(pipeline, sa, proxyCredential);
                    LOGGER.info("[Socks 5] {} -> [Remote] {}", sa, remote);
                }
                default -> {
                    LOGGER.info("[Direct] -> [Remote] {}", remote);
                }
            }
        }
    }
}
