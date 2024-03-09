package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import net.minecraft.client.MinecraftClient;

import java.net.Proxy;

public final class ProxyConfig {
    private ProxyConfig() {}

    private static String username;
    private static String password;
    private static String usernameFromGameParam;
    private static String passwordFromGameParam;

    public static void loadGameParam(String username, String password) {
        usernameFromGameParam = username;
        passwordFromGameParam = password;
        setUsername(username);
        setPassword(password);
    }

    public static Proxy getProxy() {
        Proxy proxy = MinecraftClient.getInstance().getNetworkProxy();
        if (proxy.address() == null || proxy.type() != Proxy.Type.SOCKS) {
            return null;
        }
        return proxy;
    }

    public static void setUsername(String s) {
        username = s;
    }

    public static void setPassword(String s) {
        password = s;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUsernameFromGameParam() {
        return usernameFromGameParam;
    }

    public static String getPasswordFromGameParam() {
        return passwordFromGameParam;
    }

    private static SocksProxyClientConfig config() {
        return SocksProxyClientConfig.get();
    }

    public static int getSocksVersion() {
        return config().getSocksVersion();
    }

    public static boolean shouldProxyLoopback() {
        return config().shouldProxyLookback();
    }

    public static boolean shouldProxyResourcePackDownloading() {
        return config().shouldProxyResourcePackDownloading();
    }
}
