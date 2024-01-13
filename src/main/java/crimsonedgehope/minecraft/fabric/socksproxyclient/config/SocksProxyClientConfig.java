package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "SocksProxyClient")
public final class SocksProxyClientConfig implements ConfigData {

    @ConfigEntry.Gui.EnumHandler
    private Socks socksVersion = Socks.VERSION_5;

    private boolean proxyLoopback = false;

    public int getSocksVersion() {
        return socksVersion.ver;
    }

    public boolean shouldProxyLookback() {
        return proxyLoopback;
    }

    enum Socks {
        VERSION_4(4),
        VERSION_5(5);

        final int ver;
        Socks(int ver) {
            this.ver = ver;
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        if (socksVersion != Socks.VERSION_4 && socksVersion != Socks.VERSION_5) {
            socksVersion = Socks.VERSION_5;
        }
    }

    public static SocksProxyClientConfig get() {
        return AutoConfig.getConfigHolder(SocksProxyClientConfig.class).getConfig();
    }
}
