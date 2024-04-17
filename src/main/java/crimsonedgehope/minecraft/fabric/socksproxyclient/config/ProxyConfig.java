package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.cloth.ConfigDataStore;
import crimsonedgehope.minecraft.fabric.socksproxyclient.cloth.SocksProxyClientConfigData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.ServerResourcePackProvider;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.cloth.SocksProxyClientConfigData.ProxyOption;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyConfig {

    public static final String CATEGORY = "proxy";
    private static final Logger LOGGER = SocksProxyClient.logger();

    public static boolean useProxy() {
        return config().useProxy();
    }

    public static ProxyOption proxyLoopbackOption() {
        return config().getProxyLoopbackOption();
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

    @Getter
    @AllArgsConstructor
    public static final class Credential {
        @Nullable private String username;
        @Nullable private String password;
    }

    @Getter
    private static Credential credential;
    @Getter
    private static Credential credentialFromGameParam;

    public static void setCredentialFromGameParam(@Nullable String username, @Nullable String password) {
        credentialFromGameParam = new Credential(username, password);
    }

    public static void setCredential(@Nullable String username, @Nullable String password) {
        credential = new Credential(username, password);
    }

    public static int getSocksVersion() {
        return switch (config().getSocksVersion()) {
            case SOCKS4 -> 4;
            case SOCKS5 -> 5;
        };
    }

    private static SocksProxyClientConfigData config() {
        return (SocksProxyClientConfigData) ConfigDataStore.get(SocksProxyClientConfigData.ENTRY);
    }
}
