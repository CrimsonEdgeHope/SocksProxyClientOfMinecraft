package crimsonedgehope.minecraft.fabric.socksproxyclient.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "SocksProxyClient")
public final class SocksProxyClientConfig implements ConfigData {

    @ConfigEntry.Gui.EnumHandler
    private Socks socksVersion = Socks.VERSION_5;

    public int getVersion() {
        return socksVersion.ver;
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
}
