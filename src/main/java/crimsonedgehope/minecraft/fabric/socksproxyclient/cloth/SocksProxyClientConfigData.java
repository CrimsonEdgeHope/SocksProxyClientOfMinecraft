package crimsonedgehope.minecraft.fabric.socksproxyclient.cloth;

import crimsonedgehope.minecraft.fabric.socksproxyclient.SocksProxyClient;
import crimsonedgehope.minecraft.fabric.socksproxyclient.config.ProxyConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.util.ActionResult;

import static me.shedaniel.autoconfig.annotation.ConfigEntry.*;
import static me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;
import static net.minecraft.util.ActionResult.FAIL;
import static net.minecraft.util.ActionResult.PASS;

@Config(name = "SocksProxyClient")
public final class SocksProxyClientConfigData implements ConfigData {

    public enum Socks {
        SOCKS4,
        SOCKS5
    }

    public enum ProxyOption {
        GAME,   /* Use proxyHost from game param */
        CUSTOM, /* User-applied proxy host in proxyHost field */
        NONE    /* Don't use proxy */
    }

    @Category(ProxyConfig.CATEGORY)
    @Tooltip
    @Getter
    @Accessors(fluent = true)
    private boolean useProxy = false;

    @Category(ProxyConfig.CATEGORY)
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Getter
    private Socks socksVersion = Socks.SOCKS5;

    @Category(ProxyConfig.CATEGORY)
    @Tooltip(count = 3)
    @Getter
    @Accessors(fluent = true)
    private boolean useProxyHostFromGameParam = true;

    @Category(ProxyConfig.CATEGORY)
    @Tooltip
    @Getter
    @Setter
    private String proxyHost = "localhost";

    @Category(ProxyConfig.CATEGORY)
    @Getter
    @Setter
    private Integer proxyPort = 1080;

    @Category(ProxyConfig.CATEGORY)
    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Tooltip(count = 4)
    @Getter
    private ProxyOption loopbackProxyOption = ProxyOption.NONE;

    @Override
    public void validatePostLoad() throws ValidationException {
        if (validate(this).equals(FAIL)) {
            throw new ValidationException("");
        }
    }

    public static ActionResult validate(SocksProxyClientConfigData configData) {
        if (configData.getSocksVersion() != Socks.SOCKS4 && configData.getSocksVersion() != Socks.SOCKS5) {
            return FAIL;
        }

        if (configData.getProxyHost().isEmpty()) {
            configData.setProxyHost("localhost");
            updateCustomCredential(configData);
        }

        if (configData.getProxyPort() <= 0 || configData.getProxyPort() >= 65535) {
            configData.setProxyPort(1080);
        }

        return PASS;
    }

    public static ActionResult updateCustomCredential(SocksProxyClientConfigData configData) {
        int k;
        int at;

        SocksProxyClient.logger().debug("Retrieve custom proxy credential from proxyHost field");

        at = configData.getProxyHost().indexOf("@");
        if (at == -1) {
            ProxyConfig.setCustomCredential(null, null);
            return PASS;
        }

        String c = configData.getProxyHost().substring(0, at);
        k = c.indexOf(":");
        if (k == -1) {
            ProxyConfig.setCustomCredential(c, null);
            return PASS;
        }

        String u = configData.getProxyHost().substring(0, k);
        String p = configData.getProxyHost().substring(k, at);

        ProxyConfig.setCustomCredential(u, p);

        return PASS;
    }
}
