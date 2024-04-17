package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.cloth.ConfigDataStore;
import crimsonedgehope.minecraft.fabric.socksproxyclient.cloth.SocksProxyClientConfigData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.cloth.SocksProxyClientConfigData.ProxyOption;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyConfig {

    public static final String CATEGORY = "proxy";
    private static final Logger LOGGER = SocksProxyClient.logger();

    @Nullable
    public static Proxy getProxy() {
        return getProxy(config().useProxy());
    }

    public static Proxy getProxy(boolean useProxy) {
        return getProxy(useProxy, config().useProxyHostFromGameParam());
    }

    public static Proxy getProxy(ProxyOption proxyOption) {
        return getProxy(config().useProxy(), proxyOption);
    }

    public static Proxy getProxyForLoopback() {
        return getProxy(loopbackProxyOption());
    }

    public static Proxy getProxy(boolean useProxy, ProxyOption proxyOption) {
        return switch (proxyOption) {
            case GAME -> getProxy(useProxy, true);
            case CUSTOM -> getProxy(useProxy, false);
            case NONE -> getProxy(false);
        };
    }

    @Nullable
    public static Proxy getProxy(
            boolean useProxy,
            boolean useProxyHostFromGameParam
    ) {

        LOGGER.debug("useProxy {}, useProxyHostFromGameParam {}", useProxy, useProxyHostFromGameParam);

        if (!useProxy) {
            return null;
        }

        Proxy proxy;

        if (useProxyHostFromGameParam) {
            proxy = MinecraftClient.getInstance().getNetworkProxy();
            if (!proxy.type().equals(Proxy.Type.SOCKS)) {
                return null;
            }
            LOGGER.debug("Use proxyHost from game parameters.");
            return proxy;
        }

        proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(config().getProxyHost(), config().getProxyPort()));
        LOGGER.debug("Use custom proxyHost field.");

        return proxy;
    }

    public static Credential getProxyCredential() {
        return getProxyCredential(config().useProxyHostFromGameParam());
    }

    public static Credential getProxyCredential(ProxyOption proxyOption) {
        return switch (proxyOption) {
            case GAME -> getProxyCredential(true);
            case CUSTOM -> getProxyCredential(false);
            case NONE -> new Credential(null, null);
        };
    }

    public static Credential getProxyCredential(boolean useProxyHostFromGameParam) {
        LOGGER.debug("getProxyCredential: useProxyHostFromGameParam {}", useProxyHostFromGameParam);
        if (useProxyHostFromGameParam) {
            return getCredentialFromGameParam();
        }
        return getCustomCredential();
    }

    public static Credential getProxyCredentialForLoopback() {
        return getProxyCredential(loopbackProxyOption());
    }

    public static ProxyOption loopbackProxyOption() {
        return config().getLoopbackProxyOption();
    }

    @Getter
    @AllArgsConstructor
    public static final class Credential {
        @Nullable private String username;
        @Nullable private String password;
    }

    @Getter
    private static Credential customCredential;
    @Getter
    private static Credential credentialFromGameParam;

    public static void setCredentialFromGameParam(@Nullable String username, @Nullable String password) {
        credentialFromGameParam = new Credential(username, password);
    }

    public static void setCustomCredential(@Nullable String username, @Nullable String password) {
        customCredential = new Credential(username, password);
    }

    public static int getSocksVersion() {
        return switch (config().getSocksVersion()) {
            case SOCKS4 -> 4;
            case SOCKS5 -> 5;
        };
    }

    private static SocksProxyClientConfigData config() {
        return (SocksProxyClientConfigData) ConfigDataStore.get(ConfigDataStore.MAIN);
    }
}
