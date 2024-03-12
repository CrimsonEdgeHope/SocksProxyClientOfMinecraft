package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static crimsonedgehope.minecraft.fabric.socksproxyclient.config.Constant.*;
import static me.shedaniel.autoconfig.annotation.ConfigEntry.*;
import static me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

@Config(name = "SocksProxyClient")
public final class SocksProxyClientConfig implements ConfigData {

    public enum Socks {
        SOCKS4,
        SOCKS5
    }

    public enum ProxyOption {
        GAME,   /* Proxy host from game param */
        CUSTOM, /* User-applied proxy host    */
        NONE
    }

    @Category(PROXY_CONFIG_CATEGORY)
    @Tooltip
    @Getter
    @Accessors(fluent = true)
    private boolean useProxy = true;

    @Category(PROXY_CONFIG_CATEGORY)
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Getter
    private Socks socksVersion = Socks.SOCKS5;

    @Category(PROXY_CONFIG_CATEGORY)
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Tooltip
    @Getter
    private ProxyOption proxyLoopbackOption = ProxyOption.NONE;

    @Category(PROXY_CONFIG_CATEGORY)
    @Tooltip
    @Getter
    @Accessors(fluent = true)
    private boolean useProxyHostFromGameParam = true;

    @Category(PROXY_CONFIG_CATEGORY)
    @Getter
    private String proxyHost = "localhost";

    @Category(PROXY_CONFIG_CATEGORY)
    @Getter
    private Integer proxyPort = 8080;

    @Override
    public void validatePostLoad() throws ValidationException {
        if (socksVersion != Socks.SOCKS4 && socksVersion != Socks.SOCKS5) {
            socksVersion = Socks.SOCKS5;
        }
        if (proxyHost.isEmpty()) {
            proxyHost = "localhost";
        }
        if (proxyPort <= 0 || proxyPort >= 65535) {
            throw new ValidationException("Invalid proxy port!");
        }
    }

    public static SocksProxyClientConfig get() {
        return AutoConfig.getConfigHolder(SocksProxyClientConfig.class).getConfig();
    }
}
