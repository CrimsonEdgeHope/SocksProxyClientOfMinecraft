package crimsonedgehope.minecraft.fabric.socksproxyclient.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ProxyConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.util.ActionResult;

import static me.shedaniel.autoconfig.annotation.ConfigEntry.*;
import static me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;
import static net.minecraft.util.ActionResult.PASS;
import static net.minecraft.util.ActionResult.SUCCESS;

@Config(name = "SocksProxyClient")
public final class SocksProxyClientConfigData implements ConfigData {

    public static final String ENTRY = "SocksProxyClientConfigData";

    public enum Socks {
        SOCKS4,
        SOCKS5
    }

    public enum ProxyOption {
        GAME,   /* Proxy host from game param */
        CUSTOM, /* User-applied proxy host    */
        NONE
    }

    @Category(ProxyConfig.CATEGORY)
    @Tooltip
    @Getter
    @Accessors(fluent = true)
    private boolean useProxy = true;

    @Comment("Valid value: SOCKS4, SOCKS5")
    @Category(ProxyConfig.CATEGORY)
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Getter
    private Socks socksVersion = Socks.SOCKS5;

    @Comment("Valid value: GAME, CUSTOM, NONE")
    @Category(ProxyConfig.CATEGORY)
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Tooltip
    @Getter
    private ProxyOption proxyLoopbackOption = ProxyOption.NONE;

    @Category(ProxyConfig.CATEGORY)
    @Tooltip
    @Getter
    @Accessors(fluent = true)
    private boolean useProxyHostFromGameParam = true;

    @Category(ProxyConfig.CATEGORY)
    @Getter
    private String proxyHost = "localhost";

    @Category(ProxyConfig.CATEGORY)
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

    public static ActionResult updateCredential(SocksProxyClientConfigData configData) {
        int k;
        int at;

        at = configData.getProxyHost().indexOf("@");
        if (at == -1) {
            ProxyConfig.setCredential(
                    ProxyConfig.getCredentialFromGameParam().getUsername(),
                    ProxyConfig.getCredentialFromGameParam().getPassword()
            );
            return PASS;
        }

        String c = configData.getProxyHost().substring(0, at);
        k = c.indexOf(":");
        if (k == -1) {
            ProxyConfig.setCredential(c, null);
            return SUCCESS;
        }

        String u = configData.getProxyHost().substring(0, k);
        String p = configData.getProxyHost().substring(k, at);
        ProxyConfig.setCredential(u, p);

        return SUCCESS;
    }
}
