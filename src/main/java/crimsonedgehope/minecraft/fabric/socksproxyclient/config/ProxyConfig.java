package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import me.shedaniel.autoconfig.AutoConfig;

public final class ProxyConfig {
    private ProxyConfig() {}

    private static String username;
    private static String password;

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

    public static int getSocksVersion() {
        return AutoConfig.getConfigHolder(SocksProxyClientConfig.class).getConfig().getVersion();
    }
}
