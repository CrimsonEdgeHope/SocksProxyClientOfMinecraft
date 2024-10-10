package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.GeneralConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ServerConfig;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.entry.ProxyEntry;
import crimsonedgehope.minecraft.fabric.socksproxyclient.injection.access.IClientConnectionMixin;
import io.netty.channel.ChannelPipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.ClientConnection;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SocksApply {
    private static final Logger LOGGER = SocksProxyClient.logger("Connect");

    public static void info(@NotNull List<ProxyEntry> proxies, @NotNull InetSocketAddress remote) {
        Objects.requireNonNull(remote.getHostString());
        if (proxies.isEmpty()) {
            LOGGER.info("[Direct] -> [Remote] {}:{}", remote.getHostString(), remote.getPort());
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (ProxyEntry entry : proxies) {
            builder.append(String.format("[%s] %s:%s -> ",
                    entry.getVersion().desc,
                    ((InetSocketAddress) entry.getProxy().address()).getHostString(),
                    ((InetSocketAddress) entry.getProxy().address()).getPort()));
        }
        builder.append(String.format("[Remote] %s:%s", remote.getHostString(), remote.getPort()));
        LOGGER.info("{}", builder);
    }

    public static void fire(ClientConnection instance, ChannelPipeline pipeline) {
        InetSocketAddress remote = ((IClientConnectionMixin) instance).socksProxyClient$getInetSocketAddress();
        fire(remote, pipeline);
    }

    public static void fire(InetSocketAddress remote, ChannelPipeline pipeline) {
        fire(remote, pipeline, false);
    }

    public static void fire(InetSocketAddress remote, ChannelPipeline pipeline, boolean always) {
        if (Objects.isNull(remote)) {
            return;
        }

        InetAddress address = remote.getAddress();

        List<ProxyEntry> proxies;
        if (!always) {
            if (address.isLoopbackAddress()) {
                proxies = ServerConfig.getProxyEntryForMinecraftLoopback();
            } else {
                proxies = ServerConfig.getProxyEntryForMinecraft();
            }
        } else {
            proxies = GeneralConfig.getProxyEntry(true);
        }
        SocksUtils.apply(pipeline, proxies);
        info(proxies, remote);
    }
}
