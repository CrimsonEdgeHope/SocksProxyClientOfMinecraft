package crimsonedgehope.minecraft.fabric.socksproxyclient.proxy;

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

    DOHProvider(String displayName, String url) {
        this.displayName = displayName;
        this.url = url;
    }

    public static DOHProvider byDisplayName(String v) {
        for (DOHProvider provider : DOHProvider.values()) {
            if (provider.displayName.equals(v)) {
                return provider;
            }
        }
        return null;
    }
}
