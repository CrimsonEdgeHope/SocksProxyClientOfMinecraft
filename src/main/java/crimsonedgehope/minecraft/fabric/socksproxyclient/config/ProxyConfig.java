package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.SocksProxyClientConfig.ProxyOption;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyConfig {

    private static final Logger LOGGER = SocksProxyClient.logger();

    @Getter
    @AllArgsConstructor
    public static final class Credential {
        private String username;
        private String password;
    }

    @Getter
    private static Credential credential;

    public static void loadGameParam(String username, String password) {
        credential = new Credential(username, password);
    }

    public static Proxy getProxy() {
        if (!config().useProxy()) {
            return getProxy(ProxyOption.NONE);
        }
        if (config().useProxyHostFromGameParam()) {
            return getProxy(ProxyOption.GAME);
        }
        return getProxy(ProxyOption.CUSTOM);
    }

    public static Proxy getProxy(ProxyOption option) {
        switch (option) {
            case GAME -> {
                LOGGER.debug("getProxy: GAME case");
                Proxy proxy = MinecraftClient.getInstance().getNetworkProxy();
                if (proxy.address() == null || proxy.type() != Proxy.Type.SOCKS) {
                    return null;
                }
                return proxy;
            }
            case CUSTOM -> {
                LOGGER.debug("getProxy: CUSTOM case");
                return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(config().getProxyHost(), config().getProxyPort()));
            }
            default -> {
                LOGGER.debug("getProxy: NONE case");
                return null;
            }
        }
    }

    private static SocksProxyClientConfig config() {
        return SocksProxyClientConfig.get();
    }

    public static int getSocksVersion() {
        return switch (config().getSocksVersion()) {
            case SOCKS4 -> 4;
            case SOCKS5 -> 5;
        };
    }
}
