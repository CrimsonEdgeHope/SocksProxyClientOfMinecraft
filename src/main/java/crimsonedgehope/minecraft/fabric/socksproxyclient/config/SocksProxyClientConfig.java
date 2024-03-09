package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient.MISCELLANEOUS_CONFIG_CATEGORY;
import static crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient.PROXY_CONFIG_CATEGORY;
import static me.shedaniel.autoconfig.annotation.ConfigEntry.*;
import static me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

@Config(name = "SocksProxyClient")
public final class SocksProxyClientConfig implements ConfigData {

    private enum Socks {
        SOCKS4,
        SOCKS5
    }
    @RequiresRestart
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Category(PROXY_CONFIG_CATEGORY)
    private Socks socksVersion = Socks.SOCKS5;

    @Category(PROXY_CONFIG_CATEGORY)
    @Tooltip
    private boolean proxyLoopback = false;

    @Category(PROXY_CONFIG_CATEGORY)
    private boolean proxyResourcePackDownloading = true;

    public int getSocksVersion() {
        return switch (socksVersion) {
            case SOCKS4 -> 4;
            case SOCKS5 -> 5;
            default -> -1;
        };
    }

    public boolean shouldProxyLookback() {
        return proxyLoopback;
    }

    public boolean shouldProxyResourcePackDownloading() {
        return proxyResourcePackDownloading;
    }

    @Override
    public void validatePostLoad() {
        if (socksVersion != Socks.SOCKS4 && socksVersion != Socks.SOCKS5) {
            socksVersion = Socks.SOCKS5;
        }
    }

    public static SocksProxyClientConfig get() {
        return AutoConfig.getConfigHolder(SocksProxyClientConfig.class).getConfig();
    }
}
