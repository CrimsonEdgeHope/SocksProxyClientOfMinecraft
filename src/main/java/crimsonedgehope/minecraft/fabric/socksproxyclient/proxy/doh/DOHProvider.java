package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy.doh;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum DOHProvider {
    CLOUDFLARE("Cloudflare", "https://cloudflare-dns.com/dns-query"),
    GOOGLE("Google", "https://dns.google/dns-query"),
    DNS_SB("dns.sb", "https://doh.sb/dns-query"),
    QUAD9("Quad9", "https://dns.quad9.net/dns-query"),
    CISCO_UMBRELLA("Cisco OpenDNS/Cisco Umbrella", "https://doh.opendns.com/dns-query"),
    YANDEX("Yandex DNS", "https://common.dot.dns.yandex.net/dns-query"),
    ADGUARD("AdGuard DNS", "https://dns.adguard-dns.com/dns-query"),
    CUSTOM("Custom url", null);

    public final String displayName;
    public final String url;
}
