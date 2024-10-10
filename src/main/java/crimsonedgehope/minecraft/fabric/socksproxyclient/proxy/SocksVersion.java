package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SocksVersion {
    SOCKS4("Socks 4", 4),
    SOCKS5("Socks 5", 5);
    public final String desc;
    public final int ver;
}