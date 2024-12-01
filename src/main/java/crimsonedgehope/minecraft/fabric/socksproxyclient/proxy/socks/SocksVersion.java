package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.socks;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum SocksVersion {
    SOCKS4("Socks 4", 4),
    SOCKS5("Socks 5", 5);
    public final String description;
    public final int numericVersion;
}